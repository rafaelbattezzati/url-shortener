package org.rbattezzati.urlshortener.controller;

import org.rbattezzati.urlshortener.dto.UrlDto;
import org.rbattezzati.urlshortener.service.ShortenerUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static org.rbattezzati.urlshortener.util.Constants.*;

@RequestMapping("/shortener-url")
@RestController
public class ShortenerUrlController {
    @Autowired
    private ShortenerUrlService shortenerUrlService;

    public ShortenerUrlController(ShortenerUrlService shortenerUrlService) {
        this.shortenerUrlService = shortenerUrlService;
    }

    public ShortenerUrlController() {

    }

    @GetMapping("/get/{shortenedUrl}")
    public ResponseEntity<?> getOriginalUrl(@PathVariable String shortenedUrl, HttpServletResponse response) throws IOException {
        if(Optional.ofNullable(shortenedUrl).isPresent()) {
            UrlDto resultUrlDto = shortenerUrlService.getOriginalUrl(shortenedUrl);
            if (Optional.ofNullable(resultUrlDto).isPresent()) {
                response.sendRedirect(resultUrlDto.getOriginalUrl());
                return new ResponseEntity<String>(PAGE_REDIRECTED_SUCCESSFULLY, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(INVALID_SHORTENED_URL, HttpStatus.NOT_ACCEPTABLE);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveOriginalUrl(@RequestBody UrlDto shortenerUrlDto) {
        UrlDto savedUrlDto = shortenerUrlService.createOriginalUrl(shortenerUrlDto);
        if(Optional.ofNullable(savedUrlDto).isPresent()) {
            return new ResponseEntity<UrlDto>(savedUrlDto, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(SHORTENED_URL_NOT_SAVED, HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping("/cleanup")
    public ResponseEntity<?> cleanUpExpiredOriginalUrl() throws IOException {
        shortenerUrlService.cleanUpExpiredOriginalUrl();
        return new ResponseEntity<String>(ALL_EXPIRED_SHORTENED_URL_WERE_CLEANED_UP, HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<?> getEmptyOriginalUrl() throws IOException {
        return new ResponseEntity<>(INVALID_SHORTENED_URL, HttpStatus.NOT_ACCEPTABLE);
    }
}

