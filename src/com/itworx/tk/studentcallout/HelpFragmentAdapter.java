package com.itworx.tk.studentcallout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.viewpagerindicator.IconPagerAdapter;

class HelpFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
    protected static final String[] CONTENT = new String[] { String.valueOf(R.drawable.screenshot1)+"@@"+R.string.string_sc1, String.valueOf(R.drawable.screenshot2)+"@@"+R.string.string_sc2, String.valueOf(R.drawable.screenshot3)+"@@"+R.string.string_sc3};
    protected static final int[] ICONS = new int[] {
       0,0,0,0
    };

    private int mCount = CONTENT.length;

    public HelpFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return HelpFragment.newInstance(CONTENT[position % CONTENT.length]);
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return HelpFragmentAdapter.CONTENT[position % CONTENT.length];
    }

    @Override
    public int getIconResId(int index) {
      return ICONS[index % ICONS.length];
    }

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}