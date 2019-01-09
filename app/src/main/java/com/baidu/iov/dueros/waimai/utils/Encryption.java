package com.baidu.iov.dueros.waimai.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class Encryption {
    private static final String key = "HkK1fEMbCLPqGuk4";
    private static final String iv = "7RCB2V4ylqZJBoCu";


    /**
     * jie mi
     * @param data
     * @return
     * @throws Exception
     */
    public static String desEncrypt(String data) throws Exception {
        byte[] encrypted = new BASE64Decoder().decodeBuffer(data);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
        byte[] original = cipher.doFinal(encrypted);
        String originalString = new String(original);
        return originalString;
    }


    /**
     * jia mi
     * @param value
     * @return
     */
    public static String encrypt(String value) {
        try {
            IvParameterSpec ivs = new IvParameterSpec(iv.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivs);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return new BASE64Encoder().encode(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;

    }
}

