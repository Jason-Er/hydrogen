package com.thumbstage.hydrogen.view.browse.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.view.browse.BrowseCustomize;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IStartedFragment extends Fragment implements BrowseCustomize {

    @BindView(R.id.fragment_istarted_tablayout)
    TabLayout tabLayout;
    @BindView(R.id.fragment_istarted_viewpager)
    ViewPager viewPager;
    String[] titles = new String[]{"Opened","Closed"};

    IStartedFragmentPagerAdapter iStartedFragmentPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_istarted, container, false);
        ButterKnife.bind(this, view);

        for (String title: titles) {
            tabLayout.addTab(tabLayout.newTab().setText(title));
        }
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new IStartedFragmentPagerAdapter(getFragmentManager()));
        return view;
    }

    // region implement of interface BrowseCustomize
    @Override
    public void customizeToolbar(Toolbar toolbar) {
        toolbar.setTitle(getResources().getString(R.string.IStartedFragment_name));
    }

    @Override
    public void customizeFab(FloatingActionButton fab) {
        fab.show();
        fab.setImageResource(R.drawable.ic_button_plus);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    // endregion
}
