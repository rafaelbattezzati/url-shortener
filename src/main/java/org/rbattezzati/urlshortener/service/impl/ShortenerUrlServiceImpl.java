package org.rbattezzati.urlshortener.service.impl;

import org.rbattezzati.urlshortener.dto.UrlDto;
import org.rbattezzati.urlshortener.entity.UrlEntity;
import org.rbattezzati.urlshortener.repository.UrlRepository;
import org.rbattezzati.urlshortener.service.ShortenerUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.rbattezzati.urlshortener.util.Constants.MD_5;

@Service
public class ShortenerUrlServiceImpl implements ShortenerUrlService {
    @Autowired
    private UrlRepository urlRepository;
    @Override
    public UrlDto getOriginalUrl(String shortenerUrl) {
        UrlDto urlDto = null;
        // implement if shortenedUrl is present in cache
        // if not find it in DB and store in cache
        UrlEntity urlEntity = urlRepository.findByShortenedUrl(shortenerUrl);
        if(Optional.ofNullable(urlEntity).isPresent()) {
            urlDto = new UrlDto.Builder().shortenedUrl(urlEntity.getShortenedUrl()).originalUrl(urlEntity.getOriginalUrl()).build();
            return urlDto;
        }
        return null;
    }

    @Override
    public UrlDto createOriginalUrl(UrlDto originalUrlDto) {
        if(Optional.ofNullable(originalUrlDto).isPresent()) {
            String encodedUrl = createEncodedUrl(originalUrlDto);
            UrlEntity urlEntity = setUrlEntity(originalUrlDto, encodedUrl);
            saveOriginalUrl(urlEntity);
            UrlDto saveOriginalUrl = new UrlDto.Builder()
                    .shortenedUrl(urlEntity.getShortenedUrl())
                    .originalUrl(urlEntity.getOriginalUrl())
                    .createdAt(urlEntity.getCreatedAt())
                    .expiredAt(urlEntity.getExpiredAt())
                    .build();
            return saveOriginalUrl;
        }
        return null;
    }

    private UrlEntity saveOriginalUrl(UrlEntity urlEntity) {
        return urlRepository.save(urlEntity);
    }

    private static UrlEntity setUrlEntity(UrlDto originalUrlDto, String encodedUrl) {
        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setShortenedUrl(encodedUrl);
        urlEntity.setOriginalUrl(originalUrlDto.getOriginalUrl());
        urlEntity.setCreatedAt(LocalDateTime.now());
        urlEntity.setExpiredAt(LocalDateTime.now().plusDays(7L));
        return urlEntity;
    }

    private String createEncodedUrl(UrlDto originalUrlDto) {
        String originalUrl =  originalUrlDto.getOriginalUrl().concat(LocalDateTime.now().toString());
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(MD_5);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md.update(originalUrl.getBytes());
        byte[] b = md.digest();
        String encodedString = Base64.getUrlEncoder().encodeToString(b).substring(0, 6);
        return encodedString;
    }

    public void cleanUpExpiredOriginalUrl(){
        List<UrlEntity> allUrlEntities = urlRepository.findAll();
        List<UrlEntity> expiredUrlList = allUrlEntities.stream().filter(url -> url.getExpiredAt().isBefore(LocalDateTime.now())).collect(Collectors.toList());
        urlRepository.deleteAll(expiredUrlList);
    }
}
