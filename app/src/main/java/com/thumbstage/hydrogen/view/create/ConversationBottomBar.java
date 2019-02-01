package com.thumbstage.hydrogen.view.create;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.view.common.ConversationBottomBarEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.leancloud.chatkit.utils.LCIMPathUtils;
import cn.leancloud.chatkit.utils.LCIMSoftInputUtils;
import cn.leancloud.chatkit.view.LCIMRecordButton;

public class ConversationBottomBar extends LinearLayout {

    /**
     * 文本输入框
     */
    @BindView(R.id.input_bar_et_content)
    EditText contentEditText;

    /**
     * 发送文本的Button
     */
    @BindView(R.id.input_bar_btn_send_text)
    View sendTextBtn;

    /**
     * 切换到语音输入的 Button
     */
    @BindView(R.id.input_bar_btn_voice)
    View voiceBtn;

    /**
     * 切换到文本输入的 Button
     */
    @BindView(R.id.input_bar_btn_keyboard)
    View keyboardBtn;

    /**
     * 底部的layout，包含 emotionLayout 与 actionLayout
     */
    @BindView(R.id.input_bar_layout_more)
    View moreLayout;

    /**
     * 录音按钮
     */
    @BindView(R.id.input_bar_btn_record)
    LCIMRecordButton recordBtn;

    /**
     * action layout
     */
    @BindView(R.id.input_bar_layout_action)
    LinearLayout actionLayout;

    /**
     * 最小间隔时间为 1 秒，避免多次点击
     */
    private final int MIN_INTERVAL_SEND_MESSAGE = 1000;

    public ConversationBottomBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.layout_conversation_bottom_bar, this);
    }

    /**
     * 隐藏底部的图片、emtion 等 layout
     */
    public void hideMoreLayout() {
        moreLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);

        setEditTextChangeListener();
        initRecordBtn();

        contentEditText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                moreLayout.setVisibility(View.GONE);
                LCIMSoftInputUtils.showSoftInput(getContext(), contentEditText);
            }
        });
    }

    /**
     * 加号 Button
     */
    @OnClick(R.id.input_bar_btn_action)
    public void actionBtn(View view) {
        boolean showActionView =
                (GONE == moreLayout.getVisibility() || GONE == actionLayout.getVisibility());
        moreLayout.setVisibility(showActionView ? VISIBLE : GONE);
        actionLayout.setVisibility(showActionView ? VISIBLE : GONE);
        LCIMSoftInputUtils.hideSoftInput(getContext(), contentEditText);
    }

    @OnClick(R.id.input_bar_btn_action)
    public void sendTextBtn(View view) {
        String content = contentEditText.getText().toString();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(getContext(), cn.leancloud.chatkit.R.string.lcim_message_is_null, Toast.LENGTH_SHORT).show();
            return;
        }

        contentEditText.setText("");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sendTextBtn.setEnabled(true);
            }
        }, MIN_INTERVAL_SEND_MESSAGE);

        EventBus.getDefault().post(
                new ConversationBottomBarEvent(content, "text"));
    }

    @OnClick(R.id.input_bar_btn_keyboard)
    public void keyboardBtn(View view) {
        showTextLayout();
    }

    @OnClick(R.id.input_bar_btn_voice)
    public void voiceBtn(View view) {
        showAudioLayout();
    }

    /**
     * 初始化录音按钮
     */
    private void initRecordBtn() {
        recordBtn.setSavePath(LCIMPathUtils.getRecordPathByCurrentTime(getContext()));
        recordBtn.setRecordEventListener(new LCIMRecordButton.RecordEventListener() {
            @Override
            public void onFinishedRecord(final String audioPath, int secs) {
                if (secs > 0)
                    EventBus.getDefault().post(
                            new ConversationBottomBarEvent(audioPath, "voice"));
                recordBtn.setSavePath(LCIMPathUtils.getRecordPathByCurrentTime(getContext()));
            }

            @Override
            public void onStartRecord() {
            }
        });
    }

    /**
     * 展示文本输入框及相关按钮，隐藏不需要的按钮及 layout
     */
    private void showTextLayout() {
        contentEditText.setVisibility(View.VISIBLE);
        recordBtn.setVisibility(View.GONE);
        voiceBtn.setVisibility(contentEditText.getText().length() > 0 ? GONE : VISIBLE);
        sendTextBtn.setVisibility(contentEditText.getText().length() > 0 ? VISIBLE : GONE);
        keyboardBtn.setVisibility(View.GONE);
        moreLayout.setVisibility(View.GONE);
        contentEditText.requestFocus();
        LCIMSoftInputUtils.showSoftInput(getContext(), contentEditText);
    }

    /**
     * 展示录音相关按钮，隐藏不需要的按钮及 layout
     */
    private void showAudioLayout() {
        contentEditText.setVisibility(View.GONE);
        recordBtn.setVisibility(View.VISIBLE);
        voiceBtn.setVisibility(GONE);
        keyboardBtn.setVisibility(VISIBLE);
        moreLayout.setVisibility(View.GONE);
        LCIMSoftInputUtils.hideSoftInput(getContext(), contentEditText);
    }

    /**
     * 设置 text change 事件，有文本时展示发送按钮，没有文本时展示切换语音的按钮
     */
    private void setEditTextChangeListener() {
        contentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                boolean showSend = charSequence.length() > 0;
                keyboardBtn.setVisibility(!showSend ? View.VISIBLE : GONE);
                sendTextBtn.setVisibility(showSend ? View.VISIBLE : GONE);
                voiceBtn.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}
