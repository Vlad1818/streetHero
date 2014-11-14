package sonic.industries.aye;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.Timer;  
import java.util.TimerTask;  
import java.util.logging.Logger;

/*
 * service that is used by the requesting user
 * gathers gps and wifi data and posts it to the server
 */
public class HttpClientService extends IntentService {
	private static final Logger log = Logger.getLogger( HttpClientService.class.getName() );

	public HttpClientService(){
		super("HttpClientService");
    	log.info("HttpClientService started");
		
         class SendGPSData extends TimerTask { 

            @Override  
            public void run() {  
            	log.info("Started GPS task");
            	double lat, lon;
            	   // Get the location manager
            	try{
	                LocationManager locationManager = (LocationManager) 
	                		getSystemService(LOCATION_SERVICE);
	                Criteria criteria = new Criteria();
	                String bestProvider = locationManager.getBestProvider(criteria, false);
	              
                Location location = locationManager.getLastKnownLocation(bestProvider);
            	log.info("Started GPS task5");

				if (location != null)
				{
                    lat = location.getLatitude();
                    lon = location.getLongitude();

                    SharedPreferences settings = getSharedPreferences(MainActivity.appPrefs, 0);
                   // settings.getInt(MainActivity.Radius, 500);
                    Utils.uploadGPSdata(lon, lat, settings.getInt(MainActivity.Radius, 500));
                	log.info("lat: "+lat+"  lon: "+lon);
				}
                } catch (Exception e) {
                    log.info("Error"+e.getMessage());
                }
            }
        }
         

 		//sending GPS data
         log.info("starting GPS scheduler");
 		SendGPSData task = new SendGPSData();  
         Timer timer = new Timer();  
         timer.scheduleAtFixedRate(task, 10000, 3600000);  
        
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		
	}
	

}

