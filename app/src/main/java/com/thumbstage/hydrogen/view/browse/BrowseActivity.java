package com.thumbstage.hydrogen.view.browse;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.api.IMService;
import com.thumbstage.hydrogen.event.AtMeEvent;
import com.thumbstage.hydrogen.event.BrowseItemEvent;
import com.thumbstage.hydrogen.event.IMMessageEvent;
import com.thumbstage.hydrogen.event.IMMicEvent;
import com.thumbstage.hydrogen.event.NaviViewEvent;
import com.thumbstage.hydrogen.model.bo.AtMe;
import com.thumbstage.hydrogen.model.bo.Mic;
import com.thumbstage.hydrogen.model.dto.IMMessage;
import com.thumbstage.hydrogen.utils.BoUtil;
import com.thumbstage.hydrogen.utils.NotificationUtils;
import com.thumbstage.hydrogen.utils.StringUtil;
import com.thumbstage.hydrogen.view.account.AccountActivity;
import com.thumbstage.hydrogen.view.common.Navigation;
import com.thumbstage.hydrogen.view.create.CreateActivity;
import com.thumbstage.hydrogen.view.create.fragment.TopicHandleType;
import com.thumbstage.hydrogen.view.show.ShowActivity;
import com.thumbstage.hydrogen.viewmodel.AtMeViewModel;
import com.thumbstage.hydrogen.viewmodel.TopicViewModel;
import com.thumbstage.hydrogen.viewmodel.UserViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.NoSubscriberEvent;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class BrowseActivity extends AppCompatActivity
        implements HasSupportFragmentInjector {

    private final String TAG = "BrowseActivity";

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    IMService imService;

    @BindView(R.id.browse_content)
    ViewPager viewPager;
    BrowseFragmentPagerAdapter pagerAdapter;

    TopicViewModel topicViewModel;
    UserViewModel userViewModel;
    AtMeViewModel atMeViewModel;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    BrowseNavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);


        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        fab.hide();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                switch (menuItemId) {
                    case R.id.menu_browse_sign:
                        navi2AccountOrSignIn();
                        break;
                }
                return false;
            }
        });

        AndroidInjection.inject(this);
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        topicViewModel = ViewModelProviders.of(this, viewModelFactory).get(TopicViewModel.class);
        atMeViewModel = ViewModelProviders.of(this, viewModelFactory).get(AtMeViewModel.class);

        pagerAdapter = new BrowseFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if ( pagerAdapter.getItem(position) instanceof IBrowseCustomize) {
                    ((IBrowseCustomize) pagerAdapter.getItem(position)).customizeToolbar(toolbar);
                    ((IBrowseCustomize) pagerAdapter.getItem(position)).customizeFab(fab);
                } else {
                    // default function
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        viewPager.addOnPageChangeListener(pageChangeListener);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseMessageEvent(final NaviViewEvent event) {
        switch (event.getMessage()) {
            case "userAvatar":
                navi2AccountOrSignIn();
                break;
            case "userContact":
                Intent intent = new Intent(this, AccountActivity.class);
                intent.putExtra(AccountActivity.Type.class.getSimpleName(), AccountActivity.Type.CONTACT.name());
                startActivity(intent);
                break;
        }
    }

    private void navi2AccountOrSignIn() {
        if( userViewModel.getCurrentUser().getId().equals(StringUtil.DEFAULT_USERID) ) {
            Navigation.sign2SignIn(BrowseActivity.this);
        } else {
            Navigation.sign2Account(BrowseActivity.this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseMessageEvent(final AtMeEvent event) {
        switch (event.getMessage()) {
            case "click":
                AtMe atMe = (AtMe) event.getData();
                Intent intent = new Intent(this, CreateActivity.class);
                intent.putExtra(Mic.class.getSimpleName(), atMe.getMic().getId());
                intent.putExtra(TopicHandleType.class.getSimpleName(),
                        TopicHandleType.CONTINUE.name());
                startActivity(intent);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseMessageEvent(final BrowseItemEvent event) {
        Mic mic = (Mic) event.getData();
        Intent intent = new Intent();
        intent.putExtra(Mic.class.getSimpleName(), mic.getId());
        switch (event.getMessage()) {
            case "PublishedOpenedViewHolder":
                intent.setClass(this, CreateActivity.class);
                if(mic.getTopic().getStarted_by().equals(userViewModel.getCurrentUser())) {
                    intent.putExtra(TopicHandleType.class.getSimpleName(), TopicHandleType.EDIT.name());
                } else {
                    intent.putExtra(TopicHandleType.class.getSimpleName(), TopicHandleType.ATTEND.name());
                }
                break;
            case "IStartedClosedViewHolder":
            case "IPublishedClosedViewHolder":
            case "IAttendedClosedViewHolder":
            case "PublishedClosedViewHolder":
                intent.setClass(this, ShowActivity.class);
                break;
            case "IPublishedOpenedViewHolder":
            case "IStartedOpenedViewHolder":
                intent.setClass(this, CreateActivity.class);
                intent.putExtra(TopicHandleType.class.getSimpleName(), TopicHandleType.EDIT.name());
                break;
            case "IAttendedOpenedViewHolder":
                intent.setClass(this, CreateActivity.class);
                intent.putExtra(TopicHandleType.class.getSimpleName(), TopicHandleType.CONTINUE.name());
                break;
        }
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseMessageEvent(final IMMicEvent event) {
        if(event.getMessage().equals("onUnreadMessage")) {
            final IMMessage imMessage = (IMMessage) event.getData();
            topicViewModel.pickUpTopic(imMessage.getMicId()).observe(this, new Observer<Mic>() {
                @Override
                public void onChanged(@Nullable Mic mic) {
                    AtMe atMe = new AtMe();
                    atMe.setMic(mic);
                    atMe.setWhat(imMessage.getWhat());
                    atMe.setWho(BoUtil.findById(mic.getTopic().getMembers(), imMessage.getWhoId()));
                    atMe.setMe(userViewModel.getCurrentUser());
                    atMe.setWhen(imMessage.getWhen());
                    atMeViewModel.saveAtMe(atMe);
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseMessageEvent(final NoSubscriberEvent event) {
        if(event.originalEvent instanceof IMMessageEvent) {
            Intent intent = new Intent(this, CreateActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            IMMessage imMessage = (IMMessage) ((IMMessageEvent) event.originalEvent).getData();
            intent.putExtra(Mic.class.getSimpleName(), imMessage.getMicId());
            intent.putExtra(TopicHandleType.class.getSimpleName(), TopicHandleType.EDIT.name());
            NotificationUtils.showNotification(this, "userName", "say what", intent);
        }
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        pagerAdapter.updateFragmentsBy(userViewModel.getCurrentUser().getPrivileges());
        navigationView.updateStateBy(userViewModel.getCurrentUser(), viewPager, drawer);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_browse, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.menu_browse_sign) {
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }


}
