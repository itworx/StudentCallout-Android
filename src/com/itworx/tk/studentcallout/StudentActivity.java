package com.itworx.tk.studentcallout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class StudentActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		WindowManager.LayoutParams params = getWindow().getAttributes();   
		params.height = 200;  
		params.width = 200;   

		this.getWindow().setAttributes(params); 
		
		setContentView(R.layout.activity_student);
		
		Intent intent = this.getIntent();
		Student student = intent.getParcelableExtra("studentModel");
		
		ImageView imageView = (ImageView)this.findViewById(R.id.imageViewStudent);
		TextView textView = (TextView)this.findViewById(R.id.textViewStudent);
		
		textView.setText(student.firstname + " " + student.lastname);
		
		
		Button buttonNext = (Button)this.findViewById(R.id.buttonCardNext);
		buttonNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setResult(1);
				finish();
			}
		});		
	}
}