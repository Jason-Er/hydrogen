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
import com.thumbstage.hydrogen.model.bo.Privilege;
import com.thumbstage.hydrogen.model.bo.User;

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
        TextView contact = headerView.findViewById(R.id.nav_account_contact);
        Glide.with(userAvatar).load(user.getAvatar()).into(userAvatar);
        userName.setText(user.getName());

        // update menu
        Menu menu = getMenu();
        menu.clear();
        SubMenu subMenu = menu.addSubMenu(Menu.NONE, Menu.NONE, 200, "mine");
        for(Privilege pr : user.getPrivileges()) {
            switch (pr) {
                case BROWSE_PUBLISHEDOPENED:
                    menu.add(R.id.nav_menu_group, R.id.nav_publishedOpened, 100, "BROWSE_PUBLISHEDOPENED")
                            .setIcon(R.drawable.ic_menu_publish);
                    break;
                case BROWSE_PUBLISHEDCLOSED:
                    menu.add(R.id.nav_menu_group, R.id.nav_publishedOpenedClosed, 100, "BROWSE_PUBLISHEDCLOSED")
                            .setIcon(R.drawable.ic_menu_transcribe_close);
                    break;
                case BROWSE_AT_ME:
                    subMenu.add(Menu.NONE, R.id.nav_atMe, Menu.NONE, "BROWSE_AT_ME")
                            .setIcon(R.drawable.ic_menu_publish);
                    break;
                case BROWSE_ISTARTED:
                    subMenu.add(Menu.NONE, R.id.nav_iStarted, Menu.NONE, "BROWSE_ISTARTED")
                            .setIcon(R.drawable.ic_menu_okay);
                    break;
                case BROWSE_IATTENDED:
                    subMenu.add(Menu.NONE, R.id.nav_iAttended, Menu.NONE, "BROWSE_IATTENDED")
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
                viewPager.setCurrentItem(privileges.indexOf(Privilege.BROWSE_PUBLISHEDOPENED));
                break;
            case R.id.nav_publishedOpenedClosed:
                viewPager.setCurrentItem(privileges.indexOf(Privilege.BROWSE_PUBLISHEDCLOSED));
                break;
            case R.id.nav_atMe:
                viewPager.setCurrentItem(privileges.indexOf(Privilege.BROWSE_AT_ME));
                break;
            case R.id.nav_iStarted:
                viewPager.setCurrentItem(privileges.indexOf(Privilege.BROWSE_ISTARTED));
                break;
            case R.id.nav_iAttended:
                viewPager.setCurrentItem(privileges.indexOf(Privilege.BROWSE_IATTENDED));
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
