package com.thumbstage.hydrogen.view.show.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.event.HyMenuItemEvent;
import com.thumbstage.hydrogen.event.PlayerControlEvent;
import com.thumbstage.hydrogen.event.ShowFragmentEvent;
import com.thumbstage.hydrogen.model.bo.CanOnTopic;
import com.thumbstage.hydrogen.model.bo.TopicTag;
import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.model.vo.Line;
import com.thumbstage.hydrogen.model.vo.Mic;
import com.thumbstage.hydrogen.model.vo.Topic;
import com.thumbstage.hydrogen.model.vo.User;
import com.thumbstage.hydrogen.utils.DensityUtil;
import com.thumbstage.hydrogen.utils.GlideUtil;
import com.thumbstage.hydrogen.view.common.HyMenuItem;
import com.thumbstage.hydrogen.view.create.fragment.PopupWindowAdapter;
import com.thumbstage.hydrogen.viewmodel.TopicViewModel;
import com.thumbstage.hydrogen.viewmodel.UserViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class ShowFragment extends Fragment {

    final String TAG = "TopicFragment";

    @BindView(R.id.fragment_show_bk)
    ImageView background;
    @BindView(R.id.fragment_show_chat)
    RecyclerView recyclerView;
    @BindView(R.id.loading_spinner)
    ProgressBar spinner;
    @BindView(R.id.fragment_show_pullrefresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.fragment_show_subtitle)
    TextSwitcher subtitle;
    @BindView(R.id.fragment_show_bottom_bar)
    PlayerControlBar playerControlBar;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    TopicViewModel topicViewModel;
    UserViewModel userViewModel;

    ShowAdapter showAdapter;
    ShowLayoutManager layoutManager;
    ListPopupWindow popupWindow;
    PopupWindowAdapter popupWindowAdapter;
    Map<User, RecyclerView.ViewHolder> membersViewHolderMap = new HashMap<>();
    LineTextViewHolder lineTextViewHolder;
    LineAudioViewHolder lineAudioViewHolder;
    PopupWindow lineTextPopup;
    PopupWindow lineAudioPopup;

    Mic mic;
    int currentIndex = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        refreshLayout.setEnabled(false);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                topicViewModel.refreshTheTopic();
            }
        });

        subtitle.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                final TextView tv = new TextView(getContext());
                tv.setTextSize(DensityUtil.dp2px(getContext(), 15));
                tv.setTextColor(Color.BLACK);
                tv.setEllipsize(TextUtils.TruncateAt.END);
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.gravity = Gravity.CENTER;
                tv.setLayoutParams(lp);
                return tv;
            }
        });

        popupWindow = new ListPopupWindow(getContext());
        popupWindowAdapter = new PopupWindowAdapter(getContext());
        popupWindow.setAdapter(popupWindowAdapter);
        popupWindow.setWidth(DensityUtil.dp2px(getContext(),200));
        popupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
        popupWindow.setModal(true);

        showAdapter = new ShowAdapter();
        layoutManager = new ShowLayoutManager();
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setAdapter(showAdapter);

        View lineAudioView = inflater.inflate(R.layout.item_line_left_audio, null);
        lineAudioViewHolder = new LineAudioViewHolder(lineAudioView);
        lineAudioPopup = new PopupWindow(lineAudioView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        lineAudioPopup.setTouchable(false);
        lineAudioPopup.setBackgroundDrawable(null);

        View lineTextView = inflater.inflate(R.layout.item_line_left_text, null);
        lineTextViewHolder = new LineTextViewHolder(lineTextView);
        lineTextPopup = new PopupWindow(lineTextView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        lineTextPopup.setTouchable(false);
        lineTextPopup.setBackgroundDrawable(null);

        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        configureDagger();
        configureViewModel();
    }

    boolean isStop = true;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseMessageEvent(final PlayerControlEvent event) {
        if(membersViewHolderMap.size() != mic.getTopic().getMembers().size()) {
            makeUpMembersViewHolderMap(membersViewHolderMap);
        }
        switch (event.getMessage()) {
            case "STOP":
                synchronized (this) {
                    isStop = true;
                    currentIndex = 0;
                }
                updateProgress(currentIndex);
                lineTextPopup.dismiss();
                lineAudioPopup.dismiss();
                break;
            case "PLAY":
                isStop = false;
                updateProgress(currentIndex);
                autoSpeakLine(currentIndex);
                break;
            case "PAUSE":
                isStop = true;
                pauseSpeakLine(currentIndex);
                break;
            case "SEEK":
                currentIndex = (int) (mic.getTopic().getDialogue().size() * event.getSeekProcess());
                seek2Line(currentIndex);
                break;
        }
    }

    private void pauseSpeakLine(int lineIndex) {
        if(lineIndex < mic.getTopic().getDialogue().size()) {
            Line line = mic.getTopic().getDialogue().get(currentIndex);
            switch (line.getMessageType()) {
                case TEXT:
                    break;
                case AUDIO:
                    lineAudioViewHolder.pausePlay();
                    break;
            }
        }
    }

    private void seek2Line(int lineIndex) {
        if(lineIndex < mic.getTopic().getDialogue().size()) {
            if(isStop) {
                showLine(lineIndex);
            } else {
                autoSpeakLine(lineIndex);
            }
        }
    }

    int preLineIndex = 0;
    private void showLine(int lineIndex) {
        if(lineIndex < mic.getTopic().getDialogue().size()) {
            Line line = mic.getTopic().getDialogue().get(currentIndex);
            View anchorView = membersViewHolderMap.get(line.getWho()).itemView;
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            switch (line.getMessageType()) {
                case TEXT: {
                    lineTextViewHolder.setiFinishCallBack(null);
                    lineTextViewHolder.setContent(line.getWhat());
                    lineTextViewHolder.itemView.measure(size.x, size.y);
                    int offsetY = -(lineTextViewHolder.itemView.getMeasuredHeight()+anchorView.getMeasuredHeight());
                    if(lineIndex != preLineIndex && lineAudioPopup.isShowing())
                        lineAudioPopup.dismiss();
                    if(lineIndex != preLineIndex || (lineIndex == preLineIndex && !lineTextPopup.isShowing()))
                        lineTextPopup.showAsDropDown(anchorView, 0, offsetY, Gravity.END); }
                break;
                case AUDIO: {
                    lineAudioViewHolder.setiFinishCallBack(null);
                    lineAudioViewHolder.setContent(line.getWhat());
                    lineAudioViewHolder.itemView.measure(size.x, size.y);
                    int offsetY = -(lineAudioViewHolder.itemView.getMeasuredHeight()+anchorView.getMeasuredHeight());
                    if(lineIndex != preLineIndex && lineTextPopup.isShowing())
                        lineTextPopup.dismiss();
                    if(lineIndex != preLineIndex || (lineIndex == preLineIndex && !lineAudioPopup.isShowing()))
                        lineAudioPopup.showAsDropDown(anchorView, 0, offsetY, Gravity.END); }
                break;
            }
            preLineIndex = lineIndex;
        }
    }

    private void updateProgress(int currentIndex) {
        Log.i("ShowFragment","currentIndex:"+currentIndex);
        if(currentIndex <= mic.getTopic().getDialogue().size()) {
            int progress = (int) ((float) currentIndex / (float) mic.getTopic().getDialogue().size() * 100);
            Log.i("ShowFragment","progress:"+progress);
            playerControlBar.setProgress(progress);
        }
    }

    private void autoSpeakLine(int lineIndex) {
        if(lineIndex < mic.getTopic().getDialogue().size() && !isStop) {
            Line line = mic.getTopic().getDialogue().get(currentIndex);
            View anchorView = membersViewHolderMap.get(line.getWho()).itemView;
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            switch (line.getMessageType()) {
                case TEXT: {
                    lineTextViewHolder.setiFinishCallBack(new IFinishCallBack() {
                        @Override
                        public void finish() {
                            if(!isStop) {
                                synchronized (this) {
                                    currentIndex++;
                                    updateProgress(currentIndex);
                                    lineTextPopup.dismiss();
                                }
                                autoSpeakLine(currentIndex);
                            }
                        }
                    });
                    lineTextViewHolder.setContent(line.getWhat());
                    lineTextViewHolder.itemView.measure(size.x, size.y);
                    int offsetY = -(lineTextViewHolder.itemView.getMeasuredHeight()+anchorView.getMeasuredHeight());
                    if(!isStop) {
                        if(lineIndex != preLineIndex && lineAudioPopup.isShowing())
                            lineAudioPopup.dismiss();
                        if(lineIndex != preLineIndex || (lineIndex == preLineIndex && !lineTextPopup.isShowing()))
                            lineTextPopup.showAsDropDown(anchorView, 0, offsetY, Gravity.END);
                    }}
                    break;
                case AUDIO: {
                    lineAudioViewHolder.setiFinishCallBack(new IFinishCallBack() {
                        @Override
                        public void finish() {
                            if (!isStop) {
                                synchronized (this) {
                                    currentIndex++;
                                    updateProgress(currentIndex);
                                    lineAudioPopup.dismiss();
                                }
                                autoSpeakLine(currentIndex);
                            }
                        }
                    });
                    lineAudioViewHolder.setContent(line.getWhat());
                    lineAudioViewHolder.itemView.measure(size.x, size.y);
                    int offsetY = -(lineAudioViewHolder.itemView.getMeasuredHeight() + anchorView.getMeasuredHeight());
                    if (!isStop) {
                        if(lineIndex != preLineIndex && lineTextPopup.isShowing())
                            lineTextPopup.dismiss();
                        if(lineIndex != preLineIndex || (lineIndex == preLineIndex && !lineAudioPopup.isShowing()))
                            lineAudioPopup.showAsDropDown(anchorView, 0, offsetY, Gravity.END);
                        lineAudioViewHolder.play();
                    } }
                    break;
            }
            preLineIndex = lineIndex;
        } else {
            synchronized (this) {
                isStop = true;
                currentIndex = 0;
                playerControlBar.stopAction();
            }
        }
    }

    private void makeUpMembersViewHolderMap(Map<User, RecyclerView.ViewHolder> map) {
        for(int i=0;i<recyclerView.getChildCount();i++) {
            View view = recyclerView.getChildAt(i);
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
            if(viewHolder instanceof ParticipantViewHolder) {
                map.put(((ParticipantViewHolder) viewHolder).getUser(), viewHolder);
            }
        }
    }

    private void configureDagger(){
        AndroidSupportInjection.inject(this);
    }

    private void configureViewModel() {
        final String micId = getActivity().getIntent().getStringExtra(Mic.class.getSimpleName());
        topicViewModel = ViewModelProviders.of(this, viewModelFactory).get(TopicViewModel.class);
        topicViewModel.pickUpTopic(micId).observe(this, new Observer<Mic>() {
            @Override
            public void onChanged(@Nullable Mic micl) {
                if(micl != null) {
                    mic = micl;
                    if (mic.getTopic().getSetting() != null) {
                        GlideUtil.inject(background.getContext(), mic.getTopic().getSetting(), background);
                    }
                    EventBus.getDefault().post(new ShowFragmentEvent(mic.getTopic().getName(), "title"));
                    showAdapter.setMic(mic);
                    topicViewModel.micHasRead();
                    spinner.setVisibility(View.GONE);
                    refreshLayout.setRefreshing(false);
                }
            }
        });
        userViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(UserViewModel.class);
        refreshLayout.setEnabled(true);
    }

    HyMenuItem recommendItem = new HyMenuItem(R.drawable.ic_menu_recommend_g, CanOnTopic.RECOMMEND);

    protected Set<CanOnTopic> getUserCan(Topic topic, String userId) {
        Set<CanOnTopic> userCan = null;
        if(topic.getUserCan()!= null) {
            if(topic.getUserCan().containsKey(userId)) {
                userCan = topic.getUserCan().get(userId);
            }
        }
        return userCan;
    }

    private void setUpPopupMenu(Topic topic, String userId) {
        List<HyMenuItem> itemList = new ArrayList<>();
        Set<CanOnTopic> userCan = getUserCan(topic, userId);
        if(userCan != null) {
            for(CanOnTopic can: userCan) {
                switch (can) {
                    case RECOMMEND:
                        itemList.add(recommendItem);
                        break;
                }
            }
        }
        popupWindowAdapter.setItemList(itemList);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        final MenuItem menuItemSetup = menu.findItem(R.id.menu_item_setup);
        menuItemSetup.setVisible(false);
        topicViewModel.getTheTopic().observe(this, new Observer<Mic>() {
            @Override
            public void onChanged(@Nullable Mic mic) {
                if(mic!=null && mic.getTopic()!=null) {
                    Map<String, Set<CanOnTopic>> userCan = mic.getTopic().getUserCan();
                    String userId = userViewModel.getCurrentUser().getId();
                    if(userCan != null && userCan.containsKey(userId)) {
                        if(userCan.get(userId).size() > 0) {
                            menuItemSetup.setVisible(true);
                            setUpPopupMenu(mic.getTopic(), userId);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_create_default, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_setup:
                Log.i(TAG, "menu_item_setup");
                View anchor = getActivity().findViewById(R.id.menu_item_setup);
                popupWindow.setAnchorView(anchor);
                popupWindow.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseMessageEvent(final HyMenuItemEvent event) {
        switch ((CanOnTopic) event.getData()) {
            case RECOMMEND:
                mic.getTopic().setTags(new HashSet<TopicTag>(){{add(TopicTag.SELECTED);}});
                topicViewModel.updateTheTopic(new IReturnBool() {
                    @Override
                    public void callback(Boolean isOK) {

                    }
                });
                popupWindow.dismiss();
                break;
        }
    }

}
