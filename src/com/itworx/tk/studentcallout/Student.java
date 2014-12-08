package com.itworx.tk.studentcallout;

import android.os.Parcel;
import android.os.Parcelable;

public class Student implements Parcelable {
	int id;
	String firstname;
	String lastname;
	String imagePath;
	Object image;
	Boolean isPicked;
	int color;
	
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(firstname);
		dest.writeString(lastname);
		dest.writeString(imagePath);
		dest.writeInt(color);
	}
	
	
	public static final Parcelable.Creator<Student> CREATOR = new Parcelable.Creator<Student>() {
        public Student createFromParcel(Parcel in) {
            return new Student(in);
        }

		@Override
		public Student[] newArray(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}
    };
	
    private Student(Parcel in) {
        firstname = in.readString();
        lastname = in.readString();
        imagePath = in.readString();
        color = in.readInt();
    }
    
	public Student() {
		// TODO Auto-generated constructor stub
	}
}
