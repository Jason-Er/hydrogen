package com.thumbstage.hydrogen.view.create.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.utils.IMAudioHelper;

/**
 * Created by lzw on 14-9-22.
 * 语音播放按钮
 */
public class IMPlayButton extends AppCompatTextView implements View.OnClickListener {
    private String path;
    private boolean leftSide;
    private AnimationDrawable anim;
    private IMAudioHelper.AudioFinishCallback finishCallback;

    public IMPlayButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        leftSide = getLeftFromAttrs(context, attrs);
        setLeftSide(leftSide);
        setOnClickListener(this);
    }

    /**
     * 设置语音按钮的方向
     * 因为聊天中左右 item 都可能有语音
     *
     * @param leftSide
     */
    public void setLeftSide(boolean leftSide) {
        this.leftSide = leftSide;
        stopRecordAnimation();
    }

    private boolean getLeftFromAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.topic_line_play_button);
        boolean left = true;
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.topic_line_play_button_left) {
                left = typedArray.getBoolean(attr, true);
            }
        }
        return left;
    }

    public void setPath(String path) {
        this.path = path;
        stopRecordAnimation();
        if (isPlaying()) {
            IMAudioHelper.getInstance().addFinishCallback(new IMAudioHelper.AudioFinishCallback() {
                @Override
                public void onFinish() {
                    stopRecordAnimation();
                }
            });
            startRecordAnimation();
        }
    }

    private boolean isPlaying() {
        return IMAudioHelper.getInstance().isPlaying() == true &&
                IMAudioHelper.getInstance().getAudioPath().equals(path);
    }

    @Override
    public void onClick(View v) {
        if (IMAudioHelper.getInstance().isPlaying() == true &&
                IMAudioHelper.getInstance().getAudioPath().equals(path)) {
            IMAudioHelper.getInstance().pausePlayer();
            stopRecordAnimation();
        } else {
            IMAudioHelper.getInstance().playAudio(path);
            IMAudioHelper.getInstance().addFinishCallback(new IMAudioHelper.AudioFinishCallback() {
                @Override
                public void onFinish() {
                    Log.i("IMPlayButton","onFinish"+IMPlayButton.this);
                    if(finishCallback!=null) {
                        finishCallback.onFinish();
                    }
                    stopRecordAnimation();
                }
            });
            startRecordAnimation();
        }
    }

    public void pausePlaying() {
        if (IMAudioHelper.getInstance().isPlaying() == true &&
                IMAudioHelper.getInstance().getAudioPath().equals(path)) {
            IMAudioHelper.getInstance().pausePlayer();
            stopRecordAnimation();
        }
    }

    private void startRecordAnimation() {
        setCompoundDrawablesWithIntrinsicBounds(leftSide ? R.drawable.hy_chat_anim_voice_left : 0,
                0, !leftSide ? R.drawable.hy_chat_anim_voice_right : 0, 0);
        anim = (AnimationDrawable) getCompoundDrawables()[leftSide ? 0 : 2];
        anim.start();
    }

    private void stopRecordAnimation() {
        setCompoundDrawablesWithIntrinsicBounds(leftSide ? R.drawable.hy_chat_voice_right3 : 0,
                0, !leftSide ? R.drawable.hy_chat_voice_left3 : 0, 0);
        if (anim != null) {
            anim.stop();
        }
    }

    public void setFinishCallback(IMAudioHelper.AudioFinishCallback finishCallback) {
        this.finishCallback = finishCallback;
    }

}
