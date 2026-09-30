package com.glemser.invoice.config;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;

@Configuration
public class FirebaseConfig {
	
	@PostConstruct
	public void initFirebase() throws IOException {
		
		if(!FirebaseApp.getApps().isEmpty()) {
			return;
		}
		
		InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("firebase-service-account.json");
		
		if(serviceAccount == null) {
			throw new RuntimeException("Firebase service account file was not found");
		}
		
		FirebaseOptions options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
		
		FirebaseApp.initializeApp(options);
		
		System.out.println("Firebase connected successfully");
		
	}

}
