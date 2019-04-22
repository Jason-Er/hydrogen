package com.thumbstage.hydrogen.view.create.assist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.User;
import com.thumbstage.hydrogen.utils.CollectionsUtil;
import com.thumbstage.hydrogen.utils.DensityUtil;
import com.thumbstage.hydrogen.view.account.AccountActivity;
import com.thumbstage.hydrogen.view.common.RequestResultCode;
import com.thumbstage.hydrogen.viewmodel.UserViewModel;

import java.util.List;
import java.util.zip.Inflater;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.AndroidSupportInjection;

public class TopicMemberSelectDialog extends DialogFragment {

    @BindView(R.id.fragment_dialog_member_container)
    ViewGroup container;
    @BindView(R.id.fragment_dialog_member_plus)
    ImageButton plusMember;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    UserViewModel userViewModel;

    public interface IOnOK {
        void callback(List<User> users);
    }

    IOnOK iOnOK;
    List<User> userList;
    List<String> memberIds;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_dialog_topic_member_select, container);
        ButterKnife.bind(this, contentView);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().getWindow().setWindowAnimations(R.style.BottomDialog_Animation);

        return contentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        int width = getResources().getDisplayMetrics().widthPixels - DensityUtil.dp2px(getActivity(), 16f);
        getDialog().getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AndroidSupportInjection.inject(this);
        configureViewModel();

        Bundle bundle = getArguments();
        memberIds = bundle.getStringArrayList(RequestResultCode.MemberIds);
    }

    @Override
    public void onResume() {
        super.onResume();
        showMemberAvatars(memberIds);
    }

    private void configureViewModel(){
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
    }

    @OnClick(R.id.dialog_topic_select_ok)
    public void onOK(View view) {
        if(iOnOK != null && userList != null) {
            iOnOK.callback(userList);
        }
        dismiss();
    }

    @OnClick(R.id.dialog_topic_select_cancel)
    public void onCancel(View view) {
        dismiss();
    }

    public void setIOnOK(IOnOK iOnOK) {
        this.iOnOK = iOnOK;
    }

    @OnClick(R.id.fragment_dialog_member_plus)
    public void memberPlus(View view) {
        Intent intent = new Intent(getContext(), AccountActivity.class);
        intent.putExtra(AccountActivity.Type.class.getSimpleName(), AccountActivity.Type.SELECT_MEMBER.name());
        startActivityForResult(intent, RequestResultCode.SELECT_CONTACT_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == RequestResultCode.SELECT_CONTACT_REQUEST_CODE && resultCode == RequestResultCode.SELECT_CONTACT_RESULT_CODE ) {
            List<String> memberIds = data.getExtras().getStringArrayList(RequestResultCode.SelectContactKey);
            this.memberIds.addAll(memberIds);
            CollectionsUtil.removeDuplicate(this.memberIds);
            showMemberAvatars(this.memberIds);
        }
    }

    private void showMemberAvatars(@NonNull  List<String> memberIds) {
        if(memberIds != null && !memberIds.isEmpty()) {
            userViewModel.getUsers(memberIds).observe(this, new Observer<List<User>>() {
                @Override
                public void onChanged(@Nullable List<User> users) {
                    userList = users;
                    container.removeAllViews();
                    for(User user: users) {
                        View view = getLayoutInflater().inflate(R.layout.item_avatar, container, false);
                        ImageButton imageButton = view.findViewById(R.id.member_avatar);
                        container.addView(view);
                        Glide.with(container).load(user.getAvatar()).into(imageButton);
                    }
                    container.addView(plusMember);
                }
            });
        }

    }
}
