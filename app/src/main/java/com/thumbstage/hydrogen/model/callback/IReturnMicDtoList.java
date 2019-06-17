package com.thumbstage.hydrogen.model.callback;

import com.thumbstage.hydrogen.model.dto.MicDto;

import java.util.List;

public interface IReturnMicDtoList {
    void callback(List<MicDto> micDtoList);
}
