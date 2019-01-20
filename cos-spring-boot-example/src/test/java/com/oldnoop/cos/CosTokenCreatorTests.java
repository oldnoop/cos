package com.oldnoop.cos;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oldnoop.CosApplicationTests;
import com.oldnoop.cos.tmpkey.CosTmpTokenCreator;
import com.qcloud.Utilities.Json.JSONObject;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=CosApplicationTests.class)
public class CosTokenCreatorTests {
	
	@Autowired
	private CosTmpTokenCreator creator;

	@Test
	public void testGenCosToken() throws Exception{
		String userId = "123458";
		String fileName = "1.txt";
		JSONObject token = creator.genCosToken(userId);
		System.out.println(token);
		JSONObject credentials = (JSONObject) token.get("credentials");
		/**
		 * 
		 {
			durationSeconds = 3600, 
			name = cos-sts-java, 
			policy = {
				"statement": [{
					"resource": ["qcs::cos:ap-beijing:uid/1234567567:prefix//1234567567/example/123456/*"],
					"effect": "allow",
					"action": ["name/cos:PutObject", "name/cos:InitiateMultipartUpload", "name/cos:ListParts", "name/cos:UploadPart", "name/cos:CompleteMultipartUpload", "name/cos:AbortMultipartUpload"]
				}, {
					"resource": ["qcs::cos:ap-beijing:uid/1234567567:prefix//1234567567/example/*"],
					"effect": "allow",
					"action": ["name/cos:GetObject"]
				}],
				"version": "2.0"
			}
		}
		 
		 {
			"credentials": {
				"tmpSecretId": "AKIDMFtFMAuTb7OK70516tmNTRC2YbuGa2I0",
				"tmpSecretKey": "V66FcxZgjOoWEq0fP34OXmjBzheesI2M",
				"sessionToken": "3fd39aa3d63d2a7976e65c88a6ffcd02874dc7ac30001"
			},
			"startTime": 1547538271,
			"expiredTime": 1547540071
		}
		 */
		String tmpSecretId = (String) credentials.get("tmpSecretId");
		String tmpSecretKey = (String) credentials.get("tmpSecretKey");
		String sessionToken = (String) credentials.get("sessionToken");
		
		// 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(tmpSecretId, tmpSecretKey);
        // 2 设置 bucket 区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region(creator.getKeyProperties().getServerProperties().getRegion()));
        // 3 生成cos客户端
        COSClient cosclient = new COSClient(cred, clientConfig);
        // bucket名需包含appid
        String bucketName = creator.getKeyProperties().getServerProperties().getBucket();

        String key = userId + "/" + fileName;
        // 上传 object, 建议 20M 以下的文件使用该接口
		String path = CosTokenCreatorTests.class.getPackage().getName().replaceAll("\\.", "/");
//		path = CosTokenCreatorTest.class.getResource("/").toURI().getPath() + path + "/1.txt";
		path = CosTokenCreatorTests.class.getResource("/").toURI().getPath() + path + "/" + fileName;
        System.out.println(path);
		File localFile = new File(path);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);

        // 设置 x-cos-security-token header 字段
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setSecurityToken(sessionToken);
        putObjectRequest.setMetadata(objectMetadata);

        try {
            PutObjectResult putObjectResult = cosclient.putObject(putObjectRequest);
            // putobjectResult会返回文件的etag
            String etag = putObjectResult.getETag();
            System.out.println(etag);
        } catch (Exception e) {
            e.printStackTrace();
        } 

        // 关闭客户端
        cosclient.shutdown();
	}

}
