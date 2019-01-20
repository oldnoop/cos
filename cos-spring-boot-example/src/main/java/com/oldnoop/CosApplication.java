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

package com.oldnoop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
2.运行启动类 CosApplication
3.访问http://localhost:8080/user/toLogin
 * </pre>
 * @author oldnoop
 * @date 2019年1月19日
 */
@SpringBootApplication
public class CosApplication {

	public static void main(String[] args) {
		SpringApplication.run(CosApplication.class, args);
	}

}

