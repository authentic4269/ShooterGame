package hack;

//import bluetooth.*;
import javax.bluetooth.*;

import java.io.DataInputStream;
import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import bluetooth.SocketServer;

public class FishController {
	private Thread viewthread;
	private Thread bluetoothServerThread;
	private static FishView view;
	private static FishModel model;

	
	public static void main(String args[]){
		
		//FishView view = new FishView();
		//FishModel model = new FishModel();
		FishController controller = new FishController();
		view = new FishView();
		model = new FishModel();
	}
	
	public FishController()
	{
		bluetoothServerThread = new Thread(new SocketServer(this), "Socket Server");
		bluetoothServerThread.start();

		//viewthread = new Thread(view1, "View Thread");
		
		//viewthread.start();
	}


}

