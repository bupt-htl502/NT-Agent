package com.coldwindx.server.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentTranscriptControllerTest {
    @Autowired
    StudentTranscriptController studentTranscriptController;


    @Test
    public void testDownloadCsv() throws Exception {
        long studentId = 167L;

        // 构造 Mock 响应对象
        MockHttpServletResponse response = new MockHttpServletResponse();

        // 调用下载方法
        studentTranscriptController.download(studentId, response);

        // 获取返回内容
        String content = response.getContentAsString();
        System.out.println("下载的CSV内容：\n" + content);

    }
}
