package com.nailesh.flocknsave.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.nailesh.flocknsave.R;
import com.nailesh.flocknsave.fragment.AboutHowFragment;
import com.nailesh.flocknsave.fragment.AboutWhatFragment;
import com.nailesh.flocknsave.fragment.AboutWhenFragment;
import com.nailesh.flocknsave.fragment.AboutWhereFragment;
import com.nailesh.flocknsave.fragment.AboutWhoFragment;
import com.nailesh.flocknsave.fragment.AboutWhyFragment;

public class SectionPagerAdapter extends FragmentPagerAdapter {

    private static final int[] TAB_TITLES = new int[]
            {R.string.about_how,R.string.about_why,R.string.about_what,R.string.about_when,R.string.about_where,
                    R.string.about_who};
    private static final int[] TAB_IMAGES = new int[]
            {R.drawable.icon_how,R.drawable.icon_why,R.drawable.icon_what,R.drawable.icon_when,
                    R.drawable.icon_where,R.drawable.icon_who};

    private final Context mContext;

    public SectionPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new AboutHowFragment();
                break;
            case 1:
                fragment = new AboutWhyFragment();
                break;
            case 2:
                fragment = new AboutWhatFragment();
                break;
            case 3:
                fragment = new AboutWhenFragment();
                break;
            case 4:
                fragment = new AboutWhereFragment();
                break;
            case 5:
                fragment = new AboutWhoFragment();
                break;
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 6 total pages.
        return TAB_TITLES.length;
    }

}
