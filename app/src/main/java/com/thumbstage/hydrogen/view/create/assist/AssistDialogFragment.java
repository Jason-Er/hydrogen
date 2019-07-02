package com.thumbstage.hydrogen.view.create.assist;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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
import com.thumbstage.hydrogen.utils.RSBlurProcessor;
import com.thumbstage.hydrogen.utils.UIUtil;
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AndroidSupportInjection.inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        View view = getActivity().getWindow().getDecorView().getRootView();
        Bitmap bitmap = UIUtil.getBitmapFromView(view);
        Bitmap bitmapBlur = rsBlurProcessor.blur(bitmap, 10, 3);
        getDialog().getWindow().setBackgroundDrawable(new BitmapDrawable(getResources(), bitmapBlur));
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        pagerAdapter.dismiss();
    }
}
