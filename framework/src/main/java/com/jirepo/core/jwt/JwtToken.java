package com.jirepo.core.jwt;


import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;


/**
 * JSON Web Token을 편리하게 다루기 위한 클래스이다. 
 */
public class JwtToken {
  
  private Jws<Claims> claimsJws;
  
  /**
   * 생성자
   * @param encodedKey JSON 생성 키
   * @param jws JWS 
   */
  public JwtToken(String encodedKey, String jws) {
    this.claimsJws = JwtUtils.readToken(encodedKey, jws);
  }

  /** Issuer를 구한다.  */
  public String getIssuer(){ 
   return JwtUtils.getIssuer(claimsJws); 
  }
  /** Audience를 구한다. */
  public String getAudience() {
    return JwtUtils.getAudience(claimsJws);
  }
  /** Token 아이디를 구한다.  */
  public String getId() {
    return JwtUtils.getId(claimsJws);
  }
  /** Subject를 구한다.  */
  public String getSubject() {
    return JwtUtils.getSubject(claimsJws);
  }
  /** 만기일을 구한다.  */
  public Date getExpiration(){ 
    return JwtUtils.getExpiration(claimsJws);
  }
  /** 토큰이 유효한지 체크한다.  */
  public boolean isValid() {
    return !JwtUtils.isExpired(this.getExpiration());
  }
  /**
   * 클레임을 구한다. 
   * @param key Cliaim의 키 
   * @return
   *  클레임
   */
  public String getCliam(String key) {
    return JwtUtils.getClaim(claimsJws, key);
  }

}///~
