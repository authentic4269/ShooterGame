package com.androidcontroller;

import java.util.LinkedList;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;

public class AccelerometerService extends Service implements SensorEventListener, DataService {

	protected final int INVERSE_DRAW_FREQUENCY = 1000;
	protected static final int HISTORY_SIZE = 50;
	protected static final int ROTATIONAL_SAMPLING_FREQUENCY = SensorManager.SENSOR_DELAY_NORMAL;
	protected static final int ACCELERATION_SAMPLING_FREQUENCY = SensorManager.SENSOR_DELAY_NORMAL;
	// Smaller history size gives more accuracy and faster processing
	protected boolean LOCATION = false;
	private SensorManager mSensorManager;
	private Sensor mRotationVectorSensor;
	private Sensor mLinearAccelerationSensor;
	private long START = System.currentTimeMillis();
	protected String ID;//
	
	private SimpleXYSeries xRotationSeries = null;
	private SimpleXYSeries yRotationSeries = null;
	private SimpleXYSeries zRotationSeries = null;
	private SimpleXYSeries xAccelerationSeries = null;
	private SimpleXYSeries yAccelerationSeries = null;
	private SimpleXYSeries zAccelerationSeries = null;
	
	private DataCallback callback = null;
	
	private double xRotationAvg = 0.0;
	private double yRotationAvg = 0.0;
	private double zRotationAvg = 0.0;
	private int xRotationCount = 0;
	private int yRotationCount = 0;
	private int zRotationCount = 0;
	private LinkedList<Double> xRotationData = new LinkedList<Double>();
	private LinkedList<Double> yRotationData = new LinkedList<Double>();
	private LinkedList<Double> zRotationData = new LinkedList<Double>();
	
	private final IBinder mBinder = new DataBinder();
	
    public class DataBinder extends Binder {
        AccelerometerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return AccelerometerService.this;
        }
    }
	
	@Override
	public IBinder onBind(Intent arg0) {
		initialize();
		return mBinder;
	}
	
	public void onCreate() {
		initialize();
	}
	
	private void initialize()
	{
        mSensorManager = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        mRotationVectorSensor = mSensorManager.getDefaultSensor(
    			Sensor.TYPE_ROTATION_VECTOR);
        mLinearAccelerationSensor = mSensorManager.getDefaultSensor(
    			Sensor.TYPE_LINEAR_ACCELERATION);
        
   		mSensorManager.registerListener(this, mRotationVectorSensor, ROTATIONAL_SAMPLING_FREQUENCY);
		mSensorManager.registerListener(this, mLinearAccelerationSensor, ACCELERATION_SAMPLING_FREQUENCY);
		xRotationData.addLast(0.0);
		yRotationData.add(0.0);
		zRotationData.add(0.0);
	    final Handler h = new Handler();
	    h.postDelayed(new Runnable()
	    {
	        @Override
	        public void run()
	        {
	            h.postDelayed(this, INVERSE_DRAW_FREQUENCY);
	        }
	    }, INVERSE_DRAW_FREQUENCY);

        xRotationSeries = new SimpleXYSeries("X Rotation");
        yRotationSeries = new SimpleXYSeries("Y Rotation");
        zRotationSeries = new SimpleXYSeries("Z Rotation");
        yAccelerationSeries = new SimpleXYSeries("Y Linear Acceleration");
        zAccelerationSeries = new SimpleXYSeries("Z Linear Acceleration");
        xAccelerationSeries = new SimpleXYSeries("X Linear Acceleration");
	}

 
	public void stop() {
		// make sure to turn our sensor off when the activity is paused
		mSensorManager.unregisterListener(this);
	}
	
	public void installCallback(DataCallback call) {
		this.callback = call;
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// we received a sensor event. it is a good practice to check
		// that we received the proper event
		if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
			// convert the rotation-vector to a 4x4 matrix. the matrix
			// is interpreted by Open GL as the inverse of the
			// rotation-vector, which is what we want.
			if (xRotationSeries.size() > HISTORY_SIZE) {
				
				// Stuff you told me to add
				xRotationSeries.removeFirst();
				yRotationSeries.removeFirst();
				zRotationSeries.removeFirst();
			}
			else {
				xRotationCount++;
				yRotationCount++;
				zRotationCount++;
			}

			// Stuff you wanted me to add
			double dif[] = new double[3];
			dif[0] = Math.abs(event.values[0] - xRotationData.getLast() % 180) - xRotationAvg * 0.8;
			dif[1] = Math.abs(event.values[1] - yRotationData.getLast() % 180) - yRotationAvg * 0.8;
			dif[2] = Math.abs(event.values[2] - zRotationData.getLast() % 180) - zRotationAvg * 0.8;
			

			
			xRotationAvg += dif[0];
			xRotationAvg /= xRotationCount;
			yRotationAvg += dif[1];
			yRotationAvg /= yRotationCount;
			zRotationAvg += dif[2];
			zRotationAvg /= zRotationCount;
			
			xRotationData.addLast((double) event.values[0]);
			yRotationData.addLast((double) event.values[1]);
			zRotationData.addLast((double) event.values[2]);
			if (callback != null)
				this.callback.processData(dif, Sensor.TYPE_ROTATION_VECTOR);
		}
		if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
			if (yAccelerationSeries.size() > HISTORY_SIZE) {
				
				//Stuff you told me to add
				yAccelerationSeries.removeFirst();
				zAccelerationSeries.removeFirst();
				xAccelerationSeries.removeFirst();
			}
			
			//Stuff you told me to add
			xAccelerationSeries.addLast(System.currentTimeMillis() - START, event.values[0]);
			yAccelerationSeries.addLast(System.currentTimeMillis() - START, event.values[1]);
			zAccelerationSeries.addLast(System.currentTimeMillis() - START, event.values[2]);

		}
	}


	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

}
