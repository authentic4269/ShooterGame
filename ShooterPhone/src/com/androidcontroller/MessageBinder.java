package com.androidcontroller;

import android.os.Binder;

public class MessageBinder extends Binder {
	ServerConnection serv;
    public ServerConnection getService() {
        // Return this instance of LocalService so clients can call public methods
        return serv;
    }
    
    public MessageBinder() {
    	serv = null;
    }
    
    public MessageBinder(ServerConnection s) {
    	serv = s;
    }
    
}
