package com.helmo.archi.google.googleuse.storage;

import com.google.cloud.datastore.*;
import com.helmo.archi.google.googleuse.model.Bird;
import com.helmo.archi.google.googleuse.tools.HELMoCredentialsProvider;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BirdDatastore implements BirdDAO {
	
	private final Datastore datastore;
	private final KeyFactory rootKeyFact;
	private final String rootKind;
	private final String dataKind;
	private final String pictureKind;
	private final String multipleKind;
	private final String childKind;
	
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
		this.rootKind = "birds";
		dataKind = "data";
		pictureKind = "picture";
		multipleKind = "multiple";
		childKind = "child";
		
		this.rootKeyFact = datastore
				.newKeyFactory()
				.setKind(rootKind);
	}
	
	private KeyFactory getKeyFact() {
		return datastore.newKeyFactory();
	}
	
	private Bird entityToBird(Entity entity) {
		Bird bird = new Bird(); //TODO Define builder
		bird.setId(entity.getKey().getId());
		bird.setName(entity.getString(Bird.NAME));
		bird.setDescription(entity.getString(Bird.DESCRIPTION));
		bird.setData(new HashMap<>());
		bird.setPicture(new ArrayList<>());
		bird.setMultiple(new HashMap<>());
		
		Set<String> names = entity.getNames();
		
		for(String item : names)
			if(!item.equals(Bird.NAME.toLowerCase()) && !item.equals(Bird.DESCRIPTION.toLowerCase())) {
				String[] values = item.split("_");
				switch (values[0]) {
					case "DATA" :
						bird.putIntoData(item.substring(5), entity.getString(item));
						break;
					case "PICTURE" :
						bird.add(entity.getString(item));
						break;
					case "MULTIPLE" :
						bird.putIntoMultiple(values[1], entity.getString(item));
						break;
				}
			}
		
		return bird;
	}
	
	private Entity birdToEntity(Bird bird) {
		Key key = rootKeyFact.newKey(bird.getId());
		Entity entity = Entity.newBuilder(key)
				.set(Bird.NAME, bird.getName())
				.set(Bird.DESCRIPTION, bird.getDescription())
				.build();
		
		for(int i = 0; i < bird.getPicture().size(); i++)
			entity = Entity.newBuilder(entity)
					.set(Bird.PICTURE.toUpperCase() + "_" + i, bird.getFromPicture(i))
					.build();
		
		for(String itemKey : bird.getData().keySet()) {
			entity = Entity.newBuilder(entity)
					.set(Bird.DATA.toUpperCase() + "_" + itemKey, bird.getFromData(itemKey))
					.build();
		}
		
		for(String itemKey : bird.getMultiple().keySet()) {
			List<String> values = bird.getFromMultiple(itemKey);
			for(int i = 0; i < values.size(); i++)
				entity = Entity.newBuilder(entity)
						.set(Bird.MULTIPLE.toUpperCase() + "_" + itemKey + "_" + i, values.get(i))
						.build();
		}
		
		return entity;
	}
	
