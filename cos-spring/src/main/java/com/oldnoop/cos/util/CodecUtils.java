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

package com.oldnoop.cos.util;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

/**
 * <pre>
 * 加密工具类,私钥加密,公钥解密
 * 命令行运行
 * java -cp cos-client-x.y.z.jar com.oldnoop.cos.util.CodecUtils 明文1 明文2 
 * 编写java类main方法,运行
 * public static void main(String[] args) throws Exception {
 *     CodecUtils.genKey("明文");
 * }
 * </pre>
 * 
 * @author oldnoop
 * @date 2019年1月18日
 */
public class CodecUtils {

    private static final String ALGORITHM_RSA = "RSA";

    public static void main(String[] args) throws Exception {
        if (args != null && args.length > 0) {
            genKey(args);
        } 
    }
    
    public static String[] genKey(String... texts) {
        return genKey(512, texts);
    }
    public static String[] genKey(int keySize, String... texts) {
        try {
            String[] arr = genKeyPair(512);
            System.out.println("-------------RSA private key,public key-----------------");
            System.out.println("privateKey:\n\t" + arr[0]);
            System.out.println("publicKey:\n\t" + arr[1]);
            System.out.println("-------------RSA generate encrypted String-----------------");
            List<String> list = new ArrayList<String>();
            list.add(arr[1]);
            for (String text : texts) {
                System.out.println("--------------------------------------");
                System.out.println("text:\n\t" + text);
                String textEncrypt = encrypt(arr[0], text);
                System.out.println("textEncrypt:\n\t" + textEncrypt);
                String textDecrypt = decrypt(arr[1], textEncrypt);
                System.out.println("textDecrypt:\n\t" + textDecrypt);
                list.add(textEncrypt);
            }
            return list.toArray(new String[list.size()]);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String publicKeyText, String cipherText) {
        try {
            byte[] publicKeyBytes = Base64.decodeBase64(publicKeyText);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);
            PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);

            Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            if (cipherText == null || cipherText.length() == 0) {
                return cipherText;
            }
            byte[] cipherBytes = Base64.decodeBase64(cipherText);
            byte[] plainBytes = cipher.doFinal(cipherBytes);
            return new String(plainBytes);
        } catch (Exception e) {
            return null;
        }
    }

    public static String encrypt(String privateKeyText, String plainText) throws Exception {
        try {
            byte[] keyBytes = Base64.decodeBase64(privateKeyText);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory factory = KeyFactory.getInstance(ALGORITHM_RSA);
            PrivateKey privateKey = factory.generatePrivate(spec);
            Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes("UTF-8"));
            String encryptedString = Base64.encodeBase64String(encryptedBytes);
            return encryptedString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String[] genKeyPair(int keySize) throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator gen = KeyPairGenerator.getInstance(ALGORITHM_RSA);
        gen.initialize(keySize, new SecureRandom());
        KeyPair pair = gen.generateKeyPair();
        String[] keyPairs = new String[2];
        keyPairs[0] = Base64.encodeBase64String(pair.getPrivate().getEncoded());
        keyPairs[1] = Base64.encodeBase64String(pair.getPublic().getEncoded());
        return keyPairs;
    }

}
