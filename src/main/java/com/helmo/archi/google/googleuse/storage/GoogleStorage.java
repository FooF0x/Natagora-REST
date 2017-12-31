package com.helmo.archi.google.googleuse.storage;

import com.google.cloud.ReadChannel;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.*;
import com.helmo.archi.google.googleuse.tools.HELMoCredentialsProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Component
public class GoogleStorage {
	
	private final Storage storage;
	private String bucketName;
	
	public GoogleStorage() {
		storage = StorageOptions.newBuilder()
			  .setCredentials(HELMoCredentialsProvider.getCredential())
			  .build()
			  .getService();
		bucketName = "natagora-grimar";
	}
	
	private Blob createBlob(Path path) {
		Blob blob = storage.get(BlobId.of(bucketName, path.toString().replace("\\", "/")));
		if (blob == null) {
			System.out.println("No such object");
			return null;
		}
		return blob;
	}
	
	public void uploadPicture(Path path, Path onlinePath, String ext) throws IOException {
		if (isASubfolder(onlinePath)) uploadFolder(onlinePath);
		uploadMedia(path, onlinePath, "image/" + ext);
	}
	
	public void uploadVideoMP4(Path path, Path onlinePath) throws IOException {
		if (isASubfolder(onlinePath)) uploadFolder(onlinePath);
		uploadMedia(path, onlinePath, "video/mp4");
	}
	
	public void uploadAudioMP3(Path path, Path onlinePath) throws IOException {
		if (isASubfolder(onlinePath)) uploadFolder(onlinePath);
		uploadMedia(path, onlinePath, "audio/mpeg");
	}
	
	private void uploadMedia(Path path, Path onlinePath, String mediaType) throws IOException {
		BlobId blobId = BlobId.of(bucketName, onlinePath.toString().replace("\\", "/"));
		BlobInfo blobInfo = BlobInfo
			  .newBuilder(blobId)
			  .setContentType(mediaType)
//			  .setAcl(new ArrayList<>(Arrays.asList(Acl.of(Acl.User.)))
			  .setAcl(new ArrayList<>(Arrays.asList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER))))
			  .build();
		
		uploadContent(path, blobInfo);
	}
	
	public void uploadFolder(Path fullFolderName) {
		BlobId blobId = BlobId.of(bucketName, fullFolderName + "/");
		BlobInfo blobInfo = BlobInfo
			  .newBuilder(blobId)
			  .setContentType("Folder/folder")
			  .build();
		storage.create(blobInfo, new byte[0]);
	}
	
	private void uploadContent(Path uploadFrom, BlobInfo blobInfo) throws IOException {
		if (Files.size(uploadFrom) > 1_000_000) {
			// When content is not available or large (1MB or more) it is recommended
			// to write it in chunks via the blob's channel writer.
			try (WriteChannel writer = storage.writer(blobInfo)) {
				byte[] buffer = new byte[1024];
				try (InputStream input = Files.newInputStream(uploadFrom)) {
					int limit;
					while ((limit = input.read(buffer)) >= 0) {
						writer.write(ByteBuffer.wrap(buffer, 0, limit));
					}
				}
			}
		} else {
			byte[] bytes = Files.readAllBytes(uploadFrom);
			// create the blob in one request.
			storage.create(blobInfo, bytes);
		}
	}
	
	private boolean isASubfolder(Path path) {
		return path.startsWith("\\");
	}
	
	public boolean deleteMedia(Path onlinePath) {
		return storage.delete(BlobId.of(bucketName, onlinePath.toString()));
	}
	
	public boolean exist(Path onlinePath) {
		return createBlob(onlinePath) != null;
	}
	
	public String getPublicLink(Path onlinePath) {
		Blob blob;
		if((blob =createBlob(onlinePath)) == null) throw new IllegalArgumentException("No such blob");
		BlobInfo info = BlobInfo.newBuilder(blob.getBlobId())
			  .setAcl(Arrays.asList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER)))
			  .build();
		try {
			storage.update(info);
		} catch (StorageException ex) {
			return "";
		}
		return "https://storage.googleapis.com/" + bucketName + "/" + onlinePath.toString().replace("\\", "/");
//		return blob.getMediaLink();
	}
}
