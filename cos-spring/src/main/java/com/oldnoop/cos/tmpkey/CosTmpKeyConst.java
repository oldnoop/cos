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


/**
 * @author oldnoop
 * @date 2019年1月18日
 */
public interface CosTmpKeyConst {

    public static final String PROP_VERSION="version";
    public static final String PROP_SECRET_ID="secretId";
    public static final String PROP_SECRET_KEY="secretKey";
    public static final String PROP_DECRYPT_ENABLED="decryptEnabled";
    public static final String PROP_DECRYPT_PUBLIC_KEY="decryptPublicKey";
    public static final String PROP_DURATION_SECONDS="durationSeconds";
    public static final String PROP_REGION="region";
    public static final String PROP_BUCKET="bucket";
    public static final String PROP_UPLOAD_ACTION="uploadActions";
    public static final String PROP_DOWNLOAD_ACTION="downloadActions";
    public static final String PROP_TOKEN_CREATOR_CLASS="tokenCreatorClass";
    public static final String PROP_TOKEN_CREATOR_NAME="tokenCreatorName";
    public static final String PROP_TOKEN_CREATOR="tokenCreator";
    public static final String PROP_KEY_POLICY="keyPolicy";
    public static final String PROP_USER_ID_SESSION_ATTRIBUTE="userIdSessionAttribute";
}
