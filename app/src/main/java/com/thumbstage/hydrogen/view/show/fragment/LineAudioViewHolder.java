package com.thumbstage.hydrogen.view.show.fragment;

import android.view.View;

import com.thumbstage.hydrogen.R;
import com.thumbstage.hydrogen.view.create.fragment.IMPlayButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LineAudioViewHolder extends ViewHolder{

    @BindView(R.id.chat_item_audio_play_btn)
    IMPlayButton imPlayButton;

    public LineAudioViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setContent(String content) {
        imPlayButton.setPath(content);
    }

}
