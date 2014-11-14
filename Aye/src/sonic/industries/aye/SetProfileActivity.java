package sonic.industries.aye;

import java.util.logging.Logger;

import android.app.Activity;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

public class SetProfileActivity extends ListActivity {
		
	private SeekBar seekBar;
	private TextView seekBarValue;
	private String[]existingProfiles;
	private String[] userProfiles;
	private int currentListentingRadius;
	private static final Logger log = Logger.getLogger(SetProfileActivity.class.getName());
	
	
	@Override
	public void onCreate(Bundle icicle) {
	super.onCreate(icicle);	
	setContentView(R.layout.activity_set_profile);
	seekBar = (SeekBar) findViewById(R.id.radiusBar);
	seekBarValue = (TextView) findViewById(R.id.radiusBarProgress);	
	initialize();
	}
	
	public void accept(View v){
		log.info("activate button activated");
		SharedPreferences settings = getSharedPreferences(MainActivity.appPrefs, 0);
		Editor editor = settings.edit();
		log.info("setting radis");
		editor.putInt(MainActivity.Radius, seekBar.getProgress());
		StringBuilder builder = new StringBuilder();
	
		ListView mainListView = getListView();
		SparseBooleanArray checkedItems = mainListView.getCheckedItemPositions();
		int len = mainListView.getCount();
		for (int i = 0; i < len; i++)
		   {
			 if (checkedItems.get(i)) {
				 builder.append(String.valueOf(i)+",");
			  /* do whatever you want with the checked item */
			 }
		   }
		
		String result = builder.toString();
		result = result.length() == 0 ? result : result.substring(0, result.length()-1);
		editor.putString(MainActivity.UserProfiles, result);
		editor.commit();
		finish();
	}
		
	public void restore(View v){
		log.info("restore button activated");
		SetUserDefualts();
	}
	
	private void initialize(){
	
		log.info("Initializing activity");
		SharedPreferences settings = getSharedPreferences(MainActivity.appPrefs, 0);
		log.info("Extracting existing profiles");
		existingProfiles = settings.getString(MainActivity.Profiles,"").split(",");
		log.info("existing profiles: "+ settings.getString(MainActivity.Profiles,"") );
		setListAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,
				existingProfiles));	
		
		log.info("user profiles: "+settings.getString(MainActivity.UserProfiles, ""));
		userProfiles = settings.getString(MainActivity.UserProfiles, "").split(",");
		
		log.info("current radius: "+ settings.getInt(MainActivity.Radius, 0));
		currentListentingRadius = settings.getInt(MainActivity.Radius, 0);
		
		SetUserDefualts();
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){ 

			   @Override 
			   public void onProgressChanged(SeekBar seekBar, int progress, 
			     boolean fromUser) { 
			    // TODO Auto-generated method stub 
			    seekBarValue.setText(String.valueOf(progress)+"m"); 
			   } 
			   
			   @Override
			   public void onStartTrackingTouch(SeekBar seekBar) { } 
			   @Override 
			   public void onStopTrackingTouch(SeekBar seekBar) { } 
			       }); 
		
	}
	
	private void SetUserDefualts(){
		log.info("setting defaults");
		if(userProfiles[0] != ""){
			ListView mainListView = getListView();
			
			SparseBooleanArray checkedItems = mainListView.getCheckedItemPositions();
			int len = mainListView.getCount();
			for (int i = 0; i < len; i++)
 		   {
 			 if (checkedItems.get(i)) {
 				 log.info("item: "+ i + " is checked");
 				 mainListView.setItemChecked(i, false);
 			  /* do whatever you want with the checked item */
 			 }
 		   }
			
			for(String userProfile : userProfiles){
				log.info("setting item checked" + userProfile);				
				mainListView.setItemChecked(Integer.parseInt(userProfile), true);	
			}
		}
		
		log.info("setting current listening radius" + currentListentingRadius);
		seekBarValue.setText(String.valueOf(currentListentingRadius));
		seekBar.setProgress(currentListentingRadius);
	}	
}
