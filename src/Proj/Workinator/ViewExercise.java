package Proj.Workinator;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ViewExercise extends ListActivity {
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		String[] exercises = getResources().getStringArray(R.array.search_array);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.exercises,R.id.txtexercise, exercises);
		setListAdapter(adapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		String selection = l.getItemAtPosition(position).toString();
		Intent myIntent = new Intent(ViewExercise.this, SubExercise.class);
		myIntent.putExtra("type", selection);
		ViewExercise.this.startActivity(myIntent);
	}
	
}