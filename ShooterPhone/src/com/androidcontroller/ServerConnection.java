package com.androidcontroller;

import java.io.OutputStreamWriter;

import android.app.Service;

public abstract class ServerConnection extends Service {
	public int sendJson(String json) {
		return -1;
	}
	
	private OutputStreamWriter connection;
	// listOptions provides the options, choose server name from among them, then call connecttoserver with it
	public OutputStreamWriter connectToServer(String serverName) {
		return null;
	}
	
	public String[] listOptions() { 
		return null;
	}
	
	public void kill() {
		return;
	}
}
