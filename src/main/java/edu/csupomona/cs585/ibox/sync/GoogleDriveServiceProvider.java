package edu.csupomona.cs585.ibox.sync;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

public class GoogleDriveServiceProvider {

	private static String CLIENT_ID = "363064861461-mj562uer7pc06hepb3e2u3us278cqhd1.apps.googleusercontent.com";
	private static String CLIENT_SECRET = "v2mh7lizh9nXHfkeAPRZd099";
	private static String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
	private static GoogleDriveServiceProvider INSTANCE;
	private Drive googleDriveClient;

	private GoogleDriveServiceProvider() {
		try {
			initGoogleDriveServices();
		} catch (IOException e) {
			System.out.println("Failed to create the Google drive client. Please check your Internet connection and your Google credentials.");
			e.printStackTrace();
		}
	}

	public static GoogleDriveServiceProvider get() {
		if (INSTANCE == null) {
			INSTANCE = new GoogleDriveServiceProvider();
		}
		return INSTANCE;
	}
	public void initGoogleDriveServices() throws IOException {
        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();

        try{
            GoogleCredential credential = new  GoogleCredential.Builder()
              .setTransport(httpTransport)
              .setJsonFactory(jsonFactory)
              .setServiceAccountId("144784541611-ecl3sj5f1l3l1hbm035lmua2j119nuce@developer.gserviceaccount.com")
              .setServiceAccountScopes(Collections.singleton(DriveScopes.DRIVE))
              .setServiceAccountPrivateKeyFromP12File(new File("GoogleSync-0e930e58cf1b.p12"))
              .build();

            googleDriveClient = new Drive.Builder(httpTransport, jsonFactory, credential).setApplicationName("ibox").build();
        }catch(GeneralSecurityException e){
            e.printStackTrace();
        }

    }    
	public Drive getGoogleDriveClient() {
		return googleDriveClient;
	}
}
