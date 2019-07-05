package com.thumbstage.hydrogen.view.create.fragment;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.event.LineTypeEvent;
import com.thumbstage.hydrogen.event.TopicBottomBarEvent;
import com.thumbstage.hydrogen.model.bo.LineType;
import com.thumbstage.hydrogen.model.bo.MessageType;
import com.thumbstage.hydrogen.model.vo.Line;
import com.thumbstage.hydrogen.utils.DensityUtil;
import com.thumbstage.hydrogen.utils.PathUtils;
import com.thumbstage.hydrogen.utils.SoftInputUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    @BindView(R.id.input_bar_btn_type)
    RecyclerView recyclerView;

    LineTypeAdapter lineTypeAdapter;
    LinearLayoutManager layoutManager;
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

        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        lineTypeAdapter = new LineTypeAdapter();
        recyclerView.setAdapter(lineTypeAdapter);
        int padding = DensityUtil.dp2px(getContext(), 50) / 2 - DensityUtil.dp2px(getContext(), 30) / 2;
        recyclerView.setPadding(0,padding, 0, padding);
        LinearSnapHelper linearSnapHelper = new LinearSnapHelper();
        linearSnapHelper.attachToRecyclerView(recyclerView);

        setEditTextChangeListener();
        initRecordBtn();

        contentEditText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SoftInputUtils.showSoftInput(getContext(), contentEditText);
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseMessageEvent(final LineTypeEvent event) {
        switch (event.getMessage()) {
            case "click":
                int position = layoutManager.findFirstCompletelyVisibleItemPosition();
                if(position==0)
                    recyclerView.smoothScrollToPosition(1);
                else
                    recyclerView.smoothScrollToPosition(0);
                break;
        }
    }

    @OnClick(R.id.input_bar_btn_send_text)
    public void sendTextBtn(View view) {
        String content = contentEditText.getText().toString();
        if (TextUtils.isEmpty(content)) {
            // Toast.makeText(getContext(), cn.leancloud.chatkit.R.string.lcim_message_is_null, Toast.LENGTH_SHORT).show();
            return;
        }
        int position = layoutManager.findFirstCompletelyVisibleItemPosition();
        LineType lineType = lineTypeAdapter.getLineType(position);

        contentEditText.setText("");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sendTextBtn.setEnabled(true);
            }
        }, MIN_INTERVAL_SEND_MESSAGE);

        Line line = new Line(null, new Date(), content, lineType);
        line.setMessageType(MessageType.TEXT);
        EventBus.getDefault().post(
                new TopicBottomBarEvent(line, "text"));
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
                if (secs > 0) {
                    int position = layoutManager.findFirstCompletelyVisibleItemPosition();
                    LineType lineType = lineTypeAdapter.getLineType(position);
                    Line line = new Line(null, new Date(), audioPath, lineType);
                    line.setMessageType(MessageType.AUDIO);
                    EventBus.getDefault().post(
                            new TopicBottomBarEvent(line, "audio"));
                }
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
