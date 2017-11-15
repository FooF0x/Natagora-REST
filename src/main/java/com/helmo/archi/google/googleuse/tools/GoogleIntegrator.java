package com.helmo.archi.google.googleuse.tools;

import com.helmo.archi.google.googleuse.ml.GoogleTranslate;
import com.helmo.archi.google.googleuse.ml.GoogleVision;
import com.helmo.archi.google.googleuse.model.Observation;
import com.helmo.archi.google.googleuse.model.User;
import com.helmo.archi.google.googleuse.storage.GoogleStorage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoogleIntegrator {
	
	private enum APIType {
		VISION,
		STORAGE,
		SQL,
		TRANSLATE
	}
	
	private GoogleVision vision;
	private GoogleTranslate translate;
	private GoogleStorage storage;
	
	
	public GoogleIntegrator() {	}
	
	private void lazyLoader(APIType... types) {
		try {
			for (APIType type : types) {
				switch (type) {
					case SQL:
//						if (pool == null)
//							pool = ConnectionPool.getInstance(Resources.getString("connectionString"));
						break;
					case VISION:
						if (vision == null) vision = new GoogleVision();
						break;
					case TRANSLATE:
						if (translate == null) translate = new GoogleTranslate();
						break;
					case STORAGE:
						if (storage == null) storage = new GoogleStorage("nat-test");
						break;
					default:
						throw new IllegalArgumentException("Not a good type");
				}
			}
		} catch (IOException e) {
			//TODO todo.
			e.printStackTrace();
		}
	}
	
	public List<String> getRGB(String localPath) throws Exception {
		lazyLoader(APIType.VISION);
		return vision.getRGB(localPath);
	}
	
	public Map<String, String> getFRSuggestions(String localPath) throws Exception {
		lazyLoader(APIType.VISION, APIType.TRANSLATE);
		Map<String, String> labelsEn = vision.labelAsMap(localPath);
		
		String temp = "";
		Map<String, String> labelsFR = new HashMap<>();
		for (String item : labelsEn.keySet()) temp += item + ";";
		
		String translation = translate.translateWithOption(temp.substring(0, temp.length() - 1), "en", "fr");
		
		String[] labelsFRItems = translation.split(",");
		int count = 0;
		for (String key : labelsEn.keySet()) labelsFR.put(labelsFRItems[count++].trim(), labelsEn.get(key).trim());
		
		return labelsFR;
	}
	
	public void saveAndProcessPicture(Observation... observs) throws Exception {
		lazyLoader(APIType.VISION, APIType.SQL);
		boolean wasNull;
		
//		if (wasNull = (sqlConnection == null)) sqlConnection = pool.getSQL();
		for (Observation obs : observs) { //TODO Does it work ?
			obs.setAnalyseResult(vision.safeSearchAnalyze(obs.getLocalAsString()));
			uploadPicture(obs.getLocalAsString(), obs.getOnlineAsString()); //To Async
		}
//		sqlConnection.addObservations(observs);
//		if (wasNull) sqlConnection.close();
	}
	
	private void uploadPicture(String localPath, String onlinePath) throws IOException { //TODO Work with path
		lazyLoader(APIType.STORAGE);
		
		String localExt = localPath.substring(localPath.lastIndexOf('.') + 1).toLowerCase();
		String onlineExt = onlinePath.substring(onlinePath.lastIndexOf('.') + 1).toLowerCase();
		
//		if (localExt.equals(onlineExt))
//			if (allowedExt.contains(localExt))
//				storage.uploadPicture(localPath, onlinePath, localExt);
//			else throw new IllegalArgumentException("file must be an image");
//		else throw new IllegalArgumentException("Extension not match");
	}
	
	public User loadSimpleUser(long id) {
		return null;
	}
	
	public long signUp(User usr, String password) throws IOException, SQLException {
		if(usr == null) throw new NullPointerException("User is NULL");
		if(password == null) throw new NullPointerException("Password is NULL");
		
		lazyLoader(APIType.SQL);
		boolean wasNull;
		
//		if (wasNull = (sql == null)) sql = pool.getSQL();
//		usr.setId(sql.addUser(usr));
//		sql.addPassword(usr, password); //TODO Check by id (-1)
		
//		if(usr.getOnlinePath() != null && usr.getLocalPath() != null)
//			if(usr.getOnlinePath().toString().isEmpty())
//				usr.setOnlinePath(Paths.get(Resources.getString("defaultPicOnlineLocation")));
//			else if(usr.getLocalPath().toString().isEmpty())
//				uploadPicture(usr.getLocalPath().toString(), usr.getOnlinePath().toString());
		
//			if (wasNull) sql.close();
		return usr.getId();
	}
	
	public User signIn(String email, String password) throws SQLException, IOException {
		if(email == null) throw new NullPointerException("Email is NULL");
		if(password == null) throw new NullPointerException("Password is NULL");
		
		lazyLoader(APIType.SQL);
		boolean wasNull;
		User rtn = new User();
//		if (wasNull = (sql == null)) sql = pool.getSQL();
//		rtn = (User) sql.getUserByLogin(email, password);
//		if (wasNull) sql.close();
		return rtn;
	}
}
