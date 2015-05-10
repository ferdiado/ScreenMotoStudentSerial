package com.example.screenmotostudentSerial;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
 

import com.hoho.android.usbserial.driver.UsbSerialProber;
 

 




 


import com.hoho.android.usbserial.util.SerialInputOutputManager;

import motostudent.GenerarBD;
import BaseDatos.BaseDatos;
import EnviarArchivo.SocketEnvio;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorJoiner.Result;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.screenmotostudentSerial.R;
import com.example.screenmotostudentSerial.ControlUSB;
import com.example.screenmotostudentSerial.SerialConsoleActivity;


public class MainActivity extends Activity {
    private Button insertarRPM;
    public TextView rpmActual, velocidad, temperatura;
	public static TextView textRxUSB;
    private ImageView imagenModo;
    private EditText textoRpm;
    private ToggleButton tb;
    private Button enviar;
    String rpmActualtext;
    int numerito;
    int rpm = 7499;
   // private static UsbSerialDriver Sdriver=null;
    private final String TAG = MainActivity.class.getSimpleName();
   // private SerialInputOutputManager mSerialIoManager;
    //private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private UsbManager myUsbManager;
    RelativeLayout layout1;
    DeviceEntry DispEntrada;
    UsbSerialDriver sDriver=null;
    UsbDevice device=null;
    List<DeviceEntry> mEntries;
   public static BaseDatos datosBd ;
    @Override
    @SuppressWarnings("deprecation")
  
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
       // generarDatos();
        velocidad = (TextView) findViewById(R.id.velocidad);
        temperatura = (TextView)findViewById(R.id.temperatura);
        insertarRPM = (Button)findViewById(R.id.insertarRpm);
        rpmActual = (TextView)findViewById(R.id.rmpActual);
        textoRpm = (EditText)findViewById(R.id.editText);
        textRxUSB=(TextView)findViewById(R.id.RXusb);
        imagenModo = (ImageView) findViewById(R.id.imageView);
        layout1 = (RelativeLayout) findViewById(R.id.layout);
        layout1.setBackgroundColor(Color.GREEN);
        myUsbManager=(UsbManager)getSystemService(Context.USB_SERVICE);
        datosBd=new BaseDatos(this,"DBmotostudent",null,1);
        SQLiteDatabase baseDatos=datosBd.getWritableDatabase();
		baseDatos.execSQL("DELETE FROM "+ "tablaDatos"+";");
       
      
        miThread();
        miThread2();

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

        String resultado = Settings.Global.getString(getContentResolver(), Settings.Global.AIRPLANE_MODE_ON);

        if(Integer.parseInt(resultado) == 0){
            imagenModo.setImageResource(R.drawable.ic_action_network_cell);

        }else{
            imagenModo.setImageResource(R.drawable.ic_action_airplane_mode_off);

        }
        Toast.makeText(this, "result:"+resultado, Toast.LENGTH_SHORT).show();

        tb=(ToggleButton)findViewById(R.id.toggleButton);  //Getting the Toggle Button Reference
        //Next few lines of Code for setting the status of Toggle Button(CHECKED or UNCHECKED) based on Current AIRPLANE Mode.

