package controlador.Registro;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// Asegúrate de que esta ruta apunte a tu DatabaseHelper correcto
import com.franco.asistenciaiquique.R;

import controlador.BaseDeDatos.DatabaseHelper;

public class RegistroActivity extends AppCompatActivity {

    // Cambiamos el nombre de la variable a etNuevoCorreo
    private EditText etNuevoCorreo, etNuevaPass;
    private Button btnGuardarRegistro;

    private DatabaseHelper bdHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_layout_activity);

        // 1. Enlazamos con el nuevo ID del XML
        etNuevoCorreo = findViewById(R.id.etNuevoCorreo);
        etNuevaPass = findViewById(R.id.etNuevaPass);
        btnGuardarRegistro = findViewById(R.id.btnGuardarRegistro);

        bdHelper = new DatabaseHelper(this);

        // 2. Acción al registrar
        btnGuardarRegistro.setOnClickListener(v -> {
            // Extraemos los datos
            String correo = etNuevoCorreo.getText().toString().trim();
            String password = etNuevaPass.getText().toString().trim();

            // Validamos que no estén vacíos
            if (correo.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Ejecutamos el método que ya tienes en DatabaseHelper
            // Mandando: correo, contraseña y el rol "Usuario"
            boolean insertExitoso = bdHelper.insertarUsuario(correo, password, "Usuario");

            if (insertExitoso) {
                Toast.makeText(this, "¡Cuenta creada con éxito!", Toast.LENGTH_SHORT).show();
                finish(); // Cierra esta pantalla y vuelve al Login
            } else {
                Toast.makeText(this, "Error: El correo ya está registrado", Toast.LENGTH_LONG).show();
            }
        });
    }
}