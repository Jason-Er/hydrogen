package com.thumbstage.hydrogen.view.create;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.event.TopicSettingEvent;
import com.thumbstage.hydrogen.model.TopicEx;
import com.thumbstage.hydrogen.view.create.fragment.TopicFragment;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;

public class CreateActivity extends AppCompatActivity {
    final String TAG = "CreateActivity";
    TopicFragment topicFragment;
    int REQUEST_CODE_CHOOSE = 123;

    public enum TopicHandleType {
        CREATE, ATTEND, CONTINUE
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.create_activity_name));

        topicFragment = (TopicFragment) getSupportFragmentManager().findFragmentById(R.id.activity_create_fragment);

        final TopicEx topicEx = getIntent().getParcelableExtra(TopicEx.class.getSimpleName());
        String handleType = getIntent().getStringExtra(TopicHandleType.class.getSimpleName());
        if( handleType == null) {
            throw new IllegalArgumentException("No TopicHandleType found!");
        }
        switch (TopicHandleType.valueOf(handleType)) {
            case CREATE:
                topicFragment.createTopic();
                break;
            case ATTEND:
                topicFragment.attendTopic(topicEx.getTopic());
                break;
            case CONTINUE:
                topicFragment.continueTopic(topicEx.getTopic(), topicEx.getPipe());
                break;
        }
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseMessageEvent(TopicSettingEvent event) {
        switch (event.getMessage()) {
            case "select":
                Log.d(TAG,"receive signUser");
                Matisse.from(this)
                        .choose(MimeType.ofAll(), false) // 选择 mime 的类型
                        .countable(true)
                        .maxSelectable(1) // 图片选择的最多数量
                        // .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f) // 缩略图的比例
                        .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                        .forResult(REQUEST_CODE_CHOOSE); // 设置作为标记的请求码
                break;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public void navigateUp() {
        finish();
        super.onSupportNavigateUp();
    }
}
