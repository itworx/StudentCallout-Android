package com.itworx.tk.studentcallout;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ShareActionProvider;
import android.widget.ShareActionProvider.OnShareTargetSelectedListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements IStudentsActivity {
	StudentsPresenter studentsPresenter;
	Context context;
	View studentCard;
	Button buttonNext;
	ProgressDialog progDailog;
	String mFromTK = "";
	Menu menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().setTitle("Student Callout");
		context = this;
		try {
			mFromTK = getIntent().getStringExtra("fromTK");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (null != mFromTK && mFromTK.equalsIgnoreCase("true")
				&& mFromTK != "") {
			getActionBar().setDisplayShowHomeEnabled(false);
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setTitle("TeacherKit");
		} else {
			getActionBar().setDisplayShowHomeEnabled(true);
			getActionBar().setDisplayHomeAsUpEnabled(false);
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
		this.menu = menu;

		MenuItem item = menu.findItem(R.id.menu_item_share);
		ShareActionProvider myShareActionProvider = (ShareActionProvider) item
				.getActionProvider();
		//myShareActionProvider.setShowHistory(false);
		Intent myIntent = new Intent();
		myIntent.setAction(Intent.ACTION_SEND);
		myIntent.putExtra(Intent.EXTRA_TEXT,
				this.getString(R.string.Share_Message));
		myIntent.putExtra(Intent.EXTRA_SUBJECT,
				this.getString(R.string.Share_Subject));
		myIntent.setType("text/plain");
		myShareActionProvider.setShareIntent(myIntent);
		//myShareActionProvider.setOnShareTargetSelectedListener((OnShareTargetSelectedListener) getApplication());		
		
		this.RefreshMenuItems();
		return true;
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		int id = item.getItemId();
		if (id == R.id.action_repetition) {
			studentsPresenter
					.setAllowRepetition(!studentsPresenter.allowRepetition);
		} else if (id == R.id.action_reset) {
			studentsPresenter.reset();
		} else if (id == 16908332) {
			// finish();
			PackageManager pm = getPackageManager();
			Intent intent = pm.getLaunchIntentForPackage("com.teacherkit.app");
			startActivity(intent);
		} else if (id == R.id.action_help) {
			Intent intent = new Intent(getApplicationContext(),
					SampleCirclesDefault.class);
			startActivity(intent);
		}

		this.RefreshMenuItems();

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

		ApplicationManager applicationManager = (ApplicationManager) getApplicationContext();
		applicationManager.selectedStudent = student;
		this.buttonNext.setVisibility(View.INVISIBLE);
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, StudentActivity.class);
		startActivityForResult(intent, 0);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 0) {
			if (resultCode == 0) {
				this.buttonNext.setVisibility(View.VISIBLE);
				// nothing
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
				ImageView selectImageView = (ImageView) view
						.findViewById(R.id.student_SelectImageView);
				ImageView imageView = (ImageView) view
						.findViewById(R.id.student_ImageView);

				if (student.isPicked) {
					selectImageView.setVisibility(View.VISIBLE);
					AlphaAnimation alpha = new AlphaAnimation(0.5F, 0.5F); // change
																			// values
																			// as
																			// you
																			// want
					imageView.setBackgroundColor(0);
					alpha.setDuration(0);
					alpha.setFillAfter(true);
					imageView.startAnimation(alpha);
				} else {
					selectImageView.setVisibility(View.INVISIBLE);
					AlphaAnimation alpha = new AlphaAnimation(1F, 1F); // change
																		// values
																		// as
																		// you
																		// want
					imageView.setBackgroundColor(1);
					alpha.setDuration(0);
					alpha.setFillAfter(true);
					imageView.startAnimation(alpha);
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
		if (progDailog == null) {
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

	@Override
	public void showAlert(String titleText, String messageBody) {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this).setTitle(titleText)
				.setMessage(messageBody)
				.setPositiveButton(this.getString(R.string.OK), null).show();
		this.buttonNext.setVisibility(View.VISIBLE);
	}

	@Override
	public void showAck(String text) {
		// TODO Auto-generated method stub
		Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
	}

	void RefreshMenuItems() {
		MenuItem repetitionItem = menu.findItem(R.id.action_repetition);
		MenuItem resetItem = menu.findItem(R.id.action_reset);
		repetitionItem
				.setTitle(studentsPresenter.allowRepetition ? getString(R.string.allow_repetition_on)
						: getString(R.string.allow_repetition_off));
		resetItem.setEnabled(!studentsPresenter.allowRepetition);
	}

	@Override
	public void showHideHelp(Boolean show) {
		// TODO Auto-generated method stub
		TextView textView = (TextView) this.findViewById(R.id.textEmpty);
		textView.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
	}
}
