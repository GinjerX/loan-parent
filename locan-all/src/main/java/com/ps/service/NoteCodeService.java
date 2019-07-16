package com.ps.service;

import com.ps.domain.CodeVO;

public interface NoteCodeService {

    String getRandomNumber4();

    void doGetTestWayOne(String phone,String type);

    CodeVO getCode(String userPhone, String type);


}
