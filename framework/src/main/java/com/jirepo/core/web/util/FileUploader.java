package com.jirepo.core.web.util;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.jirepo.core.util.IoUtils;

/** 파일업로드를 처리하는 클래스이다. */
public class FileUploader {

  /**
   * 파일업로드 처리한다. 
   * @param uploadDir 업로드 디렉토리
   * @param fileName  파일명  예) aaa.txt 
   * @param file     파일 정보
   * @throws IOException
   */
  public static void upload(String uploadDir, String fileName, MultipartFile file) throws IOException {
    // if(file.getOriginalFilename().isEmpty()) {
    //   throw new FileNotFoundException();
    // }
    IoUtils.createDirectories(uploadDir); // 디렉터리 생성

    // baseDir / userid /post/guid.[ext]
    //BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File("path", file.getOriginalFilename())));
    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(uploadDir,  fileName)));
    bos.write(file.getBytes());
    bos.flush();
    bos.close();
  }//:
  

  // public static void upload(MultipartFile[] files) throws IOException {
  //   for(MultipartFile file : files) {
  //     upload(file);
  //   }
  // }//:
 

}///~