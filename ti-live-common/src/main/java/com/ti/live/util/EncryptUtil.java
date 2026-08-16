package com.ti.live.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class EncryptUtil {

    private static final String ALGORITHM_AES = "AES";
    private static final String ALGORITHM_AES_GCM = "AES/GCM/NoPadding";
    private static final String ALGORITHM_MD5 = "MD5";
    private static final String ALGORITHM_SHA256 = "SHA-256";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;

    /**
     * AES加密
     * @param data 明文数据
     * @param key 密钥（16/24/32位）
     * @return Base64编码的密文
     */
    public static String aesEncrypt(String data, String key) {
        try {
            byte[] iv = new byte[GCM_IV_LENGTH];
            new SecureRandom().nextBytes(iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM_AES_GCM);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM_AES);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);

            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

            ByteBuffer buffer = ByteBuffer.allocate(iv.length + encrypted.length);
            buffer.put(iv);
            buffer.put(encrypted);

            return Base64.getEncoder().encodeToString(buffer.array());
        } catch (Exception e) {
            throw new RuntimeException("AES加密失败", e);
        }
    }

    /**
     * AES解密
     * @param encryptedData Base64编码的密文
     * @param key 密钥
     * @return 明文数据
     */
    public static String aesDecrypt(String encryptedData, String key) {
        try {
            byte[] decoded = Base64.getDecoder().decode(encryptedData);
            ByteBuffer buffer = ByteBuffer.wrap(decoded);

            byte[] iv = new byte[GCM_IV_LENGTH];
            buffer.get(iv);
            byte[] encrypted = new byte[buffer.remaining()];
            buffer.get(encrypted);

            Cipher cipher = Cipher.getInstance(ALGORITHM_AES_GCM);
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM_AES);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);

            byte[] decrypted = cipher.doFinal(encrypted);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("AES解密失败", e);
        }
    }

    /**
     * MD5加密
     * @param data 原始数据
     * @return MD5十六进制字符串
     */
    public static String md5(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM_MD5);
            byte[] hash = md.digest(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5加密失败", e);
        }
    }

    /**
     * SHA256加密
     * @param data 原始数据
     * @return SHA256十六进制字符串
     */
    public static String sha256(String data) {
        try {
            MessageDigest sha = MessageDigest.getInstance(ALGORITHM_SHA256);
            byte[] hash = sha.digest(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA256加密失败", e);
        }
    }

    /**
     * SHA256加盐加密
     * @param data 原始数据
     * @param salt 盐值
     * @return 加盐后的SHA256十六进制字符串
     */
    public static String sha256WithSalt(String data, String salt) {
        return sha256(data + salt);
    }

    /**
     * 生成随机密钥
     * @param keySize 密钥长度（128/192/256）
     * @return Base64编码的密钥
     */
    public static String generateAESKey(int keySize) {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM_AES);
            keyGen.init(keySize);
            SecretKey secretKey = keyGen.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (Exception e) {
            throw new RuntimeException("生成AES密钥失败", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
