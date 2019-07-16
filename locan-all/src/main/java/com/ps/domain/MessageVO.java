package com.ps.domain;

import lombok.Data;

@Data
public class MessageVO<T> {
    private Integer code;
    private String msg;
    private T data;
    private Integer total;




}
