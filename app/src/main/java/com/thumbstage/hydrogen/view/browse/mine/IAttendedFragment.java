package com.thumbstage.hydrogen.view.browse.mine;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
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
import com.thumbstage.hydrogen.event.FabEvent;
import com.thumbstage.hydrogen.model.bo.Privilege;
import com.thumbstage.hydrogen.view.browse.IAdapterFunction;
import com.thumbstage.hydrogen.view.browse.IBrowseCustomize;
import com.thumbstage.hydrogen.viewmodel.UserViewModel;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class IAttendedFragment extends Fragment implements IBrowseCustomize, IAdapterFunction {

    @BindView(R.id.fragment_iattended_tablayout)
    TabLayout tabLayout;
    @BindView(R.id.fragment_iattended_viewpager)
    ViewPager viewPager;

    IAttendedFragmentPagerAdapter pagerAdapter;
    Toolbar toolbar;
    FloatingActionButton fab;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    UserViewModel userViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_iattended, container, false);
        ButterKnife.bind(this, view);

        tabLayout.setupWithViewPager(viewPager);
        pagerAdapter = new IAttendedFragmentPagerAdapter(getChildFragmentManager());
        for (String title: pagerAdapter.getTitles() ) {
            tabLayout.addTab(tabLayout.newTab().setText(title));
        }
        viewPager.setAdapter(pagerAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AndroidSupportInjection.inject(this);
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
    }

    // region implement of interface IBrowseCustomize
    @Override
    public void customizeToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
        toolbar.setTitle(toolbar.getContext().getResources().getString(R.string.IAttendedFragment_name));
        if( pagerAdapter.getItem(viewPager.getCurrentItem()) instanceof IBrowseCustomize){
            ((IBrowseCustomize) pagerAdapter.getItem(viewPager.getCurrentItem())).customizeToolbar(toolbar);
        }
    }

    @Override
    public void customizeFab(FloatingActionButton fab) {
        this.fab = fab;
        if(userViewModel.getCurrentUser().getPrivileges().contains(Privilege.CREATE_TOPIC) ||
                userViewModel.getCurrentUser().getPrivileges().contains(Privilege.CREATE_SEMINAR)) {
            fab.show();
            fab.setImageResource(R.drawable.ic_button_plus);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new FabEvent("plus"));
                }
            });
        }
    }
    // endregion

    // region implement of interface IAdapterFunction
    @Override
    public long getItemId() {
        return Privilege.BROWSE_I_ATTENDED.ordinal();
    }
    // endregion
}
