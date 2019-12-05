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

public class AboutUsFragment extends Fragment {

    ImageView about_image;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_scope,container,false);

        about_image = view.findViewById(R.id.about_tab_image);
        about_image.setImageResource(R.drawable.ic_home);


        SectionPagerAdapter sectionPagerAdapter = new SectionPagerAdapter(getContext(), getFragmentManager(), about_image);
        ViewPager viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionPagerAdapter);
        TabLayout tabs = view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        about_image.setVisibility(View.GONE);
                        about_image.setImageResource(R.drawable.ic_home);
                        about_image.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        about_image.setVisibility(View.GONE);
                        about_image.setImageResource(R.drawable.ic_account);
                        about_image.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        about_image.setVisibility(View.GONE);
                        about_image.setImageResource(R.drawable.ic_add_photo);
                        about_image.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        about_image.setVisibility(View.GONE);
                        about_image.setImageResource(R.drawable.ic_add_product);
                        about_image.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        about_image.setVisibility(View.GONE);
                        about_image.setImageResource(R.drawable.ic_dashboard);
                        about_image.setVisibility(View.VISIBLE);
                        break;
                    case 5:
                        about_image.setVisibility(View.GONE);
                        about_image.setImageResource(R.drawable.ic_login);
                        about_image.setVisibility(View.VISIBLE);
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




        return view;


    }
}