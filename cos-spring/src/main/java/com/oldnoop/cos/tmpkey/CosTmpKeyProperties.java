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

import com.oldnoop.cos.CosServerProperties;

/**
 * 临时密钥信息类
 * @author oldnoop
 * @date 2019-01-18
 */
public class CosTmpKeyProperties {
    
    private CosServerProperties serverProperties;
    
    /**
     * 前端请求本地服务端临时密钥的路径
     */
    private String keyApplyPath="/coskey";
    
    /**
     * 默认的临时密钥策略CosTmpKeyPolicyDefault使用的session中存储的登录用户的userId数据名称
     * 可选配置
     */
    private String userIdSessionAttribute;
    
    /**
     * 临时密钥的过期时间,单位秒
     */
    private Integer durationSeconds = 1800;

    /**
     * 上传允许的操作
     */
    private String[] uploadActions=DEFAULT_UPLOAD_ACTIONS;
    
    /**
     * 下载允许的操作
     */
    private String[] downloadActions=DEFAULT_DOWNLOAD_ACTIONS;
    
    public static final String[] DEFAULT_UPLOAD_ACTIONS = {
       // 简单上传
       "name/cos:PutObject",
       // 分片上传
       "name/cos:InitiateMultipartUpload",
       "name/cos:ListParts",
       "name/cos:UploadPart",
       "name/cos:CompleteMultipartUpload",
       "name/cos:ListMultipartUploads",
       "name/cos:AbortMultipartUpload"
   };
   
    public static final String[] DEFAULT_DOWNLOAD_ACTIONS = { "name/cos:GetBucket","name/cos:GetObject" };

    public CosServerProperties getServerProperties() {
        return serverProperties;
    }
    
    public void setServerProperties(CosServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    public String getKeyApplyPath() {
        return keyApplyPath;
    }

    public void setKeyApplyPath(String keyApplyPath) {
        this.keyApplyPath = keyApplyPath;
    }
    
    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }
    
    public void setDurationSeconds(String durationSeconds) {
        this.durationSeconds = Integer.valueOf(durationSeconds);
    }

    public String[] getUploadActions() {
        return uploadActions;
    }

    public void setUploadActions(String[] uploadActions) {
        this.uploadActions = uploadActions;
    }

    public void setUploadAction(String uploadActions) {
        if (uploadActions == null || "".equals(uploadActions.trim())) {
            return;
        }
        this.uploadActions = getActions(uploadActions);
    }

    public String[] getDownloadActions() {
        return downloadActions;
    }

    public void setDownloadActions(String[] downloadActions) {
        this.downloadActions = downloadActions;
    }

    public void setDownloadAction(String downloadActions) {
        if (downloadActions == null || "".equals(downloadActions.trim())) {
            return;
        }
        this.downloadActions = getActions(downloadActions);
    }
    
    public String getUserIdSessionAttribute() {
        return userIdSessionAttribute;
    }
    
    public void setUserIdSessionAttribute(String userIdSessionAttribute) {
        this.userIdSessionAttribute = userIdSessionAttribute;
    }

    private String[] getActions(String actions) {
        String[] arr = actions.split(",");
        String[] res = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[i].trim();
        }
        return res;
    }
}
