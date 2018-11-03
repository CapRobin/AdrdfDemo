package com.adrdf.base.util;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
/**
 * Copyright © CapRobin
 *
 * Name：RdfSha256
 * Describe：sha256工具类
 * Date：2017-06-27 11:08:55
 * Author: CapRobin@yeah.net
 *
 */
public class RdfSha256 {

    /**
     * sha256 加密
     */
    public static String sha256(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes("UTF-8"));
            return new String(Hex.encodeHex(hash));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
