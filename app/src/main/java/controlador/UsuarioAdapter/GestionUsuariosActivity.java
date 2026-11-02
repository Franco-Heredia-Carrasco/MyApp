package controlador.UsuarioAdapter;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.franco.asistenciaiquique.R;

import java.util.ArrayList;
import controlador.BaseDeDatos.DatabaseHelper;

public class GestionUsuariosActivity extends AppCompatActivity {

    private RecyclerView rvUsuarios;
    private DatabaseHelper dbHelper;
    private ArrayList<String> listaCorreos;
    private UsuarioAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gestion_de_usuarios);

        rvUsuarios = findViewById(R.id.rv_usuarios);
        rvUsuarios.setLayoutManager(new LinearLayoutManager(this)); // Formato de lista vertical

        dbHelper = new DatabaseHelper(this);

        cargarLista();
    }

    private void cargarLista() {
        listaCorreos = dbHelper.obtenerTodosLosUsuarios();

        // Inicializamos el adaptador y le pasamos lo que debe hacer al tocar el botón eliminar
        adapter = new UsuarioAdapter(listaCorreos, correoSeleccionado -> mostrarDialogoEliminar(correoSeleccionado));

        rvUsuarios.setAdapter(adapter);
    }

    private void mostrarDialogoEliminar(String correo) {
        if (correo.equals("profe@santotomas.cl")) {
            Toast.makeText(this, "No puedes eliminar cuentas de administrador", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Eliminar Usuario")
                .setMessage("¿Estás seguro de que deseas eliminar permanentemente a " + correo + "?")
                .setPositiveButton("Sí, eliminar", (dialog, which) -> {
                    boolean eliminado = dbHelper.eliminarUsuario(correo);
                    if (eliminado) {
                        Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                        cargarLista(); // Refresca la vista
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}