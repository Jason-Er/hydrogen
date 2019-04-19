package com.thumbstage.hydrogen.view.show.fragment;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.event.PlayerControlEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayerControlBar extends LinearLayout {

    @BindView(R.id.footerMain_seekBar)
    SeekBar seekBar;
    @BindView(R.id.footerMain_playerControl_play)
    ImageButton imageButtonPlay;

    @OnClick({R.id.footerMain_playerControl_stop,
            R.id.footerMain_playerControl_play,
            R.id.footerMain_playerControl_volume})
    public void responseClick(ImageButton button) {
        PlayerControlEvent event = new PlayerControlEvent("NULL");
        switch (button.getId()) {
            case R.id.footerMain_playerControl_stop:
                stopAction();
                event.setMessage("STOP");
                break;
            case R.id.footerMain_playerControl_play:
                if( button.getTag() == null || !(boolean) button.getTag() ) {
                    button.setTag(true);
                    event.setMessage("PLAY");
                    button.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_pause));
                } else {
                    button.setTag(false);
                    event.setMessage("PAUSE");
                    button.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_play));
                }
                break;
            case R.id.footerMain_playerControl_volume:
                event.setMessage("VOLUME");
                break;
        }
        EventBus.getDefault().post(event);
    }

    public PlayerControlBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    Log.d("PlayerControlBar", "current process: " + progress + "%");
                    PlayerControlEvent event = new PlayerControlEvent("SEEK");
                    event.setSeekProcess((float) progress / seekBar.getMax());
                    EventBus.getDefault().post(event);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d("PlayerControlBar","touch down ");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("PlayerControlBar","touch up ");
            }
        });

    }

    private void stopAction() {
        imageButtonPlay.setTag(false);
        imageButtonPlay.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_play));
    }
}
