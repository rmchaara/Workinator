package Proj.Workinator;


import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


public class Search extends Activity {
	
	private static Button search;
	private static Button browse;
	private static TextView name;
	Dialog dialog;
	boolean searching;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search);
        
        name = (TextView) findViewById(R.id.txtSearch);
        //Get pointers to the buttons
        search = (Button) findViewById(R.id.btnSearch);
        browse = (Button) findViewById(R.id.btnBrowse);
        
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
    
    public void executeSearch(View v){
    	executeSearching("search");
    }
    
    public void executeBrowse(View v){
    	executeSearching("browse");
    }
    
    public void executeSearching(String type){
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpResponse HResponse;
	    
	    String iWOName, iRating, iDescript, responseText = "", request;
	    
	    EditText workoutTitle = (EditText) findViewById(R.id.edtName);
	    iWOName = URLEncoder.encode( workoutTitle.getText().toString());
	    Log.d("EXECUTE SEARCH", "workoutTitle: " + iWOName);
	    
	    searching = true;
	    Runnable showWaitDialog = new Runnable() {
			public void run() {
				while (searching) {
				}
				dialog.dismiss();
			}
		};
		dialog = ProgressDialog.show(Search.this, "Please wait...",
				"Searching for workouts ...", true);
		Thread t = new Thread(showWaitDialog);
		t.start();
		
	    
	    if(type == "search"){
	    	request = "sql_query=selectwo" + "&";
	    	request = request + "iWOName=" + iWOName;
	    } else {
	    	request = "sql_query=selectall";
	    }
	    
	    Log.d("QUERY RAMIE", request);
    	try{
    		HttpGet HGet = new HttpGet("https://www.student.cs.uwaterloo.ca/~rmchaara/index.php?" + request);
	    	Log.d("QUERY RAMIE", "Executing server Query");
			HResponse = httpclient.execute(HGet);
			Log.d("QUERY RAMIE", "Finished Executing server query");
			HttpEntity result = HResponse.getEntity();
			responseText = EntityUtils.toString(result);
			Log.d("QUERY RAMIE", "RESPONSE TEXT: " + responseText);
    	} catch (ClientProtocolException e){
    		e.printStackTrace();
    	} catch ( IOException e) {
    		e.printStackTrace();
    	}
    	
    	
    	String [] response = responseText.trim().split("\n");
    	List list = Arrays.asList(response);
    	final String [] workoutTitles = new String[list.size()];
    	final int [] workoutRatings = new int[list.size()];
    	final int [] workoutCodes = new int[list.size()];
    	String [] listResult = new String[list.size()];
    	int code, ratings;
    	for(int i = 0; i < list.size(); i++){
    		//if(i == 0) continue;
    		String [] tokens = list.get(i).toString().split("#");
    		List listPound = Arrays.asList(tokens);
    		for(int k = 0; k < listPound.size(); k++){
    			switch (k){
    				case 0:
    					//Workout Name
    					workoutTitles[i] = listPound.get(k).toString();
    					if (workoutTitles[i].equals("")){
    						ListView lv = (ListView) findViewById(R.id.listResults);
    						String [] temp = {"NO RESULTS"};
    						ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.defaultlist, temp);
    						lv.setAdapter(adapter);
    						searching = false;
    						return;
    					}
    					break;
    				case 1:
    					//code
    					workoutCodes[i] = Integer.parseInt(listPound.get(k).toString());
    					break;
    				case 2:
    					// rating
    					workoutRatings[i] = Integer.parseInt(listPound.get(k).toString());
    					break;
    				default:
    					//Fail case
    					break;
    			}
    		}
    		listResult[i] = "Title: " + workoutTitles[i] + " Rank: " + workoutRatings[i] + " Code: " + workoutCodes[i];
    	}
    	
    	
    	ListView lv = (ListView) findViewById(R.id.listResults);
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.defaultlist, listResult);
    	lv.setAdapter(adapter);
    	
    	searching = false;
    	
    	lv.setOnItemClickListener(new OnItemClickListener() {
    		public void onItemClick(AdapterView<?> parent, View view,
    			int position, long id) {
    			createpopup(position, id);
		    }
    		
    	    void createpopup(int position, long id){
    	    	//set up dialog
    	        dialog = new Dialog(Search.this);
    	        dialog.setContentView(R.layout.descriptionpopup);
    	        dialog.setTitle("Exercise Description");
    	        //close dialog with back key
    	        dialog.setCancelable(true);

    	        //get UI elements
    	        Button rate = (Button) dialog.findViewById(R.id.btnRate);
    	        Button close = (Button) dialog.findViewById(R.id.btnReturnClose);
    	        TextView nameView, youtubeView, rateView, descView;
    	        nameView = (TextView) dialog.findViewById(R.id.txtReturnName);
    	        youtubeView = (TextView) dialog.findViewById(R.id.txtYoutube);
    	        rateView = (TextView) dialog.findViewById(R.id.txtReturnRating);
    	        descView = (TextView) dialog.findViewById(R.id.txtDescription);
    	        
    	        String sql_quer = "selectcode";
    		    String request = "sql_query=" + sql_quer + "&";
    		    final int coda = workoutCodes[position];
    		    request = request + "iCode=" + workoutCodes[position];
    		    HttpClient httpClient = new DefaultHttpClient();
    		    HttpResponse ttpResponse;
    		    String resp = "";
    		    Log.d("QUERY RAMIE SECOND", request);
    	    	try{
    	    		HttpGet HGet = new HttpGet("https://www.student.cs.uwaterloo.ca/~rmchaara/index.php?" + request);
    		    	Log.d("QUERY RAMIE SECOND", "Executing server Query");
    				ttpResponse = httpClient.execute(HGet);
    				Log.d("QUERY RAMIE SECOND", "Finished Executing server query");
    				HttpEntity result = ttpResponse.getEntity();
    				resp = EntityUtils.toString(result);
    				Log.d("QUERY RAMIE SECOND", "RESPONSE TEXT: " + resp);
    	    	} catch (ClientProtocolException e){
    	    		e.printStackTrace();
    	    	} catch ( IOException e) {
    	    		e.printStackTrace();
    	    	}
    	    	
    	    	String [] replyHash = resp.trim().split("#");
    	    	List replyH = Arrays.asList(replyHash);
    	        nameView.setText(workoutTitles[position]);
    	        youtubeView.setText(replyHash[3].toString());      
    	        rateView.setText("" + workoutRatings[position]);
    	        final int rating = (workoutRatings[position]) + 1;
    	    	if(replyHash.length < 5){
    	    		descView.setText("SOMEONE DID NOT ENTER A WORKOUT!!!!!");
    	    	} else {
    	    		descView.setText(replyHash[4].toString());
    	    	}
    	    	
    	        close.setOnClickListener(new OnClickListener() {
    	        	@Override
    	            public void onClick(View v) {
    	                dialog.dismiss();
    	            }
    	        });

    	        rate.setOnClickListener(new OnClickListener() {
    	            @Override
    	                public void onClick(View v) {
    	                    //rating shit
    	    	        String sql_quer = "update";
    	    		    String request = "sql_query=" + sql_quer + "&";
    	    		    request = request + "iRating=" + rating + "&";
    	    		    request = request + "iCode=" + coda;
    	    		    HttpClient httpClient = new DefaultHttpClient();
    	    		    HttpResponse ttpResponse;
    	    		    String resp = "";
    	    		    Log.d("QUERY RAMIE SECOND", request);
    	    	    	try{
    	    	    		HttpGet HGet = new HttpGet("https://www.student.cs.uwaterloo.ca/~rmchaara/index.php?" + request);
    	    		    	Log.d("QUERY RAMIE SECOND", "Executing server Query");
    	    				ttpResponse = httpClient.execute(HGet);
    	    				Log.d("QUERY RAMIE SECOND", "Finished Executing server query");
    	    				HttpEntity result = ttpResponse.getEntity();
    	    				resp = EntityUtils.toString(result);
    	    				Log.d("QUERY RAMIE SECOND", "RESPONSE TEXT: " + resp);
    	    	    	} catch (ClientProtocolException e){
    	    	    		e.printStackTrace();
    	    	    	} catch ( IOException e) {
    	    	    		e.printStackTrace();
    	    	    	}
    	                    //dialog.dismiss();
    	                }
    	            });
    	 
    	        
    	        dialog.show();
    	    }
		});
    }
    
}