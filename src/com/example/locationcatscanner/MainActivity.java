package com.example.locationcatscanner;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends ActionBarActivity {

	private static final String TAG = "MainActivity";

	RelativeLayout mLayout = null;
	
	TextView mPositionValue = null;
	ToggleButton mNetworkButton = null;
	ToggleButton mSatelliteButton = null;
	ToggleButton mAutoButton = null;

	LocationManager locMan = null;
	
	boolean mGpsInUse = false;
	boolean mNetworkInUse = false;
	boolean mAutoInUse = true;
	
	LocationListener locListener;
	
	long MIN_TIME = 1000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mLayout = (RelativeLayout)RelativeLayout.inflate(this, R.layout.activity_main, null);

		Log.d(TAG, "***onCreate***");

		locMan = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		if (locMan != null) {
			 locListener = new LocationListener() {

				@Override
				public void onStatusChanged(String provider, int status,
						Bundle extras) {
					Log.d(TAG, "***onStatusChanged to " + status);

				}

				@Override
				public void onProviderEnabled(String provider) {

				}

				@Override
				public void onProviderDisabled(String provider) {
				}

				@Override
				public void onLocationChanged(Location location) {
					Log.d(TAG,
							"***onLocationChanged to lat "
									+ location.getLatitude() + ", long "
									+ location.getLongitude());
					printPosition(location);
					
				}
			};

		} else {
			Log.d(TAG, "locMan is null");
		}
		updateLocationManagerListener();

		mPositionValue = (TextView) mLayout.findViewById(R.id.positionValue);
		
		mNetworkButton = (ToggleButton) mLayout.findViewById(R.id.networkButton);
		mNetworkButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				forceNetworkUse();
			}
		});
		mSatelliteButton = (ToggleButton) mLayout.findViewById(R.id.satelliteButton);
		mSatelliteButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				forceSatelliteUse();
			}
		});
		mAutoButton = (ToggleButton) mLayout.findViewById(R.id.autoButton);
		mAutoButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				forceAutoUse();
			}
		});
		mAutoButton.setPressed(true);
		mAutoButton.setChecked(true);
		setContentView(mLayout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	void updateLocationManagerListener(){
		if (locMan == null) {
			locMan = (LocationManager) this
					.getSystemService(Context.LOCATION_SERVICE);
		}
		else{
			locMan.removeUpdates(locListener);
			if (mAutoInUse){
				locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, 0.1f,
						locListener);
				locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, 0.1f,
						locListener);
			}
			else{
				if(mGpsInUse)
					locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, 0.1f,
							locListener);
				if(mNetworkInUse)
					locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, 0.1f,
							locListener);
			}
		} 
	}

	void forceNetworkUse(){
		mNetworkInUse = true;
		mGpsInUse = false;
		mAutoInUse = false;
		updateLocationManagerListener();
		
		mSatelliteButton.setChecked(false);
		mAutoButton.setChecked(false);
	}

	void forceSatelliteUse(){
		mNetworkInUse = false;
		mGpsInUse = true;
		mAutoInUse = false;
		updateLocationManagerListener();
		
		mNetworkButton.setChecked(false);
		mAutoButton.setChecked(false);
	}

	void forceAutoUse(){
		mNetworkInUse = false;
		mGpsInUse = false;
		mAutoInUse = true;
		updateLocationManagerListener();
		
		mNetworkButton.setChecked(false);
		mSatelliteButton.setChecked(false);
	}
	
	void networkOn(){
		if (!mNetworkInUse){
		mNetworkInUse = true;
		mNetworkButton.setChecked(true);
		}
	}
	void networkOff(){
		if (mNetworkInUse){
		mNetworkInUse = false;
		mNetworkButton.setChecked(false);
		}
	}
	
	void gpsOff(){
		if (mGpsInUse){
			mGpsInUse = false;
			mSatelliteButton.setChecked(false);
		}
	}
	
	void gpsOn(){
		if (!mGpsInUse){
			mGpsInUse = true;
			mSatelliteButton.setChecked(true);
		}
	}
		
	
	public void printPosition(Location loc) {
		String provider = loc.getProvider();
		String position = "lat: " + loc.getLatitude() + " long "
				+ loc.getLongitude() + provider;
		
		mPositionValue.setText(position);
		
		if (provider.equals("network")){
			networkOn();
			gpsOff();
		}
		else{
			networkOff();
			gpsOn();
		}
			
	}
}
