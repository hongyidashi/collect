package org.collect.test.controller;


import org.collect.test.service.DroolsService;
import org.collect.test.vo.DroolsReqVo;
import org.collect.test.vo.DroolsRespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述:
 * 作者: panhongtong
 * 创建时间: 2021-02-18 16:15
 **/
@RestController
public class DroolsController {

    @Autowired
    private DroolsService droolsService;

    @PostMapping("/login")
    public DroolsRespVo login(@RequestBody DroolsReqVo reqVo) {
        return droolsService.login(reqVo);
    }
}
