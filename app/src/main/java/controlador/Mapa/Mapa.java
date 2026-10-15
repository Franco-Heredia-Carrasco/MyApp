
package controlador.Mapa;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.franco.asistenciaiquique.R;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class Mapa extends Fragment {

    private MapView map;
    private MyLocationNewOverlay myLocationOverlay;
    private Polyline lineaRecorrido;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private boolean seguimientoActivo = false;

    private static final float PRECISION_MINIMA_METROS = 5.0f;
    private static final float MOVIMIENTO_MINIMO_METROS = 3.0f;
    private Location ultimaUbicacionAceptada;

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.RequestPermission(),
                    isGranted -> {

                        if (!isAdded()) {
                            return;
                        }

                        if ((isGranted || tienePermisoUbicacion())
                                && map != null) {

                            iniciarGPSyRecorrido();

                        } else {
                            Context context = getContext();

                            if (context != null) {
                                Toast.makeText(
                                        context,
                                        "Permiso de ubicación denegado. "
                                                + "El mapa no mostrará tu ubicación.",
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }
                    });

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        Context context = requireContext();
        Context appContext = context.getApplicationContext();

        Configuration.getInstance().load(
                appContext,
                appContext.getSharedPreferences(
                        "osmdroid",
                        Context.MODE_PRIVATE
                )
        );

        Configuration.getInstance()
                .setUserAgentValue(context.getPackageName());

        View view = inflater.inflate(
                R.layout.mapa_layout,
                container,
                false
        );

        map = view.findViewById(R.id.mapa_osm);

        if (map == null) {
            Toast.makeText(
                    context,
                    "No se encontró el mapa. Revisa mapa_layout.xml.",
                    Toast.LENGTH_LONG
            ).show();

            return view;
        }

        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        org.osmdroid.api.IMapController mapController =
                map.getController();

        mapController.setZoom(14.0);

        mapController.setCenter(
                new GeoPoint(-20.2133, -70.1502)
        );

        if (tienePermisoUbicacion()) {
            iniciarGPSyRecorrido();
        } else {
            requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
            );
        }

        return view;
    }

    private boolean tienePermisoUbicacion() {

        Context context = getContext();

        if (context == null) {
            return false;
        }

        boolean permisoPreciso =
                ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED;

        boolean permisoAproximado =
                ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED;

        return permisoPreciso || permisoAproximado;
    }

    private void iniciarGPSyRecorrido() {

        if (!isAdded()
                || map == null
                || seguimientoActivo
                || !tienePermisoUbicacion()) {
            return;
        }

        Context context = getContext();

        if (context == null) {
            return;
        }

        locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);

        if (locationManager == null) {
            Toast.makeText(
                    context,
                    "No se pudo acceder al servicio de ubicación.",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        try {

            if (!locationManager.isProviderEnabled(
                    LocationManager.GPS_PROVIDER)) {

                Toast.makeText(
                        context,
                        "Activa el GPS del teléfono para registrar el recorrido.",
                        Toast.LENGTH_LONG
                ).show();

                return;
            }

            if (myLocationOverlay == null) {

                GpsMyLocationProvider provider =
                        new GpsMyLocationProvider(context);

                myLocationOverlay =
                        new MyLocationNewOverlay(provider, map);

                map.getOverlays().add(myLocationOverlay);
            }

            myLocationOverlay.enableMyLocation();
            myLocationOverlay.enableFollowLocation();

            if (lineaRecorrido == null) {

                lineaRecorrido = new Polyline();

                lineaRecorrido.setColor(Color.BLUE);
                lineaRecorrido.setWidth(12.0f);

                map.getOverlays().add(lineaRecorrido);
            }

            locationListener = new LocationListener() {

                @Override
                public void onLocationChanged(
                        @NonNull Location location) {

                    if (map == null || lineaRecorrido == null) {
                        return;
                    }

                    if (location.hasAccuracy()
                            && location.getAccuracy()
                            > PRECISION_MINIMA_METROS) {
                        return;
                    }

                    if (ultimaUbicacionAceptada != null
                            && ultimaUbicacionAceptada.distanceTo(location)
                            < MOVIMIENTO_MINIMO_METROS) {
                        return;
                    }

                    ultimaUbicacionAceptada = new Location(location);

                    GeoPoint puntoActual = new GeoPoint(
                            location.getLatitude(),
                            location.getLongitude()
                    );

                    lineaRecorrido.addPoint(puntoActual);

                    map.invalidate();
                }

                @Override
                public void onProviderEnabled(
                        @NonNull String provider) {
                }

                @Override
                public void onProviderDisabled(
                        @NonNull String provider) {

                    Context currentContext = getContext();

                    if (currentContext != null && isAdded()) {
                        Toast.makeText(
                                currentContext,
                                "El GPS se ha desactivado.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }

                @SuppressWarnings("deprecation")
                @Override
                public void onStatusChanged(
                        String provider,
                        int status,
                        Bundle extras) {
                }
            };

            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    2000L,
                    2.0f,
                    locationListener,
                    Looper.getMainLooper()
            );

            seguimientoActivo = true;

        } catch (SecurityException | IllegalArgumentException e) {

            detenerSeguimiento();

            Toast.makeText(
                    context,
                    "No se pudo iniciar el GPS. "
                            + "Revisa los permisos y la configuración.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private void detenerSeguimiento() {

        if (locationManager != null
                && locationListener != null) {

            try {
                locationManager.removeUpdates(locationListener);

            } catch (SecurityException ignored) {
            }
        }

        locationListener = null;
        seguimientoActivo = false;

        if (myLocationOverlay != null) {
            myLocationOverlay.disableFollowLocation();
            myLocationOverlay.disableMyLocation();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (map != null) {
            map.onResume();

            if (tienePermisoUbicacion()) {
                iniciarGPSyRecorrido();
            }
        }
    }

    @Override
    public void onPause() {
        detenerSeguimiento();

        if (map != null) {
            map.onPause();
        }

        super.onPause();
    }

    @Override
    public void onDestroyView() {
        detenerSeguimiento();

        if (map != null) {

            if (myLocationOverlay != null) {
                map.getOverlays().remove(myLocationOverlay);
            }

            if (lineaRecorrido != null) {
                map.getOverlays().remove(lineaRecorrido);
            }

            map.onDetach();
        }

        myLocationOverlay = null;
        lineaRecorrido = null;
        locationManager = null;
        locationListener = null;
        ultimaUbicacionAceptada = null;
        map = null;

        super.onDestroyView();
    }
}


