package org.rbattezzati.urlshortener.dto;

import java.time.LocalDateTime;

public class UrlDto {

    //Mandatory attribute
    private final String originalUrl;
    //Mandatory attribute
    private final String shortenedUrl;
    //Not mandatory
    private final LocalDateTime createdAt;
    //Not mandatory
    private final LocalDateTime expiredAt;

    public UrlDto(String originalUrl, String shortenedUrl, LocalDateTime createdAt, LocalDateTime expiredAt) {
        this.originalUrl = originalUrl;
        this.shortenedUrl = shortenedUrl;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
    }

    public static class Builder {
        private String originalUrl = "";
        private String shortenedUrl = "";
        private LocalDateTime createdAt;
        private LocalDateTime expiredAt;

        public Builder() {}

        public Builder originalUrl(String originalUrl) {
            this.originalUrl = originalUrl;
            return this;
        }

        public Builder shortenedUrl(String shortenedUrl) {
            this.shortenedUrl = shortenedUrl;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder expiredAt(LocalDateTime expiredAt) {
            this.expiredAt = expiredAt;
            return this;
        }

        public UrlDto build() {
            return new UrlDto(originalUrl,shortenedUrl, createdAt, expiredAt);
        }
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getShortenedUrl() {
        return shortenedUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }
}
