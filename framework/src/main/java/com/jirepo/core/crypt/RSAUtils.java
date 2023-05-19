package com.jirepo.core.crypt;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

/** RSA 암호화 지원 클래스이다.  */
public class RSAUtils {

	public static final String RSA_PRIVATE_KEY_DESC = "RSA PRIVATE KEY"; 
	public static final String RSA_PUBLIC_KEY_DESC = "RSA PUBLIC KEY";
	
 
	// Key Section
	/**
	 * 공개키/개인키 KeyPair을 생성합니다.
	 * 
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws Exception
	 */
	public static KeyPair createKeyPair(int keySize) throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		//keyPairGen.initialize(2048);
		keyPairGen.initialize(keySize);
		return keyPairGen.genKeyPair();
	}// :

	/**
	 * 개인키의 byte[]로 개인키를 생성합니다.
	 * 
	 * @param keyBytes 개인키의 바이트배열
	 * @return 개인키
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 * @throws Exception                the Exception
	 */
	public static PrivateKey getPrivateKey(byte[] keyBytes) throws InvalidKeySpecException, NoSuchAlgorithmException {
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}// :

	/**
	 * 공개키의 바이트 배열로부터 공개키를 생성한다.
	 * 
	 * @param keyBytes 공개키의 바이트 배열
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}//

	/**
	 * 개인키를 Base64로 encoding하여 반환한다.
	 * 
	 * @param keyPair KeyPair 인스턴스
	 * @return Base64로 인코딩된 문자열
	 */
	public static String privateKeyToBase64(KeyPair keyPair) {
		byte[] privBytes = keyPair.getPrivate().getEncoded();
		return new String(java.util.Base64.getEncoder().encode(privBytes));
	}// :

	/**
	 * 공개키를 Base64로 encoding하여 반환한다.
	 * 
	 * @param keyPair KeyPair 인스턴스
	 * @return Base64로 인코딩된 문자열
	 */
	public static String publicKeyToBase64(KeyPair keyPair) {
		byte[] privBytes = keyPair.getPublic().getEncoded();
		return new String(java.util.Base64.getEncoder().encode(privBytes));
	}// :

	/**
	 * Base64로 인코딩된 문자열을 PrivateKey로 변환한다.
	 * 
	 * @param encodedPrivateKey
	 * @return PrivateKey 인스턴스
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 */
	public static PrivateKey base64ToPrivateKey(String encodedPrivateKey)
			throws InvalidKeySpecException, NoSuchAlgorithmException {
		byte[] decodedByte = Base64.getDecoder().decode(encodedPrivateKey.getBytes());
		return getPrivateKey(decodedByte);
	}

	/**
	 * Base64로 인코딩된 문자열을 PublicKey로 변환한다.
	 * 
	 * @param encodedPublicKey 인코딩된 문자열
	 * @return PublicKey 인스턴스
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static PublicKey base64ToPublicKey(String encodedPublicKey)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		byte[] decodedByte = Base64.getDecoder().decode(encodedPublicKey.getBytes());
		return getPublicKey(decodedByte);
	}

	// encrypt/decryt section

	/**
	 * 공개키로 바이트를 암호화 한다.
	 * 
	 * @param bytesToEncrypt 암호화할 바이트 배열
	 * @param publicKey      공개키
	 * @return 암호화된 바이트 배열
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] bytesToEncrypt, PublicKey publicKey) throws InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		// Perform the actual encryption on those bytes
		byte[] cipherText = cipher.doFinal(bytesToEncrypt);
		return cipherText;
	}// :

	/**
	 * 개인키로 암호화된 바이트 배열을 복호화 한다.
	 * 
	 * @param bytesToDecrypt 복호화 할 바이트 배열
	 * @param privateKey     개인키
	 * @return 복호화된 바이트 배열
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] bytesToDecrypt, PrivateKey privateKey) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(bytesToDecrypt);
	}// :

	/**
	 * 문자열을 암호화한 후 Base64로 encodging 하여 반환한다.
	 * 
	 * @param strToEncrypt     암호화할 문자열
	 * @param base64PublicKey Base64로 인코딩된 공개키 문자열
	 * @return Base64로 변환된 암호화된 문자열
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws NoSuchPaddingException
	 */
	public static String encrypt(String strToEncrypt, String base64PublicKey)
			throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, NoSuchPaddingException {
		PublicKey pubKey = base64ToPublicKey(base64PublicKey); // Base64로 인코딩된 문자열을 공개키로 변환
		byte[] encryptedByte = encrypt(strToEncrypt.getBytes(), pubKey); // 암호화
		return new String(Base64.getEncoder().encode(encryptedByte)); // Base64 변환
	}// :

	/**
	 * Base64로 인코딩된 암호화된 문자열을 문자열로 변환한다. 
	 * @param strToDecrypt Base64로 인코딩된 암호화된 문자열
	 * @param base64PrivKey Base64로 인코딩된 암호화된 개인키 
	 * @return
	 * 		복호화된 문자열
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String decrypt(String strToDecrypt, String base64PrivKey) 
			throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		PrivateKey privKey = base64ToPrivateKey(base64PrivKey); // Base64로 인코딩된 문자열을 개인키로 변환
		byte[] encryptedByte = Base64.getDecoder().decode(strToDecrypt); // Base64 decoding
		byte[] decryptedByte = decrypt(encryptedByte, privKey); // 복호화 
		return new String(decryptedByte, "UTF-8");  // 화면에서 넘어온 값을 decode할 때 UTF-8 설정을 하지 않으면 한글 깨진다
	}// :

	// utility

	/**
	 * 공개키/개인키를 파일로 저장. 공개키를 디스크에 저장 String publicKeyFile = "/usr/home/key";
	 * RSAUtils.saveKeyAsFile(keyPair.getPublic().getEncoded(), publicKeyFile);
	 * 
	 * 개인키를 디스크에 저장 String privateKeyFile = "/usr/home/key";
	 * RSAUtils.saveKeyAsFile(keyPair.getPrivate().getEncoded(), privateKeyFile);
	 *
	 * @param keyBytes 키의 바이트배열
	 * @param filePath 저장할 파일명(풀패스)
	 * @throws IOException
	 * @throws Exception   the Exception
	 */
	public static void saveKeyAsFile(byte[] keyBytes, String filePath) throws IOException {
		// 파일 시스템에 암호화된 공개키를 쓴다.
		FileOutputStream fos = new FileOutputStream(filePath);
		fos.write(keyBytes);
		fos.close();
	}// :

	/**
	 * 공개키를 파일로 저장
	 * 
	 * @param keyPair
	 * @param filePath
	 * @throws IOException
	 */
	public static void savePublicKeyAsFile(KeyPair keyPair, String filePath) throws IOException {
		saveKeyAsFile(keyPair.getPublic().getEncoded(), filePath);
	}// :

	/**
	 * 개인키를 파일로 저장
	 * 
	 * @param keyPair
	 * @param filePath
	 * @throws IOException
	 */
	public static void savePrivateKeyAsFile(KeyPair keyPair, String filePath) throws IOException {
		saveKeyAsFile(keyPair.getPrivate().getEncoded(), filePath);
	}// :

	/**
	 * 공개키/개인키를 파일로 부터 읽어들인다.
	 * 
	 * @param filePath 저장된 파일명(풀패스)
	 * @return 키의 바이트배열
	 * @throws Exception the Exception
	 */
	public static byte[] getKeyFromFile(String filePath) throws Exception {

		FileInputStream fis = new FileInputStream(filePath);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int theByte = 0;
		while ((theByte = fis.read()) != -1) {
			baos.write(theByte);
		}
		fis.close();
		byte[] keyBytes = baos.toByteArray();
		baos.close();
		return keyBytes;
	}// :
 

	/**
	 * 공개키 또는 개인키를 PEM 파일형식으로 파일로 저장한다. 
	 * @param key    개인키 또는 공개키 
	 * @param description  키 구분자. "RSA PRIVATE KEY" 또는 "RSA PUBLIC KEY"
	 * @param filename 저장할 파일명 
	 * @throws Exception
	 */
  public static void writePemFile(Key key, String description, String filename)   {
    PemObject pemObject = new PemObject (description, key.getEncoded());
    PemWriter pemWriter = null;
    try {
      pemWriter = new PemWriter(new OutputStreamWriter(new FileOutputStream(filename)));  
      pemWriter.writeObject(pemObject);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }finally { 
			try {
				pemWriter.close();	
			} catch (Exception e) {	}
    }
  }//:

	/**
	 * 공개키 또는 개인키를 PEM 형식으로 변환하여 반환한다.  
	 * @param key 공개키 또는 개인키
	 * @param description 키 구분자. "RSA PRIVATE KEY" 또는 "RSA PUBLIC KEY"
	 * @return
	 * 	PEM 형식 문자열
	 */
  public static String writePemToString(Key key, String description)   {
    PemObject pemObject = new PemObject (description, key.getEncoded());
    PemWriter pemWriter = null;
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    try {
      pemWriter = new PemWriter(new OutputStreamWriter( bout ));  
      pemWriter.writeObject(pemObject);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }finally { 
			try {
				pemWriter.close();	
			} catch (Exception e) {	}
    }
    return bout.toString();
  }//:
}///~
