package com.thumbstage.hydrogen.view.browse.published;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.view.browse.IBrowseCustomize;

public class PublishedClosedFragment extends Fragment implements IBrowseCustomize {

    // region implement of interface IBrowseCustomize
    @Override
    public void customizeToolbar(Toolbar toolbar) {
        toolbar.setTitle(getResources().getString(R.string.PublishedClosedFragment_name));
    }

    @Override
    public void customizeFab(FloatingActionButton fab) {
        fab.hide();
    }
    // endregion
}
