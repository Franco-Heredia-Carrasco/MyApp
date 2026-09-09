package com.example.myapp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PlantillaActivity extends AppCompatActivity{

    private TextView txtTitulo, txtContenido;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_guia_generico);

        TextView txtTitulo = findViewById(R.id.txtTituloGuia);
        TextView txtGuia = findViewById(R.id.txtGuiaGenerica);

        String TituloRecibido = getIntent().getStringExtra("Titulo De Cambio");
        String GuiaRecibida = getIntent().getStringExtra("Texto de cambio guia");


        if (TituloRecibido != null && GuiaRecibida != null) {
            txtTitulo.setText(TituloRecibido);
            txtGuia.setText(GuiaRecibida);
        }

    }
}

