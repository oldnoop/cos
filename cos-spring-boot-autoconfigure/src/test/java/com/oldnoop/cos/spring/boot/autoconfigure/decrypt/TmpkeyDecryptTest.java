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

package com.oldnoop.cos.spring.boot.autoconfigure.decrypt;

import static org.junit.Assert.*;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.oldnoop.cos.CosServerProperties;
import com.oldnoop.cos.tmpkey.CosTmpKeyPolicy;
import com.oldnoop.cos.tmpkey.CosTmpKeyProperties;
import com.oldnoop.cos.tmpkey.CosTmpTokenCreator;

/**
 * @author oldnoop
 * @date 2019年1月18日
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CosApplicationDecrypt.class)
@ActiveProfiles(profiles="decrypt")
public class TmpkeyDecryptTest {
    
    @Autowired
    private CosServerProperties serverProperties;
    
    @Autowired
    private CosTmpKeyProperties keyProperties;
    
    @Autowired
    private CosTmpTokenCreator tokenCreator;
    
    @Value("${spring.cos.oldnoop.server.secret-id}")
    private String secretId;
    
    @Value("${spring.cos.oldnoop.server.secret-key}")
    private String secretKey;
    
    @Value("${spring.cos.oldnoop.server.decrypt-enabled}")
    private boolean decryptEnabled;
    
    @Value("${spring.cos.oldnoop.server.decrypt-public-key}")
    private String decryptPublicKey;
    
    @Test
    public void test() throws Exception {
        System.out.println(serverProperties);
        System.out.println(keyProperties);
        System.out.println(tokenCreator);
        CosTmpKeyPolicy keyPolicy = tokenCreator.getAvailablePolicy();
        System.out.println(keyPolicy);
        System.out.println(Arrays.asList(keyPolicy.getDownloadDirs("123456")));
        System.out.println(Arrays.asList(keyPolicy.getUploadDirs("123456")));
        System.out.println("property,secret id:" + secretId);
        System.out.println("property,secret key:" + secretKey);
        System.out.println("property,decryptEnabled:" + decryptEnabled);
        System.out.println("property,decryptPublicKey:" + decryptPublicKey);
        System.out.println("afterProperties,secret id:" + serverProperties.getSecretId());
        System.out.println("afterProperties,secret key:" + serverProperties.getSecretKey());
        assertNotNull(serverProperties);
        assertNotNull(keyProperties);
        assertNotNull(keyPolicy);
        assertNotNull(keyProperties.getServerProperties());
        assertNotNull(tokenCreator);
        assertNotNull(tokenCreator.getKeyProperties());
        assertNotNull(tokenCreator.getAvailablePolicy());
        assertEquals("id", serverProperties.getSecretId());
        assertEquals("key", serverProperties.getSecretKey());
    }

}

@SpringBootApplication 
class CosApplicationDecrypt {
    
    @Bean
    @ConditionalOnMissingBean
    public CosTmpKeyPolicy keyPolicy(){
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

    public static void main(String[] args) {
        SpringApplication.run(CosApplicationDecrypt.class, args);
    }
}