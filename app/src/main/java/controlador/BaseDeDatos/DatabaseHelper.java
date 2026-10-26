package controlador.BaseDeDatos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AsistenciaIquique.db";
    // Usamos la versión 3 para forzar a que Android borre cualquier prueba anterior
    // y cargue estos nuevos usuarios que vas a insertar.
    private static final int DATABASE_VERSION = 3;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. Creamos la tabla de usuarios y roles
        db.execSQL("CREATE TABLE Usuarios (id INTEGER PRIMARY KEY AUTOINCREMENT, correo TEXT UNIQUE, password TEXT)");
        db.execSQL("CREATE TABLE Roles (rol TEXT PRIMARY KEY, correo TEXT, FOREIGN KEY (correo) REFERENCES Usuarios(correo))");
        // 2. Insertamos mi cuenta real y la del profesor (puedes cambiar estos datos)
        db.execSQL("INSERT INTO Usuarios (correo, password) VALUES ('francoalonsokaled@gmail.com', 'HCar2006.')");
        db.execSQL("INSERT INTO Usuarios (correo, password) VALUES ('profe@santotomas.cl', '1234567')");
        // 3. Insertamos los roles a ambas cuentas
        db.execSQL("INSERT INTO Roles (rol, correo) VALUES ('Administrador', 'profe@santotomas.cl')");
        db.execSQL("INSERT INTO Roles (rol, correo) VALUES ('Usuario', 'francoalonsokaled@gmail.com')");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Si cambiamos la versión, borra la tabla vieja y vuelve a ejecutar onCreate
        db.execSQL("DROP TABLE IF EXISTS Usuarios");
        db.execSQL("DROP TABLE IF EXISTS Roles");
        onCreate(db);
    }


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
}
