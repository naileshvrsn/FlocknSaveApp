package com.nailesh.flocknsave.adapter;

import android.content.Context;

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
import com.nailesh.flocknsave.fragment.AccountElectricityFragment;
import com.nailesh.flocknsave.fragment.AccountLocationFragment;

public class AccountPagerAdapter extends FragmentPagerAdapter {

    private static final int[] TAB_TITLES = new int[]
            {R.string.account_electricity,R.string.account_delivery_location};

    private final Context mContext;

    public AccountPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new AccountElectricityFragment();
                break;
            case 1:
                fragment = new AccountLocationFragment();
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
