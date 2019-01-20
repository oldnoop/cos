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

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.oldnoop.cos.tmpkey.CosTmpKeyProperties;

/**
 * @author oldnoop
 * @date 2019年1月18日
 */
@ConfigurationProperties(prefix="spring.cos.oldnoop.tmpkey")
public class CosTmpkeyPropertiesWapper extends CosTmpKeyProperties{

}
