package com.example.hashesappfront.config;


public class RestApiConfig {
  public static String restApiUrl = "http://localhost:8080/HashesApp-1.0-SNAPSHOT/api";
  public static String hashApiUrl = restApiUrl + "/hash";
  public static String getAllHashesApiUrl = hashApiUrl + "/all";
  public static String generateHashApiUrl = hashApiUrl + "/generate";
}
