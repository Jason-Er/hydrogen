package com.thumbstage.hydrogen.view.create;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.TopicEx;
import com.thumbstage.hydrogen.view.create.fragment.TopicFragment;

import butterknife.ButterKnife;

public class CreateActivity extends AppCompatActivity {
    final String TAG = "CreateActivity";
    TopicFragment topicFragment;

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
                topicFragment.onActionOK();
                navigateUp();
                break;
            case R.id.action_publish:
                Log.i(TAG, "action_publish");
                topicFragment.onActionPublish();
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
