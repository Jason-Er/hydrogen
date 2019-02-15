package com.thumbstage.hydrogen.view.create;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMMessagesQueryCallback;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.app.User;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.utils.ClassDBUtil;
import com.thumbstage.hydrogen.view.create.fragment.TopicFragment;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;

public class CreateActivity extends AppCompatActivity {
    final String TAG = "CreateActivity";
    TopicFragment topicFragment;
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

        topicFragment = (TopicFragment) getSupportFragmentManager().findFragmentById(R.id.activity_create_fragment);

        final Topic topic = getIntent().getParcelableExtra("Topic");
        if( topic != null ) { // use this topic
            /*
            final AVIMClient starter = AVIMClient.getInstance(topic.getStarted_by());
            AVIMConversation conv = starter.getConversation(topic.getConversation_id());
            conv.queryMessages(new AVIMMessagesQueryCallback() {
                @Override
                public void done(final List<AVIMMessage> messages, AVIMException e) {
                    if (e == null) {
                        Log.d(TAG,"get messages size()" + messages.size());
                        User.getInstance().getClient().createConversation(
                                Arrays.asList(topic.getStarted_by()), "attend", null, false, false, new AVIMConversationCreatedCallback() {
                                    @Override
                                    public void done(AVIMConversation avimConversation, AVIMException e) {
                                        if (e == null) {
                                            Log.i(TAG, "create a conversation");
                                            conversation = avimConversation;
                                            AVIMConversation starter_conv = starter.getConversation(avimConversation.getConversationId());
                                            for(AVIMMessage message: messages) {
                                                starter_conv.sendMessage(message, new AVIMConversationCallback() {
                                                    @Override
                                                    public void done(AVIMException e) {

                                                    }
                                                });
                                            }
                                            topicFragment.setConversation(avimConversation);
                                        }
                                    }
                                });


                    }
                }

            });
            conversation = User.getInstance().getClient().getConversation(topic.getConversation_id());
            topicFragment.setConversation(conversation);
            */
        } else { // create one
            User.getInstance().getClient().createConversation(
                    Arrays.asList(""), "start", null, false, false, new AVIMConversationCreatedCallback() {
                        @Override
                        public void done(AVIMConversation avimConversation, AVIMException e) {
                            if (e == null) {
                                Log.i(TAG, "create a conversation");
                                conversation = avimConversation;
                                topicFragment.setConversation(avimConversation);
                            }
                        }
                    });
        }
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
                ClassDBUtil.createTopic("IStartedOpened", AVUser.getCurrentUser(), topicFragment.getDialogue());
                navigateUp();
                break;
            case R.id.action_publish:
                Log.i(TAG, "action_publish");
                ClassDBUtil.createTopic("PublishedOpened", AVUser.getCurrentUser(), topicFragment.getDialogue());
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
