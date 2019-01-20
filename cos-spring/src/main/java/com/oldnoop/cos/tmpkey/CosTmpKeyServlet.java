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

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.oldnoop.cos.CosServerProperties;
import com.oldnoop.cos.util.PropertyUtils;
import com.qcloud.Utilities.Json.JSONObject;

import static com.oldnoop.cos.tmpkey.CosTmpKeyConst.*;

/**
 * 
<pre>
cos 临时密钥请求处理的控制器
 返回的token数据格式:
 {
    "credentials": {
        "tmpSecretId": "AKIDxxxxxx",
        "tmpSecretKey": "xxxxxx",
        "sessionToken": "xxxxxx"
    },
    "startTime": 1547538271,
    "expiredTime": 1547540071
}
</pre>
 * @author oldnoop
 * @date 2019-01-18
 */
public class CosTmpKeyServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * cos 临时密钥的配置文件*.properties或*.xml
     */
    private static final String PARAM_COS_CONFIG_LOCATION="cosConfigLocation";
    private static final String PARAM_TOKEN_CREATOR_CLASS="tokenCreatorClass";
    private static final String PARAM_TOKEN_CREATOR_NAME="tokenCreatorName";
    private static final String DEFAULT_PROPERTIES_COS_CONFIG_LOCATION="classpath:coskey.properties";
    private static final String DEFAULT_XML_COS_CONFIG_LOCATION="classpath:coskey.xml";
    
    private CosTmpTokenCreator creator;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json;charset=UTF-8");
        //临时密钥策略
        CosTmpKeyPolicy keyPolicy = creator.getAvailablePolicy();
        //验证请求是否合法
        if(!keyPolicy.ensureSecure(req)){
            resp.getWriter().write("{\"errorCode\":403,\"errorMessage\":\"禁止请求临时密钥\"}");
            return;
        }
        //获取用户id
        String userId = keyPolicy.getUserId(req);
        //生成临时密钥的token
        JSONObject token = creator.genCosToken(userId);
        //返回token的json数据
        resp.getWriter().write(token.toString());
    }

    @Override
    public void init(ServletConfig sc) throws ServletException {
        System.out.println("CosTmpKeyServlet init");
        
        // 如果creator不为null,通过注册servlet为spring的bean注入creator
        if (creator != null) {
            return;
        }
        // 如果creator为null,
        // 通过servlet的初始化参数contextConfigLocation配置的属性文件获取
        if((creator = getTokenCreatorFromProperties(sc))!=null){
            return;
        }
        // 通过servlet初始化参数tokenCreator配置的bean类型或tokenCreatorName配置的bean名称获取
        // tokenCreator配置CosTmpTokenCreator的java类型
        creator = getTokenCreatorFromSpring(sc);
    }
    
    /**
     * 通过spring容器获取
     * @param sc
     * @return
     */
    private CosTmpTokenCreator getTokenCreatorFromSpring(ServletConfig sc) {
        String tokenCreatorClassName = sc.getInitParameter(PARAM_TOKEN_CREATOR_CLASS);
        ApplicationContext atx = WebApplicationContextUtils.getWebApplicationContext(sc.getServletContext());
        if (atx == null) {
            return null;
        }
        CosTmpTokenCreator creator = null;
        if (tokenCreatorClassName != null && !tokenCreatorClassName.trim().equals("")) {
            try {
                Class<?> tokenCreatorClass = Class.forName(tokenCreatorClassName.trim());
                creator = (CosTmpTokenCreator) atx.getBean(tokenCreatorClass);
                return creator;
            } catch (Exception e) {
                String message = String.format("通过servlet初始化参数%s(%s)获取TokenCreator失败", PARAM_TOKEN_CREATOR_CLASS,
                                               tokenCreatorClassName);
                System.out.println(message);
            }
        }
        String tokenCreatorBeanName = sc.getInitParameter(PARAM_TOKEN_CREATOR_NAME);
        if (tokenCreatorBeanName != null && !tokenCreatorBeanName.trim().equals("")) {
            try {
                creator = (CosTmpTokenCreator) atx.getBean(tokenCreatorBeanName.trim());
                return creator;
            } catch (Exception e) {
                String message = String.format("通过servlet初始化参数%s(%s)获取TokenCreator失败", PARAM_TOKEN_CREATOR_NAME,
                                               tokenCreatorBeanName);
                System.out.println(message);
            }
        }
        return null;
    }
    
    /**
     * <pre>
     * 通过servlet的初始化参数,初始化CosTokenCreator
     * 需要配置servlet的初始化参数 
     * version
     * secretId
     * secretKey
     * durationSeconds,临时密钥失效时间
     * region,区域
     * bucket,存储桶
     * uploadActions,多值用逗号拼接
     * downloadActions,多值用逗号拼接
     * tokenCreator,CosTokenCreator类型
     * keyPolicy,CosTmpKeyPolicy类型
     * userIdSessionAttribute,如果使用默认的临时密钥策略类,需要配置在session中保存的登录用户的userId的数据名称
     * </pre>
     * @return
     */
    private CosTmpTokenCreator getTokenCreatorFromProperties(ServletConfig sc) {
        
        String configFile = sc.getInitParameter(PARAM_COS_CONFIG_LOCATION);

        Properties properties = null;

        // 尝试从配置的属性资源文件*.properties或*.xml加载
        if (configFile == null) {// 尝试默认的classpath:coskey.properties
            configFile = DEFAULT_PROPERTIES_COS_CONFIG_LOCATION;
        }
        properties = PropertyUtils.loadProperties(configFile, sc.getServletContext());
        if (properties == null) {// 尝试默认的classpath:coskey.xml
            configFile = DEFAULT_XML_COS_CONFIG_LOCATION;
            properties = PropertyUtils.loadProperties(configFile, sc.getServletContext());
        }
        if (properties == null) {
            return null;
        }

        String version = properties.getProperty(PROP_VERSION);
        String secretId = properties.getProperty(PROP_SECRET_ID);
        String secretKey = properties.getProperty(PROP_SECRET_KEY);

        String decryptEnabled = properties.getProperty(PROP_DECRYPT_ENABLED);
        String decryptPublicKey = properties.getProperty(PROP_DECRYPT_PUBLIC_KEY);

        String durationSeconds = properties.getProperty(PROP_DURATION_SECONDS);
        String region = properties.getProperty(PROP_REGION);
        String bucket = properties.getProperty(PROP_BUCKET);
        String uploadActions = properties.getProperty(PROP_UPLOAD_ACTION);
        String downloadActions = properties.getProperty(PROP_DOWNLOAD_ACTION);

        String tokenCreatorClass = properties.getProperty(PROP_TOKEN_CREATOR);
        String keyPolicyClass = properties.getProperty(PROP_KEY_POLICY);
        String userIdSessionAttribute = properties.getProperty(PROP_USER_ID_SESSION_ATTRIBUTE);

        CosTmpTokenCreator creator = null;
        CosTmpKeyPolicy keyPolicy = null;

        if (tokenCreatorClass != null) {
            try {
                Class<?> clazz = Class.forName(tokenCreatorClass);
                creator = (CosTmpTokenCreator) clazz.newInstance();
            } catch (Exception e) {
                String message = String.format("通过properties参数%s(%s)获取TokenCreator失败", PROP_TOKEN_CREATOR,
                                               tokenCreatorClass);
                System.out.println(message);
            }
        }

        if (creator == null) {
            System.out.println(String.format("没有配置properties参数%s,使用默认的%s", PROP_TOKEN_CREATOR,
                                             CosTmpTokenCreator.class.getName()));
            creator = new CosTmpTokenCreator();
        }

        CosTmpKeyProperties keyProperties = new CosTmpKeyProperties();
        CosServerProperties serverProperties = new CosServerProperties();
        keyProperties.setServerProperties(serverProperties);
        if (version != null) {
            serverProperties.setVersion(version);
        }
        if (secretId != null) {
            serverProperties.setSecretId(secretId);
        }
        if (secretKey != null) {
            serverProperties.setSecretKey(secretKey);
        }
        if (decryptEnabled != null) {
            serverProperties.setDecryptEnabled(Boolean.parseBoolean(decryptEnabled));
        }
        if (decryptPublicKey != null) {
            serverProperties.setDecryptPublicKey(decryptPublicKey);
        }
        if (region != null) {
            serverProperties.setRegion(region);
        }
        if (bucket != null) {
            serverProperties.setBucket(bucket);
        }
        if (durationSeconds != null) {
            keyProperties.setDurationSeconds(durationSeconds);
        }
        if (uploadActions != null) {
            keyProperties.setUploadAction(uploadActions);
        }
        if (downloadActions != null) {
            keyProperties.setDownloadAction(downloadActions);
        }
        if (userIdSessionAttribute != null) {
            keyProperties.setUserIdSessionAttribute(userIdSessionAttribute);
        }
        serverProperties.afterProperties();

        if (keyPolicyClass != null) {
            try {
                Class<?> clazz = Class.forName(keyPolicyClass);
                keyPolicy = (CosTmpKeyPolicy) clazz.newInstance();
            } catch (Exception e) {
                String message = String.format("通过properties参数%s(%s)获取KeyPolicy失败,使用默认的%s", PROP_KEY_POLICY,
                                               keyPolicyClass, CosTmpKeyPolicyDefault.class.getName());
                System.out.println(message);
            }
        }
        creator.setKeyProperties(keyProperties);
        if (keyPolicy != null) {
            creator.setKeyPolicy(keyPolicy);
        }
        return creator;
    }
    
    public CosTmpTokenCreator getCreator() {
        return creator;
    }

    public void setCreator(CosTmpTokenCreator creator) {
        this.creator = creator;
    }

}