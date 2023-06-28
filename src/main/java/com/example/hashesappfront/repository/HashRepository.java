package com.example.hashesappfront.repository;

import com.example.hashesappfront.config.RestApiConfig;
import com.example.hashesappfront.model.Hash;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.JsonbBuilder;
import jakarta.servlet.http.Part;
import jakarta.ws.rs.WebApplicationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

@NoArgsConstructor
@AllArgsConstructor
@ApplicationScoped
public class HashRepository {
  @Inject
  private HttpClient httpClient;

  public Hash saveHash(Hash hash) throws URISyntaxException, IOException, InterruptedException {
    var json = JsonbBuilder.create().toJson(hash);
    HttpRequest httpRequest = HttpRequest.newBuilder()
        .uri(new URI(RestApiConfig.hashApiUrl))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(json))
        .build();
    HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() != 201) {
      throw new WebApplicationException(response.statusCode());
    }

    var reader = response.body();

    return JsonbBuilder.create().fromJson(reader, Hash.class);
  }

  public List<Hash> getAllHashes() throws URISyntaxException, IOException, InterruptedException {
    HttpRequest httpRequest = HttpRequest.newBuilder()
        .uri(new URI(RestApiConfig.getAllHashesApiUrl))
        .GET()
        .build();
    HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

    var reader = response.body();
    return JsonbBuilder.create().fromJson(reader, new ArrayList<Hash>(){}.getClass().getGenericSuperclass());
  }

  public void removeHash(UUID uuid) throws URISyntaxException, IOException, InterruptedException {
    HttpRequest httpRequest = HttpRequest.newBuilder()
        .uri(new URI(RestApiConfig.hashApiUrl + "/id/" + uuid))
        .DELETE()
        .build();
    HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

    if (response.statusCode() != 200) {
      throw new WebApplicationException(response.statusCode());
    }
  }

  public Hash generateHash(File fileToSend) throws IOException {
    HttpPost httpPost = new HttpPost(RestApiConfig.generateHashApiUrl);

    FileBody fileBody = new FileBody(fileToSend, ContentType.DEFAULT_BINARY);
    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    builder.addPart("file", fileBody);
    HttpEntity entity = builder.build();

    httpPost.setEntity(entity);

    try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
      CloseableHttpResponse response = client.execute(httpPost);
      var reader = response.getEntity().getContent();
      return JsonbBuilder.create().fromJson(reader, Hash.class);
    }
//    CloseableHttpClient httpClient = HttpClients.createDefault();
//    HttpPost httpPost = new HttpPost(RestApiConfig.generateHashApiUrl);
//
//    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//    builder.addTextBody("file", "yes", ContentType.TEXT_PLAIN);
//
//    builder.addBinaryBody(
//        "file",
//        new FileInputStream(uploadedFile.getContentType()),
//        ContentType.DEFAULT_BINARY,
//        uploadedFile.getName()
//    );
//
//    HttpEntity multipart = builder.build();
//    httpPost.setEntity(multipart);
//
//    CloseableHttpResponse response = httpClient.execute(httpPost);
//    HttpEntity responseEntity = response.getEntity();
//
//    String responseString = EntityUtils.toString(responseEntity);
//    return JsonbBuilder.create().fromJson(responseString, Hash.class);
  }
}
