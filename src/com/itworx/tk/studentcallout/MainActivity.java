package com.itworx.tk.studentcallout;

import java.util.ArrayList;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;


public class MainActivity extends Activity implements IStudentsActivity {
	StudentsPresenter studentsPresenter;
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this; 
		studentsPresenter = new StudentsPresenter(this,this);
		studentsPresenter.getStudents();
		
		
		Button buttonNext = (Button)this.findViewById(R.id.buttonNext);
		buttonNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				studentsPresenter.selectNextStudent();
			}
    });
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void showStudents(ArrayList<Student> students) {
		// TODO Auto-generated method stub
		GridView gridView = (GridView) findViewById(R.id.gridView);
		StudentsAdapter adapter = new StudentsAdapter(this, students);
		gridView.setAdapter(adapter);
	}

	@Override
	public void showStudent(Student student) {
		// TODO Auto-generated method stub
		
		Intent intent = new Intent(this,StudentActivity.class);
		intent.putExtra("studentModel", student);
		startActivityForResult(intent, 0);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	    if (requestCode == 0) {
	        if (resultCode == 0) { // nothing
	        }
	        else if (resultCode == 1) { // pick next
	        	studentsPresenter.selectNextStudent();
	        }
	    }
	}
}
