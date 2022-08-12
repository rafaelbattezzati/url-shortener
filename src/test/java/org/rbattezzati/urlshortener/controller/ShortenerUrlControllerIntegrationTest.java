package org.rbattezzati.urlshortener.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.rbattezzati.urlshortener.dto.UrlDto;
import org.rbattezzati.urlshortener.service.ShortenerUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.rbattezzati.urlshortener.util.JsonUtil;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ShortenerUrlController.class)
public class ShortenerUrlControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ShortenerUrlService shortenerUrlService;

    @Test
    public void givenShortenedUrl_whenGetOriginalUrl_thenRedirectToOriginalUrl()
            throws Exception {

        UrlDto urlDto = new UrlDto.Builder()
                .shortenedUrl("1234567")
                .originalUrl("www.google.com/search").build();

        given(shortenerUrlService.getOriginalUrl("1234567")).willReturn(urlDto);

        mvc.perform(get("/shortener-url/get/1234567")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void whenPostOriginalUrl_thenCreateShortenedUrl() throws Exception {
        UrlDto urlDto = new UrlDto.Builder()
                .shortenedUrl("1234567")
                .originalUrl("www.google.com/search").build();

        given(shortenerUrlService.createOriginalUrl(Mockito.any())).willReturn(urlDto);

        mvc.perform(post("/shortener-url/save").contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(urlDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("shortenedUrl", is("1234567")));
        verify(shortenerUrlService, VerificationModeFactory.times(1)).createOriginalUrl(Mockito.any());
        reset(shortenerUrlService);
    }

}
