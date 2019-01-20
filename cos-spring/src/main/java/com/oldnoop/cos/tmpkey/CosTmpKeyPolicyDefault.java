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

import javax.servlet.http.HttpServletRequest;

/**
 * 默认的临时密钥策略类,需要读取session中保存的登录用户的userId的数据名称,判断是否有访问权限
 * @author oldnoop
 * @date 2019年1月19日
 */
public class CosTmpKeyPolicyDefault implements CosTmpKeyPolicy {
    
    private String userIdSessionAttribute = USER_ID_SESSION_ATTRIBUTE_DEFAULT;
    
    private static final String USER_ID_SESSION_ATTRIBUTE_DEFAULT="userId";
    
    @Override
    public String getUserId(HttpServletRequest req) {
        return (String) req.getSession().getAttribute(userIdSessionAttribute);
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
        return req.getSession().getAttribute(userIdSessionAttribute) != null;
    }
    
    public String getUserIdSessionAttribute() {
        return userIdSessionAttribute;
    }

    public CosTmpKeyPolicyDefault setUserIdSessionAttribute(String userIdSessionAttribute) {
        this.userIdSessionAttribute = userIdSessionAttribute;
        return this;
    }
    
}
