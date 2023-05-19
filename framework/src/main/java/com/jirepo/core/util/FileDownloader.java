package com.jirepo.core.util;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;

import com.jirepo.core.constants.ResMediaType;

/** 파일 다운로드 처리 클래스 */
public class FileDownloader {

  /**
   * 
   * @param request  웹 요청
   * @param response 웹 응답
   * @param file     다운로드 받을 파일
   * @param toName   변경할 파일명
   * @throws IOException
   */
  public static void download(HttpServletRequest request, HttpServletResponse response, File file, String toName)
      throws IOException {

    response.setContentType(ResMediaType.OCTET_STREAM);
    response.setHeader("Content-Disposition", "attachment;filename=" + toName);
    response.setHeader("Content-Transfer-Encoding", "binary");
    response.setContentLength((int) file.length());
    OutputStream out = response.getOutputStream();
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(file);
      FileCopyUtils.copy(fis, out);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      throw e;
    } finally {
      if (fis != null) {
        try {
          fis.close();
        } catch (Exception e) {
        }
      }
    }
    out.flush();
  }// :


  public static  byte[] readByteRange(File file, long start, long end) throws IOException {
    FileInputStream inputStream = null;
    try {
      inputStream = new FileInputStream(file);
      ByteArrayOutputStream bufferedOutputStream = new ByteArrayOutputStream();
      byte[] data = new byte[1024];
      int nRead;
      while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
        bufferedOutputStream.write(data, 0, nRead);
      }
      bufferedOutputStream.flush();
      byte[] result = new byte[(int) (end - start)];
      System.arraycopy(bufferedOutputStream.toByteArray(), (int) start, result, 0, (int) (end - start));
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      throw e; 
    } finally {
      if(inputStream != null) {
        try {
          inputStream.close();
        } catch (Exception e) { }
      }
    }
  }//:
  

	private static ResourceRegion resourceRegion(UrlResource video, HttpHeaders headers) throws IOException {
		
		final long chunkSize = 1000000L;

		long contentLength = video.contentLength();
		HttpRange httpRange = headers.getRange().stream().findFirst().get();
		if (httpRange != null) {
			long start = httpRange.getRangeStart(contentLength);
			long end = httpRange.getRangeEnd(contentLength);
			long rangeLength = Long.min(chunkSize, end - start + 1);
			return new ResourceRegion(video, start, rangeLength);
		} else {
			long rangeLength = Long.min(chunkSize, contentLength);
			return new ResourceRegion(video, 0, rangeLength);
		}
	}

  public static ResponseEntity<ResourceRegion>  partialDownload(HttpServletRequest request, HttpServletResponse response, HttpHeaders headers, UrlResource urlResource) throws IOException {
    //UrlResource video = new UrlResource("classpath:/public/media/" + fileName);
		ResourceRegion region = resourceRegion(urlResource, headers);
		return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
				.contentType(MediaTypeFactory.getMediaType(urlResource).orElse(MediaType.APPLICATION_OCTET_STREAM)).body(region);
  }

}/// ~
