package GrabarDatos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import android.os.Environment;


public class GrabarLererTarjeta {
	
	public String recuperaDatos(String nomArchivo){
		File file=new File("sdcard/Moto/");
		 if(!file.exists()){
	            file.mkdirs();
	        }
		File tarjeta = Environment.getExternalStorageDirectory();
		  file =  (new File (file.getAbsoluteFile(), nomArchivo));
		String todo = "";
		try {
			FileInputStream fIn = new FileInputStream(file);
			InputStreamReader archivo = new InputStreamReader(fIn);
			BufferedReader br = new BufferedReader(archivo);
			String linea = br.readLine();
			
			while (linea != null) {
				todo = todo + linea + "\n";
				linea = br.readLine();
				
			}
			br.close();
			archivo.close();
			

		} catch (IOException e) {
		}
		return todo;
		
	}
	public void grabarDatos(String nomArchivo,String datosAnt,String datosNuevos){
      
		 
		String contenido=datosAnt+datosNuevos;
		try {
			File file=new File("sdcard/Moto/");
	        if(!file.exists()){
	            file.mkdirs();
	        }
			File tarjeta = Environment.getExternalStorageDirectory();
			file = new File(file.getAbsoluteFile() , nomArchivo);
			OutputStreamWriter osw = new OutputStreamWriter(
					new FileOutputStream(file));
			osw.write(contenido);
			osw.flush();
			osw.close();
			
			
		} catch (IOException ioe) {
		}
	}
}
