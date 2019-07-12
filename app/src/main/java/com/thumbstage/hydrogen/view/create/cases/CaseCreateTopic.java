package com.thumbstage.hydrogen.view.create.cases;

import android.view.Menu;
import android.view.MenuInflater;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.view.create.feature.ICanCreateOptionsMenu;

public class CaseCreateTopic extends CaseBase implements ICanCreateOptionsMenu {
    @Override
    public void createOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_case_create, menu);
    }
}
