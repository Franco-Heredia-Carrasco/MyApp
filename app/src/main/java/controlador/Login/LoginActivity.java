package controlador.Login;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.franco.asistenciaiquique.R;
import controlador.BaseDeDatos.DatabaseHelper;

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
    }

    private void iniciarSesion(String correo, String password) {
        String rolObtenido = dbHelper.ObtenerRol(correo, password);

        if (rolObtenido != null) {
            // Guardamos la sesión y el nivel de acceso en SharedPreferences
            SharedPreferences preferencias = getSharedPreferences("SesionApp", MODE_PRIVATE);
            preferencias.edit()
                    .putBoolean("isLogged", true)
                    .putString("rolUsuario", rolObtenido)
                    .apply();

            Toast.makeText(this, "Bienvenido " + rolObtenido, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    }
}