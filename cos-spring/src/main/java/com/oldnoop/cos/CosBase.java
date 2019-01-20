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

import com.qcloud.Module.Base;

/**
 * 
 * @author oldnoop
 * @date 2019-01-18
 *
 */
public class CosBase extends Base {
	
	public CosBase(){
		this.serverHost = "sts.api.qcloud.com";
	}
	
	public String getServerHost(){
		return this.serverHost;
	}
	
	public String getServerUri(){
		return this.serverUri;
	}

	public void setServerHost(String serverHost){
		this.serverHost = serverHost;
	}
	
	public void setServerUri(String serverUri){
		this.serverUri = serverUri;
	}
	
}
