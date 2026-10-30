package com.franco.asistenciaiquique;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import controlador.Calculadora.CalculadoraFragment;
import controlador.Guias.GuiasFragment;
import controlador.UsuarioAdapter.GestionUsuariosActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnIniciarSesion, btnMapa, btnCerrarSession, btnadministracion;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnIniciarSesion = findViewById(R.id.boton_iniciar_sesion);
        btnMapa = findViewById(R.id.boton_mapa);
        btnCerrarSession = findViewById(R.id.boton_cerrar_sesion);
        btnadministracion = findViewById(R.id.btnPanelAdmin);

        SharedPreferences preferencias = getSharedPreferences("SesionApp", MODE_PRIVATE);
        String miRol = preferencias.getString("rolUsuario", "");

        btnIniciarSesion.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, controlador.Login.LoginActivity.class));
        });

        btnMapa.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new controlador.Mapa.Mapa())
                    .addToBackStack(null)
                    .commit();
        });

        btnCerrarSession.setOnClickListener(V -> {
            // SOLUCIÓN 2: Borrar efectivamente la sesión de la memoria al hacer clic
            SharedPreferences prefCerrar = getSharedPreferences("SesionApp", MODE_PRIVATE);
            prefCerrar.edit().clear().commit();

            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
            actualizarBotones(); // Actualizamos la interfaz inmediatamente
        });

        btnadministracion.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, GestionUsuariosActivity.class);
            startActivity(intent);
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
        actualizarBotones();
    }

    // Método que controla qué botones se ven dependiendo de si hay sesión iniciada
    private void actualizarBotones() {
        SharedPreferences preferencias = getSharedPreferences("SesionApp", MODE_PRIVATE);
        boolean isLogged = preferencias.getBoolean("isLogged", false);
        String miRol = preferencias.getString("rolUsuario","");

        if (isLogged) {
            // Usuario conectado
            btnIniciarSesion.setVisibility(View.GONE);
            btnMapa.setVisibility(View.VISIBLE);
            btnCerrarSession.setVisibility(View.VISIBLE);
            //Validacion de rol del boton
            if (miRol != null && miRol.equals("Administrador")) {
                btnadministracion.setVisibility(View.VISIBLE);
            } else {
                btnadministracion.setVisibility(View.GONE);
            }

        } else {
            // Usuario sin conectar
            btnIniciarSesion.setVisibility(View.VISIBLE);
            btnMapa.setVisibility(View.GONE);
            btnCerrarSession.setVisibility(View.GONE);
            btnadministracion.setVisibility(View.GONE);
        }
    }
}