package com.itworx.tk.studentcallout;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy;


import android.R.bool;
import android.R.integer;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import com.itworx.tk.studentcallout.Student;

public class StudentsPresenter {
	
	IStudentsActivity studentsActivity;
	Boolean allowRepetition; 
	ArrayList<Student> students;
	ArrayList<Integer> studentIndicesToPickFrom;
	DataManager databaseHelper ;

	
	public StudentsPresenter(IStudentsActivity studentsActivity,Context context) {
		this.studentsActivity = studentsActivity;
		this.databaseHelper = new DataManager(context);
		this.studentIndicesToPickFrom = new ArrayList<Integer>();
		
		//load from persistent settings
		this.setAllowRepetition(false);
	}
	
	void setAllowRepetition (Boolean allow){
	  this.allowRepetition = allow;
	  if(allow){
		this.studentIndicesToPickFrom = new ArrayList<Integer>();  
	  }
	  // 
	}
	
	
	void getStudents() {
		//dummy students
//		this.students = new ArrayList<Student>();
		
//		Student student = new Student();
//		student.firstname = "Mourad";
//		student.lastname = "Aly";
//		student.color = Color.RED;
//		this.students.add(student);
////		
//		student = new Student();
//		student.firstname = "Tefa";
//		student.lastname = "x";
//		student.color = Color.RED;
//		this.students.add(student);
////		
//		student = new Student();
//		student.firstname = "islam";
//		student.lastname = "shalaan";
//		student.color = Color.RED;
//		this.students.add(student);
////		
//		student = new Student();
//		student.firstname = "aya";
//		student.lastname = "akl";
//		student.color = Color.RED;
//		this.students.add(student);		
////		
////		
//		student = new Student();
//		student.firstname = "heba";
//		student.lastname = "youssry";
//		student.color = Color.RED;
//		this.students.add(student);		
////		
////		
//		student = new Student();
//		student.firstname = "amr";
//		student.lastname = "else7emy";
//		student.color = Color.RED;
//		this.students.add(student);		
////		
//		student = new Student();
//		student.firstname = "xyz";
//		student.lastname = "abc";
//		student.imagePath = "xyz";		
//		student.color = Color.RED;
//		this.students.add(student);		
//				
//		//For dummy-data  
//		for(int i=0;i<this.students.size();i++){
//		 	this.databaseHelper.addStudent(this.students.get(i)); 
//		}
		
		new LoadStudents().execute();     
		
//		studentsActivity.showStudents(students);
		
	}
	
	void selectNextStudent() {
		Random randomGenerator = new Random(); 
		if(this.allowRepetition){			
	      int index = randomGenerator.nextInt(this.students.size());
	      Student selectedStudent = this.students.get(index);
	      studentsActivity.selectStudent(selectedStudent);	      
		}
		else {
				if(this.studentIndicesToPickFrom.size() == 0){
				  //Show MSG	s
					this.reset();
				}
				int index = randomGenerator.nextInt(this.studentIndicesToPickFrom.size());
				int pickedIndex = this.studentIndicesToPickFrom.get(index);
				this.studentIndicesToPickFrom.remove(index);
				Student selectedStudent = this.students.get(pickedIndex);
			    studentsActivity.selectStudent(selectedStudent);				
		}
	}
	
	void reset(){
		//loop in student and set isPicked = No;
		
		if(!this.allowRepetition){
			this.studentIndicesToPickFrom.removeAll(this.studentIndicesToPickFrom);
			for(int i=0 ; i<this.students.size(); i ++){
			   this.studentIndicesToPickFrom.add(i);
			}
		}		
	}
	
	
	class LoadStudents extends AsyncTask<Void, Void, ArrayList<Student>>
	{
		@Override
		protected void onPreExecute() {
		   super.onPreExecute();			
		}
		
		
		@Override
		protected ArrayList<Student> doInBackground(Void... params) {
			students  = databaseHelper.getAllStudents();
			return students;
			
		}
		
		@Override
		protected void onPostExecute(ArrayList<Student> lst) {
			// TODO Auto-generated method stub
			//super.onPostExecute(result);
			studentsActivity.showStudents(lst);
		}
		
	}
}
