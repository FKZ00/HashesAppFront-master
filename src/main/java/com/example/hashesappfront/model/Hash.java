package com.example.hashesappfront.model;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hash {
  @Id
  private UUID id;

  @NotBlank
  private String hashValue;

  public String getHashValue() {
    return this.hashValue;
  }

  public void setHashValue(String hashValue) {
    this.hashValue = hashValue;
  }
}
