package com.shooterphone;


import com.shooterphone.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

public class ShootingActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hunting_screen);
	}
	
	public void shoot(View v) {
		final ImageView background = (ImageView) findViewById(R.id.revolver);
		background.setImageResource(R.raw.revolver2);
		
		Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               background.setImageResource(R.raw.revolver);

            }
        }, 500);
	}
}
