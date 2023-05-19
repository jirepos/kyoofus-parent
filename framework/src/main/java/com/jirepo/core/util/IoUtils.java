package com.jirepo.core.util;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.apache.commons.io.FilenameUtils;


/** IO 공통클래스이다.  */
public class IoUtils {

  
  /** 파일이름에서 확장자만 돌려준다.  */
  public static String getFilenameExtension(String path) { 
    return  FilenameUtils.getExtension(path);
  }//:

  

  /**
   * 경로에 있는 디렉토리를 생성한다. 
   * @param strPath   생성할 디렉토리 경로  "d:\\aaa\\bbb\\ccc"
   * @throws IOException
   *    생성 실패시 예외를 던진다. 
   */
  public static void createDirectories(String strPath) throws IOException  { 
    Path path = Paths.get(strPath);
    Files.createDirectories(path);
  }//:
  


  /**
   * classpath에 파일을 읽어 반환한다.  경로는 'com/sogood/aaa.txt'와 같이 작성한다. 
   * @param path clasdspath 경로 파일명
   * @return
   *    File
   */
  public static File getFileInClasspath(String path) {
    try {
      ClassPathResource resource = new ClassPathResource(path);
      return resource.getFile();  
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }//:

  /**
   * classpath에 파일을 읽어 반환한다.  경로는 'com/sogood'와 같이 작성한다. 
   * @param path clasdspath 경로
   * @param fileName 파일명 
   * @return
   *    File
   */
  public static File createFileInClasspath(String path, String fileName) {
    try {
      ClassPathResource resource = new ClassPathResource(path);
      return  new File(resource.getURI() + fileName);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }//:


  /**
   * 파일을 읽어 byte[]로 반환한다.  
   * @param f 읽을 파일
   * @return
   *    파일 내용의 byte[] 배열 
   */
  public static byte[] readFileToByteArray(File f)  { 
    try {
      return FileUtils.readFileToByteArray(f);  
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }//:


  /**
   * 파일을 읽어서 List<Sring>으로 반환한다. 
   * @param f 파일 객체 
   * @param charset  문자셋 
   * @return
   *    파일에서 읽은 문자열 
   */
  public static List<String> readLines(File f, String charset) {
    try {
      return FileUtils.readLines(f, charset);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }//:



  /**
   * 파일을 끝까지 읽고 문자열로 반환한다.
   * @param f 파일 
   * @param charset  문자셋 
   * @return
   *    파일 내용 
   */
  public static String readFileToString(File f, String charset) {
    try {
      return FileUtils.readFileToString(f, charset);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }//:


  /**
   * 클래스 패스의 파일을 읽는다. 
   * @param pathAndFile 클래스경로와 파일이름. 예) com/sogood/aaa.txt 
   * @param charset 파일의 charset  
   * @return
   *  파일 내용
   */
  public static String readFileClasspathToString(String pathAndFile, String charset) {
    try {
      //ClassPathResource resource = new ClassPathResource(pathAndFile);
      // jar 파일에 묶여 있을 경우에는 getFile()이 동작하지 않는다. 
      //System.out.println(resource.getPath());
      //System.out.println(resource.getURI().getPath().
      //return FileUtils.readFileToString(ResourceUtils.getFile("classpath:" + pathAndFile), charset);

      InputStream is =  IoUtils.class.getResourceAsStream("/" + pathAndFile);
      InputStreamReader isr = new InputStreamReader(is);
      BufferedReader br = new BufferedReader(isr);
      StringBuilder sb = new StringBuilder();
      String line = "";
      while((line = br.readLine()) != null) {
        sb.append(line);
      }
      isr.close();
      br.close();
      return sb.toString();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }//:
  


  public  String readFileClasspathToString2(String pathAndFile, String charset) {
    try {
      // ClassPathResource resource = new ClassPathResource(pathAndFile);
      // // jar 파일에 묶여 있을 경우에는 getFile()이 동작하지 않는다. 
      // System.out.println(resource.getPath());
      // //System.out.println(resource.getURI().getPath().
      //return FileUtils.readFileToString(resource.getFile(), charset);
      

      InputStream is = IoUtils.class.getClass().getResourceAsStream(pathAndFile);
      InputStreamReader isr = new InputStreamReader(is);
      BufferedReader br = new BufferedReader(isr);
      StringBuilder sb = new StringBuilder();
      String line = "";
      while((line = br.readLine()) != null) {
        sb.append(line);
      }
      isr.close();
      br.close();
      return sb.toString();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }//:



  /**
   * 파일에 내용을 쓴다. 
   * @param f 쓸 파일 
   * @param data  쓸 내용 
   * @param charset 문자셋 
   */
  public static void writeStringToFile(File f, String data, String charset) {
    try {
      FileUtils.writeStringToFile(f, data, charset);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }//:


}///~