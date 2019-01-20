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

package com.oldnoop.cos.util;

import java.util.Properties;

import javax.servlet.ServletContext;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.context.support.ServletContextResource;

/**
 * @author oldnoop
 * @date 2019年1月18日
 */
public final class PropertyUtils {
    
    private static final String RESOURCE_PREFIX_CLASSPATH = "classpath:";
    private static final String RESOURCE_PREFIX_FILE = "file:";

    public static Properties loadProperties(String configFile,ServletContext sc){
        Resource resource = null;
        if(configFile.startsWith(RESOURCE_PREFIX_CLASSPATH)){
            resource = new ClassPathResource(configFile.substring(RESOURCE_PREFIX_CLASSPATH.length()));
            
        }else if(configFile.startsWith("file:")){
            resource = new FileSystemResource(configFile.substring(RESOURCE_PREFIX_FILE.length()));
        }else{
            resource = new ServletContextResource(sc, configFile);
        }
        try {
            Properties props = PropertiesLoaderUtils.loadProperties(resource);
            return props;
        } catch (Exception e) {
           return null;
        }
    }
}
