package Proj.Workinator;

import android.app.Dialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class SubExercise extends ListActivity {
	
	/** Called when the activity is first created. */
	Dialog dialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		Bundle extras = getIntent().getExtras();
		String type = extras.getString("type");
		
		String [] subexercises;
		if (type.equals("Neck")) {
			subexercises = getResources().getStringArray(R.array.neck_array);
		} else if (type.equals("Shoulders")) {
			subexercises = getResources().getStringArray(R.array.shoulder_array);
		} else if (type.equals("Arms")) {
			subexercises = getResources().getStringArray(R.array.arm_array);
		} else if (type.equals("Back")) {
			subexercises = getResources().getStringArray(R.array.back_array);
		} else if (type.equals("Chest")) {
			subexercises = getResources().getStringArray(R.array.chest_array);
		} else if (type.equals("Abdominal")) {
			subexercises = getResources().getStringArray(R.array.abdominal_array);
		} else if (type.equals("Hips")) {
			subexercises = getResources().getStringArray(R.array.hip_array);
		} else if (type.equals("Thighs")) {
			subexercises = getResources().getStringArray(R.array.thigh_array);
		} else if (type.equals("Calves")) {
			subexercises = getResources().getStringArray(R.array.calf_array);
		} else {
			subexercises = new String [] {"NO EXERCISES"};
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.subexercises,R.id.txtsubexercise, subexercises);
		setListAdapter(adapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		String selection = l.getItemAtPosition(position).toString();
	
		selection = selection.replaceAll(" ", "_");
		
		dialog = new Dialog(SubExercise.this);
        dialog.setContentView(R.layout.instructions);
        dialog.setTitle("Exercise Instructions");
        dialog.setCancelable(true);
        
        Button close = (Button) dialog.findViewById(R.id.btnInstrCancel);
        TextView instr = (TextView) dialog.findViewById(R.id.lblReturnInstr);
        
        close.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		dialog.dismiss();
        	}
        });
        
		int res_id = getResources().getIdentifier(selection, "string", this.getPackageName());
		if (res_id != 0){
			String instructions =  getResources().getString(res_id);        
	        instr.setText(instructions);
		} else {
			instr.setText("Instructions Coming Soon!");
		}
        dialog.show();
	}
	

}