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
import com.thumbstage.hydrogen.view.browse.IAdapterFunction;
import com.thumbstage.hydrogen.view.browse.IBrowseCustomize;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IStartedFragment extends Fragment implements IBrowseCustomize, IAdapterFunction {

    @BindView(R.id.fragment_istarted_tablayout)
    TabLayout tabLayout;
    @BindView(R.id.fragment_istarted_viewpager)
    ViewPager viewPager;
    IStartedFragmentPagerAdapter pagerAdapter;
    String[] titles = new String[]{"Opened","Closed"};

    Toolbar toolbar;
    FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_istarted, container, false);
        ButterKnife.bind(this, view);

        for (String title: titles) {
            tabLayout.addTab(tabLayout.newTab().setText(title));
        }
        tabLayout.setupWithViewPager(viewPager);
        pagerAdapter = new IStartedFragmentPagerAdapter(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if ( pagerAdapter.getItem(position) instanceof IBrowseCustomize) {
                    ((IBrowseCustomize) pagerAdapter.getItem(position)).customizeToolbar(toolbar);
                    ((IBrowseCustomize) pagerAdapter.getItem(position)).customizeFab(fab);
                } else {
                    // default function
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return view;
    }

    // region implement of interface IBrowseCustomize
    @Override
    public void customizeToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
        toolbar.setTitle(getResources().getString(R.string.IStartedFragment_name));
        if( pagerAdapter.getItem(viewPager.getCurrentItem()) instanceof IBrowseCustomize){
            ((IBrowseCustomize) pagerAdapter.getItem(viewPager.getCurrentItem())).customizeToolbar(toolbar);
        }
    }

    @Override
    public void customizeFab(FloatingActionButton fab) {
        this.fab = fab;
        fab.hide();
        if( pagerAdapter.getItem(viewPager.getCurrentItem()) instanceof IBrowseCustomize){
            ((IBrowseCustomize) pagerAdapter.getItem(viewPager.getCurrentItem())).customizeFab(fab);
        }
    }
    // endregion

    // region implement of interface IAdapterFunction
    @Override
    public long getItemId() {
        return 3;
    }
    // endregion
}
