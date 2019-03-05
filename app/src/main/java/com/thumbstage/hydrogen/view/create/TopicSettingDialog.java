package com.thumbstage.hydrogen.view.create;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.utils.DensityUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TopicSettingDialog extends DialogFragment {

    @BindView(R.id.dialog_topic_setting_name)
    EditText name;
    @BindView(R.id.dialog_topic_setting_brief)
    EditText brief;
    @BindView(R.id.dialog_topic_setting_setting)
    EditText setting;
    @BindView(R.id.dialog_topic_setting_pic)
    ImageView settingPic;

    Topic topic;
    ImagePicker imagePicker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.dialog_topic_setting, container);
        ButterKnife.bind(this, contentView);

        /*
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels - DensityUtil.dp2px(getActivity(), 16f);
        params.bottomMargin = DensityUtil.dp2px(getActivity(), 8f);
        contentView.setLayoutParams(params);
        */

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().getWindow().setWindowAnimations(R.style.BottomDialog_Animation);

        imagePicker = new ImagePicker();
        imagePicker.setTitle("setting");
        imagePicker.setCropImage(true);

        return contentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        int width = getResources().getDisplayMetrics().widthPixels - DensityUtil.dp2px(getActivity(), 16f);
        getDialog().getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    /*
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.dialog_topic_setting);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消

        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);

        ButterKnife.bind(this, dialog); // Dialog即View

        return super.onCreateDialog(savedInstanceState);
    }
    */

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_topic_setting, null);
        setContentView(contentView);
        ButterKnife.bind(this);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = getContext().getResources().getDisplayMetrics().widthPixels - DensityUtil.dp2px(getContext(), 16f);
        params.bottomMargin = DensityUtil.dp2px(getContext(), 8f);
        contentView.setLayoutParams(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
    }
    */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    @OnClick(R.id.dialog_topic_setting_ok)
    public void onOK(View view) {
        topic.setName(name.getText().toString());
        topic.setBrief(brief.getText().toString());

    }

    @OnClick(R.id.dialog_topic_setting_select)
    public void onSelectPic(View view) {
        imagePicker.startGallery(this, new ImagePicker.Callback() {
            @Override
            public void onPickImage(Uri imageUri) {

            }

            @Override
            public void onCropImage(Uri imageUri) {
                setting.setText(imageUri.toString());
                Glide.with(getContext()).load(imageUri).into(settingPic);
            }

            @Override
            public void cropConfig(CropImage.ActivityBuilder builder) {
                builder.setMultiTouchEnabled(false)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setRequestedSize(960, 540)
                        .setAspectRatio(16, 9);
            }
            @Override
            public void onPermissionDenied(int requestCode, String[] permissions,
                                                     int[] grantResults) {
            }
        });
    }

}
