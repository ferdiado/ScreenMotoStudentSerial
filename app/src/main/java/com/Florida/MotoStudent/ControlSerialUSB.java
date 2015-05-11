package com.Florida.MotoStudent;

import android.app.Activity;
import android.util.Log;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.util.HexDump;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by FADAD on 25/05/2015.
 */
public class ControlSerialUSB extends Activity{

    private UsbSerialDriver serialDriver;
    private ExecutorService mExecutor;
    private SerialInputOutputManager mSerialIoManager;

    private String textoAnterior= "";
    private int filas = 0;

    public ControlSerialUSB(UsbSerialDriver sDriver){
        serialDriver = sDriver;
        mExecutor = Executors.newSingleThreadExecutor();
    }

    private SerialInputOutputManager.Listener mListener = new SerialInputOutputManager.Listener(){
        @Override
        public void onRunError(Exception e) {
            Log.e("SerialInputOutputManager", "Runner parado por un error");
        }

        @Override
        public void onNewData(final byte[] data) {
            ControlSerialUSB.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateReceivedData(data);
                }
            });
        }
    };


    public void stopIoManager(){
        if(mSerialIoManager != null){
            Log.d("stopIoManager","Parando el IOManager...");
            mSerialIoManager.stop();
            mSerialIoManager = null;
        }
    }

    public void starIoManager(){
        if(serialDriver != null){
            Log.i("startIoManager","Iniciando IOManager...");
            mSerialIoManager = new SerialInputOutputManager(serialDriver, mListener);
            mExecutor.submit(mSerialIoManager);
        }
    }

    public void onDeviceStateChange(){
        stopIoManager();
        starIoManager();
    }

    public void updateReceivedData(byte[] data){
        String msg = HexDump.dumpHexString(data);
        boolean escribe= false;
        if(data.length>0){
            Log.d("updateReceivedData", "String leido:"+msg);
            escribe = true;
        }else{
            Log.d("updateReceivedData", "No hay datos");
        }
        if(escribe){
            InicioActivity.textUSB.setText(textoAnterior + "\n" + msg);
            filas++;
            if(filas == 5){
                textoAnterior = "";
            }else{
                textoAnterior = msg;
            }
        }
    }
}
