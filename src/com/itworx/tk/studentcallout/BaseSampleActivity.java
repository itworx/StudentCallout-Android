package com.itworx.tk.studentcallout;

import java.util.Random;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.viewpagerindicator.PageIndicator;

public abstract class BaseSampleActivity extends FragmentActivity {
	private static final Random RANDOM = new Random();

	TestFragmentAdapter mAdapter;
	ViewPager mPager;
	PageIndicator mIndicator;
}