package com.androidcontroller;

import android.hardware.*;

public interface DataCallback {
	public void processData(double data[], int typeRotationVector);
}
