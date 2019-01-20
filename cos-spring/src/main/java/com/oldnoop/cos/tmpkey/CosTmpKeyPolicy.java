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
 * 临时密钥采用的策略
 * @author oldnoop
 * @date 2019-01-18
 */
public interface CosTmpKeyPolicy {
	
	/**
	 * 获取用户信息
	 * @param req
	 * @return
	 */
	String getUserId(HttpServletRequest req);
	
	/**
	 * 验证安全,判断用户是否可以 请求后端获取临时token
	 * @param req
	 * @return
	 */
	boolean ensureSecure(HttpServletRequest req);

	/**
	 * 根据用户id获取上传的目录
	 * @param userId
	 * @return
	 */
	String[] getUploadDirs(String userId);
	
	/**
	 * 根据用户id获取下载的目录
	 * @param userId
	 * @return
	 */
	String[] getDownloadDirs(String userId);
	
}
