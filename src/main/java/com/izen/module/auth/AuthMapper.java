package com.izen.module.auth;

import com.izen.module.auth.dto.request.LoginRequestDto;
import com.izen.module.auth.dto.vo.LoginVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthMapper {
    LoginVo findLoginInfo(LoginRequestDto loginRequestDto);

    LoginVo findRefreshInfo(@Param("accountId") Long accountId);
}
