package com.thumbstage.hydrogen.view.browse.mine;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.view.browse.BrowseCustomize;

public class IStartedClosedFragment extends Fragment implements BrowseCustomize {
    // region implement of interface BrowseCustomize
    @Override
    public void customizeToolbar(Toolbar toolbar) {

    }

    @Override
    public void customizeFab(FloatingActionButton fab) {
        fab.hide();
    }
    // endregion
}
