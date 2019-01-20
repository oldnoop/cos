/*
 * Copyright 2019 oldnoop.tech.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.oldnoop.cos.spring.boot.autoconfigure;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * <pre>
1.按需配置src/test/resources/application.properties
    修改如下的值
    secret-id,secret-key修改为云存储用户的secretId,secretKey
    bucket和region修改为实际的值
    spring.cos.oldnoop.server.secret-id=
    spring.cos.oldnoop.server.secret-key=
    spring.cos.oldnoop.server.region=ap-beijing
    spring.cos.oldnoop.server.bucket=example-1234567567
2.运行启动类 TmpkeyApplication
3.访问http://127.0.0.1:8080/userid
4.访问http://127.0.0.1:8080/coskey

 * </pre>
 * 
 * @author oldnoop
 * @date 2019年1月19日
 */
@SpringBootApplication
@ServletComponentScan
public class TmpkeyApplication {

    public static void main(String[] args) {
        SpringApplication.run(TmpkeyApplication.class, args);
    }
    
}

@WebServlet(urlPatterns="/userid")
class UserLoginServlet extends HttpServlet{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
                                                                             IOException {
        req.getSession().setAttribute("userId", "123456");
    }
}
