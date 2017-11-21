package com.helmo.archi.google.googleuse.storage;

import com.mongodb.*;

import java.net.UnknownHostException;

public class BirdMongoDB {
	
	private MongoClient mongoClient;
	
	private void test() {
		mongoClient = MongoClientProvider.getInstance();
		DB database = mongoClient.getDB("NatagoraDb");
		DBCollection collection = database.getCollection("birds");
		
		DBObject object = new BasicDBObject();
		
	}
	
}