        //Getting the State of AirPlane Mode
        int status=Settings.System.getInt(getContentResolver(),Settings.System.AIRPLANE_MODE_ON, 0);
        if(status==1) //Means Airplane mode is ON
        {
            tb.setChecked(true);  //setting toggle Button as Checked
            Toast.makeText(getApplicationContext(), "Air Plane Mode is On",Toast.LENGTH_LONG).show();//Displaying Message to User
        }
        else  //if Airplane mode is OFF
        {
            tb.setChecked(false);//setting toggle Button as Unchecked
            Toast.makeText(getApplicationContext(), "Air Plane Mode is Off",Toast.LENGTH_LONG).show();//Displaying Message to User
        }
        
        
        
    }


    //Called every time when toggle button clicked
    @SuppressWarnings("deprecation")
    public void changeAirPlaneStatus(View v)
    {
        if(tb.isChecked())//means this is the request to turn ON AIRPLANE mode
        {
            Settings.System.putInt(getContentResolver(),Settings.System.AIRPLANE_MODE_ON, 1);//Turning ON Airplane mode.
            Toast.makeText(getApplicationContext(), "Air Plane Mode is On",Toast.LENGTH_LONG).show();//Displaying a Message to user
            Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);//creating intent and Specifying action for AIRPLANE mode.
            intent.putExtra("state", true);////indicate the "state" of airplane mode is changed to ON
            sendBroadcast(intent);//Broadcasting and Intent

        }
        else//means this is the request to turn OFF AIRPLANE mode
        {
            Settings.System.putInt(getContentResolver(),Settings.System.AIRPLANE_MODE_ON, 0);//Turning OFF Airplane mode.
            Toast.makeText(getApplicationContext(), "Air Plane Mode is Off",Toast.LENGTH_LONG).show();//Displaying a Message to user
            Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);//creating intent and Specifying action for AIRPLANE mode.
            intent.putExtra("state", false);//indicate the "state" of airplane mode is changed to OFF
            sendBroadcast(intent);//Broadcasting and Intent
        }
    }


    /*
    *Trheads para lanzar la velocidad temperatura y cambio de revoluciones
    */


    final Handler handler = new Handler();
    final Handler handle = new Handler();
    protected void miThread(){
        Thread t = new Thread(){
            public void run(){
                try {
                    Thread.sleep(1000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                handler.post(proceso);
            }
        };
        t.start();
    }
    protected void miThread2(){
        Thread t2 = new Thread(){
            public void run(){
               try {
                        Thread.sleep(1000);
               }catch (InterruptedException e){
                   e.printStackTrace();
               }
                handle.post(proceso2);
           }
        };
        t2.start();
    }
    final Runnable proceso2 = new Runnable() {
        @Override
        public void run() {
            velocidad.setText(""+150+"Km/h");
            temperatura.setText(""+60+"º");
        }
    };
    final Runnable proceso = new Runnable() {
        @Override
        public void run() {
            insertarRPM.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rpmActualtext = textoRpm.getText().toString();
                    numerito = Integer.parseInt(rpmActualtext);

                    if(numerito > 0 && numerito< 4000){
                        layout1.setBackgroundColor(Color.GREEN);
                    }
                    if (numerito>4000 && numerito < 7500) {

                        layout1.setBackgroundColor(Color.YELLOW);
                        //rpmActual.setText(textoRpm.getText().toString());

                    }if(numerito > 7500) {
                        //layout1 = (RelativeLayout) findViewById(R.id.layout);
                        layout1.setBackgroundColor(Color.RED);

                    }
                }
            });
        }
    };


        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void generarDatos(){
    	
		
		 
		SQLiteDatabase baseDatos=datosBd.getWritableDatabase();
		baseDatos.execSQL("DELETE FROM "+ "tablaDatos"+";");//borramos los datos de la base anterior
		if(baseDatos!=null)
		{
			for(int i=1;i<=5000;i++){
				int id=i;
				String nombre="Sensor 1= "+i;
				//baseDatos.execSQL("INSERT INTO tablaDatos(id,nombre)"+  "VALUES (" + id + ", '" + nombre +"')" );
				 
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
		//resultado.setText("");
		//Nos aseguramos de que existe al menos un registro
		if (c.moveToFirst()) {
		     //Recorremos el cursor hasta que no haya m�s registros
		     do {
		          String id= c.getString(0);
		          String nombre = c.getString(1);
		          //resultado.append("id:"+" " + id + " - " + nombre + "\n");
		     } while(c.moveToNext());
		}
		baseDatos.close();
    }
    
    public static void insertaDatos(int id,String nombre){
    	SQLiteDatabase baseDatos=datosBd.getWritableDatabase();
    	if(baseDatos!=null)
		{
    	
    	baseDatos.execSQL("INSERT INTO tablaDatos(id,nombre)"+  "VALUES (" + id + ", '" + nombre +"')" );
   
		}
    	 baseDatos.close();
    }
    
    //**************************************************USB SERIE***********************************
    @Override
    protected void onResume() {
        super.onResume();
         
        refreshDeviceList();
        // mEntries = new ArrayList<DeviceEntry>();
      
        //myControlUsb.onDeviceStateChange();
    }

    /** Simple contenedor para UsbDevice y su  driver. */
   public static class DeviceEntry {
        public UsbDevice getDevice() {
			return device;
		}

		public void setDevice(UsbDevice device) {
			this.device = device;
		}

		public UsbSerialDriver getDriver() {
			return driver;
		}

		public void setDriver(UsbSerialDriver driver) {
			this.driver = driver;
		}

		public UsbDevice device;
        public UsbSerialDriver driver;

        DeviceEntry(UsbDevice device, UsbSerialDriver driver) {
            this.device = device;
            this.driver = driver;
        }
        
    }
   private void  refreshDeviceList() {
     

       new AsyncTask<Void, Void, List<DeviceEntry>>() {
           @Override
           protected List<DeviceEntry> doInBackground(Void... params) {
               Log.d(TAG, "Refreshing device list ...");
               SystemClock.sleep(1000);
               final List<DeviceEntry> result = new ArrayList<DeviceEntry>();
               for (final UsbDevice device : myUsbManager.getDeviceList().values()) {
                   final List<UsbSerialDriver> drivers =
                           UsbSerialProber.probeSingleDevice(myUsbManager, device);
                   Log.d(TAG, "Found usb device: " + device);
                   if (drivers.isEmpty()) {
                       Log.d(TAG, "  - No UsbSerialDriver available.");
                       result.add(new DeviceEntry(device, null));
                   } else {
                       for (UsbSerialDriver driver : drivers) {
                           Log.d(TAG, "  + " + driver);
                           result.add(new DeviceEntry(device, driver));
                       }
                   }
               }
               
               return result;
           }

           @Override
           protected void onPostExecute(List<DeviceEntry> result) {
        	if(result!=null){
        	   DispEntrada = result.get(0);
        	   sDriver= DispEntrada.getDriver();
        	  // ControlUSB myControlUsb=new ControlUSB(sDriver);
               Log.d(TAG, "Resumed, sDriver=" + sDriver);
               if (sDriver == null) {
                 //  mTitleTextView.setText("No serial device.");
               } else {
                   try {
                	   
                       sDriver.open();
                      // sDriver.setDTR(true);
                       sDriver.setParameters(115200, 8, UsbSerialDriver.STOPBITS_1, UsbSerialDriver.PARITY_NONE);
                   } catch (IOException e) {
                       Log.e(TAG, "Error setting up device: " + e.getMessage(), e);
                    //   mTitleTextView.setText("Error opening device: " + e.getMessage());
                       try {
                           sDriver.close();
                       } catch (IOException e2) {
                           // Ignore.
                       }
                       sDriver = null;
                       return;
                   }
                    ControlUSB myControlUsb=new ControlUSB(sDriver);
                  // showConsoleActivity( sDriver);
                    myControlUsb.onDeviceStateChange();
                   
               }
                 
               
        	}    
           }

       }.execute((Void) null);
       
       
   }
  
   private void showConsoleActivity(UsbSerialDriver driver) {
       SerialConsoleActivity.show(this, driver);
   }

}
