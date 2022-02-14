package com.botw.utils;

import java.beans.Encoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;


public class Password {

	public static final char[] PASSWORD="enfldsgbnlsngdlksdsgm".toCharArray();
	
	public static final byte[] SALT= {
			(byte)0xde,(byte)0x33,(byte)0x10, (byte) 0x12,
			(byte)0xde,(byte)0x33,(byte)0x10, (byte)0x12
			};
	
	
	public static void main(String[] args) {

	}
	
	public static byte[] encrypt(String property) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException {
		SecretKeyFactory keyfactory= SecretKeyFactory.getInstance("PBEWithMD5AndDES");
		SecretKey key=keyfactory.generateSecret(new PBEKeySpec(PASSWORD));
		Cipher  pbeCipher = Cipher.getInstance("PBEWithMD5AndDES"); 
		pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT,20));
		return base64Encode(pbeCipher.doFinal(property.getBytes("UTF-8")));
	}

	public static byte[] base64Encode(byte[] bytes) {
		return new Base64.Encoder().encode(bytes);
	}
	
}