//	@Override
//	public Long save(Bird bird) {
//		IncompleteKey birdKey = rootKeyFact.newKey();          // Key will be assigned once written
//		FullEntity<IncompleteKey> incBirdEntity = Entity.newBuilder(birdKey)  // Create the Entity
//				.set(Bird.NAME, bird.getName())
//				.set(Bird.DESCRIPTION, bird.getDescription())
//				.build();
//
//		FullEntity<IncompleteKey> incDataEntity = createDataEntity(bird);
//
//		for(String itemKey : bird.getMultiple().keySet()) {
//			List<String> values = bird.getFromMultiple(itemKey);
//			for(int i = 0; i < values.size(); i++)
//				incBirdEntity = Entity.newBuilder(incBirdEntity)
//						.set(Bird.MULTIPLE.toUpperCase() + "_" + itemKey + "_" + i, values.get(i))
//						.build();
//		}
//
//		Entity birdEntity = datastore.add(incBirdEntity); // Save the Entity
//		return birdEntity.getKey().getId();
//	}
	
	private FullEntity<IncompleteKey> createDataEntity(Bird bird){
		IncompleteKey dataKey = getKeyFact()
				.addAncestor(PathElement.of(rootKind, bird.getId()))
				.setKind(dataKind)
				.newKey();
		
		FullEntity<IncompleteKey> incDataEntity = Entity.newBuilder(dataKey)
				.build();
		
		for(String itemKey : bird.getData().keySet()) {
			incDataEntity = Entity.newBuilder(incDataEntity)
					.set(itemKey, bird.getFromData(itemKey))
					.build();
		}
		return incDataEntity;
	}
	
	private FullEntity<IncompleteKey> createPictureEntity(Bird bird) {
		IncompleteKey pictureEntity = getKeyFact()
			.addAncestor(PathElement.of(rootKind, bird.getId()))
			.setKind(pictureKind)
			.newKey();
		
		FullEntity<IncompleteKey> incPictureEntity = Entity.newBuilder(pictureEntity)
				.build();
		
		for(int i = 0; i < bird.getPicture().size(); i++)
			incPictureEntity = Entity.newBuilder(incPictureEntity)
					.set(Integer.toString(i), bird.getFromPicture(i))
					.build();
		return incPictureEntity;
	}
	
	private FullEntity<IncompleteKey> createMultipleEntity(Bird bird) {
		IncompleteKey multipleEntity = getKeyFact()
			.addAncestor(PathElement.of(rootKind, bird.getId()))
			.setKind(multipleKind)
			.newKey();
		
		FullEntity<IncompleteKey> inMultipleEntity = Entity.newBuilder(multipleEntity)
				.build();
		
		for(String itemKey : bird.getMultiple().keySet())
			createMultipleChildEntity(bird, itemKey, bird.getFromMultiple(itemKey));
		
		return inMultipleEntity;
	}
	
	private FullEntity<IncompleteKey> createMultipleChildEntity(Bird bird, String name, List<String> items) {
		IncompleteKey childEntity = getKeyFact()
			.addAncestor(PathElement.of(rootKind, bird.getId()))
			.setKind(childKind)
			.newKey();
		
		FullEntity<IncompleteKey> incPictureEntity = Entity.newBuilder(childEntity)
				.build();
		
		for(int i = 0; i < bird.getPicture().size(); i++)
			incPictureEntity = Entity.newBuilder(incPictureEntity)
					.set(Integer.toString(i), bird.getFromPicture(i))
					.build();
		return incPictureEntity;
	}
	
	
	
	@Override
	public Long save(Bird bird) {
		IncompleteKey key = rootKeyFact.newKey();          // Key will be assigned once written
		FullEntity<IncompleteKey> incBirdEntity = Entity.newBuilder(key)  // Create the Entity
				.set(Bird.NAME, bird.getName())
				.set(Bird.DESCRIPTION, bird.getDescription())
				.build();

		for(int i = 0; i < bird.getPicture().size(); i++)
			incBirdEntity = Entity.newBuilder(incBirdEntity)
					.set(Bird.PICTURE.toUpperCase() + "_" + i, bird.getFromPicture(i))
					.build();

		for(String itemKey : bird.getData().keySet()) {
			incBirdEntity = Entity.newBuilder(incBirdEntity)
					.set(Bird.DATA.toUpperCase() + "_" + itemKey, bird.getFromData(itemKey))
					.build();
		}

		for(String itemKey : bird.getMultiple().keySet()) {
			List<String> values = bird.getFromMultiple(itemKey);
			for(int i = 0; i < values.size(); i++)
				incBirdEntity = Entity.newBuilder(incBirdEntity)
						.set(Bird.MULTIPLE.toUpperCase() + "_" + itemKey + "_" + i, values.get(i))
						.build();
		}

		Entity birdEntity = datastore.add(incBirdEntity); // Save the Entity
		return birdEntity.getKey().getId();
	}
	
	@Override
	public List<Bird> findAll() {
		EntityQuery query = Query.newEntityQueryBuilder()
				.setKind(rootKind)
				.build();
		QueryResults<Entity> result = datastore.run(query);
		List<Bird> rtn = new ArrayList<>();
		while (result.hasNext())
			rtn.add(entityToBird(result.next()));
		return rtn;
	}
	
	@Override
	public Bird find(Long id) {
		return entityToBird(datastore.get(rootKeyFact.newKey(id)));
	}
	
	@Override
	public void update(Bird entity) {
		datastore.update(birdToEntity(entity));
	}
	
	@Override
	public void delete(Long id) {
		datastore.delete(rootKeyFact.newKey(id));
	}
	
	@Override
	public boolean exist(Long id) {
		return datastore.get(rootKeyFact.newKey(id)) != null;
	}
}
