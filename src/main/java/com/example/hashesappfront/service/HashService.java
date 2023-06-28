package com.example.hashesappfront.service;

import com.example.hashesappfront.model.Hash;
import com.example.hashesappfront.repository.HashRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@ApplicationScoped
public class HashService {
  @Inject
  private HashRepository hashRepository;

  public Hash saveHash(Hash hash) throws URISyntaxException, IOException, InterruptedException {
    return hashRepository.saveHash(hash);
  }

  public List<Hash> getAllHashes() throws URISyntaxException, IOException, InterruptedException {
    return hashRepository.getAllHashes();
  }

  public void removeHash(UUID uuid) throws URISyntaxException, IOException, InterruptedException {
    hashRepository.removeHash(uuid);
  }

  public Hash generateHash(File fileToSend) throws IOException, URISyntaxException, InterruptedException {
    return hashRepository.generateHash(fileToSend);
  }
}
