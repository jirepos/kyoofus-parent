package com.jirepo.core.google;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.util.FileCopyUtils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.model.Document;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;
import com.jirepo.core.config.util.ApplicationContextHolder;

/**
 * 구글 API를 이용하기 위한 Service Class의 Base Class
 */
public class GoogleBaseService {

    protected static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * RefreshToken으로 사용자의 Credential을 생성한다.
     * 
     * @param gmailId 사용자 메일 아이디
     * @return
     *         Credential 객체
     * @throws Exception
     */
    protected Credential getCredentialWithRefreshToken(String gmailId) throws Exception {
        String refreshToken = GoogleOauthUtil.getRefreshTokenFromFile(gmailId);
        TokenResponse tokenReponse = GoogleOauthUtil.getRefreshToken(refreshToken);
        Credential credential = new GoogleCredential(); // mark this because GoogleCredential was deprecated
        credential.setAccessToken(tokenReponse.getAccessToken());
        return credential;
    }// :

    /**
     * Google Drive Service 인스턴스를 생성한다.
     * 
     * @return
     *         생성된 Drive 객체
     * @throws Exception
     */
    protected Drive createDrive(String gmailId) throws Exception {

        GoogleApiSettings settings = (GoogleApiSettings)ApplicationContextHolder.getBean("googleApiSettings");
        final NetHttpTransport httpTransprot = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = getCredentialWithRefreshToken(gmailId);
        Drive service = new Drive.Builder(httpTransprot, JSON_FACTORY, credential)
                .setApplicationName( settings.getApplicationName())
                .build();
        return service;
    }// :

