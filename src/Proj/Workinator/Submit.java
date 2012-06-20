package Proj.Workinator;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Submit extends Activity {
	
	private static Button submitworkout;
	private static Button clear;
	private static TextView workout;
	
	private static Button neck;
	private static Button shoulders;
	private static Button arms;
	private static Button back;
	private static Button chest;
	private static Button abdominal;
	private static Button hips;
	private static Button thighs;
	private static Button calves;
	
	Dialog dialog, videodialog, youtubedialog;
	boolean videoadded = false;
	String title;
	File youtubevideo;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.submit);
        
        workout = (TextView) findViewById(R.id.lblWorkout);
        //Get pointers to the buttons
        submitworkout = (Button) findViewById(R.id.btnSubmitWorkout);
        clear = (Button) findViewById(R.id.btnClear);
        
        neck = (Button) findViewById(R.id.btnNeck);
    	shoulders = (Button) findViewById(R.id.btnShoulders);
    	arms = (Button) findViewById(R.id.btnArms);
    	back = (Button) findViewById(R.id.btnBack);
    	chest = (Button) findViewById(R.id.btnChest);
    	abdominal = (Button) findViewById(R.id.btnAbdominal);
    	hips = (Button) findViewById(R.id.btnHips);
    	thighs = (Button) findViewById(R.id.btnThighs);
    	calves = (Button) findViewById(R.id.btnCalves);
        
        
        
        //Configuring buttons
    	neck.setOnClickListener(neckListener);
    	shoulders.setOnClickListener(shouldersListener);
    	arms.setOnClickListener(armsListener);
    	back.setOnClickListener(backListener);
    	chest.setOnClickListener(chestListener);
    	abdominal.setOnClickListener(abdominalListener);
    	hips.setOnClickListener(hipsListener);
    	thighs.setOnClickListener(thighsListener);
    	calves.setOnClickListener(calvesListener);
        
    	
        submitworkout.setOnClickListener(submitworkoutListener);
        clear.setOnClickListener(clearListener);
        
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
    
    
    //click listeners for exercises
    
    private OnClickListener neckListener = new OnClickListener(){
		@Override
		public void onClick(View info) {			
			exercisepopup("neck_array");
		}
    };  
    private OnClickListener shouldersListener = new OnClickListener(){
		@Override
		public void onClick(View info) {			
			exercisepopup("shoulder_array");
		}
    };   
    private OnClickListener armsListener = new OnClickListener(){
		@Override
		public void onClick(View info) {			
			exercisepopup("arm_array");
		}
    };   
    private OnClickListener backListener = new OnClickListener(){
		@Override
		public void onClick(View info) {			
			exercisepopup("back_array");
		}
    };    
    private OnClickListener chestListener = new OnClickListener(){
		@Override
		public void onClick(View info) {			
			exercisepopup("chest_array");
		}
    };    
    private OnClickListener abdominalListener = new OnClickListener(){
		@Override
		public void onClick(View info) {			
			exercisepopup("abdominal_array");
		}
    };   
    private OnClickListener hipsListener = new OnClickListener(){
		@Override
		public void onClick(View info) {			
			exercisepopup("hip_array");
		}
    };   
    private OnClickListener thighsListener = new OnClickListener(){
		@Override
		public void onClick(View info) {			
			exercisepopup("thigh_array");
		}
    };  
    private OnClickListener calvesListener = new OnClickListener(){
		@Override
		public void onClick(View info) {			
			exercisepopup("calf_array");
		}
    };
       
    void exercisepopup(String exercises){
    	//set up dialog
        dialog = new Dialog(Submit.this);
        dialog.setContentView(R.layout.addexercise);
        dialog.setTitle("Add Exercise");
        //close dialog with back key
        dialog.setCancelable(true);

        //get UI elements
        Button add = (Button) dialog.findViewById(R.id.btnAdd);
        Button cancel = (Button) dialog.findViewById(R.id.btnCancel);
        Spinner reps = (Spinner) dialog.findViewById(R.id.spnReps);
        Spinner exs = (Spinner) dialog.findViewById(R.id.spnExercise);
        
        //populating exercise spinner
        int res_id = getResources().getIdentifier(exercises, "array", this.getPackageName());
        ArrayAdapter<CharSequence> ex_adapter = ArrayAdapter.createFromResource(
        		this, res_id, android.R.layout.simple_spinner_item);
        ex_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exs.setAdapter(ex_adapter);
        
        //populating reps spinner
        ArrayAdapter<CharSequence> rep_adapter = ArrayAdapter.createFromResource(
        		this, R.array.reps_array, android.R.layout.simple_spinner_item);
        rep_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reps.setAdapter(rep_adapter);
        
        
        cancel.setOnClickListener(new OnClickListener() {
        @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        add.setOnClickListener(new OnClickListener() {
            @Override
                public void onClick(View v) {
                    //adding shit
            		TextView workout = (TextView) findViewById(R.id.lblWorkout);
            		Spinner reps = (Spinner) dialog.findViewById(R.id.spnReps);
                    Spinner exs = (Spinner) dialog.findViewById(R.id.spnExercise);
                    
                    workout.setText(workout.getText() + exs.getSelectedItem().toString() + "," + reps.getSelectedItem().toString() + "\n");
                    dialog.dismiss();
                }
            });
        dialog.show();
    }
    
    //click listener for the submit button
    private OnClickListener submitworkoutListener = new OnClickListener(){
    	@Override
    	public void onClick(View info) {			
    		//set up dialog
    		TextView temp = (TextView) findViewById(R.id.lblWorkout);
    		if (temp != null && temp.getText().equals("")){
    			Toast.makeText(Submit.this, "Please enter a workout routine before submitting", Toast.LENGTH_LONG).show();
    			return;
    		}
    		
    		dialog = new Dialog(Submit.this);
    		dialog.setContentView(R.layout.submitpopup);
    		dialog.setTitle("Submit Workout");
    		//close dialog with back key
    		dialog.setCancelable(true);

    		//set up button
    		Button send = (Button) dialog.findViewById(R.id.btnContinue);
    		Button cancel = (Button) dialog.findViewById(R.id.btnCancel);
    		Button video = (Button) dialog.findViewById(R.id.btnVideo);

    		
    		title = "";
    		videoadded = false;
    		
    		cancel.setOnClickListener(new OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				dialog.dismiss();
    			}
    		});

    		send.setOnClickListener(new OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				EditText workoutTitle = (EditText) dialog.findViewById(R.id.edtWorkOutName);
    				title = workoutTitle.getText().toString();
    				if (title.equals("")){
    					dialog.setTitle("Please enter a workout name");
    				} else {
    					dialog.dismiss();
    					send();
    				}
    			}
    		});

    		video.setOnClickListener(new OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				//do video shit
    				getVideo();
    			}
    		});

    		dialog.show();
    	}
    };

    //click listener for the clear button
    private OnClickListener clearListener = new OnClickListener(){
		@Override
		public void onClick(View info) {			
			workout.setText("");
		}
    };
    
    private void send(){
    	String youtubelink = "none";
    	
    	//send video to youtube
    	if (videoadded){
    		HttpClient httpclient = new DefaultHttpClient();
    		HttpPost httppost = new HttpPost("https://www.google.com/accounts/ClientLogin");

    		try {
    			// Add your data
    			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
    			nameValuePairs.add(new BasicNameValuePair("accountType", "HOSTED_OR_GOOGLE"));
    			nameValuePairs.add(new BasicNameValuePair("Email", "the.workinator@gmail.com"));
    			nameValuePairs.add(new BasicNameValuePair("Passwd", "ourfirstapp"));
    			nameValuePairs.add(new BasicNameValuePair("service", "youtube"));
    			nameValuePairs.add(new BasicNameValuePair("source", "networksProject-theWorkinator-1"));
    			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    			String responseStream =  EntityUtils.toString(httppost.getEntity());
    			Log.v("GOOGLE RESPONSE", responseStream);
    			// Execute HTTP Post Request
    			HttpResponse response = httpclient.execute(httppost);

    			String iWOName = title;

    			responseStream =  EntityUtils.toString(response.getEntity());
    			Log.v("GOOGLE RESPONSE", responseStream);
    			//read response
    			String [] lineA = responseStream.trim().split("\n");
    			List<String> listPound = Arrays.asList(lineA);
    			String authKey = listPound.get(2).split("=")[1];
    			Log.v("GOOGLE RESPONSE", authKey);
    			String entry = "<?xml version=\"1.0\"?>"
    					+ "<entry xmlns=\"http://www.w3.org/2005/Atom\""
    					+ " xmlns:media=\"http://search.yahoo.com/mrss/\""
    					+ " xmlns:yt=\"http://gdata.youtube.com/schemas/2007\">"
    					+ "<media:group>"
    					+ "<media:title type=\"plain\">" + iWOName + "</media:title>"
    					+ "<media:description type=\"plain\">"
    					+ "Uploaded from the workinator"
    					+ "</media:description>"
    					+ "<media:category"
    					+ " scheme=\"http://gdata.youtube.com/schemas/2007/categories.cat\">People"
    					+ "</media:category>"
    					+ "<media:keywords>workinator,app</media:keywords>"
    					+ "</media:group>"
    					+ "</entry>";



    			File file = youtubevideo;

    			String boundary = "f93dcbA3";
    			String endLine = "\r\n";

    			StringBuilder sb = new StringBuilder();
    			sb.append("--");
    			sb.append(boundary);
    			sb.append(endLine);
    			sb.append("Content-Type: application/atom+xml; charset=UTF-8"); 
    			sb.append(endLine);
    			sb.append(endLine);
    			sb.append(entry);
    			sb.append(endLine);
    			sb.append("--");
    			sb.append(boundary);
    			sb.append(endLine);
    			sb.append("Content-Type: video/3gpp");
    			sb.append(endLine);
    			sb.append("Content-Transfer-Encoding: binary");
    			sb.append(endLine);
    			sb.append(endLine);

    			String bodyStart = sb.toString();
    			Log.d("BODY START", bodyStart);

    			sb = new StringBuilder();
    			sb.append(endLine);
    			sb.append("--");
    			sb.append(boundary);
    			sb.append("--");

    			String bodyEnd = sb.toString();
    			Log.d("BODY END", bodyEnd);

    			HttpURLConnection conn;
    			try {
    				FileInputStream fIn = new FileInputStream(file);
    				byte fileBytes[] = new byte[(int) file.length()];
    				fIn.read(fileBytes);

    				conn = (HttpURLConnection) new URL("http://uploads.gdata.youtube.com/feeds/api/users/theworkinator/uploads")
    				.openConnection();

    				conn.setRequestMethod("POST");
    				conn.setRequestProperty("Content-Type", "multipart/related; boundary=\"" + boundary + "\"");
    				conn.setRequestProperty("Host", "uploads.gdata.youtube.com");
    				conn.setRequestProperty("Authorization", "GoogleLogin auth=" + authKey);
    				conn.setRequestProperty("GData-Version", "2");
    				conn.setRequestProperty("X-GData-Key", "key=AI39si7h7WllDyHDQoP2fklTQyRW51BsrSAnKP8lmfdkL_IL2Umy55YuuwvoV_1KvipWYn_QplsHyd1Y5E8maIaJdT-tNPGY1Q");
    				conn.setRequestProperty("Slug", "video.3gp");
    				conn.setRequestProperty("Content-Length", "" + (bodyStart.getBytes().length
    						+ fileBytes.length + bodyEnd.getBytes().length)); 
    				conn.setRequestProperty("Connection", "close");

    				conn.setDoOutput(true);
    				conn.setDoInput(true);
    				conn.setUseCaches(false);
    				try {
    					conn.connect();

    					Log.d("ID", "" + file.length());

    					try {
    						OutputStream os = new BufferedOutputStream(conn.getOutputStream());

    						os.write(bodyStart.getBytes());
    						os.write(fileBytes);
    						os.write(bodyEnd.getBytes());
    						os.flush();

    						String responseTwo = "";
    						try {
    							InputStream in  =  conn.getInputStream();
								final char[] buffer = new char[0x100000];
								StringBuilder out = new StringBuilder();
								Reader r = new InputStreamReader(in, "UTF-8");
								int read;
								do {
									read = r.read(buffer, 0, buffer.length);
									if (read>0) {
										out.append(buffer, 0, read);
									}
								} while (read>=0);
								responseTwo = "Success! " + out;
								//writetofile(responseTwo);

								youtubelink = responseTwo.split("<yt:videoid>")[1].split("</yt:videoid>")[0];
								Log.d("ID TAG", youtubelink);
    						} catch (FileNotFoundException e) {
    							// Error Stream contains JSON that we can parse to a FB error
    							responseTwo = "Error!" + conn.getErrorStream().read();
    						}

    						Log.d("ID", responseTwo);

    					} catch (FileNotFoundException e1) {
    						Log.d("ID", e1.getMessage(), e1);
    					} catch (IOException e) {
    						Log.d("ID", e.getMessage(), e);
    					}
    				} catch (IOException e2) {
    					Log.d("ID", e2.getMessage(), e2);
    				}
    			} catch (MalformedURLException e3) {
    				Log.d("ID", e3.getMessage(), e3);
    			} catch (IOException e3) {
    				Log.d("ID", e3.getMessage(), e3);
    			}
    		} catch (ClientProtocolException e) {
    			// TODO Auto-generated catch block
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    		}
    	}
    	
    	//sending data to our server
    	
    	String iWOName = "testTitle";
		String iRating = "0";
		String iDescript = "something";
		String sql_query = "insert";
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse HResponse;

		EditText workoutTitle = (EditText) dialog.findViewById(R.id.edtWorkOutName);
		iWOName = URLEncoder.encode(title);

		TextView workoutDescription = (TextView) findViewById(R.id.lblWorkout);
		iDescript = URLEncoder.encode( workoutDescription.getText().toString());

		String request = "sql_query=" + sql_query + "&";
		request = request + "iWOName=" + iWOName + "&";
		request = request + "iRating=" + iRating + "&";
		request = request + "iDescript=" + iDescript + "&";
		if(videoadded){
			request = request + "iYoutube=http://www.youtube.com/watch?v=" + youtubelink;
		}else{
			request = request + "iYoutube=none";
		}

		Log.d("QUERY RAMIE", request);
		try{
			HttpGet HGet = new HttpGet("https://www.student.cs.uwaterloo.ca/~rmchaara/index.php?" + request);
			Log.d("QUERY RAMIE", "Executing server Query");
			HResponse = httpclient.execute(HGet);
			Log.d("QUERY RAMIE", "Finished Executing server BLS");
			HttpEntity result = HResponse.getEntity();
			String responseText = EntityUtils.toString(result);
			Log.d("QUERY RAMIE", responseText);
			workoutDescription.setText("");
		} catch (ClientProtocolException e){
			e.printStackTrace();
		} catch ( IOException e) {
			e.printStackTrace();
		}
		
		Toast.makeText(Submit.this, "Successfully uploaded workout!", Toast.LENGTH_LONG).show();
    	
    }
    
    private boolean getVideo(){

    	// File path = Environment.getExternalStoragePublicDirectory(Environment.);
    	String videoPath = "/mnt/sdcard/DCIM/Camera/";
    	File videoDirectory = new File(videoPath);
    	File [] videoFiles = videoDirectory.listFiles();

    	if (videoFiles.length == 0){
    		Toast.makeText(Submit.this, "You have no video files!", Toast.LENGTH_LONG).show();
    		return false;
    	}

    	final List<File> listFiles = Arrays.asList(videoFiles);
    	List<String> listFileNames = new ArrayList();
    	for(int p = 0; p < listFiles.size(); p++){
    		listFileNames.add(listFiles.get(p).getName());
    	}

    	videodialog = new Dialog(Submit.this);
    	videodialog.setContentView(R.layout.addvideo);
    	videodialog.setTitle("Add Video");
    	//close dialog with back key
    	videodialog.setCancelable(true);

    	ListView lv = (ListView) videodialog.findViewById(R.id.listVideos);
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.defaultlist, listFileNames);
    	lv.setAdapter(adapter);

    	videodialog.show();

    	lv.setOnItemClickListener(new OnItemClickListener() {
    		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    			// Create a new HttpClient and Post Header
    			File file = listFiles.get(position);
    			youtubevideo = file;
    			videoadded = true;
    			Log.v("DEBUG", "FILE SELECTED: " + file.toString());
    			videodialog.dismiss();
    		}
    	});

    	return true;
    }
}