package com.helmo.archi.google.googleuse.storage;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.net.UnknownHostException;

public class MongoClientProvider {
	
	private static MongoClient instance;
	
	private MongoClientProvider() {
	}
	
	public static synchronized MongoClient getInstance(){
		if(instance == null){
			synchronized (MongoClientProvider.class) {
				if(instance == null){
					try {
						instance = new MongoClient(new MongoClientURI("mongodb://root:mynameisroot@92.222.9.70:27017"));
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return instance;
	}
}
