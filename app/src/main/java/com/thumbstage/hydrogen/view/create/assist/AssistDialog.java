package com.thumbstage.hydrogen.view.create.assist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thumbstage.hydrogen.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AssistDialog extends BottomSheetDialogFragment {

    @BindView(R.id.fragment_dialog_assist_viewpager)
    ViewPager viewPager;
    AssistPagerAdapter pagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_dialog_assist, container);
        ButterKnife.bind(this, contentView);

        pagerAdapter = new AssistPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        return contentView;
    }


}
