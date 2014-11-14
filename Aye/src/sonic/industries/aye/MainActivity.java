package sonic.industries.aye;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;


/*
 * activity using by the user that requests aid
 * user also to configure buttons
 * */

public class MainActivity extends Activity {
	

    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    String SENDER_ID = "AIzaSyAPxjEggEl_tejGBVkdlkpvxGvaNR0waUs";
	
	public static final String BigBtn1 = "BigBtn1";
	public static final String BigBtn2 = "BigBtn2";
	public static final String SmallBtn1 = "SmallBtn1";
	public static final String SmallBtn2 = "SmallBtn2";
	public static final String SmallBtn3 = "SmallBtn3";
	public static final String SmallBtn4 = "SmallBtn4";
	public static final String SmallBtn5 = "SmallBtn5";
	public static final String SmallBtn6 = "SmallBtn6";
	public static final String Profiles = "profiles";
	public static final String UserProfiles = "userProfiles";
	public static final String Radius = "radius";
	public static final Logger log = Logger.getLogger(MainActivity.class.getName());
	public static String appPrefs = "SolacePref";
	public static String androidId;
	
	
	
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    Context context;

    String regid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    	androidId = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
    	
        SharedPreferences settings = getSharedPreferences(appPrefs, 0);
        String profile = settings.getString(BigBtn1, "Not Found");
        if(profile=="Not Found"){
        	Initiallize();
        }
        

        Intent i = new Intent(this, HttpClientService.class);
        startService(i);
        
        
        GoogleCloudMessaging gcm;
        String regid;
        try{
        	 gcm = GoogleCloudMessaging.getInstance(this);
             regid = getRegistrationId(getApplicationContext());

             if (regid==null) {
                 registerInBackground();
             }
        }
        catch(Exception e){
        	log.info("cERROR" + e.getMessage());
        }

      	try{
      		log.info("starting loading gps");
            LocationManager locationManager = (LocationManager) 
            		getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String bestProvider = locationManager.getBestProvider(criteria, false);
            Location location = locationManager.getLastKnownLocation(bestProvider);

        LocationListener loc_listener = new LocationListener() {

            public void onLocationChanged(Location l) {}

            public void onProviderEnabled(String p) {}

            public void onProviderDisabled(String p) {}

            public void onStatusChanged(String p, int status, Bundle extras) {}
        };
        locationManager
                .requestLocationUpdates(bestProvider, 0, 0, loc_listener);
  		log.info("did request to gps");
        } catch (Exception e) {
            log.info("Error"+e.getMessage());
        }
    
    }
    
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences(context);
        int appVersion = getAppVersion(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }
    
    
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
             //   mDisplay.append(msg + "\n");
            }
        }.execute(null, null, null);
    }
    private SharedPreferences getGcmPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }
    
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId==null) {
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            return "";
        }
        return registrationId;
    }
    
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
    
    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }
    
    public void distressButtonHandler(View v) {

    	switch(v.getId())
    	{
	    	case R.id.ImageButton1:
	    		sendIntentonDistress("BigBtn1");
	    	break;
	    	case R.id.ImageButton2:
	    		sendIntentonDistress("BigBtn2");
	    	break;
	    	case R.id.ImageButton3:
	    		sendIntentonDistress("SmallBtn1");
	    	break;
	    	case R.id.ImageButton4:
	    		sendIntentonDistress("SmallBtn4");
	    	break;
	    	case R.id.ImageButton5:
	    		sendIntentonDistress("SmallBtn2");
	    	break;
	    	case R.id.ImageButton6:
	    		sendIntentonDistress("SmallBtn5");
	    	break;
	    	case R.id.ImageButton7:
	    		sendIntentonDistress("SmallBtn3");
	    	break;
	    	case R.id.ImageButton8:
	    		sendIntentonDistress("SmallBtn6");
	    	break;
	    	default:
	    	throw new RuntimeException("Unknow button ID");
    	}
    	
    }
    
    private void sendIntentonDistress(String btn){ 
        log.info("sendIntentonDistress");   	
        SharedPreferences settings = getSharedPreferences(appPrefs, 0);
        double lat,lon = -1;
        String profile = settings.getString(btn, "Not Found");
        if(profile=="Not Found"){
        	Initiallize();
        }
                LocationManager locationManager = (LocationManager) 
                		getSystemService(LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                String bestProvider = locationManager.getBestProvider(criteria, false);
              
            Location location = locationManager.getLastKnownLocation(bestProvider);
            lat = location.getLatitude();
            lon = location.getLongitude();
            log.info("sendIntentonDistress - before upload");   
        	  Utils.uploadGPSdata(lon, lat, 0.0,"Panic");
              log.info("sendIntentonDistress - after upload"); 
              
    }
    

    public void startButtonChangeActivity(View v){
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);

        dlgAlert.setMessage("ButtonChangeActivity");
        dlgAlert.setTitle("Note");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();

    }

    public void startProfileChangeActivity(View v){
    	log.info("started activity");
    	startActivity(new Intent(this, SetProfileActivity.class));
    	

    }
    
    
    private void Initiallize(){
        SharedPreferences settings = getSharedPreferences(appPrefs, 0);
        Editor editor = settings.edit();
        editor.putString(BigBtn1, "A");
        editor.putString(BigBtn2, "B");
        editor.putString(SmallBtn1, "C");
        editor.putString(SmallBtn2, "D");
        editor.putString(SmallBtn3, "E");
        editor.putString(SmallBtn4, "F");
        editor.putString(SmallBtn5, "G");
        editor.putString(SmallBtn6, "H");
        
        String profiles = "A,B,C,D,E,F,G,H,U,X";
        editor.putString(Profiles, profiles);
        
        editor.putInt(Radius, 500);
        
        editor.putString(UserProfiles, "");
        
        editor.commit(); 
    }

    
    
}   
