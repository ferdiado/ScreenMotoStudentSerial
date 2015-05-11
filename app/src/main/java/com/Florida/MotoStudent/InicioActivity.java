package com.Florida.MotoStudent;

import android.app.Activity;
import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.screenmotostudentSerial.R;
import com.hoho.android.usbserial.driver.UsbSerialDriver;

public class InicioActivity extends Activity {

    private UsbManager myUsbManager;
    private USBSerial myUsbSerial;

    Button coneUSB, descUSB, grabGPS, stopGPS;
    public static TextView textUSB, textGPS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);

        coneUSB = (Button) findViewById(R.id.conectarUSB);
        coneUSB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textUSB.setText(R.string.conectarUSB_text);
                Log.d("conectaUSB", "Iniciamos el USBManager");
                myUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
                Log.d("conectaUSB", "Iniciamos el USB");
                myUsbSerial = new USBSerial(myUsbManager, 115200, 8, UsbSerialDriver.STOPBITS_1,UsbSerialDriver.PARITY_NONE);
                Toast.makeText(getApplicationContext(),"conectando la comunicación",Toast.LENGTH_LONG).show();
                myUsbSerial.refreshDeviceList();
            }
        });
        descUSB = (Button) findViewById(R.id.desconectarUSB);
        descUSB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("desconectarUSB","Desconectamos elUSBManaer");
                myUsbManager = null;
                myUsbSerial.stopUSBSerial();
                textUSB.setText(R.string.desconectarUSB_text);
            }
        });
        grabGPS = (Button) findViewById(R.id.grabaGPS);
        stopGPS = (Button) findViewById(R.id.detieneGPS);
        textUSB = (TextView) findViewById(R.id.contenedorTextoUSB);
        textGPS = (TextView) findViewById(R.id.contenedorTextoGPS);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inicio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
