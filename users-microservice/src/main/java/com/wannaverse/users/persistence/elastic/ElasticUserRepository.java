package com.wannaverse.users.persistence.elastic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElasticUserRepository extends ElasticsearchRepository<ElasticUser, Long> {
    Page<ElasticUser> findUserByFirstNameIgnoreCaseOrLastNameIgnoreCaseOrDisplayNameIgnoreCase(
            String firstName, String lastName, String displayName, Pageable pageable);
}
