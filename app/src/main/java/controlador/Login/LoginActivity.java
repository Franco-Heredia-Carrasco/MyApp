package controlador.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.franco.asistenciaiquique.MainActivity;
import com.franco.asistenciaiquique.R;
import controlador.BaseDeDatos.DatabaseHelper;
import controlador.Registro.RegistroActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etCorreo, etPassword;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializamos nuestra base de datos local SQLite
        dbHelper = new DatabaseHelper(this);

        etCorreo = findViewById(R.id.et_correo);
        etPassword = findViewById(R.id.et_password);
        Button btnIngresar = findViewById(R.id.btn_ingresar);

        btnIngresar.setOnClickListener(v -> {
            String correo = etCorreo.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (correo.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            iniciarSesion(correo, password);
        });
        TextView tvRegistrarse = findViewById(R.id.tvRegistrarse);

        // Listener para ir a la pantalla de registro
        tvRegistrarse.setOnClickListener(View -> {
            Intent intent = new Intent(LoginActivity.this, RegistroActivity.class); // Reemplaza 'RegistroActivity' por el nombre exacto de tu pantalla de registro
            startActivity(intent);
        });
    }

    private void iniciarSesion(String correo, String password) {
        String rolObtenido = dbHelper.ObtenerRol(correo, password);

        // 1. Verificamos que el rol exista y no esté vacío
        if (rolObtenido != null && !rolObtenido.isEmpty()) {

            // 2. Guardamos la sesión y el nivel de acceso en SharedPreferences
            SharedPreferences preferencias = getSharedPreferences("SesionApp", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferencias.edit();
            editor.putBoolean("isLogged", true);
            editor.putString("rolUsuario", rolObtenido);
            editor.apply(); // Guarda los cambios

            Toast.makeText(this, "Bienvenido " + rolObtenido, Toast.LENGTH_SHORT).show();

            // 3. Pasas al MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Cierra el login para que no puedan volver con el botón "atrás"

        } else {
            Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
        TextView tvRegistrarse = findViewById(R.id.tvRegistrarse);

        // Listener para ir a la pantalla de registro
        tvRegistrarse.setOnClickListener(View -> {
            Intent intent = new Intent(LoginActivity.this, RegistroActivity.class); // Reemplaza 'RegistroActivity' por el nombre exacto de tu pantalla de registro
            startActivity(intent);
        });
    }


}