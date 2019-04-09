package com.thumbstage.hydrogen.view.browse.mine;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.view.browse.IBrowseCustomize;
import com.thumbstage.hydrogen.view.common.BasicBrowseFragment;

import javax.inject.Inject;

import butterknife.BindView;

public class IStartedClosedFragment extends BasicBrowseFragment {


    // region implement of interface IBrowseCustomize
    @Override
    public void customizeToolbar(Toolbar toolbar) {

    }

    @Override
    public void customizeFab(FloatingActionButton fab) {
        fab.hide();
    }
    // endregion
    @Override
    public void customObserve() {

    }

}
