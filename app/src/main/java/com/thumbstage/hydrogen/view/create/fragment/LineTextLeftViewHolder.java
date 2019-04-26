package com.thumbstage.hydrogen.view.create.fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.event.PopupMenuEvent;
import com.thumbstage.hydrogen.model.bo.Line;
import com.thumbstage.hydrogen.utils.GlideUtil;
import com.thumbstage.hydrogen.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LineTextLeftViewHolder extends RecyclerView.ViewHolder {

    Line line;

    @BindView(R.id.item_line_left_tv_time)
    TextView time;
    @BindView(R.id.item_line_left_iv_avatar)
    ImageView avatar;
    @BindView(R.id.item_line_left_text_tv_content)
    TextView content;


    public LineTextLeftViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.menu_popup_member_add, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.menu_member_add) {
                            EventBus.getDefault().post(new PopupMenuEvent(line.getWho(), "addUser"));
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    public void setLine(@NonNull Line line) {
        this.line = line;
        GlideUtil.inject(itemView.getContext(), line.getWho().getAvatar(), avatar);
        content.setText(line.getWhat());
        time.setText(StringUtil.date2String4Show(line.getWhen()));
    }
}
