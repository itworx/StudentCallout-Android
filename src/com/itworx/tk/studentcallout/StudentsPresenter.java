package com.itworx.tk.studentcallout;

import java.io.BufferedReader;
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

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

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

	public StudentsPresenter(IStudentsActivity studentsActivity,
			Context context, String fromTk) {
		this.studentsActivity = studentsActivity;
		this.databaseHelper = new DataManager(context);
		this.dbIsEmpty = this.databaseHelper.databaseIsEmpty();
		this.buttonTitle = (String) (this.dbIsEmpty ? context.getString(R.string.button_use_demo_data) : context.getString(R.string.button_random));
		this.studentsActivity.setButtonTitle(this.buttonTitle);
		this.studentIndicesToPickFrom = new ArrayList<Integer>();
		this.activityContext = context;
		SharedPreferences preferences = context.getSharedPreferences(
				"MyPreferences", Context.MODE_PRIVATE);
		this.preferencesEditor = preferences.edit();
		// TO DO: load from persistent settings
		this.allowRepetition = preferences.getBoolean(ALLOW_KEY, false);
		if (null != fromTk && fromTk.equalsIgnoreCase("true"))
			this.useDataFromExternalStorage();
		else
			getStudents();
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
			
		new UseDemoData().execute();
		
		} else {
			Random randomGenerator = new Random();
			if (this.allowRepetition) {
				int index = randomGenerator.nextInt(this.students.size());
				Student selectedStudent = this.students.get(index);
				this.studentsActivity.showStudent(selectedStudent);
			} else {
				if (this.studentIndicesToPickFrom.size() == 0) {
					// Show MSG
					studentsActivity.showAlert("All student are selected", "You have selected all your students.\nThe randomization will start from the beginning and selection will be reset.");
					this.reset();
					return;
				}
				int index = randomGenerator.nextInt(this.studentIndicesToPickFrom.size());
				int pickedIndex = this.studentIndicesToPickFrom.get(index);
				this.studentIndicesToPickFrom.remove(index);
				Student selectedStudent = this.students.get(pickedIndex);
				studentsActivity.showStudent(selectedStudent);
				selectedStudent.isPicked = true;
				this.databaseHelper.updateStudentSelectedState(selectedStudent,
						true);
				this.studentsActivity.changeSelectionOfStudent(selectedStudent);
			}
		}
	}

	void reset() {
		if (this.students == null || this.students.size() == 0) {
			return;
		}
		if (!this.allowRepetition) {
			this.studentIndicesToPickFrom
					.removeAll(this.studentIndicesToPickFrom);
			for (int i = 0; i < this.students.size(); i++) {
				this.studentIndicesToPickFrom.add(i);
				Student student = this.students.get(i);
				student.isPicked = false;
				this.studentsActivity.changeSelectionOfStudent(student);
			}
			
			this.databaseHelper.updateStudentsSelectedState(this.students, false);
		}
	}

	class LoadStudents extends AsyncTask<Void, Void, ArrayList<Student>> {
		@Override
		protected void onPreExecute() {
			studentsActivity.showHudWithText("Loading...");
			super.onPreExecute();
		}

		@Override
		protected ArrayList<Student> doInBackground(Void... params) {
			students = databaseHelper.getAllStudents();
			for (int i = 0; i < students.size(); i++) {
				Student student = students.get(i);
				if (!student.isPicked) {
					studentIndicesToPickFrom.add(i);
				}
			}

			return students;

		}

		@Override
		protected void onPostExecute(ArrayList<Student> lst) {
			// TODO Auto-generated method stub
			// super.onPostExecute(result);
			studentsActivity.showStudents(lst);
			studentsActivity.hideHud();
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
		} else {
			Toast.makeText(activityContext,
					"Error happen when reading from external storage",
					Toast.LENGTH_LONG).show();
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

	class UseDemoData extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			studentsActivity.showHudWithText("Loading...");
			
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			useDemoData();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			buttonTitle = "Pick Random";
		    studentsActivity.setButtonTitle(buttonTitle);
		    studentsActivity.hideHud();
			super.onPostExecute(result);
			
		}
	}

}
