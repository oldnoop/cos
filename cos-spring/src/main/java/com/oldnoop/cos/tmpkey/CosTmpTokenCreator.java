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

import java.util.TreeMap;

import com.oldnoop.cos.CosBase;
import com.oldnoop.cos.CosServerProperties;
import com.qcloud.QcloudApiModuleCenter;
import com.qcloud.Utilities.Json.JSONArray;
import com.qcloud.Utilities.Json.JSONObject;

/**
 * <pre>
 * COS 临时token生成器
 * 
 * 核心是组装 COS API Policy
 * COS API 授权策略（policy）是一种 json 字符串。
 * 示例:授予 APPID 为1253653367,地域为ap-beijing的
 * 存储桶为example-1253653367, 
 * 路径前缀为123456(用户id)的上传操作权限,路径前缀为*的下载操作权限
 * 的策略内容示例：
{
  "version": "2.0",
  "statement": [
    {
      "action": [
        // 简单上传
        "name/cos:PutObject",
        // 分片上传
        "name/cos:InitiateMultipartUpload",
        "name/cos:ListParts",
        "name/cos:UploadPart",
        "name/cos:CompleteMultipartUpload",
        "name/cos:ListMultipartUploads",
        "name/cos:AbortMultipartUpload"
      ],
      "effect": "allow",
      "resource": [
        "qcs::cos:ap-beijing:uid/1253653367:prefix//1253653367/example/123456/*"
      ]
    },
    {
      "action": [
        "name/cos:GetObject"
      ],
      "effect": "allow",
      "resource": [
        "qcs::cos:ap-beijing:uid/1253653367:prefix//1253653367/example/*"
      ]
    }
  ]
}
 * </pre>
 * @author oldnoop
 * @date 2019-01-18
 */
public class CosTmpTokenCreator {

	private CosTmpKeyProperties keyProperties;
	
	/**
     * 采用的临时密钥策略
     */
	private CosTmpKeyPolicy keyPolicy;
	
	/**
	 * 默认的临时密钥策略
	 */
	private CosTmpKeyPolicy defaultKeyPolicy;
	
	/**
	 * 根据用户id生成临时token数据
	 * @param userId
	 * @return
	 */
	public JSONObject genCosToken(String userId) {
	    
	    CosServerProperties serverProperties = keyProperties.getServerProperties();
		
		String secretId = serverProperties.getSecretId();
        String secretKey = serverProperties.getSecretKey();
        
        Integer durationSeconds = keyProperties.getDurationSeconds();
        
        String region = serverProperties.getRegion();
        
        String bucket = serverProperties.getBucket();
        
        String appId = serverProperties.getAppId();
        
        String bucketShortName = serverProperties.getBucketShortName();
        
        CosTmpKeyPolicy actualKeyPolicy = getAvailablePolicy();
        
        String[] uploadDirs = actualKeyPolicy.getUploadDirs(userId);
        
        String[] downloadDirs = actualKeyPolicy.getDownloadDirs(userId);
        
        TreeMap<String, Object> config = new TreeMap<String, Object>();
        
        config.put("RequestMethod", "GET");
        
//        config.put("allowPrefix", "*");
//        config.put("allowActions", "*");
        
        // 固定密钥
        config.put("SecretId", secretId);
        // 固定密钥
        config.put("SecretKey", secretKey);
        // 临时密钥有效时长，单位是秒
        config.put("durationSeconds", durationSeconds);
        // 换成你的 bucket
        config.put("bucket", bucket);
        // 换成 bucket 所在地区
        config.put("region", region);
        
        QcloudApiModuleCenter module = new QcloudApiModuleCenter(new CosBase(), config);

        
        JSONObject policy = new JSONObject();
        
        policy.put("version", "2.0");

        JSONArray statements = new JSONArray();
        policy.put("statement", statements);
        
        //上传statement
        JSONObject uploadStatement = new JSONObject();
        statements.put(uploadStatement);
        //上传statement,action
        JSONArray uploadActions = new JSONArray();
        for (String action : keyProperties.getUploadActions()) {
        	uploadActions.put(action);
        }
        uploadStatement.put("action", uploadActions);
        //上传statement,effect
        uploadStatement.put("effect", "allow");
        //上传statement,resource
        JSONArray uploadResources = new JSONArray();
        uploadStatement.put("resource", uploadResources);
        for(String dir : uploadDirs){
        	String resource = String.format("qcs::cos:%s:uid/%s:prefix//%s/%s/%s",
                    region, appId, appId, bucketShortName, dir);
        	uploadResources.put(resource);
        }
        
        //下载statement
        JSONObject downloadStatement = new JSONObject();
        statements.put(downloadStatement);
        //上传statement,action
        JSONArray downloadActions = new JSONArray();
        for (String action : keyProperties.getDownloadActions()) {
        	downloadActions.put(action);
        }
        downloadStatement.put("action", downloadActions);
        //上传statement,effect
        downloadStatement.put("effect", "allow");
        //上传statement,resource
        JSONArray downloadResources = new JSONArray();
        downloadStatement.put("resource", downloadResources);
        for(String dir : downloadDirs){
        	String resource = String.format("qcs::cos:%s:uid/%s:prefix//%s/%s/%s",
                    region, appId, appId, bucketShortName, dir);
        	downloadResources.put(resource);
        }
        
        TreeMap<String, Object> params = new TreeMap<String, Object>();
        
        params.put("name", "cos-sts-java");
        params.put("policy", policy.toString());
        params.put("durationSeconds", durationSeconds);
        
        try {
            String result = module.call("GetFederationToken", params);
            JSONObject jsonResult = new JSONObject(result);
            JSONObject data = jsonResult.optJSONObject("data");
            if (data == null) {
                data = jsonResult;
            }
            if(data.has("expiredTime")){
	            long expiredTime = data.getLong("expiredTime");
	            data.put("startTime", expiredTime - durationSeconds);
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("生成临时密钥失败");
        }
	}
	
	/**
	 * 获取可用的CosTmpKeyPolicy
	 * @return
	 */
    public CosTmpKeyPolicy getAvailablePolicy() {
        if (keyPolicy != null) {
            return keyPolicy;
        }
        return defaultKeyPolicy;
    }

    public CosTmpKeyProperties getKeyProperties() {
        return keyProperties;
    }
    
    public void setKeyProperties(CosTmpKeyProperties keyProperties) {
        this.keyProperties = keyProperties;
        if (keyProperties.getUserIdSessionAttribute() != null) {
            this.defaultKeyPolicy = new CosTmpKeyPolicyDefault().setUserIdSessionAttribute(keyProperties.getUserIdSessionAttribute());
        }
    }
    
    public CosTmpKeyPolicy getKeyPolicy() {
		return keyPolicy;
	}

	public void setKeyPolicy(CosTmpKeyPolicy keyPolicy) {
		this.keyPolicy = keyPolicy;
	}

    public CosTmpKeyPolicy getDefaultKeyPolicy() {
        return defaultKeyPolicy;
    }

    public void setDefaultKeyPolicy(CosTmpKeyPolicy defaultKeyPolicy) {
        this.defaultKeyPolicy = defaultKeyPolicy;
    }
	
}
