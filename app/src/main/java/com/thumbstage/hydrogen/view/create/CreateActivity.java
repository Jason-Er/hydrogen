package com.thumbstage.hydrogen.view.create;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.thumbstage.hydrogen.R;

import java.util.Arrays;

import butterknife.ButterKnife;
import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.activity.LCIMConversationFragment;

public class CreateActivity extends AppCompatActivity {
    final String TAG = "CreateActivity";
    LCIMConversationFragment conversationFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.create_activity_name));

        conversationFragment = (LCIMConversationFragment) getSupportFragmentManager().findFragmentById(R.id.activity_create_fragment);

        LCChatKit.getInstance().getClient().createConversation(
                Arrays.asList(""), "create", null, false, false, new AVIMConversationCreatedCallback() {
                    @Override
                    public void done(AVIMConversation avimConversation, AVIMException e) {
                        if( e == null ) {
                            Log.i(TAG, "create a conversation");
                            conversationFragment.setConversation(avimConversation);
                        }
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
