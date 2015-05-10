package BaseDatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class BaseDatos extends SQLiteOpenHelper {
	 
	String crearTabla="CREATE TABLE  tablaDatos(id INTEGER,nombre TEXT)";
	private static String DB_PATH = "/data/data/com.aguilax.motostudent/databases/";
    //private static String DB_NAME = "db_calc";
	public BaseDatos(Context contexto, String nombre, CursorFactory factory,int version) {
		super(contexto, nombre, factory, version);
		 
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		 //Creacion de la tabla la primera vez
		db.execSQL(crearTabla);
		
	} 

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		db.execSQL("DROP TABLE IF EXISTS tablaDatos");
	    db.execSQL(crearTabla);
		
	}

}
