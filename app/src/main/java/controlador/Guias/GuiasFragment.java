package controlador.Guias;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapp.R;

public class GuiasFragment extends Fragment {

    public GuiasFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.guias_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnAluvion = view.findViewById(R.id.boton_Aluvion);
        Button btnIncendios = view.findViewById(R.id.boton_Incendios);
        Button btnLluvias = view.findViewById(R.id.boton_Lluvias);
        Button btnMaremotos = view.findViewById(R.id.boton_Maremotos);
        Button btnTerremotos = view.findViewById(R.id.boton_Terremotos);
        Button btnTormentaFuego = view.findViewById(R.id.boton_Tormenta_Fuego);
        Button btnTornados = view.findViewById(R.id.boton_Tornados);

        // Asignación de clics
        if (btnAluvion != null) {
            btnAluvion.setOnClickListener(v -> abrirGuia("Guia Aluvion",
                    getString(R.string.guia_aluvion)
            ));
        }

        if (btnIncendios != null) {
            btnIncendios.setOnClickListener(v -> abrirGuia("Guia Incendios",
                    getString(R.string.guia_incendios)
            ));
        }

        if (btnLluvias != null) {
            btnLluvias.setOnClickListener(v -> abrirGuia("Guia Lluvias",
                    getString(R.string.guia_lluvias)
            ));
        }

        if (btnMaremotos != null) {
            btnMaremotos.setOnClickListener(v -> abrirGuia("Guia Marremoto",
                    getString(R.string.guia_maremotos)
            ));
        }

        if (btnTerremotos != null) {
            btnTerremotos.setOnClickListener(v -> abrirGuia("Guia Terremotos",
                    getString(R.string.guia_terremotos)
            ));
        }

        if (btnTormentaFuego != null) {
            btnTormentaFuego.setOnClickListener(v -> abrirGuia("Guia Tormenta de fuego",
                    getString(R.string.guia_firestorm)
            ));
        }

        if (btnTornados != null) {
            btnTornados.setOnClickListener(v -> abrirGuia("Guia Tornados",
                    getString(R.string.guia_tornados)

            ));
        }
    }

    private void abrirGuia(String titulo, String contenido) {
        Intent intent = new Intent(requireContext(), PlantillaActivity.class);
        intent.putExtra("Titulo De Cambio", titulo);
        intent.putExtra("Texto de cambio guia", contenido);
        startActivity(intent);
    }
}