package com.example.hashesappfront.producers;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import java.net.ProxySelector;
import java.net.http.HttpClient;

@ApplicationScoped
public class HttpProducer {
  @Produces
  @ApplicationScoped
  public HttpClient getHttpClient() {
    return HttpClient.newBuilder()
        .proxy(ProxySelector.getDefault())
        .build();
  }
}
