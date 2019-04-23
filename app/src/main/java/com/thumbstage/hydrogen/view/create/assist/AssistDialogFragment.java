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
import com.thumbstage.hydrogen.view.common.RequestResultCode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AssistDialogFragment extends BottomSheetDialogFragment {

    @BindView(R.id.fragment_dialog_assist_viewpager)
    ViewPager viewPager;
    AssistDialogPagerAdapter pagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_dialog_assist, container);
        ButterKnife.bind(this, contentView);

        pagerAdapter = new AssistDialogPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        Bundle bundle = getArguments();
        RequestResultCode.BottomSheetTab type = RequestResultCode.BottomSheetTab.valueOf(bundle.getString(RequestResultCode.BottomSheetTab.class.getName()));
        switch (type) {
            case INFO:
                viewPager.setCurrentItem(0);
                break;
            case MEMBER:
                viewPager.setCurrentItem(1);
                break;
        }
        return contentView;
    }


}
