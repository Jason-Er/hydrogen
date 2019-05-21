package com.thumbstage.hydrogen.view.account;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.vo.Setting;
import com.thumbstage.hydrogen.utils.GlideUtil;
import com.thumbstage.hydrogen.viewmodel.UserViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.AndroidSupportInjection;

public class AccountFragment extends Fragment {

    @BindView(R.id.activity_account_avatar)
    ImageView avatar;
    @BindView(R.id.activity_account_name)
    EditText name;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    UserViewModel userViewModel;
    ImagePicker imagePicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this, view);

        imagePicker = new ImagePicker();
        imagePicker.setTitle("avatarURL");
        imagePicker.setCropImage(true);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AndroidSupportInjection.inject(this);
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        name.setText(userViewModel.getCurrentUser().getName());
        Glide.with(avatar).load(userViewModel.getCurrentUser().getAvatar()).into(avatar);

    }

    @OnClick(R.id.activity_account_signOut)
    public void signOut(View view) {
        userViewModel.signOut();
        ((AccountActivity)getActivity()).onSupportNavigateUp();
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

    @OnClick(R.id.activity_account_avatar)
    public void avatarClick(View view) {
        Log.i("AccountFragment", "avatarClick");
        imagePicker.startGallery(this, new ImagePicker.Callback() {
            @Override
            public void onPickImage(Uri imageUri) {

            }

            @Override
            public void onCropImage(Uri imageUri) {
                //mic.getTopic().setSetting(new Setting(null, imageUri.getPath(), false));
                // GlideUtil.inject(getContext(), imageUri.toString(), settingPic);
            }

            @Override
            public void cropConfig(CropImage.ActivityBuilder builder) {
                builder.setMultiTouchEnabled(false)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setRequestedSize(640, 640)
                        .setAspectRatio(5, 5);
            }
            @Override
            public void onPermissionDenied(int requestCode, String[] permissions,
                                           int[] grantResults) {
            }
        });
    }
}
