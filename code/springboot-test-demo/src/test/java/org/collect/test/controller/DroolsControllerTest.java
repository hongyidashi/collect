package org.collect.test.controller;

import cn.hutool.json.JSONUtil;
import org.collect.test.vo.DroolsReqVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class DroolsControllerTest {

    /**
     * MockMvc 实现了对Http请求的模拟，能够直接使用网络的形式，转换到Controller的调用，使得测试速度快、不依赖网络环境
     */
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void login() throws Exception {
        DroolsReqVo reqVo = new DroolsReqVo();
        reqVo.setName("test");
        reqVo.setPassword("123");
        String json = JSONUtil.toJsonStr(reqVo);

        // perform执行一个请求
        ResultActions resultActions = mockMvc.perform(
                // MockMvcRequestBuilders.post(“XXX”)构造一个请求
                MockMvcRequestBuilders.post("http://localhost:8080/login")
                // json 传参
                .content(json.getBytes(StandardCharsets.UTF_8))
                // accept()设置返回类型
                .accept(MediaType.APPLICATION_JSON)
                // 代表发送端发送的数据格式是application/json;charset=UTF-8
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                // ResultActions.andExpect添加执行完成后的断言
                // 看请求的状态响应码是否为200如果不是则抛异常，测试不通过
                .andExpect(MockMvcResultMatchers.status().isOk());

        // ResultActions.andReturn表示执行完成后返回相应的结果
        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
        resultActions.andExpect(MockMvcResultMatchers.content().string("{\"msg\":\"登录失败\"}"))
                // ResultActions.andDo：添加一个结果处理器，表示要对结果做点什么事情
                .andDo(mvcResult -> System.out.println(mvcResult.getResponse().getContentAsString()));
    }


}