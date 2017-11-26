package com.helmo.archi.google.googleuse.storage;

import com.google.cloud.ReadChannel;
import com.google.cloud.WriteChannel;
import com.google.cloud.storage.*;
import com.helmo.archi.google.googleuse.tools.HELMoCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Component
public class GoogleStorage { //TODO Work with path not strings
	
	private final Storage storage;
//	@Value("${google.storage.bucketName}")
	private String bucketName;
	
	public GoogleStorage() {
		storage = StorageOptions.newBuilder()
				.setCredentials(HELMoCredentialsProvider.getCredential())
				.build()
				.getService();
		bucketName = "nat-test";
	}
	
	public void uploadPicture(String path, String onlinePath, String ext) throws IOException {
		if(isASubfolder(onlinePath)) createTreeFolder(onlinePath);
		uploadMedia(path, formatToOnlinePath(onlinePath), "image/" + ext);
	}
	
	public void uploadVideoMP4(String path, String onlinePath) throws IOException {
		if(isASubfolder(onlinePath)) createTreeFolder(onlinePath);
		uploadMedia(path, formatToOnlinePath(onlinePath), "video/mp4");
	}
	
	private void uploadMedia(String path, String onlinePath, String mediaType) throws IOException {
		BlobId blobId = BlobId.of(bucketName, onlinePath);
		BlobInfo blobInfo = BlobInfo
				.newBuilder(blobId)
				.setContentType(mediaType)
				.build();
		
		uploadContent(Paths.get(path), blobInfo);
	}
	
	public void uploadFolder(String bucket, String fullFolderName) {
		BlobId blobId = BlobId.of(bucket, fullFolderName + "/");
		BlobInfo blobInfo = BlobInfo
				.newBuilder(blobId)
				.setContentType("Folder/folder")
				.build();
		storage.create(blobInfo,new  byte[0]);
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
	
	private boolean isASubfolder(String path) {
		return path.contains("\\") || path.contains("/");
	}
	
	private void createTreeFolder(String path) {
		path = formatToOnlinePath(path);
		String[] tree = path.split("/");
		String temp = "";
		for(int i=0; i<tree.length-1; i++) {
			temp += tree[i];
			uploadFolder(bucketName, temp);
		}
	}
	
	public byte[] getMedia(String onlinePath) throws IOException {
		onlinePath = formatToOnlinePath(onlinePath);
		Blob blob = storage.get(BlobId.of(bucketName, onlinePath));
		if (blob == null) {
			System.out.println("No such object");
			return new byte[0];
		}
		
		byte[] rtn = new byte[1];
		
		if (blob.getSize() < 1_000_000) {
			// Blob is small read all its content in one request
			 return blob.getContent();
		} else {
			// When Blob size is big or unknown use the blob's channel reader.
			try (ReadChannel reader = blob.reader()) {
//				byte[] content = new byte[64 * 1024];
				List<Byte> content = new LinkedList<>();
				ByteBuffer bytes = ByteBuffer.allocate(64 * 1024);
				while (reader.read(bytes) > 0) {
					bytes.flip();
					for(byte tmp : bytes.array())
						content.add(tmp);
					bytes.clear();
				}
				rtn = new byte[content.size()];
				for(int i = 0; i < rtn.length; i++)
					rtn[i] = content.get(i);
			}
		}
		return rtn;
	}
	
	public void getMedia(String onlinePath, String strLocalPath) throws IOException {
		onlinePath = formatToOnlinePath(onlinePath);
		Blob blob = storage.get(BlobId.of(bucketName, onlinePath));
		if (blob == null) {
			System.out.println("No such object");
			return;
		}
		
		Path downloadTo = Paths.get(strLocalPath);
		PrintStream writeTo = System.out;
		if (downloadTo != null) {
			writeTo = new PrintStream(new FileOutputStream(downloadTo.toFile()));
		}
//		if (blob.getSize() < 1_000_000) {
//			// Blob is small read all its content in one request
//			byte[] content = blob.getContent();
//			writeTo.write(content);
//		} else {
//			// When Blob size is big or unknown use the blob's channel reader.
////			try (ReadChannel reader = storage.reader(bucketName, blob.getName())) {
//			try (ReadChannel reader = blob.reader()) {
//				WritableByteChannel channel = Channels.newChannel(writeTo);
//				ByteBuffer bytes = ByteBuffer.allocate(64 * 1024);
//				while (reader.read(bytes) > 0) {
//					bytes.flip();
//					channel.write(bytes);
//					bytes.clear();
//				}
//			}
//		}
		if (downloadTo == null) {
			writeTo.println();
		} else {
			writeTo.close();
		}
	}
	
	public boolean deleteMedia(String onlinePath) {
		return storage.delete(BlobId.of(bucketName, onlinePath));
	}
	
	private String formatToOnlinePath(String path) {
		return path.replace("\\", "/");
	}
	
	public boolean exist(String onlinePath) {
		try {
			return getMedia(onlinePath).length == 0;
		} catch (IOException ex) {
			return false;
		}
	}
}
