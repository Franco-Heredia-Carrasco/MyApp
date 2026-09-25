package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
            btnAluvion.setOnClickListener(v -> abrirGuia(
                    getString(R.string.guia_aluvion), // o el texto/título que uses
                    "Contenido o texto de la guía de aluvión"
            ));
        }

        if (btnIncendios != null) {
            btnIncendios.setOnClickListener(v -> abrirGuia(
                    getString(R.string.guia_incendios),
                    "Contenido o texto de la guía de incendios"
            ));
        }

        if (btnLluvias != null) {
            btnLluvias.setOnClickListener(v -> abrirGuia(
                    getString(R.string.guia_lluvias),
                    "Contenido o texto de la guía de lluvias"
            ));
        }

        if (btnMaremotos != null) {
            btnMaremotos.setOnClickListener(v -> abrirGuia(
                    getString(R.string.guia_maremotos),
                    "Contenido o texto de la guía de maremotos"
            ));
        }

        if (btnTerremotos != null) {
            btnTerremotos.setOnClickListener(v -> abrirGuia(
                    getString(R.string.guia_terremotos),
                    "Contenido o texto de la guía de terremotos"
            ));
        }

        if (btnTormentaFuego != null) {
            btnTormentaFuego.setOnClickListener(v -> abrirGuia(
                    getString(R.string.guia_firestorm),
                    "Contenido o texto de la guía de tormenta de fuego"
            ));
        }

        if (btnTornados != null) {
            btnTornados.setOnClickListener(v -> abrirGuia(
                    getString(R.string.guia_tornados),
                    "Contenido o texto de la guía de tornados"
            ));
        }
    }

    private void abrirGuia(String titulo, String contenido) {
        Intent intent = new Intent(requireContext(), PlantillaActivity.class);
        intent.putExtra("Titulo", titulo);
        intent.putExtra("Texto de cambio guia", contenido);
        startActivity(intent);
    }
}