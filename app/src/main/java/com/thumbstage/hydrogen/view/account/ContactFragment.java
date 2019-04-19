package com.thumbstage.hydrogen.view.account;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.event.SelectContactUserEvent;
import com.thumbstage.hydrogen.model.User;
import com.thumbstage.hydrogen.utils.PinyinComparator;
import com.thumbstage.hydrogen.utils.PinyinUtils;
import com.thumbstage.hydrogen.view.common.ClearEditText;
import com.thumbstage.hydrogen.view.common.RequestResultCode;
import com.thumbstage.hydrogen.view.common.TitleItemDecoration;
import com.thumbstage.hydrogen.view.common.WaveSideBarView;
import com.thumbstage.hydrogen.viewmodel.UserViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class ContactFragment extends Fragment {

    @BindView(R.id.fragment_contact_sidebar)
    WaveSideBarView sideBar;
    @BindView(R.id.fragment_contact_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_contact_filter)
    ClearEditText filter;

    ContactAdapter recyclerViewAdapter;
    LinearLayoutManager layoutManager;
    TitleItemDecoration itemDecoration;
    MenuItem selectMemberButton;
    List<User> contacts;
    List<String> selectedUserId;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    UserViewModel userViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        EventBus.getDefault().register(this);

        sideBar.setOnTouchLetterChangeListener(new WaveSideBarView.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                int position = recyclerViewAdapter.getPositionForSection(letter.charAt(0));
                if (position != -1) {
                    layoutManager.scrollToPositionWithOffset(position, 0);
                }
            }
        });

        recyclerViewAdapter = new ContactAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        itemDecoration = new TitleItemDecoration(getContext());
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        selectedUserId = new ArrayList<>();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void filterData(String filterStr) {
        List<User> filterDateList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = contacts;
        } else {
            filterDateList.clear();
            for (User user : (List<User>)recyclerViewAdapter.getItems()) {
                String name = user.getName();
                if (name.indexOf(filterStr) != -1 ||
                        PinyinUtils.getFirstSpell(name).startsWith(filterStr)
                        || PinyinUtils.getFirstSpell(name).toLowerCase().startsWith(filterStr)
                        || PinyinUtils.getFirstSpell(name).toUpperCase().startsWith(filterStr)
                ) {
                    filterDateList.add(user);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(filterDateList, new PinyinComparator());
        itemDecoration.setmData(filterDateList);
        recyclerViewAdapter.setItems(filterDateList);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AndroidSupportInjection.inject(this);
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        userViewModel.getContactByPageNum(0).observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> userList) {
                Log.i("ContactFragment","onChanged");
                contacts = userList;
                itemDecoration.setmData(userList);
                recyclerViewAdapter.setItems(userList);
            }
        });

        String type = getActivity().getIntent().getStringExtra(AccountActivity.Type.class.getSimpleName());
        switch (AccountActivity.Type.valueOf(type)) {
            case PROFILE:
                break;
            case CONTACT:
                recyclerViewAdapter.setAdapterModel(ContactAdapter.ModelType.LIST_CONTACT);
                break;
            case SELECT_MEMBER:
                recyclerViewAdapter.setAdapterModel(ContactAdapter.ModelType.SELECT_CONTACT);
                break;
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        String type = getActivity().getIntent().getStringExtra(AccountActivity.Type.class.getSimpleName());
        if(AccountActivity.Type.valueOf(type) == AccountActivity.Type.SELECT_MEMBER) {
            menu.add(0,R.id.menu_item_start,0, getResources().getString(R.string.action_ok)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            selectMemberButton = menu.findItem(R.id.menu_item_start);
            selectMemberButton.setEnabled(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_start:
                Intent intent = new Intent();
                intent.putStringArrayListExtra(RequestResultCode.SelectContactKey, (ArrayList<String>) selectedUserId);
                getActivity().setResult(RequestResultCode.SELECT_CONTACT_RESULT_CODE, intent);
                ((AccountActivity)getActivity()).onSupportNavigateUp();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseMessageEvent(final SelectContactUserEvent event) {
        if(selectedUserId.contains(event.getUserId())) {
            if(!event.isChecked()) {
                selectedUserId.remove(event.getUserId());
            }
        } else {
            if(event.isChecked()) {
                selectedUserId.add(event.getUserId());
            }
        }

        if( selectMemberButton != null) {
            if(selectedUserId.size() > 0) {
                selectMemberButton.setEnabled(true);
                selectMemberButton.setTitle(getResources().getString(R.string.action_ok)+" (" + selectedUserId.size() + ") ");
            } else {
                selectMemberButton.setEnabled(false);
                selectMemberButton.setTitle(getResources().getString(R.string.action_ok));
            }
        }
    }

}
