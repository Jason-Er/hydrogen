package com.thumbstage.hydrogen.view.browse;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.model.bo.Privilege;
import com.thumbstage.hydrogen.model.bo.User;

public class BrowseNavigationView extends NavigationView {


    public BrowseNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void updateStateBy(User user, FragmentPagerAdapter pagerAdapter) {

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
        SubMenu subMenu = menu.addSubMenu("mine");
        for(Privilege pr : user.getPrivileges()) {
            switch (pr) {
                case BROWSE_AT_ME:
                    subMenu.add(Menu.NONE, R.id.nav_atMe, Menu.NONE, "BROWSE_AT_ME")
                            .setIcon(R.drawable.ic_menu_publish);
                    break;
                case BROWSE_PUBLISHEDOPENED:
                    menu.add(R.id.nav_menu_group, R.id.nav_publishedOpened, Menu.NONE, "BROWSE_PUBLISHEDOPENED")
                            .setIcon(R.drawable.ic_menu_publish);
                    break;
                case BROWSE_PUBLISHEDCLOSED:
                    menu.add(R.id.nav_menu_group, R.id.nav_publishedOpenedClosed, Menu.NONE, "BROWSE_PUBLISHEDCLOSED")
                            .setIcon(R.drawable.ic_menu_transcribe_close);
                    break;
                case BROWSE_ISTARTED:
                    subMenu.add(Menu.NONE, R.id.nav_iStartedOpened, Menu.NONE, "BROWSE_ISTARTED")
                            .setIcon(R.drawable.ic_menu_okay);
                    break;
                case BROWSE_IATTENDED:
                    subMenu.add(Menu.NONE, R.id.nav_iAttended, Menu.NONE, "BROWSE_IATTENDED")
                            .setIcon(R.drawable.ic_menu_okay);
                    break;
                case BROWSE_CONTACT:
                    subMenu.add(Menu.NONE, R.id.nav_iContact, Menu.NONE, "BROWSE_CONTACT")
                            .setIcon(R.drawable.ic_menu_okay);
                    break;
            }
        }

    }


}
