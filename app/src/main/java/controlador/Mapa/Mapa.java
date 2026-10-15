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

        // Configuración de osmdroid.
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

        // Evita un NullPointerException si el ID no existe.
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

        // Centro inicial: Iquique.
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

    // Comprueba si existe permiso de ubicación precisa o aproximada.
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

    // Conserva el nombre del método original.
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

            // Comprueba si el GPS está activado.
            if (!locationManager.isProviderEnabled(
                    LocationManager.GPS_PROVIDER)) {

                Toast.makeText(
                        context,
                        "Activa el GPS del teléfono para registrar el recorrido.",
                        Toast.LENGTH_LONG
                ).show();

                return;
            }

            // Crear el indicador de ubicación una sola vez.
            if (myLocationOverlay == null) {

                GpsMyLocationProvider provider =
                        new GpsMyLocationProvider(context);

                myLocationOverlay =
                        new MyLocationNewOverlay(provider, map);

                map.getOverlays().add(myLocationOverlay);
            }

            myLocationOverlay.enableMyLocation();
            myLocationOverlay.enableFollowLocation();

            // Crear la línea del recorrido una sola vez.
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

                    // La vista podría haberse destruido.
                    if (map == null || lineaRecorrido == null) {
                        return;
                    }

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
                    // No es necesaria ninguna acción adicional.
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
                    // Compatibilidad con versiones antiguas.
                }
            };

            // Actualizar la ubicación cada 2 segundos
            // o cuando el dispositivo se mueva 2 metros.
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

    // Detiene las actualizaciones cuando ya no se necesitan.
    private void detenerSeguimiento() {

        if (locationManager != null
                && locationListener != null) {

            try {
                locationManager.removeUpdates(locationListener);

            } catch (SecurityException ignored) {
                // El permiso pudo cambiar durante la ejecución.
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
        map = null;

        super.onDestroyView();
    }
}