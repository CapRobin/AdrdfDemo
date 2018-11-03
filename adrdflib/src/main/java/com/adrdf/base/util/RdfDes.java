
package com.adrdf.base.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
/**
 * Copyright © CapRobin
 *
 * Name：RdfDes
 * Describe：DES加解密算法类
 * Date：2018-06-27 11:03:59
 * Author: CapRobin@yeah.net
 *
 */
public class RdfDes {

	private byte[] iv;

	public RdfDes(byte[] iv) {
		super();
		this.iv = iv;
	}

	public static RdfDes newInstance(byte[] iv) {
		RdfDes des = new RdfDes(iv);
		return des;
	}

	public String encrypt(byte[] encryptByte, String encryptKey) {
		try {
			IvParameterSpec zeroIv = new IvParameterSpec(iv);
			SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
			byte[] encryptedData = cipher.doFinal(encryptByte);
			return RdfBase64.encode(encryptedData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] decrypt(String encryptString, String encryptKey) {
		try {
			byte[] encryptByte = RdfBase64.decode(encryptString);
			IvParameterSpec zeroIv = new IvParameterSpec(iv);
			SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
			return cipher.doFinal(encryptByte);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
    	RdfDes des = RdfDes.newInstance("yxs!1sdf".getBytes());
    	String a = des.encrypt("||||||863920023221158||9c:a9:e4:3b:a1:8a".getBytes(),"bywhjgpt");
    	System.out.println(a);
		System.out.println(des.decrypt(a,"bywhjgpt"));
	}
}