package com.itworx.tk.studentcallout;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataManager extends SQLiteOpenHelper{

	public static final String APP_KEY = "TeacherDB";
	public static final String ID_KEY = "id";	
	public static final String FIRST_NAME_KEY = "firstName";
	public static final String LAST_NAME_KEY = "lastName";
	public static final String IMAGE_PATH_KEY = "imagePath";
	public static final String IS_PICKED_KEY = "isPicked";	
	public static final String TABLE_NAME = "student";	
	
	public DataManager(Context context) {
		super(context, "teacherkit", null, 1);
		Log.d(APP_KEY, "database created");	
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String sql =  "CREATE TABLE student (id INTEGER PRIMARY KEY,firstName TEXT NOT NULL,lastName TEXT NOT NULL,isPicked BOOL,imagePath text)";
		try{
			db.execSQL(sql);
			Log.d(APP_KEY, "student table created");
		}
		catch(Exception ex)
		{
			Log.d(APP_KEY, "fail to craete table"+ex.getMessage());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		db.execSQL("DROP TABLE student if exists");
		onCreate(db);
		
	}
	
	public void addStudent(Student student)
	{
		//open connection 
	 	SQLiteDatabase db  = getWritableDatabase();
	 	
	 	
	 	//step 2 passing values 
	 	ContentValues values = new ContentValues();
	 	values.put("firstName", student.firstname);
	 	values.put("lastName", student.lastname);
	 	values.put("imagePath", student.imagePath);	 	
	 	
	 	
	 	//step3 
	 	db.insert(TABLE_NAME,null, values);
	 	
	 	db.close();
	}

	public ArrayList<Student> getAllStudents()
	{
		SQLiteDatabase db  = 	getReadableDatabase();
		
//		db.rawQuery("select * from student",null);
		
//		Cursor cursor = db.query(TABLE_NAME, new String[]{"name"}, "name=? and id=?", new String []{String.valueOf(5),"5"}, null, null, "");
		Cursor cursor =db.query(TABLE_NAME, null, null, null, null, null, FIRST_NAME_KEY);
		Student student ; 
		ArrayList<Student> lstStudents = new ArrayList<Student>(cursor.getCount());
		while(cursor.moveToNext())
		{
			student = new Student();
			student.id = cursor.getInt(cursor.getColumnIndex(ID_KEY));			
			student.firstname = cursor.getString(cursor.getColumnIndex(FIRST_NAME_KEY));			
			student.lastname= cursor.getString(cursor.getColumnIndex(LAST_NAME_KEY));
			student.imagePath = cursor.getString(cursor.getColumnIndex(IMAGE_PATH_KEY));			
			lstStudents.add(student);
		}
		cursor.close();
		db.close();
		return lstStudents ; 
	}
	
	
}
