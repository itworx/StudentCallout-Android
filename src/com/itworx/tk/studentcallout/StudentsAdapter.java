package com.itworx.tk.studentcallout;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
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
//		TextView textView = (TextView) convertView.findViewById(R.id.textviewName);
//		textView.setText(students.get(position).firstname);
		Student student = students.get(position);
		ImageView imageView = (ImageView) convertView.findViewById(R.id.student_ImageView);
		imageView.setImageBitmap(student.imageBitmap);
//		convertView.setBackgroundColor(0xFF00FF00);
		return convertView;
	}

}
