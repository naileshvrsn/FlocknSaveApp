package com.nailesh.flocknsave.fragment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.nailesh.flocknsave.R;

public class SectionPagerAdapter extends FragmentPagerAdapter {

    private static final int[] TAB_TITLES = new int[]
            {R.string.about_how,R.string.about_what,R.string.about_when,R.string.about_where,
                    R.string.about_who,R.string.about_why};
    private static final int[] TAB_IMAGES = new int[]
            {R.drawable.ic_account,R.drawable.ic_add_photo,R.drawable.ic_add_product,R.drawable.ic_dashboard,
                    R.drawable.ic_order_list,R.drawable.ic_login};

    private final Context mContext;
    private  ImageView aboutImage;


    public SectionPagerAdapter(Context context, FragmentManager fm, ImageView img) {
        super(fm);
        mContext = context;
        aboutImage = img;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new AboutHowFragment();
                break;
            case 1:
                fragment = new AboutWhatFragment();
                break;
            case 2:
                fragment = new AboutWhenFragment();

                break;
            case 3:
                fragment = new AboutWhereFragment();

                break;
            case 4:
                fragment = new AboutWhoFragment();
                break;
            case 5:
                fragment = new AboutWhyFragment();
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
        return 6;
    }



}
