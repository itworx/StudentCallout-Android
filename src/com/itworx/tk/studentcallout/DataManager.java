package com.itworx.tk.studentcallout;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import android.R.bool;
import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class DataManager extends SQLiteOpenHelper{

	public static final String APP_KEY = "TeacherDB";
	public static final String ID_KEY = "id";	
	public static final String FIRST_NAME_KEY = "firstName";
	public static final String LAST_NAME_KEY = "lastName";
	public static final String IMAGE_PATH_KEY = "imagePath";
	public static final String IMAGE_KEY = "studentImage";
	public static final String IS_PICKED_KEY = "isPicked";	
	public static final String TABLE_NAME = "student";	
	
	public DataManager(Context context) {
		super(context, "teacherkit", null, 1);
		Log.d(APP_KEY, "database created");	
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String sql =  "CREATE TABLE student (id INTEGER PRIMARY KEY,firstName TEXT NOT NULL,lastName TEXT NOT NULL,isPicked BOOL,studentImage BLOB)";
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
	 	SQLiteDatabase db  = getWritableDatabase();
	 	
	 	ContentValues values = new ContentValues();
	 	values.put(FIRST_NAME_KEY, student.firstname);
	 	values.put(LAST_NAME_KEY, student.lastname);
	
	 	// add image as byte[]
	 	byte[] imageBlob = DataManager.convertBitmapToByteArray(student.imageBitmap);
	 	values.put(IMAGE_KEY, imageBlob);
	 	values.put(IS_PICKED_KEY, false);
	
	 	db.insert(TABLE_NAME,null, values);
	 	
	 	db.close();
	}
	
	public void updateStudentSelectedState(Student student,Boolean isPicked) {
		SQLiteDatabase db = getWritableDatabase();
	
	    ContentValues args = new ContentValues();
	    args.put(IS_PICKED_KEY, isPicked);
	    db.update(TABLE_NAME, args, "id=" + student.id, null);		
		
		db.close();
	}
	
	public void updateStudentsSelectedState(ArrayList<Student> students,Boolean isPicked) {
		SQLiteDatabase db = getWritableDatabase();
	
	    ContentValues args = new ContentValues();
	    args.put(IS_PICKED_KEY, isPicked);
	    
	    
	    for (int i = 0; i < students.size(); i++) {
	    	db.update(TABLE_NAME, args, "id=" + students.get(i).id, null);
		}
		
		db.close();
	}

	public ArrayList<Student> getAllStudents()
	{
		SQLiteDatabase db  = getReadableDatabase();
		Cursor cursor =db.query(TABLE_NAME, null, null, null, null, null, FIRST_NAME_KEY);
		Student student ; 
		ArrayList<Student> lstStudents = new ArrayList<Student>(cursor.getCount());
		while(cursor.moveToNext())
		{
			student = new Student();
			student.id = cursor.getInt(cursor.getColumnIndex(ID_KEY));			
			student.firstname = cursor.getString(cursor.getColumnIndex(FIRST_NAME_KEY));			
			student.lastname= cursor.getString(cursor.getColumnIndex(LAST_NAME_KEY));
			byte[] imageBlob = cursor.getBlob(cursor.getColumnIndex(IMAGE_KEY));
			student.imageBitmap = DataManager.getBitmabFromByteArray(imageBlob);
			student.isPicked = cursor.getInt(cursor.getColumnIndex(IS_PICKED_KEY)) > 0;
			lstStudents.add(student);
		}
		cursor.close();
		db.close();
		return lstStudents ; 
	}
	
	
	public static Bitmap getBitmabFromByteArray(byte[] imgBytes) {
		if (null != imgBytes && imgBytes.length > 0) {

			BitmapFactory.Options options = new BitmapFactory.Options();
			options = new BitmapFactory.Options();
			options.inDither = false;
			options.inPurgeable = true;
			options.inInputShareable = true;
			options.inTempStorage = new byte[1024 * 32];
			Bitmap bm = BitmapFactory.decodeByteArray(imgBytes, 0,
					imgBytes.length, options);
			return bm;
		} else
			return null;
	}
	
	
	public static byte[] convertBitmapToByteArray(Bitmap bmp) {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, bos);
		byte[] bArray = bos.toByteArray();
		return bArray;
	}

	public boolean databaseIsEmpty() {
		SQLiteDatabase db  = getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM  student", null);
		cursor.moveToFirst();
		int count = cursor.getInt(0);		
		cursor.close();
		db.close();
		return !(count>0);
	}
	
	public void clearDB(){
		SQLiteDatabase db  = getWritableDatabase();
		db.delete(TABLE_NAME, null, null);
		db.close();
	}
	
}
