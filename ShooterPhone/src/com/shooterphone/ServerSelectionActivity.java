package com.shooterphone;

import java.util.ArrayList;

import com.androidcontroller.BluetoothService;
import com.androidcontroller.MessageBinder;
import com.androidcontroller.ServerConnection;

import android.os.Bundle;
import android.os.IBinder;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ServerSelectionActivity extends ListActivity {
	private String currentServerName = "";
	private View currentServerView = null;
	ArrayList<String> computers;
    ArrayAdapter<String> adapter;
    ServerConnection conn;
    Button scanButton;
    Button startButton;
    Button connectButton;
    private boolean connected = false;
    boolean mBound = false;
    IBinder binder = new MessageBinder();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		long id = R.layout.activity_server_selection;
        setContentView(R.layout.activity_server_selection);
        computers = new ArrayList<String>();
       adapter = new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1, computers);
       scanButton = (Button) findViewById(R.id.scan);
       startButton = (Button) findViewById(R.id.start);
       connectButton = (Button) findViewById(R.id.connect);
       Intent intent = new Intent(this, BluetoothService.class);
       bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        setListAdapter(adapter);
        
        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String s[] = conn.listOptions();
                adapter.clear();
                int i;
                for (i = 0; i < s.length; i++)
                {
                	adapter.add(s[i]);
                }
                adapter.notifyDataSetChanged();
            }
        });
        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	if (connected)
            	{
            		switchActivity();
            	}
            	else
            	{
                    Context context = getApplicationContext();
                    CharSequence text = "You must connect to your computer before starting. Make sure that the client is running.";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
            	}
            }
        });
        connectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (currentServerName != "" && conn != null)
                {
                	
                	if (conn.connectToServer(currentServerName) != null)
                	{
                		connected = true;
                		Context context = getApplicationContext();
                        CharSequence text = "Connected to " + currentServerName;
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                        currentServerView.setBackgroundResource(android.R.color.holo_red_light);
                	}
                	else
                	{
                        Context context = getApplicationContext();
                        CharSequence text = "Error connecting to: " + currentServerName;
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                	}
                }
            }
        });
		
		
	}
	
	private void switchActivity() 
	{
		 Intent intent = new Intent(this, ShootingActivity.class);
		 startActivity(intent);
	}
	
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
        	
        	MessageBinder m = (MessageBinder) service;
        	conn = m.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
	
    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService

    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (currentServerName != "")
		{
			currentServerView.setBackgroundResource(0);
		}
		currentServerName = adapter.getItem(position);
		currentServerView = v;
		v.setBackgroundResource(android.R.color.holo_green_light);
		
	}
	
}
