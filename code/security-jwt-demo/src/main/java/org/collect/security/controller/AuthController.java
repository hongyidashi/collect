package org.collect.security.controller;

import org.collect.security.service.AuthService;
import org.collect.security.vo.LoginReqVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述:
 * 作者: panhongtong
 * 创建时间: 2021-01-18 15:44
 **/
@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("login")
    public Object login(@RequestBody LoginReqVo reqVo) {
        return authService.login(reqVo.getUsername(), reqVo.getPw());
    }
}
