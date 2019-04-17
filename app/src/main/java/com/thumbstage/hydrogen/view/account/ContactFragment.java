package com.thumbstage.hydrogen.view.account;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.User;
import com.thumbstage.hydrogen.view.common.TitleItemDecoration;
import com.thumbstage.hydrogen.view.common.WaveSideBarView;
import com.thumbstage.hydrogen.viewmodel.UserViewModel;

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

    ContactAdapter recyclerViewAdapter;
    LinearLayoutManager layoutManager;
    TitleItemDecoration itemDecoration;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    UserViewModel userViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        ButterKnife.bind(this, view);

        sideBar.setOnTouchLetterChangeListener(new WaveSideBarView.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {

            }
        });

        recyclerViewAdapter = new ContactAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        itemDecoration = new TitleItemDecoration(getContext());
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        return view;
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
                itemDecoration.setmData(userList);
                recyclerViewAdapter.setItems(userList);
            }
        });

    }

}
