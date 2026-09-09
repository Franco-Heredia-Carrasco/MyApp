package com.example.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button boton_Terremotos, boton_Incendios, boton_Tornados, boton_Lluvias, boton_Maremotos, boton_Tormenta_Fuego, boton_Aluvion;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        boton_Terremotos = findViewById(R.id.boton_Terremotos);
        boton_Incendios = findViewById(R.id.boton_Incendios);
        boton_Tornados = findViewById(R.id.boton_Tornados);
        boton_Lluvias = findViewById(R.id.boton_Lluvias);
        boton_Maremotos = findViewById(R.id.boton_Maremotos);
        boton_Tormenta_Fuego = findViewById(R.id.boton_Tormenta_Fuego);
        boton_Aluvion = findViewById(R.id.boton_Aluvion);


        boton_Terremotos.setOnClickListener(v -> {
            abrirGuia("Guia: Terremotos", getString(R.string.guia_terremotos));
        });

        boton_Incendios.setOnClickListener(v -> {
            abrirGuia("Guia: Incendios", getString(R.string.guia_incendios));
        });

        boton_Tornados.setOnClickListener(v -> {
            abrirGuia("Guia: Tornados", getString(R.string.guia_tornados));
        });

        boton_Lluvias.setOnClickListener(v -> {
            abrirGuia("Guia: Lluvias", getString(R.string.guia_lluvias));
        });

        boton_Maremotos.setOnClickListener(v -> {
            abrirGuia("Guia: Marremotos", getString(R.string.guia_maremotos));
        });

        boton_Tormenta_Fuego.setOnClickListener(v -> {
            abrirGuia("Guia: Firestorm", getString(R.string.guia_firestorm));
        });

        boton_Aluvion.setOnClickListener(v -> {
            abrirGuia("Guia: Aluvion", getString(R.string.guia_aluvion));
        });



    }
    private void abrirGuia(String Titulo, String Guia) {
        Intent intent = new Intent(MainActivity.this, PlantillaActivity.class);
        intent.putExtra("Titulo De Cambio", Titulo);
        intent.putExtra("Texto de cambio guia", Guia);
        startActivity(intent);
    }
}