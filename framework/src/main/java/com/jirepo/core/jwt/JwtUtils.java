package com.jirepo.core.jwt;

import java.io.File;
import java.io.FileReader;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

/**
 * @see https://github.com/jwtk/jjwt
 */
public class JwtUtils {
  
 	// private static final String JWT_KEY = "jwt.key";
	// private static String encodedKeyString;
	// //private static Key tokenKey;
	// static {
	// 	try {
	// 		File file = ClassPathFileUtils.getFileObject("application.properties");
	// 		Properties props = new Properties();
	// 		props.load(new FileReader(file));
	// 		encodedKeyString = props.getProperty(JWT_KEY);
	// 		// base64를 byte[]로 변환
	// 		byte[] decodedByte = Base64.getDecoder().decode(encodedKeyString.getBytes());
	// 		// byte[]로 Key 생성
	// 		tokenKey = Keys.hmacShaKeyFor(decodedByte);
	// 	} catch (Exception e) {
	// 		throw new RuntimeException(e);
	// 	}
	// }//:

 
	/**
	 * 토큰 생성을 위한 Key를 생성한다.
	 * 
	 * @return Base64 encoded된 문자열
	 */
	public static String createKey() {
		// JWS 쓰기
		// 알고리즘 선택
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		// 키 생성
		Key key = Keys.secretKeyFor(signatureAlgorithm);
		// 키 바이트로 변환
		byte[] encodedKey = key.getEncoded();
		// base64로 변환
		String encodedString = Base64.getEncoder().encodeToString(encodedKey);
		return encodedString;
	}// :

	/**
	 * Base64 로 인코딩된 문자열을 Key로 변환한다. 
	 * @param encodedString
	 * @return
	 */
	public static Key convertToKey(String encodedString) {
		byte[] decodedByte = Base64.getDecoder().decode(encodedString.getBytes());
		// byte[]로 Key 생성
		return 	Keys.hmacShaKeyFor(decodedByte);
	}//:

	

	/**
	 * Token을 생성한다.
	 * @param issuer 발행자 이름 
	 * @param audience 구독자 이름 
 	 * @param expiration 만료기간. millisecond 사용 ex) 하루 60 * 60 * 24 * 1000 
	 * @param claims Token에 담을 정보, 값은 암호화 해서 넣어야 한다. 
	 * @return
	 * 		생성된 Token 문자열 
	 */
	public static String createJws(String encodedKey, String subject,String issuer, String audience, long expiration, Map<String, String> claims) {
		JwtBuilder builder = Jwts.builder();
		Key tokenKey = convertToKey(encodedKey); 
		Date now = new Date();         // 현재시간
		long nowmills = now.getTime(); // 현재 날짜 시간의 timestamp
		long expiration2 = nowmills + expiration; // 만료 날짜,시간 설정을 위한 timestamp
		Date expDate = new Date(expiration2); // 만료 날짜,시간 Date 생성
		builder.setId(UUID.randomUUID().toString());
		builder.setSubject(subject);
		builder.setIssuer(issuer);
		builder.setAudience(audience);
		builder.setExpiration(expDate);
		for(Map.Entry<String, String> entry : claims.entrySet()) {
			builder.claim(entry.getKey(), entry.getValue());
		}
		return builder.signWith(tokenKey).compact();
	}//:

	/**
	 * JWS 문자열을 Jws로 반환한다. 토큰이 유효하지 않으면 오류가 발생한다. 
	 * 이 메소드를 사용할 때  try catch 블록으로 감싸야 한다. 
	 * 
	 * @param jws
	 * @return
	 * @throws  EpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException
	 */
	public static Jws<Claims> readToken(String encodedKey, String jws)  {
		// JWS 읽기
		// parse할 때 토큰 유효기간이 만료되면 오류가 발생 
		Key tokenKey = convertToKey(encodedKey); 
		Jws<Claims> jws2 = Jwts.parser().setSigningKey(tokenKey).parseClaimsJws(jws);
		return jws2;
	}// :
	

	
	/**
	 * Subject를 구한다. 
	 * @param jws  JWS 객체 
	 * @return
	 * 	
	 */
	public static String getSubject(Jws<Claims> jws) {
		return jws.getBody().getSubject();
	}
	/**
	 * Audience를 구한다. 
	 * @param jws  JWS 객체 
	 * @return
	 */
	public static String getAudience(Jws<Claims> jws) {
		return jws.getBody().getAudience();
	}
	/**
	 * Token의 ID를 구한다. 
	 * @param jws JWS 객체
	 * @return
	 */
	public static String getId(Jws<Claims> jws) {
		return jws.getBody().getId();
	}

	/**
	 * 발행자를 구한다. 
	 * @param jws JWS 객체 
	 * @return
	 */
	public static String getIssuer(Jws<Claims> jws) {
		return jws.getBody().getIssuer();
	}

	/**
	 * 만료일시를 구한다. 
	 * @param jws JWS 객체 
	 * @return
	 */
	public static Date getExpiration(Jws<Claims> jws) {
		return  jws.getBody().getExpiration();
	}//:


	/**
	 * Token에서 claim의 키로 값을 구한다. 
	 * @param jws  Jws 인스턴스
 	 * @param key  값을 구할 키 
	 * @return
	 * 		값 
	 */
	public static String getClaim(Jws<Claims> jws, String key) {
		return (String)jws.getBody().get(key);
	}//:


	/**
	 * Token의 유효기간을 체크한다. 현재 시간보다 이전이면 토큰이 유효하다. 
	 * @param expiration 만기일자시간 
	 * @return
	 * 		만기일이 없으면 true, 만기일이 지났으면 true
	 */
	public static boolean isExpired(Date expiration) { 
		if(expiration == null) return true; 
		System.out.println(">>Check");
		// when의 날짜가 expiration보다 이전이면 true 
		if(expiration.before(new Date() /* when */ ) ) {
			System.out.println("만기가 지났어요.");
			return true;
		}else {
			return false;
		}
	}//:

	
//	
//	/**
//	 * Token이 유효한지 확인한다.
//	 * 
//	 * @param jws Token 인스턴스
//	 * @return 유효한 토큰이면 true, 아니면 false를 반환한다.
//	 */
//	public static boolean isValid(Jws<Claims> jws) {
//		
////		jws.getBody().getSubject();
////		jws.getBody().getIssuer();
////		jws.getBody().getAudience();
////		jws.getBody().getId();
////		jws.getBody().getExpiration();
//		// 만료기간 구함
//		Date expdate = jws.getBody().getExpiration();
//		
//		// 현재시간
//		Date now = new Date();
//		long nowMills = now.getTime();
//		long expMills = expdate.getTime();
//		// 토큰이 만료되지 않았는지 체크
//		if (expMills >= nowMills) {
//			//System.out.println("유효한 토큰");
//			return true;
//		} else {
//			//System.out.println("유효하지 않은 토큰");
//			return false;
//		}
//	}// :

}
