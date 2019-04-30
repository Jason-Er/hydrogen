package com.thumbstage.hydrogen.view.create.assist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.vo.Mic;
import com.thumbstage.hydrogen.model.vo.Setting;
import com.thumbstage.hydrogen.utils.GlideUtil;
import com.thumbstage.hydrogen.viewmodel.TopicViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.AndroidSupportInjection;

public class TopicInfoFragment extends Fragment {

    @BindView(R.id.dialog_topic_setting_name)
    EditText name;
    @BindView(R.id.dialog_topic_setting_brief)
    EditText brief;
    @BindView(R.id.dialog_topic_setting_pic)
    ImageButton settingPic;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    TopicViewModel topicViewModel;
    Mic mic;

    ImagePicker imagePicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_topic_info, container, false);
        ButterKnife.bind(this, view);

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mic.getTopic().setName(name.getText().toString());
            }
        });

        brief.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mic.getTopic().setBrief(brief.getText().toString());
            }
        });

        imagePicker = new ImagePicker();
        imagePicker.setTitle("settingURL");
        imagePicker.setCropImage(true);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AndroidSupportInjection.inject(this);
        configureViewModel();
    }

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

    private void configureViewModel(){
        topicViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(TopicViewModel.class);
        topicViewModel.getTheTopic().observe(this, new Observer<Mic>() {
            @Override
            public void onChanged(@Nullable Mic miclcal) {
                mic = miclcal;
                name.setText(mic.getTopic().getName());
                brief.setText(mic.getTopic().getBrief());
                if(mic.getTopic().getSetting() != null) {
                    GlideUtil.inject(getContext(), mic.getTopic().getSetting().getUrl(), settingPic);
                }
            }
        });
    }

    @OnClick(R.id.dialog_topic_setting_pic)
    public void onSelectPic(View view) {
        imagePicker.startGallery(this, new ImagePicker.Callback() {
            @Override
            public void onPickImage(Uri imageUri) {

            }

            @Override
            public void onCropImage(Uri imageUri) {
                mic.getTopic().setSetting(new Setting(null, imageUri.getPath(), false));
                GlideUtil.inject(getContext(), imageUri.toString(), settingPic);
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
