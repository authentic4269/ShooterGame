package com.androidcontroller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import com.androidcontroller.MessageBinder;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
 
public class BluetoothService extends ServerConnection {

    private BluetoothAdapter mBtAdapter;
    private BluetoothSocket socket;
    private UUID MY_UUID = UUID.fromString("04c6093b-0000-1000-8000-00805f9b34fb");
    private static final String TAG = "BluetoothService";
    private HashMap<String, BluetoothDevice> map;
    
	private final IBinder mBinder = new MessageBinder(this);

    
    private OutputStreamWriter connectDeviceToSocket(BluetoothDevice bt)
    {
    	try {
    		try {
    			socket = bt.createRfcommSocketToServiceRecord(MY_UUID);
    		} catch (Exception e) {
			Log.d(TAG, e.toString());
    		}
    		Log.d(TAG, "about to connect");
    		mBtAdapter.cancelDiscovery();
    		socket.connect();
    		Log.d(TAG, "Connected!");
    		OutputStream stream = socket.getOutputStream();
    		OutputStreamWriter write = new OutputStreamWriter(stream);
    		write.write("sleep deprived", 0, 14);
    		write.flush();
    		return write;
    	} catch (IOException e) {
    		Log.d(TAG, e.toString() + " " + e.getStackTrace().toString());
    		return null;
    	}
    }


    private HashMap<String, BluetoothDevice> listDiscoverableDevices() {
        Set<BluetoothDevice> setOfDevices = mBtAdapter.getBondedDevices();
        HashMap<String, BluetoothDevice> devices = new HashMap<String, BluetoothDevice>();
        if (setOfDevices.isEmpty())
                Log.d(TAG, "NULL BLUETOOTH SET");
        for (BluetoothDevice bt : setOfDevices){
        	devices.put(bt.getName(), bt);
        }
        return devices;
    }
    
    public void kill() {
     try {
             socket.close();
     } catch (IOException e) {
             e.printStackTrace();
     }
    }

	public void initialize() {
		try {
			mBtAdapter = BluetoothAdapter.getDefaultAdapter();
			if (mBtAdapter == null) {
				Toast.makeText(BluetoothService.this,
						"Bluetooth not supported", Toast.LENGTH_SHORT).show();
				Log.d(TAG, "Bluetooth not supported.");
				this.stopService(null);
			}

		} catch (Exception e) {
			Log.e(TAG, "Error connecting to device", e);
			Toast.makeText(BluetoothService.this,
					"Error connecting to destkop application.",
					Toast.LENGTH_SHORT).show();
		}

		try {
			if (!mBtAdapter.isEnabled()) {
				mBtAdapter.enable();
			}
			while (true) {
				Log.d(TAG, "INSIDE WHILE STATE ON");
				if (mBtAdapter.getState() == BluetoothAdapter.STATE_ON)
					break;
			}

			boolean result = mBtAdapter.startDiscovery();
			Log.d(TAG, "Start discovery = " + result);

			while (true) {
				Log.d(TAG, "INSIDE WHILE IS DISCOVERING");
				if (mBtAdapter.isDiscovering() == true) {
					mBtAdapter.cancelDiscovery();
					break;
				}
			}
		} catch (Exception ex) {
			Log.d(TAG, ex.toString());
		}
	}

	@Override
	public OutputStreamWriter connectToServer(String serverName) {
		if (map.get(serverName) == null)
		{
			return null;
		}
		else
		{
			OutputStreamWriter res = connectDeviceToSocket(map.get(serverName));
			return res;
		}
	}

	@Override
	public String[] listOptions() {
		map = listDiscoverableDevices();
		Set<String> s = map.keySet();
		Iterator<String> it = s.iterator();
		String ret[] = new String[s.size()];
		int i = 0;
		while (it.hasNext())
		{
			ret[i] = it.next();
			i++;
		}
		return ret;
	}

	@Override
	public int sendJson(String json) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public IBinder onBind(Intent intent) {
		initialize();
		return mBinder;
	}
 
 
}