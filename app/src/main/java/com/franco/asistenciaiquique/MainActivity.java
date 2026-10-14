package com.franco.asistenciaiquique;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import controlador.Calculadora.CalculadoraFragment;
import controlador.Guias.GuiasFragment;

public class MainActivity extends AppCompatActivity {


    private Button btnIniciarSesion, btnMapa, btnCerrarSession;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();


        btnIniciarSesion = findViewById(R.id.boton_iniciar_sesion);
        btnMapa = findViewById(R.id.boton_mapa);
        btnCerrarSession = findViewById(R.id.boton_cerrar_sesion);


        btnIniciarSesion.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, controlador.Login.LoginActivity.class));
        });

        btnMapa.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new controlador.Mapa.Mapa())
                    .addToBackStack(null) // Esto permite usar el botón "Atrás" del celular para salir del mapa
                    .commit();
        });

        btnCerrarSession.setOnClickListener(V -> {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(this,"Cerrar Session", Toast.LENGTH_SHORT).show();
                    onResume();
                });


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new GuiasFragment())
                    .commit();
        }


        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int id = item.getItemId();

                if (id == R.id.nav_guias) {
                    selectedFragment = new GuiasFragment();
                } else if (id == R.id.nav_calculadora) {
                    selectedFragment = new CalculadoraFragment();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Verificamos si hay una sesión activa en Firebase
        FirebaseUser usuarioActual = mAuth.getCurrentUser();

        if (usuarioActual != null) {
            // Si ya inició sesión: Ocultamos Login y mostramos el Mapa
            btnIniciarSesion.setVisibility(View.GONE);
            btnMapa.setVisibility(View.VISIBLE);
            btnCerrarSession.setVisibility(View.VISIBLE);
        } else {
            // Si no hay sesión: Mostramos Login y ocultamos el Mapa
            btnIniciarSesion.setVisibility(View.VISIBLE);
            btnMapa.setVisibility(View.VISIBLE);
            btnCerrarSession.setVisibility(View.GONE);
        }
    }
}