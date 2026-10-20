package controlador.BaseDeDatos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AsistenciaIquique.db";
    // Usamos la versión 2 para forzar a que Android borre cualquier prueba anterior
    // y cargue estos nuevos usuarios que vas a insertar.
    private static final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. Creamos la tabla de usuarios
        db.execSQL("CREATE TABLE Usuarios (id INTEGER PRIMARY KEY AUTOINCREMENT, correo TEXT, password TEXT)");

        // 2. Insertamos tu cuenta real y la del profesor (puedes cambiar estos datos)
        db.execSQL("INSERT INTO Usuarios (correo, password) VALUES ('francoalonsokaled@gmail.com', 'HCar2006.')");
        db.execSQL("INSERT INTO Usuarios (correo, password) VALUES ('profe@santotomas.cl', '123456')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Si cambiamos la versión, borra la tabla vieja y vuelve a ejecutar onCreate
        db.execSQL("DROP TABLE IF EXISTS Usuarios");
        onCreate(db);
    }

    // Método para verificar si el correo y la clave coinciden
    public boolean validarLogin(String correo, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Usuarios WHERE correo = ? AND password = ?", new String[]{correo, password});

        boolean existe = cursor.getCount() > 0;
        cursor.close();
        return existe;
    }
}
