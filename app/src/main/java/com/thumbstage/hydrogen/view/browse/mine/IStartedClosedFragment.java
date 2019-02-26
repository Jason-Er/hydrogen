package com.thumbstage.hydrogen.view.browse.mine;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.thumbstage.hydrogen.view.browse.IBrowseCustomize;

public class IStartedClosedFragment extends Fragment implements IBrowseCustomize {
    // region implement of interface IBrowseCustomize
    @Override
    public void customizeToolbar(Toolbar toolbar) {

    }

    @Override
    public void customizeFab(FloatingActionButton fab) {
        fab.hide();
    }
    // endregion
}
