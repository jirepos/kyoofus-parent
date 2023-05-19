package com.jirepo.core.constants;


import org.springframework.http.MediaType;

public class ResMediaType {
  private static final String charSet = ";charset=utf-8";
  public static final String TEXT = MediaType.TEXT_PLAIN_VALUE + charSet; 
  public static final String XML = MediaType.TEXT_XML_VALUE + charSet; 
  public static final String HTML = MediaType.TEXT_HTML_VALUE + charSet; 
  public static final String JSON = MediaType.APPLICATION_JSON_VALUE + charSet; 

  // image
  public static final String IMAGE_JPEG = MediaType.IMAGE_JPEG_VALUE; 
  public static final String IMAGE_PNG = MediaType.IMAGE_PNG_VALUE;
  public static final String IMAGE_GIF = MediaType.IMAGE_GIF_VALUE; 
  // binary 
  public static final String OCTET_STREAM = MediaType.APPLICATION_OCTET_STREAM_VALUE;


}///~
