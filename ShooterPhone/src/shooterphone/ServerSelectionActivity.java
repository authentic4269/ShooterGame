package shooterphone;

import java.util.ArrayList;

import com.example.shooterphone.R;

import androidcontroller.BluetoothService;
import androidcontroller.MessageBinder;
import androidcontroller.ServerConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ServerSelectionActivity extends ListActivity {
	private long currentSelection = -1;
	ArrayList<String> computers;
    ArrayAdapter<String> adapter;
    ServerConnection conn;
    boolean mBound = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_selection);
        computers = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, R.layout.simple_list_item_activated_1, computers);
		setListAdapter(adapter);
		
	}
	
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            conn = ((MessageBinder) service).getService();
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
        Intent intent = new Intent(this, BluetoothService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
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
		currentSelection = id;
	}
	
	public void connectToComputer(View v) {
		if(currentSelection != -1) {
			// Do whatever you do when you click on the button to connect to the computer
			Context context = getApplicationContext();
			CharSequence text = computers.get((int) currentSelection);
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}
}
