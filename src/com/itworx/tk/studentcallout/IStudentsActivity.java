package com.itworx.tk.studentcallout;

import java.util.ArrayList;

import com.itworx.tk.studentcallout.Student;

public interface IStudentsActivity {
	void showStudents(ArrayList<Student> students);
	void showStudent(Student student);
	void changeSelectionOfStudent(Student student);
}