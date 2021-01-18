package org.collect.security.service.impl;

import cn.hutool.json.JSONUtil;
import org.collect.security.entity.UserDetail;
import org.collect.security.service.AuthService;
import org.collect.security.utils.AccessToken;
import org.collect.security.utils.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * 描述:
 * 作者: panhongtong
 * 创建时间: 2021-01-18 15:41
 **/
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public Object login(String loginAccount, String password) {
        // 1 创建UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken usernameAuthentication = new UsernamePasswordAuthenticationToken(loginAccount, password);
        // 2 认证
        Authentication authentication = authenticationManager.authenticate(usernameAuthentication);
        // 3 保存认证信息
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 4 生成自定义token
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        AccessToken accessToken = jwtProvider.createToken((UserDetails) authentication.getPrincipal());

        // 5 放入缓存
        // ...
        return JSONUtil.parseObj(accessToken);
    }
}
