package com.ps.domain;

import lombok.Data;

@Data
public class CodeVO {
    private int id;
    private String userPhone;
    private String verify;//验证码
    private String codeTime;
    private String type;


}
