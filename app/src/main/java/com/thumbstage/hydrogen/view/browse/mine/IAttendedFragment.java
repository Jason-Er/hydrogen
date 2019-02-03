package com.thumbstage.hydrogen.view.browse.mine;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.view.browse.BrowseCustomize;

public class IAttendedFragment extends Fragment implements BrowseCustomize {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_iattended, container, false);
        return rootView;
    }

    // region implement of interface BrowseCustomize
    @Override
    public void customizeToolbar(Toolbar toolbar) {
        toolbar.setTitle(getResources().getString(R.string.IAttendedFragment_name));
    }

    @Override
    public void customizeFab(FloatingActionButton fab) {
        fab.hide();
    }
    // endregion
}
