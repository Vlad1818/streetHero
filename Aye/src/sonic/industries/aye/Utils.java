package sonic.industries.aye;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

import android.provider.Settings;

public class Utils {
	private static final Logger log = Logger.getLogger( Utils.class.getName() );
	
	
	public static void uploadGPSdata(double lon, double lat, double radius){
        try {
        URL url = null;
        String response = null;
        
        String parameters = "uid="+MainActivity.androidId+"&longitude="+Double.toString(lon)+"&latitude="+Double.toString(lat)+"&radiusOfInterest="+Double.toString(radius);
        url = new URL("http://www.address.com/api/Panic");
        log.info("par: "+parameters);
        //create the connection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        connection.setRequestMethod("GET");
        OutputStreamWriter request = new OutputStreamWriter(connection.getOutputStream());
        request.write(parameters);
        request.flush();
        request.close();
        log.info("request.close()");
        String line = "";
        InputStreamReader isr = new InputStreamReader(
                connection.getInputStream());
        BufferedReader reader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
     //       log.info(line + "\n");
        }
        response = sb.toString();
        isr.close();
        reader.close();
    } catch (IOException e) {
        log.info("HTTP GET:"+ e.toString());
    }
	}

}
