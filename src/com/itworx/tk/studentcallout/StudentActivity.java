package com.itworx.tk.studentcallout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class StudentActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student);
		
		Intent intent = this.getIntent();
		Student student = intent.getParcelableExtra("student");
		
		ImageView imageView = (ImageView)this.findViewById(R.id.imageViewStudent);
		TextView textView = (TextView)this.findViewById(R.id.textViewStudent);
		
		textView.setText(student.firstname + " " + student.lastname);
	}
}