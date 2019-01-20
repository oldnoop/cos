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

package com.oldnoop.cos.tmpkey;

import static org.junit.Assert.*;

import java.util.Arrays;

//import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import com.oldnoop.cos.*;
import com.oldnoop.cos.util.CodecUtils;

/**
 * @author oldnoop
 * @date 2019年1月19日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CosConfig.class)
@WebAppConfiguration
@PropertySource("coskey.properties")
public class CosTmpKeyConfigTest {


    @Autowired
    private CosServerProperties   serverProperties;
    
    @Autowired
    private CosTmpKeyProperties   keyProperties;
    
    @Autowired
    private CosTmpTokenCreator   tokenCreator;

    public static String          secretId  = "id";
    public static String          secretKey = "key";


    @Test
    public void test() throws Exception {
        
        System.out.println(tokenCreator);
        CosTmpKeyPolicy keyPolicy = tokenCreator.getAvailablePolicy();
        System.out.println(keyPolicy);
        System.out.println(keyProperties);
        System.out.println(serverProperties);
        System.out.println(Arrays.asList(keyPolicy.getDownloadDirs("123456")));
        System.out.println(Arrays.asList(keyPolicy.getUploadDirs("123456")));
        
        System.out.println("afterProperties,secret id:" + serverProperties.getSecretId());
        System.out.println("afterProperties,secret key:" + serverProperties.getSecretKey());
        assertEquals(secretId, serverProperties.getSecretId());
        assertEquals(secretKey, serverProperties.getSecretKey());
        assertNotNull(tokenCreator);
        assertNotNull(keyPolicy);
        assertNotNull(keyProperties);
        assertNotNull(serverProperties);
    }

}

@Configuration
class CosConfig {

    @Bean
    public CosServerProperties cosServerProperties() {
        CosServerProperties serverProperties = new CosServerProperties();
        String[] texts = CodecUtils.genKey(CosTmpKeyConfigTest.secretId, CosTmpKeyConfigTest.secretKey);
        String publicKey = texts[0];
        String secretIdEncrypt = texts[1];
        String secretKeyEncrypt = texts[2];
        serverProperties.setSecretId(secretIdEncrypt);
        serverProperties.setSecretKey(secretKeyEncrypt);
        serverProperties.setDecryptEnabled(true);
        serverProperties.setDecryptPublicKey(publicKey);
        System.out.println("property secret id:" + serverProperties.getSecretId());
        System.out.println("property secret key:" + serverProperties.getSecretKey());
        return serverProperties;
    }
    
    @Bean
    public CosTmpKeyProperties cosTmpKeyProperties(CosServerProperties cosServerProperties){
        CosTmpKeyProperties cosTmpKeyProperties = new CosTmpKeyProperties();
        cosTmpKeyProperties.setServerProperties(cosServerProperties);
        cosTmpKeyProperties.setUserIdSessionAttribute("userId");
        return cosTmpKeyProperties;
    }
    
    @Bean
    public CosTmpTokenCreator tokenCreator(CosTmpKeyProperties keyProperties,CosTmpKeyPolicy keyPolicy){
        CosTmpTokenCreator tokenCreator = new CosTmpTokenCreator();
        tokenCreator.setKeyPolicy(keyPolicy);
        tokenCreator.setKeyProperties(keyProperties);
        return tokenCreator;
    }

    @Bean
    public CosTmpKeyPolicy cosTmpKeyPolicy() {
        return new CosTmpKeyPolicy() {

            @Override
            public String getUserId(HttpServletRequest req) {
                return (String) req.getSession().getAttribute("userId");
            }

            @Override
            public String[] getUploadDirs(String userId) {
                return new String[] { userId + "/*" };
            }

            @Override
            public String[] getDownloadDirs(String userId) {
                return new String[] { "*" };
            }

            @Override
            public boolean ensureSecure(HttpServletRequest req) {
                return req.getSession().getAttribute("userId") != null;
            }
        };
    }

}
