package com.thumbstage.hydrogen.view.create.assist;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.utils.BlurUtils;
import com.thumbstage.hydrogen.utils.RSBlurProcessor;
import com.thumbstage.hydrogen.view.common.RequestResultCode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class AssistDialogFragment extends BottomSheetDialogFragment {

    @BindView(R.id.fragment_dialog_assist_viewpager)
    ViewPager viewPager;
    AssistDialogPagerAdapter pagerAdapter;
    @Inject
    RSBlurProcessor rsBlurProcessor;
    @Inject
    BlurUtils blurUtils;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_dialog_assist, container);
        ButterKnife.bind(this, contentView);

        getDialog().getWindow().getAttributes().dimAmount = 0f;
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x7fffffff));

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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AndroidSupportInjection.inject(this);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        pagerAdapter.dismiss();
    }
}
