package sonic.industries.aye;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;

public class Utils {
	private static final Logger log = Logger.getLogger( Utils.class.getName() );
	


	public static void uploadGPSdata(double lon, double lat, double radius, String method){
        log.info("uploadGPSdata");
        try {
        //URL url = null;
        String response = null;
        String parameters = "";
        if(method!=null)
        	parameters += "method="+method+"&";
    	parameters += "uid="+MainActivity.androidId+"&longitude="+Double.toString(lon)+"&latitude="+Double.toString(lat);
        if(radius>0.1)
        	parameters += "&radiusOfInterest="+Double.toString(radius);
        //url = new URL("http://www.google.com/api/Panic/?"+parameters);
        log.info("par: "+parameters);
        URL obj = new URL("http://191.238.102.106/api/Panic?"+parameters);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		// optional default is GET
		con.setRequestMethod("GET");
        log.info("par: 1");
		int responseCode = con.getResponseCode();

        log.info("par: 2");
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));

        log.info("par: 3");
		in.close();

        log.info("par: end");
    } catch (Exception e) {
        log.info("_r_HTTP GET:"+ e.toString());
    }
	}

}
