package com.company.controller;

import com.company.dto.ShortUrlDTO;
import com.company.dto.converter.ShortUrlDTOConverter;
import com.company.dto.converter.ShortUrlRequestConverter;
import com.company.dto.request.ShortUrlRequest;
import com.company.model.ShortUrl;
import com.company.service.ShortUrlService;
import com.sun.istack.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping
public class UrlController {

    private final ShortUrlDTOConverter shortUrlDTOConverter;
    private final ShortUrlRequestConverter shortUrlRequestConverter;
    private final ShortUrlService service;

    public UrlController(ShortUrlDTOConverter shortUrlDTOConverter, ShortUrlRequestConverter shortUrlRequestConverter, ShortUrlService service) {
        this.shortUrlDTOConverter = shortUrlDTOConverter;
        this.shortUrlRequestConverter = shortUrlRequestConverter;
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ShortUrlDTO>> getAllUrls(){
        return new ResponseEntity<List<ShortUrlDTO>>(
                shortUrlDTOConverter.convertToDTO(
                        service.getAllShortUrl()), HttpStatus.OK
        );
    }

    @GetMapping("/show/{code}")
    public ResponseEntity<ShortUrlDTO> getUrlByCode(@Valid @NotNull @PathVariable String code){
        return new ResponseEntity<ShortUrlDTO>(
                shortUrlDTOConverter.convertToDTO(
                        service.getUrlByCode(code)), HttpStatus.OK
        );
    }


    @GetMapping("/{code}")
    public ResponseEntity<ShortUrlDTO> redirect(@Valid @NotNull @PathVariable String code) throws URISyntaxException {

        ShortUrl shortUrl = service.getUrlByCode(code);

        URI uri = new URI(shortUrl.getUrl());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(uri);

        return new ResponseEntity<>(
               httpHeaders,HttpStatus.SEE_OTHER
                );
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody ShortUrlRequest shortUrlRequest){
       ShortUrl shortUrl = shortUrlRequestConverter.convertToEntity(shortUrlRequest);
        return new ResponseEntity<ShortUrlDTO>(
                shortUrlDTOConverter.convertToDTO(service.create(shortUrl)),HttpStatus.OK);
    }

}
