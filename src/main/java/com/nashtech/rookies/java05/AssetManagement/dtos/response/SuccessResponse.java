package com.nashtech.rookies.java05.AssetManagement.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SuccessResponse<T> {

    public SuccessResponse(T data) {
        this.data = data;
    }
    private T data;
}
