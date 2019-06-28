package com.thumbstage.hydrogen.view.browse;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.event.NaviViewEvent;
import com.thumbstage.hydrogen.model.bo.Privilege;
import com.thumbstage.hydrogen.model.vo.User;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class BrowseNavigationView extends NavigationView implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    ViewPager viewPager;
    User user;

    public BrowseNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setNavigationItemSelectedListener(this);
    }

    public void updateStateBy(User user, ViewPager viewPager, DrawerLayout drawer) {
        this.viewPager = viewPager;
        this.drawer = drawer;
        this.user = user;
        // update header
        View headerView = getHeaderView(0);
        ImageView userAvatar = headerView.findViewById(R.id.nav_account_avatar);
        TextView userName = headerView.findViewById(R.id.nav_account_name);
        ImageView contact = headerView.findViewById(R.id.nav_account_contact);
        Glide.with(userAvatar).load(user.getAvatar()).into(userAvatar);
        userName.setText(user.getName());
        userAvatar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new NaviViewEvent("userAvatar"));
            }
        });
        contact.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new NaviViewEvent("userContact"));
            }
        });
        // update menu
        Menu menu = getMenu();
        menu.clear();
        SubMenu subMenu = menu.addSubMenu(Menu.NONE, Menu.NONE, 200, "");
        for(Privilege pr : user.getPrivileges()) {
            switch (pr) {
                case BROWSE_COMMUNITY_TOPIC:
                    menu.add(R.id.nav_menu_group, R.id.nav_publishedOpened, 100, getResources().getString(R.string.PublishedOpenedFragment_name))
                            .setIcon(R.drawable.ic_menu_publish);
                    break;
                case BROWSE_COMMUNITY_SHOW:
                    menu.add(R.id.nav_menu_group, R.id.nav_publishedOpenedClosed, 100, getResources().getString(R.string.PublishedClosedFragment_name))
                            .setIcon(R.drawable.ic_menu_transcribe_close);
                    break;
                case BROWSE_I_ATTENDED:
                    subMenu.add(Menu.NONE, R.id.nav_iAttended, Menu.NONE, getResources().getString(R.string.IAttendedFragment_name))
                            .setIcon(R.drawable.ic_menu_okay);
                    break;
            }
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        List<Privilege> privileges = new ArrayList<>(user.getPrivileges());
        switch (item.getItemId()) {
            case R.id.nav_publishedOpened:
                viewPager.setCurrentItem(privileges.indexOf(Privilege.BROWSE_COMMUNITY_TOPIC));
                break;
            case R.id.nav_publishedOpenedClosed:
                viewPager.setCurrentItem(privileges.indexOf(Privilege.BROWSE_COMMUNITY_SHOW));
                break;
            case R.id.nav_iAttended:
                viewPager.setCurrentItem(privileges.indexOf(Privilege.BROWSE_I_ATTENDED));
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
