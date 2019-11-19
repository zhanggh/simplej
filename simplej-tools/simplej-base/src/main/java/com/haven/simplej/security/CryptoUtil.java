package com.haven.simplej.security;

import com.haven.simplej.security.enums.*;
import com.vip.vjtools.vjkit.base.ExceptionUtil;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 支持HMAC-SHA1消息签名 及 DES/AES对称加密的工具类.
 * 
 * 支持Hex与Base64两种编码方式.
 */
public class CryptoUtil {



	/**
	 * 加密或解密无编码的原始字节数组, 返回无编码的字节数组结果.
	 *
	 * @param input 原始字节数组
	 * @param key 符合SecretKeyType要求的密钥
	 * @param mode Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
	 */
	private static byte[] doFinal(byte[] input, byte[] key, int mode,SecretKeyType secretKeyType,String algorithms) {
		try {
			SecretKey secretKey = new SecretKeySpec(key, secretKeyType.name());
			Cipher cipher = Cipher.getInstance(algorithms);
			cipher.init(mode, secretKey);
			return cipher.doFinal(input);
		} catch (GeneralSecurityException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * 加密或解密无编码的原始字节数组, 返回无编码的字节数组结果.
	 *
	 * @param input 原始字节数组
	 * @param key 符合SecretKeyType要求的密钥
	 * @param iv 初始向量
	 * @param mode Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
	 */
	private static byte[] doFinal(byte[] input, byte[] key, byte[] iv, int mode,SecretKeyType secretKeyType,String algorithms) {
		try {
			SecretKey secretKey = new SecretKeySpec(key, secretKeyType.name());
			IvParameterSpec ivSpec = new IvParameterSpec(iv);
			Cipher cipher = Cipher.getInstance(algorithms);
			cipher.init(mode, secretKey, ivSpec);
			return cipher.doFinal(input);
		} catch (GeneralSecurityException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}


	/**
	 * des加密
	 * @param input 原始字节数组
	 * @param key des秘钥
	 * @param algorithms 可选不同的加密工作模式/填充模式，具体见CipherAlgorithms枚举
	 * @return byte[] 加密结果
	 */
	public static byte[] desEncrypt(byte[] input, byte[] key,DESAlgorithms algorithms){
		return doFinal(input,key,Cipher.ENCRYPT_MODE,SecretKeyType.DES,algorithms.getValue());
	}


	/**
	 * des解密
	 * @param input 密文数组
	 * @param key des秘钥
	 * @param algorithms 可选不同的加密工作模式/填充模式，具体见CipherAlgorithms枚举
	 * @return byte[] 解密后明文结果
	 */
	public static byte[] desDecrypt(byte[] input, byte[] key,DESAlgorithms algorithms){
		return doFinal(input,key,Cipher.DECRYPT_MODE,SecretKeyType.DES,algorithms.getValue());
	}


	/**
	 * des加密
	 * @param input 原始字节数组
	 * @param key des秘钥
	 * @param iv 初始向量
	 * @param algorithms 可选不同的加密工作模式/填充模式，具体见CipherAlgorithms枚举
	 * @return byte[] 加密结果
	 */
	public static byte[] desEncrypt(byte[] input, byte[] key, byte[] iv,DESAlgorithms algorithms){
		return doFinal(input,key,iv,Cipher.ENCRYPT_MODE,SecretKeyType.DES,algorithms.getValue());
	}


	/**
	 * des解密
	 * @param input 密文数组
	 * @param key des秘钥
	 *  @param iv 初始向量
	 * @param algorithms 可选不同的加密工作模式/填充模式，具体见CipherAlgorithms枚举
	 * @return byte[] 解密后明文结果
	 */
	public static byte[] desDecrypt(byte[] input, byte[] key, byte[] iv,DESAlgorithms algorithms){
		return doFinal(input,key,iv,Cipher.DECRYPT_MODE,SecretKeyType.DES,algorithms.getValue());
	}


	/**
	 * aes加密
	 * @param input 原始字节数组
	 * @param key aes秘钥
	 * @param algorithms 可选不同的加密工作模式/填充模式，具体见CipherAlgorithms枚举
	 * @return byte[] 加密结果
	 */
	public static byte[] aesEncrypt(byte[] input, byte[] key,AESAlgorithms algorithms){
		return doFinal(input,key,Cipher.ENCRYPT_MODE,SecretKeyType.AES,algorithms.getValue());
	}


	/**
	 * aes解密
	 * @param input 密文数组
	 * @param key aes秘钥
	 * @param algorithms 可选不同的加密工作模式/填充模式，具体见CipherAlgorithms枚举
	 * @return byte[] 解密后明文结果
	 */
	public static byte[] aesDecrypt(byte[] input, byte[] key,AESAlgorithms algorithms){
		return doFinal(input,key,Cipher.DECRYPT_MODE,SecretKeyType.AES,algorithms.getValue());
	}


	/**
	 * aes加密
	 * @param input 原始字节数组
	 * @param key aes秘钥
	 * @param iv 初始向量
	 * @param algorithms 可选不同的加密工作模式/填充模式，具体见CipherAlgorithms枚举
	 * @return byte[] 加密结果
	 */
	public static byte[] aesEncrypt(byte[] input, byte[] key,byte[] iv,AESAlgorithms algorithms){
		return doFinal(input,key,iv,Cipher.ENCRYPT_MODE,SecretKeyType.AES,algorithms.getValue());
	}


	/**
	 * aes解密
	 * @param input 密文数组
	 * @param key aes秘钥
	 * @param iv 初始向量
	 * @param algorithms 可选不同的加密工作模式/填充模式，具体见CipherAlgorithms枚举
	 * @return byte[] 解密后明文结果
	 */
	public static byte[] aesDecrypt(byte[] input, byte[] key,byte[] iv,AESAlgorithms algorithms){
		return doFinal(input,key,iv,Cipher.DECRYPT_MODE,SecretKeyType.AES,algorithms.getValue());
	}


	/**
	 * des3加密
	 * @param input 原始字节数组
	 * @param key des3秘钥
	 * @param algorithms 可选不同的加密工作模式/填充模式，具体见CipherAlgorithms枚举
	 * @return byte[] 加密结果
	 */
	public static byte[] des3Encrypt(byte[] input, byte[] key,DESedeAlgorithms algorithms){
		return doFinal(input,key,Cipher.ENCRYPT_MODE,SecretKeyType.DESede,algorithms.getValue());
	}


	/**
	 * des3解密
	 * @param input 密文数组
	 * @param key des3秘钥
	 * @param algorithms 可选不同的加密工作模式/填充模式，具体见CipherAlgorithms枚举
	 * @return byte[] 解密后明文结果
	 */
	public static byte[] des3Decrypt(byte[] input, byte[] key,DESedeAlgorithms algorithms){
		return doFinal(input,key,Cipher.DECRYPT_MODE,SecretKeyType.DESede,algorithms.getValue());
	}


	/**
	 * des3加密
	 * @param input 原始字节数组
	 * @param key des3秘钥
	 * @param iv 初始向量
	 * @param algorithms 可选不同的加密工作模式/填充模式，具体见CipherAlgorithms枚举
	 * @return byte[] 加密结果
	 */
	public static byte[] des3Encrypt(byte[] input, byte[] key,byte[] iv,DESedeAlgorithms algorithms){
		return doFinal(input,key,iv,Cipher.ENCRYPT_MODE,SecretKeyType.DESede,algorithms.getValue());
	}


	/**
	 * des3解密
	 * @param input 密文数组
	 * @param key des3秘钥
	 * @param  iv 初始向量
	 * @param algorithms 可选不同的加密工作模式/填充模式，具体见CipherAlgorithms枚举
	 * @return byte[] 解密后明文结果
	 */
	public static byte[] des3Decrypt(byte[] input, byte[] key,byte[] iv,DESedeAlgorithms algorithms){
		return doFinal(input,key,iv,Cipher.DECRYPT_MODE,SecretKeyType.DESede,algorithms.getValue());
	}


	/**
	 * rc2加密
	 * @param input 原始字节数组
	 * @param key rc2秘钥
	 * @param algorithms 可选不同的加密工作模式/填充模式，具体见CipherAlgorithms枚举
	 * @return byte[] 加密结果
	 */
	public static byte[] rc2Encrypt(byte[] input, byte[] key,RCAlgorithms algorithms){
		return doFinal(input,key,Cipher.ENCRYPT_MODE,SecretKeyType.RC2,algorithms.getValue());
	}


	/**
	 * rc2解密
	 * @param input 密文数组
	 * @param key rc2秘钥
	 * @param algorithms 可选不同的加密工作模式/填充模式，具体见CipherAlgorithms枚举
	 * @return byte[] 解密后明文结果
	 */
	public static byte[] rc2Decrypt(byte[] input, byte[] key,RCAlgorithms algorithms){
		return doFinal(input,key,Cipher.DECRYPT_MODE,SecretKeyType.RC2,algorithms.getValue());
	}


	/**
	 * rc2加密
	 * @param input 原始字节数组
	 * @param key rc2秘钥
	 * @param iv 初始向量
	 * @param algorithms 可选不同的加密工作模式/填充模式，具体见CipherAlgorithms枚举
	 * @return byte[] 加密结果
	 */
	public static byte[] rc2Encrypt(byte[] input, byte[] key,byte[] iv,RCAlgorithms algorithms){
		return doFinal(input,key,iv,Cipher.ENCRYPT_MODE,SecretKeyType.RC2,algorithms.getValue());
	}


	/**
	 * rc2解密
	 * @param input 密文数组
	 * @param key rc2秘钥
	 * @param  iv 初始向量
	 * @param algorithms 可选不同的加密工作模式/填充模式，具体见CipherAlgorithms枚举
	 * @return byte[] 解密后明文结果
	 */
	public static byte[] rc2Decrypt(byte[] input, byte[] key,byte[] iv,RCAlgorithms algorithms){
		return doFinal(input,key,iv,Cipher.DECRYPT_MODE,SecretKeyType.RC2,algorithms.getValue());
	}

	/**
	 * rc4加密
	 * @param input 原始字节数组
	 * @param key rc4秘钥
	 * @param algorithms 可选不同的加密工作模式/填充模式，具体见CipherAlgorithms枚举
	 * @return byte[] 加密结果
	 */
	public static byte[] rc4Encrypt(byte[] input, byte[] key,RCAlgorithms algorithms){
		return doFinal(input,key,Cipher.ENCRYPT_MODE,SecretKeyType.RC4,algorithms.getValue());
	}


	/**
	 * rc4解密
	 * @param input 密文数组
	 * @param key rc4秘钥
	 * @param algorithms 可选不同的加密工作模式/填充模式，具体见CipherAlgorithms枚举
	 * @return byte[] 解密后明文结果
	 */
	public static byte[] rc4Decrypt(byte[] input, byte[] key,RCAlgorithms algorithms){
		return doFinal(input,key,Cipher.DECRYPT_MODE,SecretKeyType.RC4,algorithms.getValue());
	}


	/**
	 * rc4加密
	 * @param input 原始字节数组
	 * @param key rc4秘钥
	 * @param iv 初始向量
	 * @param algorithms 可选不同的加密工作模式/填充模式，具体见CipherAlgorithms枚举
	 * @return byte[] 加密结果
	 */
	public static byte[] rc4Encrypt(byte[] input, byte[] key,byte[] iv,RCAlgorithms algorithms){
		return doFinal(input,key,iv,Cipher.ENCRYPT_MODE,SecretKeyType.RC4,algorithms.getValue());
	}


	/**
	 * rc4解密
	 * @param input 密文数组
	 * @param key rc4秘钥
	 * @param  iv 初始向量
	 * @param algorithms 可选不同的加密工作模式/填充模式，具体见CipherAlgorithms枚举
	 * @return byte[] 解密后明文结果
	 */
	public static byte[] rc4Decrypt(byte[] input, byte[] key,byte[] iv,RCAlgorithms algorithms){
		return doFinal(input,key,iv,Cipher.DECRYPT_MODE,SecretKeyType.RC4,algorithms.getValue());
	}

	/**
	 * rsa加密
	 * @param input 原始字节数组
	 * @param key rsa 公开秘钥
	 * @param algorithms 可选不同的加密工作模式/填充模式，具体见CipherAlgorithms枚举
	 * @return byte[] 加密结果
	 */
	public static byte[] rsaEncrypt(byte[] input, byte[] key,RSAAlgorithms algorithms){
		try {
			// 转换公钥材料
			// 实例化密钥工厂
			KeyFactory keyFactory = KeyFactory.getInstance(SecretKeyType.RSA.name());
			// 初始化公钥
			// 密钥材料转换
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
			// 产生公钥
			PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
			Cipher cipher = Cipher.getInstance(algorithms.getValue());
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			return cipher.doFinal(input);
		} catch (GeneralSecurityException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}


	/**
	 * rsa解密
	 * @param input 密文数组
	 * @param key rsa 私钥秘钥
	 * @param algorithms 可选不同的加密工作模式/填充模式，具体见CipherAlgorithms枚举
	 * @return byte[] 解密后明文结果
	 */
	public static byte[] rsaDecrypt(byte[] input, byte[] key,RSAAlgorithms algorithms){
		try {
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
			KeyFactory keyFactory = KeyFactory.getInstance(SecretKeyType.RSA.name());
			Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
			Cipher cipher = Cipher.getInstance(algorithms.getValue());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return cipher.doFinal(input);
		} catch (GeneralSecurityException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * rsa加密
	 * @param input 原始字节数组
	 * @param key rsa 公开秘钥
	 * @param iv 初始向量
	 * @param algorithms 可选不同的加密工作模式/填充模式，具体见CipherAlgorithms枚举
	 * @return byte[] 加密结果
	 */
	public static byte[] rsaEncrypt(byte[] input, byte[] key,byte[] iv,RSAAlgorithms algorithms){
		try {
			// 转换公钥材料
			// 实例化密钥工厂
			KeyFactory keyFactory = KeyFactory.getInstance(SecretKeyType.RSA.name());
			// 初始化公钥
			// 密钥材料转换
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
			// 产生公钥
			PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
			Cipher cipher = Cipher.getInstance(algorithms.getValue());
			cipher.init(Cipher.ENCRYPT_MODE, pubKey,new IvParameterSpec(iv));
			return cipher.doFinal(input);
		} catch (GeneralSecurityException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}


	/**
	 * rsa解密
	 * @param input 密文数组
	 * @param key rsa 私钥秘钥
	 * @param iv 初始向量
	 * @param algorithms 可选不同的加密工作模式/填充模式，具体见CipherAlgorithms枚举
	 * @return byte[] 解密后明文结果
	 */
	public static byte[] rsaDecrypt(byte[] input, byte[] key,byte[] iv,RSAAlgorithms algorithms){
		try {
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
			KeyFactory keyFactory = KeyFactory.getInstance(SecretKeyType.RSA.name());
			Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
			Cipher cipher = Cipher.getInstance(algorithms.getValue());
			cipher.init(Cipher.DECRYPT_MODE, privateKey,new IvParameterSpec(iv));
			return cipher.doFinal(input);
		} catch (GeneralSecurityException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}

	/**
	 * pbe加密
	 * @param input 原始字节数组
	 * @param password pbe 字符串秘钥 可以任意长度
	 * @param salt 盐 Salt must be 8 bytes long
	 * @param algorithms 可选不同的加密工作模式/填充模式，具体见CipherAlgorithms枚举
	 * @return byte[] 加密结果
	 */
	public static byte[] pbeEncrypt(byte[] input, String password,byte[] salt,PEBAlgorithms algorithms){
		try {
			PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithms.getValue());
			SecretKey secretKey = keyFactory.generateSecret(keySpec);
			PBEParameterSpec paramSpec = new PBEParameterSpec(salt, 100);//100:iterationCount - the iteration count.
			Cipher cipher = Cipher.getInstance(algorithms.getValue());
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
			return cipher.doFinal(input);
		} catch (GeneralSecurityException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}


	/**
	 * pbe解密
	 * @param input 密文数组
	 * @param password pbe 字符串秘钥 可以任意长度
	 * @param salt 盐值 Salt must be 8 bytes long
	 * @param algorithms 可选不同的加密工作模式/填充模式，具体见CipherAlgorithms枚举
	 * @return byte[] 解密后明文结果
	 */
	public static byte[] pbeDecrypt(byte[] input, String password,byte[] salt,PEBAlgorithms algorithms){
		try {
			PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithms.getValue());
			SecretKey secretKey = keyFactory.generateSecret(keySpec);
			PBEParameterSpec paramSpec = new PBEParameterSpec(salt, 100);//100:iterationCount - the iteration count.
			Cipher cipher = Cipher.getInstance(algorithms.getValue());
			cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
			return cipher.doFinal(input);
		} catch (GeneralSecurityException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}


	/**
	 * DiffieHellman算法加密
	 * @param input 加密原文
	 * @param publicKey 对方公钥
	 * @param privateKey 我方私钥
	 * @param keyAlgorithms 生成的对称秘钥算法：仅支持 DES/DESede/AES
	 * @return byte[] 密文
	 * @throws Exception
	 */
	public static byte[] dhEncrypt(byte[] input,PublicKey publicKey,PrivateKey privateKey,KeyGeneratorType keyAlgorithms)
			throws Exception {
		SecretKey secretKey = KeyUtil.generateKey(publicKey, privateKey, keyAlgorithms);
		// 数据解密
		Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		return cipher.doFinal(input);
	}


	/**
	 * DiffieHellman算法解密
	 * @param input 加密密文
	 * @param publicKey 对方公钥
	 * @param privateKey 我方私钥
	 * @param keyAlgorithms 生成的对称秘钥算法：仅支持 DES/DESede/AES
	 * @return byte[] 解密明文
	 * @throws Exception
	 */
	public static byte[] dhDencrypt(byte[] input,PublicKey publicKey,PrivateKey privateKey,KeyGeneratorType keyAlgorithms)
			throws Exception {
		SecretKey secretKey = KeyUtil.generateKey(publicKey, privateKey, keyAlgorithms);
		// 数据解密
		Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		return cipher.doFinal(input);
	}
}
