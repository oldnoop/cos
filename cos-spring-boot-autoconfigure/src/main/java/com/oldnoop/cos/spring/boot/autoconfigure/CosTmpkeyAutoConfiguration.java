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

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.oldnoop.cos.CosServerProperties;
import com.oldnoop.cos.tmpkey.CosTmpKeyPolicy;
import com.oldnoop.cos.tmpkey.CosTmpKeyProperties;
import com.oldnoop.cos.tmpkey.CosTmpTokenCreator;
import com.oldnoop.cos.tmpkey.CosTmpKeyServlet;

/**
 * @author oldnoop
 * @date 2019年1月18日
 */
@Configuration
@ConditionalOnClass(CosTmpTokenCreator.class)
//@EnableConfigurationProperties({CosServerPropertiesWapper.class, CosTmpkeyPropertiesWapper.class})
public class CosTmpkeyAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties(prefix="spring.cos.oldnoop.server")
    public CosServerProperties cosServerProperties() {
        return new CosServerPropertiesWapper();
    }
    
    @Bean
    @ConditionalOnMissingBean
    @ConfigurationProperties(prefix="spring.cos.oldnoop.tmpkey")
    public CosTmpKeyProperties cosKeyProperties(CosServerProperties cosServerProperties) {
        CosTmpkeyPropertiesWapper cosTmpKeyPropertiesWapper = new CosTmpkeyPropertiesWapper();
        cosTmpKeyPropertiesWapper.setServerProperties(cosServerProperties);
        return cosTmpKeyPropertiesWapper;
    }
    
    @Autowired(required=false)
    private CosTmpKeyPolicy cosKeyPolicy;
    
    @Bean
    @ConditionalOnMissingBean
    //@ConditionalOnBean(CosTmpKeyPolicy.class)
    public CosTmpTokenCreator cosTokenCreator(CosTmpKeyProperties cosKeyProperties) {
        CosTmpTokenCreator tokenCreator = new CosTmpTokenCreator();
        tokenCreator.setKeyProperties(cosKeyProperties);
        tokenCreator.setKeyPolicy(cosKeyPolicy);
        return tokenCreator;
    }
    
    @Bean
    @ConditionalOnWebApplication
    public ServletRegistrationBean servletRegistrationBean(CosTmpTokenCreator tokenCreator) {
        CosTmpKeyServlet cosTmpKeyServlet = new CosTmpKeyServlet();
        cosTmpKeyServlet.setCreator(tokenCreator);
        return new ServletRegistrationBean(cosTmpKeyServlet, tokenCreator.getKeyProperties().getKeyApplyPath());
    }
    
}
