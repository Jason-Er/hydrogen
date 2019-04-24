package com.thumbstage.hydrogen.view.show.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.event.PlayerControlEvent;
import com.thumbstage.hydrogen.model.bo.Mic;
import com.thumbstage.hydrogen.utils.DensityUtil;
import com.thumbstage.hydrogen.viewmodel.TopicViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

public class ShowFragment extends Fragment implements TextToSpeech.OnInitListener{

    final String TAG = "TopicFragment";

    @BindView(R.id.fragment_topic_bk)
    ImageView background;
    @BindView(R.id.loading_spinner)
    ProgressBar spinner;
    @BindView(R.id.fragment_show_subtitle)
    TextSwitcher subtitle;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    TopicViewModel topicViewModel;

    TextToSpeech textToSpeech;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show, container, false);
        ButterKnife.bind(this, view);

        subtitle.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                final TextView tv = new TextView(getContext());
                tv.setTextSize(DensityUtil.dp2px(getContext(), 15));
                tv.setTextColor(Color.BLACK);
                tv.setEllipsize(TextUtils.TruncateAt.END);
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.gravity = Gravity.CENTER;
                tv.setLayoutParams(lp);
                return tv;
            }
        });

        textToSpeech = new TextToSpeech(getContext(), this);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        configureDagger();
        configureViewModel();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResponseMessageEvent(final PlayerControlEvent event) {
        switch (event.getMessage()) {
            case "STOP":
                textToSpeech.shutdown();
                break;
            case "PLAY":
                if(textToSpeech !=null && !textToSpeech.isSpeaking()) {
                    subtitle.setText("Hello world");
                    textToSpeech.setPitch(0.5f);
                    textToSpeech.setSpeechRate(1.5f);
                    textToSpeech.speak("hello", TextToSpeech.QUEUE_FLUSH, null, null);
                }
                break;
            case "PAUSE":
                textToSpeech.stop();
                break;
            case "SEEK":

                break;
            case "VOLUME":

                break;
        }
    }

    private void configureDagger(){
        AndroidSupportInjection.inject(this);
    }

    private void configureViewModel() {
        final String micId = getActivity().getIntent().getStringExtra(Mic.class.getSimpleName());
        topicViewModel = ViewModelProviders.of(this, viewModelFactory).get(TopicViewModel.class);
        topicViewModel.pickUpTopic(micId).observe(this, new Observer<Mic>() {
            @Override
            public void onChanged(@Nullable Mic mic) {
                if(mic.getTopic().getSetting() != null) {
                    Glide.with(background).load(mic.getTopic().getSetting().getUrl()).into(background);
                }
                spinner.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.ENGLISH);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(getContext(), "数据丢失或不支持", Toast.LENGTH_SHORT).show();
            } else {
                textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String utteranceId) {
                        Log.i("textToSpeech onStart","utteranceId: "+ utteranceId);
                    }

                    @Override
                    public void onDone(String utteranceId) {
                        Log.i("textToSpeech onDone","utteranceId: "+ utteranceId);
                    }

                    @Override
                    public void onError(String utteranceId) {
                        Log.i("textToSpeech onError","utteranceId: "+ utteranceId);
                    }
                });
            }

        }
    }
}
