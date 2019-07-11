package com.thumbstage.hydrogen.model.dto;

import com.thumbstage.hydrogen.database.entity.LikeEntity;

public class LikeDto extends LikeEntity {
    public LikeDto(String userId, String topicId) {
        setUserId(userId);
        setTopicId(topicId);
    }
}
