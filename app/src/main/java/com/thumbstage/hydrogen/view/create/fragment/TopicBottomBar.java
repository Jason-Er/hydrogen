package com.thumbstage.hydrogen.view.create.fragment;

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
import com.thumbstage.hydrogen.app.UserGlobal;
import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.model.LineType;
import com.thumbstage.hydrogen.utils.PathUtils;
import com.thumbstage.hydrogen.utils.SoftInputUtils;
import com.thumbstage.hydrogen.event.ConversationBottomBarEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TopicBottomBar extends LinearLayout {

    @BindView(R.id.input_bar_et_content)
    EditText contentEditText;
    @BindView(R.id.input_bar_btn_send_text)
    View sendTextBtn;
    @BindView(R.id.input_bar_btn_voice)
    View voiceBtn;
    @BindView(R.id.input_bar_btn_keyboard)
    View keyboardBtn;
    @BindView(R.id.input_bar_btn_record)
    VoiceRecordButton recordBtn;

    /**
     * 最小间隔时间为 1 秒，避免多次点击
     */
    private final int MIN_INTERVAL_SEND_MESSAGE = 1000;

    public TopicBottomBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.layout_topic_bottom_bar, this);
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
                SoftInputUtils.showSoftInput(getContext(), contentEditText);
            }
        });
    }

    /**
     * 加号 Button
     */
    @OnClick(R.id.input_bar_btn_action)
    public void actionBtn(View view) {
        SoftInputUtils.hideSoftInput(getContext(), contentEditText);
    }

    @OnClick(R.id.input_bar_btn_send_text)
    public void sendTextBtn(View view) {
        String content = contentEditText.getText().toString();
        if (TextUtils.isEmpty(content)) {
            // Toast.makeText(getContext(), cn.leancloud.chatkit.R.string.lcim_message_is_null, Toast.LENGTH_SHORT).show();
            return;
        }

        contentEditText.setText("");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sendTextBtn.setEnabled(true);
            }
        }, MIN_INTERVAL_SEND_MESSAGE);

        Line line = new Line(UserGlobal.getInstance().getCurrentUser(), new Date(), content, LineType.LT_DIALOGUE);
        EventBus.getDefault().post(
                new ConversationBottomBarEvent(line, "text"));
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
        recordBtn.setSavePath(PathUtils.getRecordPathByCurrentTime(getContext()));
        recordBtn.setRecordEventListener(new VoiceRecordButton.RecordEventListener() {
            @Override
            public void onFinishedRecord(final String audioPath, int secs) {
                if (secs > 0)
                    EventBus.getDefault().post(
                            new ConversationBottomBarEvent(audioPath, "voice"));
                recordBtn.setSavePath(PathUtils.getRecordPathByCurrentTime(getContext()));
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
        contentEditText.requestFocus();
        SoftInputUtils.showSoftInput(getContext(), contentEditText);
    }

    /**
     * 展示录音相关按钮，隐藏不需要的按钮及 layout
     */
    private void showAudioLayout() {
        contentEditText.setVisibility(View.GONE);
        recordBtn.setVisibility(View.VISIBLE);
        voiceBtn.setVisibility(GONE);
        keyboardBtn.setVisibility(VISIBLE);
        SoftInputUtils.hideSoftInput(getContext(), contentEditText);
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
