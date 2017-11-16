package com.helmo.archi.google.googleuse.storage;

import com.google.cloud.datastore.*;
import com.helmo.archi.google.googleuse.model.Bird;
import com.helmo.archi.google.googleuse.tools.HELMoCredentialsProvider;

import java.io.IOException;
import java.util.*;

public class GoogleDatastore implements BirdDAO {
	
	private final Datastore datastore;
	private final KeyFactory keyFactory;
	private final String kind;
	
	/*
	RESUME :
		Kind = Table
		Entity = Row
		Key = Primary Key
		Property = Field
	*/
	
	public GoogleDatastore() {
		this.datastore = DatastoreOptions.newBuilder()
				.setCredentials(HELMoCredentialsProvider.getCredential())
				.build()
				.getService();
		this.kind = "birds";
		this.keyFactory = datastore
				.newKeyFactory()
				.setKind(kind);
	}
	
	private void test() {
		// The kind for the new entity
		String kind = "Task";
		// The name/ID for the new entity
		String name = "sampletask1";
		// The Cloud Datastore key for the new entity
		Key taskKey = datastore.newKeyFactory().setKind(kind).newKey(name);
		
		// Prepares the new entity
		Entity task = Entity.newBuilder(taskKey)
				.set("description", "Buy milk")
				.build();
		
		// Saves the entity
		datastore.put(task);
		
		/*
		KeyFactory keyFactory = datastore.newKeyFactory().setKind("Person");
		Key key = keyFactory.newKey("john.doe@gmail.com");
		Entity entity = Entity.newBuilder(key)
		    .set("name", "John Doe")
		    .set("age", 51)
		    .set("favorite_food", "pizza")
		    .build();
		datastore.put(entity);
		 */
		
	}
	
	public Bird entityToBird(Entity entity) {
		Bird bird = new Bird(); //TODO Define builder
		bird.setName(entity.getString(Bird.NAME));
		bird.setDescription(entity.getString(Bird.DESCRIPTION));
		bird.setDatas(new HashMap<>());
		Set<String> names = entity.getNames();
		
		for(String item : names)
			if(!item.equals(Bird.NAME.toLowerCase()) && !item.equals(Bird.DESCRIPTION.toLowerCase()))
			bird.put(item, entity.getString(item));
		
		return bird;
	}
	
	@Override
	public Long createBird(Bird bird) {
		IncompleteKey key = keyFactory.newKey();          // Key will be assigned once written
		FullEntity<IncompleteKey> incBirdEntity = Entity.newBuilder(key)  // Create the Entity
				.set(Bird.NAME, bird.getName())
				.set(Bird.DESCRIPTION, bird.getDescription())
				.build();
		FullEntity<IncompleteKey> temp = Entity.newBuilder(incBirdEntity).build();
		for(String itemKey : bird.getDatas().keySet()) {
			temp = Entity.newBuilder(temp)
					.set(itemKey, bird.get(itemKey))
					.build();
		}
		
		Entity birdEntity = datastore.add(temp); // Save the Entity
		return birdEntity.getKey().getId();
	}
	
	@Override
	public Bird readBird(Long idBird) {
		Entity birdEntity = datastore.get(keyFactory.newKey(idBird)); // Load an Entity for Key(id)
		return entityToBird(birdEntity);
		
	}
	
	@Override
	public void updateBird(Bird bird) {
	
	}
	
	@Override
	public void deleteBird(Bird bird) {
	
	}
	
	@Override
	public List<Bird> findAll() {
		EntityQuery query = Query.newEntityQueryBuilder()
				.setKind(kind)
				.build();
		QueryResults<Entity> result = datastore.run(query);
		List<Bird> rtn = new ArrayList<>();
		while (result.hasNext())
			rtn.add(entityToBird(result.next()));
		return rtn;
	}
}
