package EnviarArchivo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;

public class SocketEnvio implements Runnable {
	int counter = 0;
    static final int UPDATE_INTERVAL = 1;
    private Timer timer = new Timer();
    
	@Override
	public void run() {
		Socket cliente;
		 File file;
		 
			
		 
			 
		 
		try {
			
		    //cliente = new Socket("192.168.10.32", 5678);
			cliente = new Socket("192.168.10.5", 5678);
		    HacerCadaMiliseg(); 
        	//file = new File("sdcard/Caldera/Sem 18");
        	file = new File("data/data/com.example.screenmotostudent/databases/DBmotostudent");
			Log.e("TAG","dentro");
	        Log.e("TAG",(String.valueOf(file.length() )));
	        byte[] mybyteArray = new byte[(int) file.length()]; //crea un array d bytes 	        
	        //FileInputStream fileInputStream = new FileInputStream("sdcard/Caldera/Sem 18");
	        FileInputStream fileInputStream = new FileInputStream("data/data/com.example.screenmotostudent/databases/DBmotostudent");
			BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);  
		    bufferedInputStream.read(mybyteArray, 0, mybyteArray.length); //leemos el archivo 
		    OutputStream outputStream = cliente.getOutputStream();	 
		    outputStream.write(mybyteArray, 0, mybyteArray.length); //write file to the output stream byte by byte
		    outputStream.flush();
		    bufferedInputStream.close();
		    outputStream.close();
	        cliente.close();
	       
	           
	          // text.setText("File Sent");
	      
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("TAG","ERROR:"+e);
		}
	    
	

 }
	
	 private void HacerCadaMiliseg() {
	        timer.scheduleAtFixedRate(new TimerTask() {
	 
	            @Override
	            public void run() {
	                // TODO Auto-generated method stub
	               // Log.d("han pasado" , String.valueOf(++counter)+"ms");
	            }
	 
	        }, 0, UPDATE_INTERVAL);
	    }
}
