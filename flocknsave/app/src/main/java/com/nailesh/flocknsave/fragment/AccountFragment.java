package com.nailesh.flocknsave.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.nailesh.flocknsave.R;
import com.nailesh.flocknsave.adapter.AboutUsPagerAdapter;
import com.nailesh.flocknsave.adapter.AccountPagerAdapter;

public class AccountFragment extends Fragment {

    private ImageView account_image;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account,container,false);
        setup(view);
        return view;
    }

    private void setup(View view){
        account_image = view.findViewById(R.id.account_tab_image);
        account_image.setImageResource(R.drawable.icon_electricity_account);

        AccountPagerAdapter accountPagerAdapter = new AccountPagerAdapter(getContext(), getFragmentManager());
        ViewPager viewPager = view.findViewById(R.id.account_view_pager);
        viewPager.setAdapter(accountPagerAdapter);
        TabLayout tabs = view.findViewById(R.id.account_tabs);
        tabs.setupWithViewPager(viewPager);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        account_image.setVisibility(View.GONE);
                        account_image.setImageResource(R.drawable.icon_electricity_account);
                        account_image.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        account_image.setVisibility(View.GONE);
                        account_image.setImageResource(R.drawable.ic_location);
                        account_image.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}
