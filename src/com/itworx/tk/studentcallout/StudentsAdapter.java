package com.itworx.tk.studentcallout;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.itworx.tk.studentcallout.Student;

public class StudentsAdapter extends ArrayAdapter<Student> {

	Context context;
	ArrayList<Student> students;

	public StudentsAdapter(Context context, ArrayList<Student> students) {
		super(context, R.layout.student_cell, students);

		this.context = context;
		this.students = students;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			View view = ((Activity) (context)).getLayoutInflater().inflate(
					R.layout.student_cell, parent, false);
			convertView=view;
		}
		

		ImageView selectImageView = (ImageView) convertView.findViewById(R.id.student_SelectImageView);
		ImageView imageView = (ImageView) convertView.findViewById(R.id.student_ImageView);		
		Student student = students.get(position);
		if (student.isPicked) {
			selectImageView.setVisibility(View.VISIBLE);
			AlphaAnimation alpha = new AlphaAnimation(0.5F, 0.5F); // change values as you want
			alpha.setDuration(0); 
			alpha.setFillAfter(true); 
			imageView.startAnimation(alpha);			
		}
		else {
			selectImageView.setVisibility(View.INVISIBLE);
			AlphaAnimation alpha = new AlphaAnimation(0.5F,1F); // change values as you want
			alpha.setDuration(0); 
			alpha.setFillAfter(true); 
			imageView.startAnimation(alpha);						
			
		}

//		textView.setText(students.get(position).firstname);
		// Student student = students.get(position);

		imageView.setImageBitmap(student.imageBitmap);
//		convertView.setBackgroundColor(0xFF00FF00);
		return convertView;
	}

}
