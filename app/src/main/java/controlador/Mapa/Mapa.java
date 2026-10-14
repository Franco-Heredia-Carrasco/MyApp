package controlador.Mapa; // <-- ¡Asegúrate de que este sea tu paquete correcto!

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.franco.asistenciaiquique.R;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class Mapa extends Fragment {

    private MapView map;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // 1. OBLIGATORIO PARA OSMDROID: Cargar la configuración ANTES de inflar el layout.
        // Esto le dice a los servidores de OpenStreetMap quién está pidiendo las imágenes del mapa.
        Context ctx = requireActivity().getApplicationContext();
        Configuration.getInstance().load(ctx, ctx.getSharedPreferences("osmdroid", Context.MODE_PRIVATE));
        // Ahora enviará tu nuevo ID único y real ("com.franco.asistenciaiquique")
        Configuration.getInstance().setUserAgentValue(requireActivity().getPackageName());

        // 2. Inflamos el layout (tu archivo mapa_layout.xml)
        View view = inflater.inflate(R.layout.mapa_layout, container, false);

        // 3. Vinculamos el mapa del XML con nuestra variable en Java
        map = view.findViewById(R.id.mapa_osm);

        map.getTileProvider().clearTileCache();

        // Configuramos el estilo...
        map.setTileSource(TileSourceFactory.MAPNIK);

        // 4. Configuramos el estilo del mapa y los controles
        map.setTileSource(TileSourceFactory.MAPNIK); // Estilo de mapa estándar
        map.setBuiltInZoomControls(true);            // Muestra botones de zoom en la pantalla
        map.setMultiTouchControls(true);             // Permite hacer zoom pellizcando la pantalla

        // 5. Centramos la cámara del mapa en Iquique
        IMapController mapController = map.getController();
        mapController.setZoom(16.0); // Nivel de Zoom (qué tan cerca se ve)
        GeoPoint IquiquePoint = new GeoPoint(-20.2133, -70.1502); // Coordenadas exactas
        mapController.setCenter(IquiquePoint);

        // 6. Añadimos un marcador (pin) en la ubicación
        Marker startMarker = new Marker(map);
        startMarker.setPosition(IquiquePoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setTitle("Sede INACAP Iquique"); // Puedes cambiar este texto al que necesites para tu proyecto
        map.getOverlays().add(startMarker);

        return view;
    }

    // Estos métodos son necesarios para que el mapa no gaste batería o datos
    // cuando el usuario minimiza la aplicación.
    @Override
    public void onResume() {
        super.onResume();
        if (map != null) {
            map.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (map != null) {
            map.onPause();
        }
    }
}