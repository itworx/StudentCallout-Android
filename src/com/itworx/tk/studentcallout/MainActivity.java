package com.itworx.tk.studentcallout;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements IStudentsActivity {
	StudentsPresenter studentsPresenter;
	Context context;
	View studentCard;
	Button buttonNext;
	ProgressDialog progDailog;
	String mFromTK = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		try {
			mFromTK = getIntent().getStringExtra("fromTK");
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		this.buttonNext = (Button) this.findViewById(R.id.buttonNext);
		this.buttonNext.setGravity(Gravity.CENTER);
		studentsPresenter = new StudentsPresenter(this, this, mFromTK);

		buttonNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				studentsPresenter.selectNextStudent();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {

			LinearLayout linearLayout = new LinearLayout(this);
			linearLayout.setOrientation(LinearLayout.VERTICAL);
			linearLayout.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

			ToggleButton allowRepetitionButton = new ToggleButton(this);
			allowRepetitionButton.setTextOff("Allow Repetition Off");
			allowRepetitionButton.setTextOn("Allow Repetition On");
			allowRepetitionButton.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			linearLayout.addView(allowRepetitionButton);

			final Button resetButton = new Button(this);
			resetButton.setText("Reset");
			resetButton.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			linearLayout.addView(resetButton);

			AlertDialog.Builder builder;
			AlertDialog alertDialog;
			builder = new AlertDialog.Builder(this);
			builder.setView(linearLayout);
			alertDialog = builder.create();
			alertDialog.setTitle("Settings");
			alertDialog.show();

			allowRepetitionButton.setChecked(studentsPresenter.allowRepetition);
			resetButton.setEnabled(!studentsPresenter.allowRepetition);

			allowRepetitionButton
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							studentsPresenter.setAllowRepetition(isChecked);
							resetButton
									.setEnabled(!studentsPresenter.allowRepetition);
						}
					});

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void showStudents(ArrayList<Student> students) {
		// TODO Auto-generated method stub
		GridView gridView = (GridView) findViewById(R.id.gridView);
		StudentsAdapter adapter = new StudentsAdapter(this, students);
		gridView.setAdapter(adapter);
	}

	@Override
	public void showStudent(Student student) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, StudentActivity.class);
		intent.putExtra("studentModel", student);
		startActivityForResult(intent, 0);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 0) {
			if (resultCode == 0) { // nothing
			} else if (resultCode == 1) { // pick next
				studentsPresenter.selectNextStudent();
			}
		}
	}

	@Override
	public void changeSelectionOfStudent(Student student) {
		// TODO Auto-generated method stub
		GridView gridView = (GridView) findViewById(R.id.gridView);
		final int numVisibleChildren = gridView.getChildCount();
		final int firstVisiblePosition = gridView.getFirstVisiblePosition();

		for (int i = 0; i < numVisibleChildren; i++) {
			int positionOfView = firstVisiblePosition + i;

			if (positionOfView == studentsPresenter.students.indexOf(student)) {
				View view = gridView.getChildAt(i);
				ImageView imageView = (ImageView) view
						.findViewById(R.id.student_SelectImageView);

				if (student.isPicked) {
					imageView.setVisibility(View.VISIBLE);
				} else {
					imageView.setVisibility(View.INVISIBLE);
				}

			}
		}
	}

	@Override
	public void setButtonTitle(String buttonTitle) {
		this.buttonNext.setText(buttonTitle);
	}

	@Override
	public void showHudWithText(String buttonTitle) {
		if(progDailog == null){
			progDailog = new ProgressDialog(this);
			progDailog.setMessage(this.getResources().getString(
					R.string.str_loading));
			progDailog.setIndeterminate(false);
			progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progDailog.setCancelable(false);
			progDailog.show();
		}
	}

	@Override
	public void hideHud() {
		if (null != progDailog)
			progDailog.dismiss();
			progDailog = null;
	}

}
