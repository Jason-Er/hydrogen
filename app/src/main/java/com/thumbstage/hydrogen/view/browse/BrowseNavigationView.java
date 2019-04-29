package com.thumbstage.hydrogen.view.browse;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thumbstage.hydrogen.R;
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
        getMenu().clear();
        inflateMenu(R.menu.menu_case_attend);
    }


}
