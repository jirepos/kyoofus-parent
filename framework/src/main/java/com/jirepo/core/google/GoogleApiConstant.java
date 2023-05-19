package com.jirepo.core.google;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.docs.v1.DocsScopes;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.gmail.GmailScopes;

import lombok.Getter;
import lombok.Setter;

/**
 *  Google 연동을 위한 상수를 정의한다. 
 */

public class GoogleApiConstant {


    /** Google OAuth 2 인증화면 URL */
	public static final String GOOGLE_OAUTH2_AUTH_URL = "https://accounts.google.com/o/oauth2/auth";
	/** Google로부터 Access Token을 요청할 URL */
	public static final String GOOGLE_OAUTH2_TOKEN_REQ_URL = "https://www.googleapis.com/oauth2/v4/token";
	
	
	/** 나중에 설명함 */
	public static final String GOOGLE_OAUTH2_CREDENTIALS_FILE_PATH = "/credentials.json";
	
	// /** 사용할 API의 Scopes */
	// public static final List<String> GOOGLE_OAUTH2_SCOPES = Arrays.asList(CalendarScopes.CALENDAR,  DocsScopes.DOCUMENTS,  DriveScopes.DRIVE , DriveScopes.DRIVE_FILE, DriveScopes.DRIVE_APPDATA
    //  , GmailScopes.GMAIL_LABELS);


	/** SAML Response View Page 경로 */
	public static final String GOOGLE_SSO_SAML_RESPONSE_PAGE = "saml/samlResponse";
	
	/** G Suite Admin User ID */
	public static final String GSUITE_ADMIN_USER_ID = "admin@gsuite.naonsoft.kr";
	/** G Suite Domain */
	public static final String GSUITE_DOMAIN = "gsuite.naonsoft.kr";
	/** Service account's email */ 
	public static final String GSUITE_SERVICE_ACCOUNT_EMAIL = "naon-svcacc-test@naon-gsuite-test.iam.gserviceaccount.com"; 
	public static final String GSUITE_SERVICE_ACCOUNT_KEY_FILE_PATH  ="saml/naon-gsuite-test-2f1073bf8b1c.p12";
	

	/** Google Doc Mime Type */
	public static final String MIME_TYPE_GOOGLE_DOCS = "application/vnd.google-apps.document";
	/** MS docx Mime Type */
	public static final String MIME_TYPE_MS_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
	/** MS docs Extenstion */
	public static final String EXT_MS_DOCX = "docx";
	/** Google Sheet Mime Type */
	public static final String MIME_TYPE_GOOGLE_SHEET = "application/vnd.google-apps.spreadsheet";
	/** MS Excel Mime Type */
	public static final String MIME_TYPE_MS_SHEET = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	/** MS Excel extension */
	public static final String EXT_MS_SHEET = "xlsx";
	/** Google Slide Mime Type */
	public static final String MIME_TYPE_GOOGLE_PRESENTATION = "application/vnd.google-apps.presentation";
	/** Microsoft Powerpoint Mime Type */
	public static final String MIME_TYPE_MS_PRESENTATION = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
	/** Microsfot Powerpoint's extension */
	public static final String EXT_MS_PRESENTATION = "pptx";
	
}///~
