package org.rbattezzati.urlshortener.service;

import org.rbattezzati.urlshortener.dto.UrlDto;

public interface ShortenerUrlService {
    UrlDto getOriginalUrl(String shortenerUrl);
    UrlDto createOriginalUrl(UrlDto shortenerUrlDto);
    void cleanUpExpiredOriginalUrl();
}
