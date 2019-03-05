package com.thumbstage.hydrogen.view.create;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.event.TopicSettingEvent;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.utils.DensityUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TopicSettingDialog extends DialogFragment {

    @BindView(R.id.dialog_topic_setting_name)
    EditText name;
    @BindView(R.id.dialog_topic_setting_brief)
    EditText brief;
    @BindView(R.id.dialog_topic_setting_setting)
    EditText setting;

    Topic topic;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.dialog_topic_setting, container);
        ButterKnife.bind(contentView);

        /*
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels - DensityUtil.dp2px(getActivity(), 16f);
        params.bottomMargin = DensityUtil.dp2px(getActivity(), 8f);
        contentView.setLayoutParams(params);
        */

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        return contentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        int width = getResources().getDisplayMetrics().widthPixels - DensityUtil.dp2px(getActivity(), 16f);
        getDialog().getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    /*
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.dialog_topic_setting);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消

        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);

        ButterKnife.bind(this, dialog); // Dialog即View

        return super.onCreateDialog(savedInstanceState);
    }
    */

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_topic_setting, null);
        setContentView(contentView);
        ButterKnife.bind(this);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = getContext().getResources().getDisplayMetrics().widthPixels - DensityUtil.dp2px(getContext(), 16f);
        params.bottomMargin = DensityUtil.dp2px(getContext(), 8f);
        contentView.setLayoutParams(params);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
    }
    */

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    @OnClick(R.id.dialog_topic_setting_ok)
    public void onOK(View view) {
        topic.setName(name.getText().toString());
        topic.setBrief(brief.getText().toString());

    }

    @OnClick(R.id.dialog_topic_setting_select)
    public void onSelectPic(View view) {
        dismiss();
        EventBus.getDefault().post(new TopicSettingEvent("select"));
    }

}
