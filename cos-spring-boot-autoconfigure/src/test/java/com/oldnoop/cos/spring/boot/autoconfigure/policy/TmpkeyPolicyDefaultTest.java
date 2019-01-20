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

package com.oldnoop.cos.spring.boot.autoconfigure.policy;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
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
@SpringBootTest(classes = CosApplicationPolicyDefault.class)
public class TmpkeyPolicyDefaultTest {
    
    @Autowired
    private CosServerProperties serverProperties;
    
    @Autowired
    private CosTmpKeyProperties keyProperties;
    
    @Autowired
    private CosTmpTokenCreator tokenCreator;
    
    @Test
    public void test() throws Exception {
        
        CosTmpKeyPolicy keyPolicy = tokenCreator.getAvailablePolicy();
        assertNotNull(serverProperties);
        assertNotNull(keyProperties);
        assertNotNull(keyProperties.getServerProperties());
        assertNotNull(tokenCreator);
        assertNotNull(tokenCreator.getKeyProperties());
        assertNotNull(keyPolicy);
        System.out.println(serverProperties);
        System.out.println(keyProperties);
        System.out.println(tokenCreator);
        System.out.println(keyPolicy);
        System.out.println(Arrays.asList(keyPolicy.getDownloadDirs("123456")));
        System.out.println(Arrays.asList(keyPolicy.getUploadDirs("123456")));
    }

}

@SpringBootApplication 
class CosApplicationPolicyDefault {
    
    public static void main(String[] args) {
        SpringApplication.run(CosApplicationPolicyDefault.class, args);
    }
}
