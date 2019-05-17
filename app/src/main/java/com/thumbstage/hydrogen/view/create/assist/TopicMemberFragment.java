package com.thumbstage.hydrogen.view.create.assist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.event.TopicMemberEvent;
import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.model.vo.Mic;
import com.thumbstage.hydrogen.model.vo.User;
import com.thumbstage.hydrogen.utils.CollectionsUtil;
import com.thumbstage.hydrogen.utils.DataConvertUtil;
import com.thumbstage.hydrogen.view.account.AccountActivity;
import com.thumbstage.hydrogen.view.common.RequestResultCode;
import com.thumbstage.hydrogen.viewmodel.TopicViewModel;
import com.thumbstage.hydrogen.viewmodel.UserViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class TopicMemberFragment extends Fragment implements OnDismiss {

    @BindView(R.id.fragment_dialog_member_recycler)
    RecyclerView recyclerView;

    TopicMemberAdapter recyclerViewAdapter;
    GridAutoFitLayoutManager layoutManager;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    UserViewModel userViewModel;
    TopicViewModel topicViewModel;
    Mic mic;

    boolean somethingChanged;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_topic_member, container, false);
        ButterKnife.bind(this, view);

        somethingChanged = false;
        layoutManager = new GridAutoFitLayoutManager(getContext(), 50);
        recyclerViewAdapter = new TopicMemberAdapter();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);

        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AndroidSupportInjection.inject(this);
        configureViewModel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseMessageEvent(final TopicMemberEvent event) {
        switch (event.getMessage()) {
            case "Plus":
                Intent intent = new Intent(getContext(), AccountActivity.class);
                intent.putExtra(AccountActivity.Type.class.getSimpleName(), AccountActivity.Type.SELECT_MEMBER.name());
                startActivityForResult(intent, RequestResultCode.SELECT_CONTACT_REQUEST_CODE);
                break;
            case "Added":
                if(!event.getUser().equals(userViewModel.getCurrentUser())) {
                    popUpMenu(event);
                }
                break;
        }
    }

    private void popUpMenu(final TopicMemberEvent event) {
        PopupMenu popupMenu = new PopupMenu(getContext(), event.getView());
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_popup_member_delete, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.menu_member_delete) {
                    if(!userViewModel.getCurrentUser().equals(event.getUser())) {
                        mic.getTopic().getMembers().remove(event.getUser());
                        recyclerViewAdapter.setUsers(mic.getTopic().getMembers());
                        somethingChanged = true;
                    }
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void configureViewModel(){
        userViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(UserViewModel.class);
        topicViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(TopicViewModel.class);
        topicViewModel.getTheTopic().observe(this, new Observer<Mic>() {
            @Override
            public void onChanged(@Nullable Mic miclcal) {
                mic = miclcal;
                recyclerViewAdapter.setUsers(mic.getTopic().getMembers());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( requestCode == RequestResultCode.SELECT_CONTACT_REQUEST_CODE && resultCode == RequestResultCode.SELECT_CONTACT_RESULT_CODE ) {
            List<String> memberIds = data.getExtras().getStringArrayList(RequestResultCode.SelectContactKey);
            List<String> userIds = DataConvertUtil.user2StringId(mic.getTopic().getMembers());
            memberIds.addAll(userIds);
            CollectionsUtil.removeDuplicate(memberIds);
            showMemberAvatars(memberIds);
            somethingChanged = true;
        }
    }

    private void showMemberAvatars(@NonNull List<String> memberIds) {
        if(!memberIds.isEmpty()) {
            userViewModel.getUsers(memberIds).observe(this, new Observer<List<User>>() {
                @Override
                public void onChanged(@Nullable List<User> users) {
                    mic.getTopic().setMembers(users);
                    recyclerViewAdapter.setUsers(users);
                }
            });
        }
    }

    @Override
    public void dismiss() {
        if(somethingChanged) {
            topicViewModel.updateMembers(new IReturnBool() {
                @Override
                public void callback(Boolean isOK) {

                }
            });
        }
    }
}
