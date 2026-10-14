package controlador.Login;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// Importaciones clave de Firebase
import com.franco.asistenciaiquique.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etCorreo, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 1. Inicializar la conexión a Firebase
        mAuth = FirebaseAuth.getInstance();

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

            // 2. Ejecutar el login
            iniciarSesion(correo, password);
        });
    }

    private void iniciarSesion(String correo, String password) {
        // 3. Petición a los servidores de Google
        mAuth.signInWithEmailAndPassword(correo, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Éxito: El usuario existe y la clave es correcta
                        Toast.makeText(this, "Conectado correctamente", Toast.LENGTH_SHORT).show();
                        finish(); // 4. Destruye esta pantalla y vuelve al MainActivity
                    } else {
                        // Fallo: Clave mala, no existe, o no hay internet
                        Toast.makeText(this, "Error de autenticación", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}