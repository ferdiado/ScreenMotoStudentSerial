package com.Florida.MotoStudent;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by FADAD on 25/05/2015.
 */
public class USBSerial {

    UsbManager myUsbManager;
    ControlSerialUSB myControlSerialUSB;
    DeviceEntry dispEntrada;
    UsbSerialDriver serialDriver;
    int baudios, dataBits, stopBits, parity;

    public USBSerial(UsbManager usbMan, int baud, int dBits, int sBits, int par){
        myUsbManager = usbMan;
        baudios = baud;
        dataBits = dBits;
        stopBits = sBits;
        parity = par;
    }

    public static class DeviceEntry{

        public UsbDevice device;
        public UsbSerialDriver driver;

        DeviceEntry(UsbDevice deviceUSB, UsbSerialDriver driverUSB){
            device = deviceUSB;
            driver = driverUSB;
        }

        public UsbDevice getDevice(){
            return device;
        }

        public void setDevice(UsbDevice deviceUsb){
            device = deviceUsb;
        }

        public UsbSerialDriver getDriver(){
            return driver;
        }

        public void setDriver(UsbSerialDriver driverUSB){
            driver = driverUSB;
        }

    }

    public void refreshDeviceList(){
        Log.d("refresDeviceList","Leemos la lista de dispositivos");
        new AsyncTask<Void,Void,List<DeviceEntry>>(){
            @Override
            protected List<DeviceEntry> doInBackground(Void... params) {
                Log.d("refresDeviceList","Refrescando dispositivos USB");
                SystemClock.sleep(1000);
                final List<DeviceEntry> result = new ArrayList<DeviceEntry>();
                for(final UsbDevice device: myUsbManager.getDeviceList().values()){
                    final List<UsbSerialDriver> drivers = UsbSerialProber.probeSingleDevice(myUsbManager,device);
                    Log.d("refrestDeviceList","Encontrado dispositivo "+device);
                    if(drivers.isEmpty()){
                        Log.d("refrestDeviceList","UsbSerialDriver no disponible");
                        result.add(new DeviceEntry(device,null));
                    }else{
                        for(UsbSerialDriver driver: drivers){
                            Log.d("refrestDeviceList","USBSerialDriver "+driver);
                            result.add(new DeviceEntry(device,driver));
                        }
                    }
                }
                return result;
            }

            @Override
            protected void onPostExecute(List<DeviceEntry> result) {
                if(result.isEmpty()){
                    dispEntrada = result.get(0);
                    serialDriver = dispEntrada.getDriver();
                    Log.d("refrestDeviceList","Resumido=>USBSerial Driver:"+serialDriver);
                    if(serialDriver != null){
                        try{
                            serialDriver.open();
                            Log.d("refrestDeviceList", "abriendo serial Driver"+serialDriver);
                            serialDriver.setParameters(baudios,dataBits,stopBits,parity);
                            Log.d("refrestDeviceList","Configurando los datos del serial");
                        }catch (IOException e){
                            Log.e("refrestDeviceList","Error setting up device "+e.getMessage(), e);
                            try{
                                serialDriver.close();
                            }catch (IOException e2){
                                Log.e("refrestDeviceList","No se ha podido cerrar el dispositivo");
                            }
                            serialDriver = null;
                            return;
                        }
                        myControlSerialUSB = new ControlSerialUSB(serialDriver);
                        myControlSerialUSB.onDeviceStateChange();
                    }
                }
            }
        }.execute((Void) null);
    }

    public void stopUSBSerial(){
        if(myControlSerialUSB != null){
            myControlSerialUSB.stopIoManager();
            try {
                serialDriver.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
