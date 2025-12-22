package com.wannaverse.users.persistence.elastic;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@AllArgsConstructor
@Document(indexName = "users")
public class ElasticUser {
    @Id private Long id;
    private String firstName;
    private String lastName;
    private String displayName;
}
