package Proj.Workinator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class WorkinatorActivity extends Activity {

    
	private static Button submit;
	private static Button search;
	private static Button view;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        //Get pointers to the buttons
        submit = (Button) findViewById(R.id.btnSubmit);
        search = (Button) findViewById(R.id.btnSearch);
        view = (Button) findViewById(R.id.btnView);
        
        
        //Configuring buttons
        submit.setOnClickListener(submitListener);
        search.setOnClickListener(searchListener);
        view.setOnClickListener(viewListener);
        
        if(!isOnline()){
        	EnableWifi();
        }

    }
    
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(!isOnline()){
        	EnableWifi();
        }
	}
    
    public void EnableWifi(){  
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);  
    	builder.setMessage("Wifi is needed to run this application, please enable your wifi.")  
    	.setCancelable(false)  
    	.setPositiveButton("Enable Wifi", new DialogInterface.OnClickListener(){  
    		public void onClick(DialogInterface dialog, int id){  
    			Intent wifiOptionsIntent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);  
    			startActivity(wifiOptionsIntent);  
    		}  
    	});  
    	AlertDialog alert = builder.create();  
    	alert.show();  
	}
    
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
    
    //click listener for the scan button
    private OnClickListener submitListener = new OnClickListener(){
		@Override
		public void onClick(View info) {			
			Intent myIntent = new Intent(WorkinatorActivity.this, Submit.class);
			WorkinatorActivity.this.startActivity(myIntent);

		}
    };
    
  //click listener for the scan button
    private OnClickListener searchListener = new OnClickListener(){
    	@Override
		public void onClick(View info) {			
			Intent myIntent = new Intent(WorkinatorActivity.this, Search.class);
			WorkinatorActivity.this.startActivity(myIntent);

		}
    };
    
  //click listener for the scan button
    private OnClickListener viewListener = new OnClickListener(){
		@Override
		public void onClick(View info) {			
			Intent myIntent = new Intent(WorkinatorActivity.this, ViewExercise.class);
			WorkinatorActivity.this.startActivity(myIntent);

		}
    };
}