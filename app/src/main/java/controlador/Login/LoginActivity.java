package controlador.Login;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.franco.asistenciaiquique.R;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etCorreo;
    private EditText etPassword;
    private Button btnIngresar;

    private boolean inicioSesionEnCurso = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        etCorreo = findViewById(R.id.et_correo);
        etPassword = findViewById(R.id.et_password);
        btnIngresar = findViewById(R.id.btn_ingresar);

        // Evita errores si algún ID no coincide con el XML.
        if (etCorreo == null
                || etPassword == null
                || btnIngresar == null) {

            Toast.makeText(
                    this,
                    "Error de diseño. Revisa los IDs de activity_login.xml.",
                    Toast.LENGTH_LONG
            ).show();

            finish();
            return;
        }

        // Inicializar Firebase.
        try {
            mAuth = FirebaseAuth.getInstance();

        } catch (IllegalStateException e) {

            Toast.makeText(
                    this,
                    "Firebase no está configurado. "
                            + "Revisa google-services.json y Gradle.",
                    Toast.LENGTH_LONG
            ).show();

            finish();
            return;
        }

        btnIngresar.setOnClickListener(v -> {

            // Evitar múltiples solicitudes simultáneas.
            if (inicioSesionEnCurso) {
                return;
            }

            String correo =
                    etCorreo.getText().toString().trim();

            // No eliminar espacios de la contraseña.
            String password =
                    etPassword.getText().toString();

            if (correo.isEmpty() || password.isEmpty()) {

                Toast.makeText(
                        this,
                        "Completa todos los campos",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (!Patterns.EMAIL_ADDRESS
                    .matcher(correo)
                    .matches()) {

                etCorreo.setError("Ingresa un correo válido");
                etCorreo.requestFocus();

                return;
            }

            iniciarSesion(correo, password);
        });
    }

    // Conserva el nombre y la función del método original.
    private void iniciarSesion(
            String correo,
            String password) {

        if (mAuth == null
                || btnIngresar == null
                || isFinishing()
                || isDestroyed()) {
            return;
        }

        if (inicioSesionEnCurso) {
            return;
        }

        inicioSesionEnCurso = true;
        btnIngresar.setEnabled(false);

        mAuth.signInWithEmailAndPassword(correo, password)
                .addOnCompleteListener(this, task -> {

                    inicioSesionEnCurso = false;

                    // No actualizar la interfaz si se cerró la pantalla.
                    if (isFinishing() || isDestroyed()) {
                        return;
                    }

                    if (btnIngresar != null) {
                        btnIngresar.setEnabled(true);
                    }

                    if (task.isSuccessful()) {

                        Toast.makeText(
                                this,
                                "Conectado correctamente",
                                Toast.LENGTH_SHORT
                        ).show();

                        // Mantiene el comportamiento original:
                        // cierra el login y vuelve a la pantalla anterior.
                        finish();

                    } else {

                        Exception error = task.getException();

                        if (error instanceof FirebaseNetworkException) {

                            Toast.makeText(
                                    this,
                                    "No hay conexión. "
                                            + "Revisa tu conexión a Internet.",
                                    Toast.LENGTH_LONG
                            ).show();

                        } else {

                            Toast.makeText(
                                    this,
                                    "No se pudo iniciar sesión. "
                                            + "Revisa el correo y la contraseña.",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
                });
    }
}