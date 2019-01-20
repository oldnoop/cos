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

package com.oldnoop.cos;

import javax.annotation.PostConstruct;

import com.oldnoop.cos.util.CodecUtils;

/**
 * <pre>
 * 
 COAS API 服务端信息数据类
 * 
 * </pre>
 * @author oldnoop
 * @date 2019-01-18
 */
public class CosServerProperties {
    
    @PostConstruct
    public void afterProperties(){
        // 处理加密secretId,secretKey的情况
        boolean decryptEnabled = this.isDecryptEnabled();
        String decryptPublicKey = this.getDecryptPublicKey();
        if (decryptEnabled) {
            secretId = CodecUtils.decrypt(decryptPublicKey, secretId);
            secretKey = CodecUtils.decrypt(decryptPublicKey, secretKey);
        }
    }
	
	/**
	 * 版本
	 */
	private String version="2.0";
	
	/**
	 * 申请临时密钥的操作需要的secretId
	 */
	private String secretId;
	
	/**
	 * 申请临时密钥的操作需要的secretKey
	 */
	private String secretKey;
	
	/**
	 * 是否开启secretId,secretKey加密
	 */
	private boolean decryptEnabled = false;
	
	/**
	 * 开启secretId,secretKey加密时,用到的publik key
	 */
	private String decryptPublicKey;
	
	/**
	 * 区域,比如:ap-beijing
	 */
	private String region;
	
	/**
	 * 存储桶xxx-xxx
	 */
	private String bucket;
	
	
	/**
	 * 腾讯云存储的用户id,存储桶bucket的第二部分
	 */
	private String appId;
	
	/**
	 * 存储桶名称,bucket的第一部分
	 */
	private String bucketShortName;
	
	public static final String DEFAULT_VERSION = "2.0";
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getSecretId() {
		return secretId;
	}

	public void setSecretId(String secretId) {
		this.secretId = secretId;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	
    public boolean isDecryptEnabled() {
        return decryptEnabled;
    }
    
    public void setDecryptEnabled(boolean decryptEnabled) {
        this.decryptEnabled = decryptEnabled;
    }
    
    public String getDecryptPublicKey() {
        return decryptPublicKey;
    }
    
    public void setDecryptPublicKey(String decryptPublicKey) {
        this.decryptPublicKey = decryptPublicKey;
    }

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.appId = bucket.substring(bucket.lastIndexOf("-") + 1);
		this.bucketShortName = bucket.substring(0, bucket.lastIndexOf("-"));
		this.bucket = bucket;
	}
	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getBucketShortName() {
		return bucketShortName;
	}

	public void setBucketShortName(String bucketShortName) {
		this.bucketShortName = bucketShortName;
	}
	
}
