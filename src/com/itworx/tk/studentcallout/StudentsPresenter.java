package com.itworx.tk.studentcallout;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;

import org.apache.http.util.ByteArrayBuffer;

import android.R.string;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.ContactsContract.DeletedContacts;
import android.support.v7.appcompat.*;
import android.util.Log;
import android.widget.Toast;
import android.os.Parcel;
import android.os.Parcelable;
import com.itworx.tk.studentcallout.Student;

public class StudentsPresenter {
	public static final String ALLOW_KEY = "ALLOWREPTITION";
	IStudentsActivity studentsActivity;
	Boolean allowRepetition;
	ArrayList<Student> students;
	ArrayList<Integer> studentIndicesToPickFrom;
	DataManager databaseHelper;
	Context activityContext;
	SharedPreferences.Editor preferencesEditor;
	boolean dbIsEmpty;
	String buttonTitle;

	public StudentsPresenter(IStudentsActivity studentsActivity, Context context) {
		this.studentsActivity = studentsActivity;
		this.databaseHelper = new DataManager(context);
		this.dbIsEmpty = this.databaseHelper.databaseIsEmpty();
		this.buttonTitle = this.dbIsEmpty ? "Use Demo Data" : "Pick Random";
		this.studentsActivity.setButtonTitle(this.buttonTitle);
		this.studentIndicesToPickFrom = new ArrayList<Integer>();
		this.activityContext = context;
		SharedPreferences preferences = context.getSharedPreferences(
				"MyPreferences", Context.MODE_PRIVATE);
		this.preferencesEditor = preferences.edit();
		// TO DO: load from persistent settings
		this.allowRepetition = preferences.getBoolean(ALLOW_KEY, false);
		this.useDataFromExternalStorage();
	}

	void setAllowRepetition(Boolean allow) {
		this.allowRepetition = allow;
		if (allow) {
			this.studentIndicesToPickFrom = new ArrayList<Integer>();
		}

		this.preferencesEditor.putBoolean(ALLOW_KEY, this.allowRepetition);
		this.preferencesEditor.commit();
	}

	void getStudents() {
		if (!this.dbIsEmpty)
			new LoadStudents().execute();
	}

	void selectNextStudent() {
		if (this.dbIsEmpty) {
			this.useDemoData();
			this.buttonTitle = "Pick Random";
			this.studentsActivity.setButtonTitle(buttonTitle);
		} else {
			Random randomGenerator = new Random();
			if (this.allowRepetition) {
				int index = randomGenerator.nextInt(this.students.size());
				Student selectedStudent = this.students.get(index);
			      this.studentsActivity.showStudent(selectedStudent);
			} else {
				if (this.studentIndicesToPickFrom.size() == 0) {
					// Show MSG
					this.reset();
				}
				int index = randomGenerator
						.nextInt(this.studentIndicesToPickFrom.size());
				int pickedIndex = this.studentIndicesToPickFrom.get(index);
				this.studentIndicesToPickFrom.remove(index);
				Student selectedStudent = this.students.get(pickedIndex);
				studentsActivity.showStudent(selectedStudent);
				selectedStudent.isPicked = true;
			      this.studentsActivity.changeSelectionOfStudent(selectedStudent);
			}
		}
	}

	void reset(){
		if(!this.allowRepetition){
			this.studentIndicesToPickFrom.removeAll(this.studentIndicesToPickFrom);
			for(int i=0 ; i<this.students.size(); i ++){
			   this.studentIndicesToPickFrom.add(i);
			   Student student = this.students.get(i);
			   student.isPicked = false;
			   this.studentsActivity.changeSelectionOfStudent(student);
			}
		}
	}

	class LoadStudents extends AsyncTask<Void, Void, ArrayList<Student>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected ArrayList<Student> doInBackground(Void... params) {
			students = databaseHelper.getAllStudents();
			return students;

		}

		@Override
		protected void onPostExecute(ArrayList<Student> lst) {
			// TODO Auto-generated method stub
			// super.onPostExecute(result);
			studentsActivity.showStudents(lst);
		}

	}

	void useDemoData() {

		AssetManager assetManager = this.activityContext.getAssets();
		InputStream is = null;

		try {
			is = assetManager.open("class/roster.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}

		BufferedReader reader = null;
		reader = new BufferedReader(new InputStreamReader(is,
				Charset.forName("UTF-8")));

		String line = "";
		StringTokenizer st = null;
		try {

			while ((line = reader.readLine()) != null) {
				st = new StringTokenizer(line, ",");
				Student obj = new Student();
				obj.firstname = st.nextToken();
				obj.lastname = st.nextToken();
				String imagePath = st.nextToken();
				Bitmap imageBitmap = this.getBitmapImageFromAssets("class/"
						+ imagePath);
				obj.imageBitmap = imageBitmap;
				this.databaseHelper.addStudent(obj);
			}
			this.dbIsEmpty = false;
			this.getStudents();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	void useDataFromExternalStorage() {
		this.databaseHelper.clearDB();
		String externalPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		File csvFile = new File(externalPath + "/StudentCallout",
				"students.csv");
		if (csvFile.exists()) {
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(csvFile);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			BufferedReader reader = null;
			reader = new BufferedReader(new InputStreamReader(fis,
					Charset.forName("UTF-8")));

			String line = "";
			StringTokenizer st = null;
			try {

				while ((line = reader.readLine()) != null) {
					st = new StringTokenizer(line, ",");
					Student obj = new Student();
					obj.firstname = st.nextToken();
					obj.lastname = st.nextToken();
					String imageName = st.nextToken();
					Bitmap imageBitmap = BitmapFactory.decodeFile(externalPath
							+ "/StudentCallout/" + imageName);
					obj.imageBitmap = imageBitmap;
					this.databaseHelper.addStudent(obj);
				}
				this.getStudents();
			} catch (IOException e) {

			}
		}
		else{
			Toast.makeText(activityContext, "Error happen when reading from external storage", Toast.LENGTH_LONG)
			.show();			
		}
	}

	private Bitmap getBitmapImageFromAssets(String imagePath) {
		try {

			AssetManager assetManager = this.activityContext.getAssets();
			InputStream is = assetManager.open(imagePath);
			Bitmap bitmap = BitmapFactory.decodeStream(is, null, null);
			return bitmap;

		} catch (Exception e) {
			Log.d("ImageManager", "Error: " + e.toString());

			return null;
		}
	}

}
