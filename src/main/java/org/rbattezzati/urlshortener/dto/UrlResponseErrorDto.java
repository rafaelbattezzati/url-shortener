package org.rbattezzati.urlshortener.dto;

public class UrlResponseErrorDto {

    private String errorUrl;
    private String errorMessage;

    public UrlResponseErrorDto(String errorUrl, String errorMessage) {
        this.errorUrl = errorUrl;
        this.errorMessage = errorMessage;
    }
}
