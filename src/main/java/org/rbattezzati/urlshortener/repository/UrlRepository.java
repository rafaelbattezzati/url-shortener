package org.rbattezzati.urlshortener.repository;

import org.rbattezzati.urlshortener.entity.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UrlRepository extends JpaRepository<UrlEntity, Long> {
    UrlEntity findByShortenedUrl(String shortenedUrl);
}
