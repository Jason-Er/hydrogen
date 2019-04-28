package com.thumbstage.hydrogen.view.create.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.event.HyMenuItemEvent;
import com.thumbstage.hydrogen.view.common.HyMenuItem;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class PopupWindowAdapter extends BaseAdapter {

    List<HyMenuItem> itemList = new ArrayList<>();

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.isEmpty()? null : itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return itemList.isEmpty()? 0 : itemList.get(position).getResId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final HyMenuItem item = itemList.get(position);
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_menu_item, null);
        ImageView icon = view.findViewById(R.id.menu_item_icon);
        TextView command = view.findViewById(R.id.menu_item_command);
        icon.setImageResource(item.getResId());
        command.setText(item.getCommandName().name());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new HyMenuItemEvent(item.getCommandName()));
            }
        });
        return view;
    }

    public void removeItem(HyMenuItem item) {
        itemList.remove(item);
        notifyDataSetChanged();
    }

    public List<HyMenuItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<HyMenuItem> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }
}
