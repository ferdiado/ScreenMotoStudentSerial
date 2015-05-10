package motostudent;

 
import com.example.screenmotostudentSerial.R;

import EnviarArchivo.*;
import BaseDatos.*;
import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
 

public class GenerarBD extends Activity {
	
	private Button enviar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		enviar = (Button)findViewById(R.id.button1);
		 //Button press event listener
		 enviar.setOnClickListener (new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			SocketEnvio socket=new SocketEnvio();
			Thread t1=new Thread(socket);
			t1.start();
			 
			}
		});
		/* 
		BaseDatos datosBd=new BaseDatos(this,"DBmotostudent",null,1);
		// db.execSQL("DROP TABLE IF EXISTS tablaDatos");
		 
		SQLiteDatabase baseDatos=datosBd.getWritableDatabase();
		baseDatos.execSQL("DELETE FROM "+ "tablaDatos"+";");//borramos los datos de la base anterior
		if(baseDatos!=null)
		{
			for(int i=1;i<=5000;i++){
				int id=i;
				String nombre="Sensor 1= "+i;
				baseDatos.execSQL("INSERT INTO tablaDatos(id,nombre)"+  "VALUES (" + id + ", '" + nombre +"')" );
				 
			}
			 baseDatos.close();
		}
		 baseDatos=datosBd.getWritableDatabase();
		
		//String[] campos = new String[] {"id", "nombre"};
		//String[] args = new String[] {"Usuario 4"}; seleccionar un registro
		//Cursor c = baseDatos.query("tablaDatos", campos,"nombre=?",args, null, null, null);
		String[] campos = new String[] {"id", "nombre"};
		//                                              where arg  groupby habving order by    
		Cursor c = baseDatos.query("tablaDatos", campos,null,null, null, null, null);//seleccionando todos los registros
		resultado.setText("");
		//Nos aseguramos de que existe al menos un registro
		if (c.moveToFirst()) {
		     //Recorremos el cursor hasta que no haya m�s registros
		     do {
		          String id= c.getString(0);
		          String nombre = c.getString(1);
		          resultado.append("id:"+" " + id + " - " + nombre + "\n");
		     } while(c.moveToNext());
		}
		baseDatos.close();*/
		 
	}
 
}
