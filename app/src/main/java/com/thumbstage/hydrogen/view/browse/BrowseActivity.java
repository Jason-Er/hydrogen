package com.thumbstage.hydrogen.view.browse;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.api.IMService;
import com.thumbstage.hydrogen.event.BrowseItemEvent;
import com.thumbstage.hydrogen.event.FabEvent;
import com.thumbstage.hydrogen.event.IMMessageEvent;
import com.thumbstage.hydrogen.event.IMMicEvent;
import com.thumbstage.hydrogen.event.NaviViewEvent;
import com.thumbstage.hydrogen.model.dto.IMMessage;
import com.thumbstage.hydrogen.model.dto.MicHasNew;
import com.thumbstage.hydrogen.model.vo.Mic;
import com.thumbstage.hydrogen.model.vo.User;
import com.thumbstage.hydrogen.utils.NotificationUtils;
import com.thumbstage.hydrogen.utils.StringUtil;
import com.thumbstage.hydrogen.view.account.AccountActivity;
import com.thumbstage.hydrogen.view.common.Navigation;
import com.thumbstage.hydrogen.view.create.CreateActivity;
import com.thumbstage.hydrogen.view.create.fragment.TopicHandleType;
import com.thumbstage.hydrogen.view.show.ShowActivity;
import com.thumbstage.hydrogen.viewmodel.BrowseViewModel;
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

    UserViewModel userViewModel;
    BrowseViewModel browseViewModel;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    BrowseNavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    User preUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);


        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        fab.hide();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setScrimColor(0x7fffffff);
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
        browseViewModel = ViewModelProviders.of(this, viewModelFactory).get(BrowseViewModel.class);
        preUser = userViewModel.getCurrentUser();

        pagerAdapter = new BrowseFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                customizeUIs(pagerAdapter, position);
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
    public void onResponseMessageEvent(final FabEvent event) {
        if(event.getMessage().equals("plus")) {
            Intent intent = new Intent(this, CreateActivity.class);
            intent.putExtra(TopicHandleType.class.getSimpleName(),
                    TopicHandleType.CREATE.name());
            startActivity(intent);
        }
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

    private void customizeUIs(BrowseFragmentPagerAdapter pagerAdapter, int position) {
        if ( pagerAdapter.getItem(position) instanceof IBrowseCustomize) {
            ((IBrowseCustomize) pagerAdapter.getItem(position)).customizeToolbar(toolbar);
            ((IBrowseCustomize) pagerAdapter.getItem(position)).customizeFab(fab);
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
    public void onResponseMessageEvent(final BrowseItemEvent event) {
        Mic mic = (Mic) event.getData();
        Intent intent = new Intent();
        intent.putExtra(Mic.class.getSimpleName(), mic.getId());
        switch (event.getMessage()) {
            case "CommunityTopicViewHolder":
                intent.setClass(this, CreateActivity.class);
                if(mic.getTopic().getSponsor().equals(userViewModel.getCurrentUser())) {
                    intent.putExtra(TopicHandleType.class.getSimpleName(), TopicHandleType.CONTINUE.name());
                } else {
                    intent.putExtra(TopicHandleType.class.getSimpleName(), TopicHandleType.ATTEND.name());
                }
                break;
            case "IAttendedOpenedViewHolder":
                intent.setClass(this, CreateActivity.class);
                intent.putExtra(TopicHandleType.class.getSimpleName(), TopicHandleType.CONTINUE.name());
                break;
            case "CommunityShowViewHolder":
            case "IAttendedClosedViewHolder":
                intent.setClass(this, ShowActivity.class);
                break;
        }
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseMessageEvent(final IMMicEvent event) {
        if(event.getMessage().equals("onUnreadMessage")) {
            final IMMessage imMessage = (IMMessage) event.getData();
            browseViewModel.micHasNew(new MicHasNew(imMessage.getMicId(), true));
            userViewModel.getUser(imMessage.getWhoId()).observe(this, new Observer<User>() {
                @Override
                public void onChanged(@Nullable User user) {
                    Intent intent = new Intent(BrowseActivity.this, CreateActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra(Mic.class.getSimpleName(), imMessage.getMicId());
                    intent.putExtra(TopicHandleType.class.getSimpleName(), TopicHandleType.CONTINUE.name());
                    int id = Integer.parseInt(imMessage.getMicId().replaceAll("[^\\d]", "").substring(0,5));
                    NotificationUtils.showNotification(BrowseActivity.this, user.getName(), imMessage.getWhat(), intent, id);
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseMessageEvent(final NoSubscriberEvent event) {
        if(event.originalEvent instanceof IMMessageEvent) {
            final IMMessage imMessage = (IMMessage) ((IMMessageEvent) event.originalEvent).getData();
            userViewModel.getUser(imMessage.getWhoId()).observe(this, new Observer<User>() {
                @Override
                public void onChanged(@Nullable User user) {
                    Intent intent = new Intent(BrowseActivity.this, CreateActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra(Mic.class.getSimpleName(), imMessage.getMicId());
                    intent.putExtra(TopicHandleType.class.getSimpleName(), TopicHandleType.CONTINUE.name());
                    int id = Integer.parseInt(imMessage.getMicId().replaceAll("[^\\d]", "").substring(0,5));
                    NotificationUtils.showNotification(BrowseActivity.this, user.getName(), imMessage.getWhat(), intent, id);
                }
            });
        }
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        browseViewModel.refreshObserveList();
        pagerAdapter.updateFragmentsBy(userViewModel.getCurrentUser().getPrivileges());
        navigationView.updateStateBy(userViewModel.getCurrentUser(), viewPager, drawer);
        customizeUIs(pagerAdapter, viewPager.getCurrentItem());
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
