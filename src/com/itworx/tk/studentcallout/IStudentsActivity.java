package com.itworx.tk.studentcallout;

import java.util.ArrayList;

import android.R.string;

import com.itworx.tk.studentcallout.Student;

public interface IStudentsActivity {
	void showStudents(ArrayList<Student> students);
	void showStudent(Student student);
	void setButtonTitle(String buttonTitle);
	void showHudWithText(String text);
	void showAlert(String titleText,String messageBody);
	void showAck(String textResource);
	void hideHud();
	void changeSelectionOfStudent(Student student);
}