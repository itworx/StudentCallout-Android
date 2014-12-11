package com.itworx.tk.studentcallout;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements IStudentsActivity {
	StudentsPresenter studentsPresenter;
	Context context;
	View studentCard;
	Button buttonNext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		 this.buttonNext = (Button)this.findViewById(R.id.buttonNext);
		 this.buttonNext.setGravity(Gravity.CENTER);		
		studentsPresenter = new StudentsPresenter(this,this);
		 
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
		if (studentCard != null) {
			((ViewGroup)studentCard.getParent()).removeView(studentCard);
		}
		
		
		Intent intent = new Intent(this,StudentActivity.class);
		intent.putExtra("student", student);
		startActivity(intent);
	}
	

	@Override
	public void setButtonTitle(String buttonTitle) {
		this.buttonNext.setText(buttonTitle);		
	}

	@Override
	public void showHudWithText(String buttonTitle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hideHud() {
		// TODO Auto-generated method stub
		
	}
}
