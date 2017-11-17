package com.helmo.archi.google.googleuse.storage;

import com.google.cloud.datastore.*;
import com.helmo.archi.google.googleuse.model.Bird;
import com.helmo.archi.google.googleuse.tools.HELMoCredentialsProvider;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BirdDatastore implements BirdDAO {
	
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
	
	public BirdDatastore() {
		this.datastore = DatastoreOptions.newBuilder()
				.setCredentials(HELMoCredentialsProvider.getCredential())
				.build()
				.getService();
		this.kind = "birds";
		this.keyFactory = datastore
				.newKeyFactory()
				.setKind(kind);
	}
	
	private Bird entityToBird(Entity entity) {
		Bird bird = new Bird(); //TODO Define builder
		bird.setId(entity.getKey().getId());
		bird.setName(entity.getString(Bird.NAME));
		bird.setDescription(entity.getString(Bird.DESCRIPTION));
		bird.setDatas(new HashMap<>());
		Set<String> names = entity.getNames();
		
		for(String item : names)
			if(!item.equals(Bird.NAME.toLowerCase()) && !item.equals(Bird.DESCRIPTION.toLowerCase()))
			bird.put(item, entity.getString(item));
		
		return bird;
	}
	
	private Entity birdToEntity(Bird bird) {
		Key key = keyFactory.newKey(bird.getId());
		Entity entity = Entity.newBuilder(key)
				.set(Bird.NAME, bird.getName())
				.set(Bird.DESCRIPTION, bird.getDescription())
				.build();
		Entity temp = Entity.newBuilder(entity).build();
		for(String itemKey : bird.getDatas().keySet()) {
			temp = Entity.newBuilder(temp)
					.set(itemKey, bird.get(itemKey))
					.build();
		}
		
		return temp;
	}
	
	@Override
	public Long save(Bird bird) {
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
	
	
	public Bird readBird(Long idBird) {
		Entity birdEntity = datastore.get(keyFactory.newKey(idBird)); // Load an Entity for Key(id)
		return entityToBird(birdEntity);
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
	
	@Override
	public Bird find(Long id) {
		return entityToBird(datastore.get(keyFactory.newKey(id)));
	}
	
	@Override
	public void update(Bird entity) {
		datastore.update(birdToEntity(entity));
	}
}
