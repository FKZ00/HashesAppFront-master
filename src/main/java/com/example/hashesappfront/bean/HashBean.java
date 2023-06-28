package com.example.hashesappfront.bean;

import com.example.hashesappfront.model.Hash;
import com.example.hashesappfront.service.HashService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Named("hashBean")
@ViewScoped
public class HashBean implements Serializable {
  @Inject
  private HashService hashService;
  private List<Hash> hashList;
  private Hash hashToAdd;
  private Part uploadedFile;
  private File fileToSend;

  public File getFileToSend() {
    return fileToSend;
  }

  public void setFileToSend(File file) {
    fileToSend = file;
  }

  public List<Hash> getHashList() {
    return hashList;
  }

  public void setUploadedFile(Part uploadedFile) {
    this.uploadedFile = uploadedFile;
  }

  public void setHashToAdd(Hash hashToAdd) {
    this.hashToAdd = hashToAdd;
  }

  public Hash getHashToAdd() {
    return this.hashToAdd;
  }

  public void uploadFile() throws IOException, URISyntaxException, InterruptedException {
    saveFile();
    setHashToAdd(hashService.generateHash(this.fileToSend));
  }

  public Part getUploadedFile() {
    return this.uploadedFile;
  }

  public void saveHash() {
    try {
      setHashToAdd(hashService.saveHash(getHashToAdd()));
    } catch (Exception e) {
      e.printStackTrace();
    }
    init();
  }

  public void removeHash(String id) {
    try {
      UUID uuid = UUID.fromString(id);
      hashService.removeHash(uuid);
    } catch (Exception e) {
      e.printStackTrace();
    }
    init();
  }

  public void saveFile() {
    try (InputStream input = uploadedFile.getInputStream()) {
      fileToSend = new File(".", uploadedFile.getSubmittedFileName());
      Files.copy(input, fileToSend.toPath(), StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @PostConstruct
  public void init() {
    try {
      hashList = hashService.getAllHashes();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
