package org.rbattezzati.urlshortener.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.rbattezzati.urlshortener.dto.UrlDto;
import org.rbattezzati.urlshortener.entity.UrlEntity;
import org.rbattezzati.urlshortener.repository.UrlRepository;
import org.rbattezzati.urlshortener.service.impl.ShortenerUrlServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Arrays;

@RunWith(SpringRunner.class)
public class ShortenerUrlServiceTest {

    /*UrlDto getOriginalUrl(String shortenerUrl);
    UrlDto createOriginalUrl(UrlDto shortenerUrlDto);
    UrlEntity saveOriginalUrl(UrlEntity urlEntity);
    void cleanUpExpiredOriginalUrl();*/

    @TestConfiguration
    static class ShortenerUrlServiceImplTestContextConfiguration {
        @Bean
        public ShortenerUrlService shortenerUrlService() {
            return new ShortenerUrlServiceImpl();
        }
    }
    @Autowired
    private ShortenerUrlService shortenerUrlService;

    @MockBean
    private UrlRepository shortenerUrlRepository;

    @Before
    public void setUp() {

        UrlDto urlDto = new UrlDto.Builder()
                .shortenedUrl("1234567")
                .originalUrl("www.google.com/search").build();

        UrlEntity urlEntity = new UrlEntity();
        urlEntity.setShortenedUrl(urlDto.getShortenedUrl());
        urlEntity.setOriginalUrl(urlDto.getOriginalUrl());
        Mockito.when(shortenerUrlRepository.findByShortenedUrl(urlDto.getShortenedUrl())).thenReturn(urlEntity);
        Mockito.when(shortenerUrlRepository.findByShortenedUrl("no-existing-shortened-url")).thenReturn(null);
    }

    /*
    @Test
    public void whenValidName_thenEmployeeShouldBeFound() {
        String name = "alex";
        Employee found = employeeService.getEmployeeByName(name);

        assertThat(found.getName()).isEqualTo(name);
    }

    @Test
    public void whenInValidName_thenEmployeeShouldNotBeFound() {
        Employee fromDb = employeeService.getEmployeeByName("wrong_name");
        assertThat(fromDb).isNull();

        verifyFindByNameIsCalledOnce("wrong_name");
    } */


    @Test
    public void whenGetShortenedUrlThenOriginalUrlShouldBeFound(){
        UrlDto urlDto = new UrlDto.Builder()
                .shortenedUrl("1234567")
                .originalUrl("www.google.com/search").build();

        Assertions.assertEquals(urlDto.getOriginalUrl(),
                shortenerUrlService.getOriginalUrl(urlDto.getShortenedUrl()).getOriginalUrl());
    }

    @Test
    public void whenGetShortenedUrlThenOriginalUrlShouldNotBeFound(){
        UrlDto urlDto = new UrlDto.Builder()
                .shortenedUrl("")
                .originalUrl("").build();

        Assertions.assertNotEquals(urlDto.getOriginalUrl(),
                shortenerUrlService.getOriginalUrl(urlDto.getShortenedUrl()));
    }

    @Test
    @Rollback
    public void testCreateOriginalUrl(){
        UrlDto urlDto = new UrlDto.Builder()
                .shortenedUrl("1234567")
                .originalUrl("www.google.com/search").build();

        Assertions.assertEquals(urlDto.getOriginalUrl(),
                shortenerUrlService.createOriginalUrl(urlDto).getOriginalUrl());
    }

    @Test
    @Rollback
    public void testCleanUpExpiredOriginalUrl(){
        UrlDto urlDto = new UrlDto.Builder()
                .shortenedUrl("7654321")
                .originalUrl("www.twitter.com")
                .createdAt(LocalDateTime.now().minusDays(-7))
                .expiredAt(LocalDateTime.now())
                .build();

        UrlDto urlDto2 = new UrlDto.Builder()
                .shortenedUrl("3333333")
                .originalUrl("www.linkedin.com/")
                .createdAt(LocalDateTime.now().minusDays(-7))
                .expiredAt(LocalDateTime.now())
                .build();

        UrlDto urlDto3 = new UrlDto.Builder()
                .shortenedUrl("1234567")
                .originalUrl("www.google.com/search")
                .createdAt(LocalDateTime.now().minusDays(-7))
                .expiredAt(LocalDateTime.now())
                .build();

        shortenerUrlService.createOriginalUrl(urlDto);
        shortenerUrlService.createOriginalUrl(urlDto2);
        shortenerUrlService.createOriginalUrl(urlDto3);
        shortenerUrlService.cleanUpExpiredOriginalUrl();

        Assertions.assertEquals(Arrays.asList(), shortenerUrlRepository.findAll());
    }




}
