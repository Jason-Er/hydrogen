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
import com.thumbstage.hydrogen.model.bo.CanOnTopic;
import com.thumbstage.hydrogen.view.common.HyMenuItem;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PopupWindowAdapter extends BaseAdapter {

    List<HyMenuItem> itemList = new ArrayList<>();
    Map<CanOnTopic, Integer> canMap = new HashMap<CanOnTopic, Integer>() {
        {
            put(CanOnTopic.PUBLISH, R.string.menu_item_can_publish);
            put(CanOnTopic.OPEN, R.string.menu_item_can_open);
            put(CanOnTopic.CLOSE, R.string.menu_item_can_close);
            put(CanOnTopic.DELETE, R.string.menu_item_can_delete);
            put(CanOnTopic.PARTICIPANT, R.string.menu_item_can_participant);
            put(CanOnTopic.SETUPINFO, R.string.menu_item_can_setupinfo);
            put(CanOnTopic.UPDATE, R.string.menu_item_can_update);
            put(CanOnTopic.RECOMMEND, R.string.menu_item_can_recommend);
        }
    };

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
        String name = parent.getContext().getResources().getString(canMap.get(item.getCanOnTopic()));
        command.setText(name);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new HyMenuItemEvent(item.getCanOnTopic()));
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
