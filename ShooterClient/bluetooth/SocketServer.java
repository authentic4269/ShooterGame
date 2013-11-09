package bluetooth;
 
import hack.FishController;

import java.io.DataInputStream;
import java.io.IOException;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
 
public class SocketServer implements Runnable {
        public static LocalDevice mLocalDevice;
        public StreamConnectionNotifier connectionNotifier;
        public FishController controller;
        
        public static void main(String args[])
        {
        	SocketServer s = new SocketServer();
        	s.run();
        }
        
        public void connect() throws IOException, InterruptedException {
                try
                {
                UUID uuid = new UUID(80087355);
                String url = "btspp://localhost:" + uuid.toString() + ";name=RemoteBluetooth";
                Object c = Connector.open(url);
                connectionNotifier = (StreamConnectionNotifier) c;
                while(true){
                        start();
                }
                } catch (BluetoothStateException e) {
                        System.out.println("Bluetooth not enabled!\nPlease enable your bluetooth first!");
                }
    }
 
 
    public SocketServer(FishController fishController) {
    	controller = fishController;
	}
    
    public SocketServer() {
	}

	public void start() throws IOException {
   
        StreamConnection streamConnection = connectionNotifier.acceptAndOpen();
        DataInputStream is = streamConnection.openDataInputStream();
 
        byte[] bytes = new byte[1024];
        int r;
        while ((r = is.read(bytes)) > 0) {
                System.out.println(new String(bytes, 0, r));
        }
    }


	@Override
	public void run() {
		try {
    		connect();
    	} catch (Exception e)
    	{
    		System.out.println("Exception");
    	}
	}
 
}