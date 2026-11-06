package controlador.BaseDeDatos;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AsistenciaIquique.db";
    // Usamos la versión 6 para forzar a que Android borre cualquier prueba anterior
    // y cargue estos nuevos usuarios que vas a insertar.
    private static final int DATABASE_VERSION = 6;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Usuarios (id INTEGER PRIMARY KEY AUTOINCREMENT, correo TEXT UNIQUE, password TEXT)");

        // REEMPLAZA LA LÍNEA DE LA TABLA ROLES POR ESTA:
        db.execSQL("CREATE TABLE Roles (correo TEXT PRIMARY KEY, rol TEXT, FOREIGN KEY (correo) REFERENCES Usuarios(correo))");

        db.execSQL("INSERT INTO Usuarios (correo, password) VALUES ('francoalonsokaled@gmail.com', 'HCar2006.')");
        db.execSQL("INSERT INTO Usuarios (correo, password) VALUES ('profe@santotomas.cl', '1234567')");

        // Y CAMBIA EL ORDEN AQUÍ PARA QUE COINCIDA (primero correo, luego rol):
        db.execSQL("INSERT INTO Roles (correo, rol) VALUES ('profe@santotomas.cl', 'Administrador')");
        db.execSQL("INSERT INTO Roles (correo, rol) VALUES ('francoalonsokaled@gmail.com', 'Usuario')");
    }

    //Funcion que elimina las tablas si es que existen en la DB de la APP
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Si cambiamos la versión, borra la tabla vieja y vuelve a ejecutar onCreate
        db.execSQL("DROP TABLE IF EXISTS Usuarios");
        db.execSQL("DROP TABLE IF EXISTS Roles");
        onCreate(db);
    }

    // Funcion para obtener el rol de la base de datos
    public String ObtenerRol(String correo, String password ) {
        SQLiteDatabase db = this.getReadableDatabase();
        String rol = null;
        // Buscamos el rol y el usuario con la tabla Roles
        String query = "Select r.rol FROM Usuarios U "+
                "INNER JOIN Roles r ON U.correo = r.correo "+
                "WHERE U.correo = ? AND U.password = ?";

        Cursor cursor = db.rawQuery(query, new String[]{correo, password});

        if (cursor.moveToFirst()) {
            rol = cursor.getString(0); // esto extraera el rol
        }

        cursor.close();
        return rol;
    }

    @SuppressLint("Range")
    public ArrayList<String> obtenerTodosLosUsuarios() {
        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Buscamos todos los correos en la tabla
        Cursor cursor = db.rawQuery("SELECT correo FROM Usuarios", null);

        if (cursor.moveToFirst()) {
            do {
                lista.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lista;
    }

    // Método para eliminar un usuario por su correo
    public boolean eliminarUsuario(String correo) {
        SQLiteDatabase db = this.getWritableDatabase();

        // 1. Primero borramos el registro en la tabla Roles (por la llave foránea)
        db.delete("Roles", "correo = ?", new String[]{correo});

        // 2. Luego borramos el registro en la tabla Usuarios
        int filasBorradas = db.delete("Usuarios", "correo = ?", new String[]{correo});

        // Si filasBorradas es mayor a 0, significa que se eliminó correctamente
        return filasBorradas > 0;
    }

    // =========================================================
    // MÉTODO AGREGADO: Insertar Usuario (Manteniendo tu lógica)
    // =========================================================
    public boolean insertarUsuario(String correo, String password, String rol) {
        SQLiteDatabase db = this.getWritableDatabase();

        // 1. Insertamos en la tabla Usuarios
        ContentValues valoresUsuario = new ContentValues();
        valoresUsuario.put("correo", correo);
        valoresUsuario.put("password", password);

        long idUsuario = db.insert("Usuarios", null, valoresUsuario);
        if (idUsuario == -1) {
            return false; // Falla si el correo ya existe
        }

        // 2. Insertamos en la tabla Roles
        ContentValues valoresRol = new ContentValues();
        valoresRol.put("rol", rol);
        valoresRol.put("correo", correo);

        long idRol = db.insert("Roles", null, valoresRol);

        return idRol != -1;
    }
}