package org.collect.test.service;


import org.collect.test.vo.DroolsReqVo;
import org.collect.test.vo.DroolsRespVo;
import org.springframework.stereotype.Service;

/**
 * 描述:
 * 作者: panhongtong
 * 创建时间: 2021-02-18 14:46
 **/
@Service
public class DroolsService {

    public DroolsRespVo login(DroolsReqVo reqVo) {
        DroolsRespVo respVo = new DroolsRespVo();
        System.out.println(reqVo);
        if (reqVo.getName().equals("test") && reqVo.getPassword().equals("123")) {
            respVo.setMsg("登录成功");
        } else {
            respVo.setMsg("登录失败");
        }
        return respVo;
    }

}
