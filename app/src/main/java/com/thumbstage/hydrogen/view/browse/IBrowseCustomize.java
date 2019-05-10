package com.thumbstage.hydrogen.view.browse;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;

import com.thumbstage.hydrogen.model.bo.Privilege;

import java.util.Set;

public interface IBrowseCustomize {
    void customizeToolbar(Toolbar toolbar);
    void customizeFab(FloatingActionButton fab, Set<Privilege> userPrivileges);
}
