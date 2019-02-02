package com.thumbstage.hydrogen.view.create;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.utils.ClassDBUtil;

import java.util.Arrays;

import butterknife.ButterKnife;
import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.activity.LCIMConversationFragment;

public class CreateActivity extends AppCompatActivity {
    final String TAG = "CreateActivity";
    LCIMConversationFragment conversationFragment;
    AVIMConversation conversation;

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
                Arrays.asList(""), "start", null, false, false, new AVIMConversationCreatedCallback() {
                    @Override
                    public void done(AVIMConversation avimConversation, AVIMException e) {
                        if( e == null ) {
                            Log.i(TAG, "create a conversation");
                            conversation = avimConversation;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_ok:
                Log.i(TAG, "action_ok");
                ClassDBUtil.saveConversationRecord("IStartedOpened", conversation);
                navigateUp();
                break;
            case R.id.action_publish:
                Log.i(TAG, "action_publish");
                ClassDBUtil.saveConversationRecord("PublishedOpened", conversation);
                navigateUp();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void navigateUp() {
        finish();
        super.onSupportNavigateUp();
    }
}
