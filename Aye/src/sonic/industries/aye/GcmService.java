package sonic.industries.aye;
import android.app.IntentService;
import android.content.Intent;

/*
 * intent service that is used by the gcm
 * will notify the user of a new incoming message
 * or gather more information that the user requires
 */
public class GcmService extends IntentService {

	public GcmService(){
		super("GcmService");
	}
	
	//function that is called whenever the service activated - each action on a new worker thread
	@Override
	protected void onHandleIntent(Intent i) {
		// TODO Auto-generated method stub
		
	}

}
