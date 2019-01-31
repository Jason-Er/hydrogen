package com.thumbstage.hydrogen.view.create;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.thumbstage.hydrogen.R;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.leancloud.chatkit.activity.LCIMConversationFragment;

public class CreateActivity extends AppCompatActivity {
    final String TAG = "CreateActivity";
    AVIMConversation localConversation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        ButterKnife.bind(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.create_activity_name));

        AVUser currentUser = AVUser.getCurrentUser();
        AVIMClient avimClient = AVIMClient.getInstance(currentUser);
        avimClient.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient client, AVIMException e) {
                if( e == null ) {
                    Log.i(TAG, "can conversation");
                    client.createConversation(Arrays.asList(""), "robot", null, new AVIMConversationCreatedCallback() {
                        @Override
                        public void done(AVIMConversation conversation, AVIMException e) {
                            if( e == null ) {
                                Log.i(TAG, "create a conversation");
                                // AVIMTextMessage msg = new AVIMTextMessage();
                                // msg.setText("Hello world");
                                localConversation = conversation;
                                /*
                                conversation.sendMessage(msg, new AVIMConversationCallback() {
                                    @Override
                                    public void done(AVIMException e) {
                                        if(e == null) {
                                            Log.d(TAG, "send ok!");
                                        }
                                    }
                                });
                                */
                            }
                        }
                    });
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
