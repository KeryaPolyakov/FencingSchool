package com.kirillpolyakov.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(description = "Общий объект возврата всех API")
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult<T>{
    @Schema(description = "Сообщение в случае ошибки")
    private String message;
    @Schema(description = "Возвращаемые данные в случае успеха")
    private T data;
}
