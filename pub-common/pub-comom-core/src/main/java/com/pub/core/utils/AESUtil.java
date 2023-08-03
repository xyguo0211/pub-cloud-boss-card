package com.pub.core.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

import javax.crypto.SecretKey;

public class AESUtil {

    /** 在线系统的密钥 AES加密 */
    private static final String STANDING_BOOK_KEY = "qpr6XejNJ9G6rFRTUeVaqQ==";
    /** 离线的密钥 AES加密 */
    private static final String TRACKING_KEY = "ZClj1ozIbubQzUgmO1F3ag==";

    public static String encrypt(String source, String base64Key) {
        byte[] key = Base64.decode(base64Key);
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
        return aes.encryptHex(source, CharsetUtil.CHARSET_UTF_8);
    }

    public static String decrypt(String source, String base64Key) {
        byte[] key = Base64.decode(base64Key);
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
        return aes.decryptStr(source, CharsetUtil.CHARSET_UTF_8);
    }

    public static String genBase64Key() {
        return genBase64Key(128);
    }

    public static String genBase64Key(Integer keySize) {
        SecretKey secretKey = SecureUtil.generateKey(SymmetricAlgorithm.AES.getValue(), keySize);
        return Base64.encode(secretKey.getEncoded());
    }

    /**
     * 加密 台账参数
     *
     * @param source 加密前的台账参数
     *
     * @return 加密后的台账参数
     */
    public static String encryptStandingBook(String source) {
        return encrypt(source, STANDING_BOOK_KEY);
    }

    /**
     * 解密 台账参数
     *
     * @param source 加密后的台账参数
     *
     * @return 解密后的台账参数
     */
    public static String decryptStandingBook(String source) {
        return decrypt(source, STANDING_BOOK_KEY);
    }

    /**
     * 加密 自动跟踪参数
     *
     * @param source 加密前的自动跟踪参数
     *
     * @return 加密后的自动跟踪参数
     */
    public static String encryptTracking(String source) {
        return encrypt(source, TRACKING_KEY);
    }

    /**
     * 解密 自动跟踪参数
     *
     * @param source 加密后的自动跟踪参数
     *
     * @return 解密后的自动跟踪参数
     */
    public static String decryptTracking(String source) {
        return decrypt(source, TRACKING_KEY);
    }

    public static void main(String[] args) {

        // 生成 KEY
        String key = genBase64Key(128);
        System.out.println(key);
        key = genBase64Key(128);
        System.out.println(key);
    }
}