    /**
     * 구글 드라이브의 파일의 퍼미션들을 가져온다.
     *
     * @param service API service instance의 Drive
     * @param fileId  퍼미션들을 가져오기 위한 파일의 아이디
     * @return List of permissions. 퍼미션 목록
     */
    protected List<Permission> retrievePermissions(Drive service, String fileId) {
        try {
            // setFields("*")을 해야만 email 주소를 반환한다.
            PermissionList permissions = service.permissions().list(fileId).setFields("*").execute();
            return permissions.getPermissions();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
        return null;
    }// :

    /**
     * 구글 문서 정보를 조회한다.
     * 
     * @param gmailId Gmail ID
     * @param docId   문서 ID
     */
    public void fetchDoc(String gmailId, String docId) {
        GoogleApiSettings settings = (GoogleApiSettings)ApplicationContextHolder.getBean("googleApiSettings");
        try {
            Credential credential = getCredentialWithRefreshToken(gmailId);
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Docs service = new Docs.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                    credential /* getCredentials(HTTP_TRANSPORT) */)
                    .setApplicationName( settings.getApplicationName()).build();
            // Prints the title of the requested doc:
            // https://docs.google.com/document/d/1iL_5dz5o3rGpdFiIu4JW_fZNzH7CGh3o1Jvf7d_1lVY/edit
            // docId는 "1iL_5dz5o3rGpdFiIu4JW_fZNzH7CGh3o1Jvf7d_1lVY" 이것임
            Document response = service.documents().get(docId).execute();
            String title = response.getTitle();
            System.out.printf("The title of the doc is: %s\n", title);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }// :

    /**
     * 빈문서를 생성한다.
     * 
     * @param gmailId Gmail ID
     * @return
     *         문서 ID 반환
     */
    public String createDoc(String gmailId) {
        GoogleApiSettings settings = (GoogleApiSettings)ApplicationContextHolder.getBean("googleApiSettings");
        try {
            Credential credential = getCredentialWithRefreshToken(gmailId);
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Docs service = new Docs.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName(  settings.getApplicationName()).build();
            Document doc = new Document().setTitle("테스트 latte가 생성한 문서");
            doc = service.documents().create(doc).execute();
            return doc.getDocumentId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }// :

    /**
     * 문서를 복사한다.
     * 
     * @param gmailId  Gmail ID
     * @param copyName 생성할 문서의 이름
     * @param docId    복할 문서의 아이디
     * @return
     *         복사하여 생성된 문서의 아이디
     */
    public String copyDoc(String gmailId, String copyName, String docId) {
        GoogleApiSettings settings = (GoogleApiSettings)ApplicationContextHolder.getBean("googleApiSettings");
        try {
            Credential credential = getCredentialWithRefreshToken(gmailId);
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                    .setApplicationName( settings.getApplicationName()).build();
            String copyTitle = (copyName == null) ? "Copy Title" : copyName;
            File copyMetadata = new File().setName(copyTitle);
            File documentCopyFile = service.files().copy(docId, copyMetadata).execute();
            return documentCopyFile.getId();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 문서를 HTML로 변환한다.
     * 
     * @param gmailId Gmail ID
     * @param docId   문서 아이디
     * @return
     *         변환된 HTML
     */
    public String exportDocToHtml(String gmailId, String docId) {
        GoogleApiSettings settings = (GoogleApiSettings)ApplicationContextHolder.getBean("googleApiSettings");
        try {
            final NetHttpTransport httpTransprot = GoogleNetHttpTransport.newTrustedTransport();
            Credential credential = getCredentialWithRefreshToken(gmailId);
            Drive service = new Drive.Builder(httpTransprot, JSON_FACTORY, credential)
                    .setApplicationName(settings.getApplicationName()).build();
            String html = "";
            OutputStream outputStream = new ByteArrayOutputStream();
            try {
                service.files().export(docId, "text/html").executeMediaAndDownloadTo(outputStream);
                html = outputStream.toString();
            } finally {
                outputStream.close();
                outputStream = null;
            }
            System.out.println(html);
            return html;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }// :

    /**
     * 문서를 다운로드 한다.
     * 
     * @param gmailId Gmail ID
     * @param docId   문서 아이디
     * @return
     *         다운로드한 문서의 경로
     */
    public String downloadDoc(String gmailId, String docId) {
        GoogleApiSettings settings = (GoogleApiSettings)ApplicationContextHolder.getBean("googleApiSettings");
        try {
            final NetHttpTransport httpTransprot = GoogleNetHttpTransport.newTrustedTransport();
            Credential credential = getCredentialWithRefreshToken(gmailId);
            Drive service = new Drive.Builder(httpTransprot, JSON_FACTORY, credential)
                    .setApplicationName( settings.getApplicationName()).build();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                service.files().export(docId, GoogleApiConstant.MIME_TYPE_MS_DOCX)
                        .executeMediaAndDownloadTo(outputStream);
                String filePath = settings.getSaveFolder();
                String fileUrl = filePath + java.io.File.separator + UUID.randomUUID().toString() + ".docx";
                Path path = Paths.get(filePath);
                Files.createDirectories(path);
                FileCopyUtils.copy(outputStream.toByteArray(), new FileOutputStream(fileUrl));
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (Exception e) {
        }
        return null;
    }// :

    /**
     * 문서를 공유한다.
     * 
     * @param gmailId  Gmail ID
     * @param docId    문서 아이디
     * @param gmailIds 공유할 대상 Gmail IDs
     * @throws Exception
     */
    public void shareDoc(String gmailId, String docId, List<String> gmailIds) throws Exception {
        GoogleApiSettings settings = (GoogleApiSettings)ApplicationContextHolder.getBean("googleApiSettings");

        JsonBatchCallback<Permission> callback = new JsonBatchCallback<Permission>() {
            @Override
            public void onFailure(GoogleJsonError e, HttpHeaders responseHeaders) throws IOException {
                // Handle error
                System.err.println(e.getMessage());
            }

            @Override
            public void onSuccess(Permission permission, HttpHeaders responseHeaders) throws IOException {
                System.out.println("ID that was shared is " + permission.getId());
            }
        };

        final NetHttpTransport httpTransprot = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = getCredentialWithRefreshToken(gmailId);
        Drive service = new Drive.Builder(httpTransprot, JSON_FACTORY, credential)
                .setApplicationName( settings.getApplicationName())
                .build();
        BatchRequest batch = service.batch();
        try {
            for (String address : gmailIds) {
                Permission userPermission = new Permission().setType("user").setRole("writer").setEmailAddress(address);
                service.permissions().create(docId, userPermission).setFields("id").queue(batch, callback);
            }
            batch.execute();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }// :

    /**
     * 퍼미션을 제거한다.
     * 
     * @param service      구글 Drive Service 인스턴스
     * @param fileId       퍼미션을 제거할 파일의 아이디
     * @param permissionId 제거할 퍼미션 아이디
     */
    private static void removePermission(Drive service, String fileId, String permissionId) {
        try {
            service.permissions().delete(fileId, permissionId).execute();
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
        }
    }// :

    /**
     * 공유된 Drive 문서에서 공유를 제거한다(권한 제거).
     * 
     * @param docId    문서아이디
     * @param gmailIds 공유를 제거할 email IDs
     * @throws Exception
     */
    public void removePermission(String gmailId, String docId, List<String> gmailIds) throws Exception {
        Map<String, String> mapToRemove = new HashMap<String, String>();
        for (String mailId : gmailIds) {
            mapToRemove.put(mailId, mailId);
        }
        Drive service = this.createDrive(gmailId);
        List<Permission> permissionList = retrievePermissions(service, docId);
        for (Permission perm : permissionList) {
            System.out.println("perm.getEmailAddress():" + perm.getEmailAddress()); // email이 null임. email을 받아올 방법이 필요함
            System.out.println("perm.getId():" + perm.getId());
            if (mapToRemove.containsKey(perm.getEmailAddress())) {
                removePermission(service, docId, perm.getId());
            }
        }
    }// :

}
