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
import androidx.fragment.app.FragmentManager;

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

        // CORRECCIÓN AQUÍ: Cierra el mapa si existía y lo vuelve a inflar completamente desde cero
        btnMapa.setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();

            // Si el mapa ya estaba en el BackStack, lo removemos para "cerrarlo" antes de reiniciar
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            // Creamos una transacción limpia e instanciamos un mapa totalmente nuevo
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, new controlador.Mapa.Mapa())
                    .addToBackStack(null)
                    .commit();
        });

        btnCerrarSession.setOnClickListener(V -> {
            mAuth.signOut();
            Toast.makeText(this, "Sesión Cerrada", Toast.LENGTH_SHORT).show();
            actualizarInterfazUsuario(null);
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
                    // Si cambias de pestaña abajo, limpiamos el mapa viejo del BackStack
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

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
        FirebaseUser usuarioActual = mAuth.getCurrentUser();
        actualizarInterfazUsuario(usuarioActual);
    }

    private void actualizarInterfazUsuario(FirebaseUser usuario) {
        if (usuario != null) {
            btnIniciarSesion.setVisibility(View.GONE);
            btnMapa.setVisibility(View.VISIBLE);
            btnCerrarSession.setVisibility(View.VISIBLE);
        } else {
            btnIniciarSesion.setVisibility(View.VISIBLE);
            btnMapa.setVisibility(View.GONE);
            btnCerrarSession.setVisibility(View.GONE);
        }
    }
}
