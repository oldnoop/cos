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

/**
 * @author oldnoop
 * @date 2019年1月19日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CosConfigKeyPolicyDefault.class)
@WebAppConfiguration
@PropertySource("coskey.properties")
public class CosTmpKeyPolicyDefaultTest {

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
        System.out.println(Arrays.asList(keyPolicy.getDownloadDirs("123456")));
        System.out.println(Arrays.asList(keyPolicy.getUploadDirs("123456")));
        
        assertNotNull(tokenCreator);
        assertNotNull(keyPolicy);
        assertNotNull(keyProperties);
    }

}

@Configuration
class CosConfigKeyPolicyDefault {

    @Bean
    public CosServerProperties cosServerProperties() {
        CosServerProperties serverProperties = new CosServerProperties();
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
    public CosTmpTokenCreator tokenCreator(CosTmpKeyProperties keyProperties){
        CosTmpTokenCreator tokenCreator = new CosTmpTokenCreator();
        tokenCreator.setKeyProperties(keyProperties);
        return tokenCreator;
    }

}
