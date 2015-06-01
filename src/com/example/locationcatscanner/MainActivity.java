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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

// TODO credit the icons <div>Icons made by <a href="http://www.flaticon.com/authors/freepik" title="Freepik">Freepik</a> from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a>             is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0">CC BY 3.0</a></div>

public class MainActivity extends ActionBarActivity {

	private static final String TAG = "MainActivity";

	RelativeLayout mLayout = null;
	
	TextView mPositionValue = null;
	ImageView mImageNetwork = null;
	ImageView mImageSatellite = null;

	LocationManager locMan = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mLayout = (RelativeLayout)RelativeLayout.inflate(this, R.layout.activity_main, null);

		Log.d(TAG, "***onCreate***");

		locMan = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		if (locMan != null) {
			LocationListener locListener = new LocationListener() {

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

			locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0.1f,
					locListener);
			locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0.1f,
					locListener);
		} else {
			Log.d(TAG, "locMan is null");
		}

		mPositionValue = (TextView) mLayout.findViewById(R.id.positionValue);
		
		mImageNetwork = (ImageView) mLayout.findViewById(R.id.imageView1);
		mImageSatellite = (ImageView) mLayout.findViewById(R.id.imageView2);


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


	void networkOn(){
		mImageNetwork.setImageResource(R.drawable.network);
	}
	void networkOff(){
		mImageNetwork.setImageResource(R.drawable.network_off);
	}
	
	void gpsOff(){
		mImageSatellite.setImageResource(R.drawable.satellite_off);		
	}
	
	void gpsOn(){
		mImageSatellite.setImageResource(R.drawable.satellite);
	}
		
	
	public void printPosition(Location loc) {
		String provider = loc.getProvider();
		String position = "lat: " + loc.getLatitude() + " long "
				+ loc.getLongitude();
		
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
