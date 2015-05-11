package com.example.screenmotostudentSerial;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import GrabarDatos.GrabarLererTarjeta;
import android.app.Activity;
import android.util.Log;

import com.hoho.android.usbserial.driver.UsbSerialDriver; 
 
import com.hoho.android.usbserial.util.HexDump;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

//activity sin onCreate
public class ControlUSB  extends Activity    {
	 private final String TAG = ControlUSB.class.getSimpleName();
	 private static UsbSerialDriver sDriver = null;
	 private final ExecutorService mExecutor ;
	 private SerialInputOutputManager mSerialIoManager;
	 
	 int cont=0;
	 private SerialInputOutputManager .Listener mListener =
	         new SerialInputOutputManager.Listener() {

	     @Override
	     public void onRunError(Exception e) {
	         Log.d(TAG, "Runner stopped.");
	     }

	     @Override
	     public void onNewData(final byte[] data) {
	    	 ControlUSB .this.runOnUiThread(new Runnable() {
	             @Override
	             public void run() {
	            	// String datosAnt=tarjeta.recuperaDatos("bd1");
	            	 //tarjeta.grabarDatos("bd1", datosAnt, Integer.toString(data.length));
					//MainActivity.textRxUSB.setText(data.length+"");
	            	 
					 
	            	 
	                  updateReceivedData(data);
	             }
	         });
	     }
	 }; 
 	 	 	 
public ControlUSB( UsbSerialDriver sDriver){
	mExecutor = Executors.newSingleThreadExecutor();
	 //Toast.makeText(this, "updateRx", Toast.LENGTH_LONG).show();
	this.sDriver=sDriver;
	
}

	    private void stopIoManager() {
	        if (mSerialIoManager != null) {
	            Log.i(TAG, "Stopping io manager ..");
	            mSerialIoManager.stop();
	            mSerialIoManager = null;
	        }
	    }

	private void startIoManager() {
	        if (sDriver != null) {
	            Log.i(TAG, "Starting io manager ..");
	             mSerialIoManager = new SerialInputOutputManager(sDriver, mListener);
	            mExecutor.submit(mSerialIoManager);
	        }
	    }
	 public void onDeviceStateChange() {
	        stopIoManager();
	        startIoManager();
	    }

	 public   void updateReceivedData(byte[] data) {
		 String message=  HexDump.dumpHexString(data) ;//"Read " + data.length + " bytes: \n"//String message=Integer.toString(data.length) ; 
		 
		 /* for (int x=0;x<data.length;x++){
	       //message=Byte.toString(data[x]);  
	      message= new String(data, 0, x);
	      }
	       String message="No hay datos";
	      for(int x=0;x<data.length;x++){
	          message=message+Byte.valueOf(data[x]);
	         
	      }*/
	      //String delimitador="[#]";
	     // String[] arrayMessage=message.split(delimitador);
	     // mDumpTextView.setText(message);
	     // mScrollView.smoothScrollTo(0, mDumpTextView.getBottom());
	     // Toast.makeText(this, "updateRx", Toast.LENGTH_LONG).show();
	      MainActivity.textRxUSB.setText( message);
	      MainActivity.insertaDatos(cont, message);
	      cont++; 
	  }
	 
}
 
 
