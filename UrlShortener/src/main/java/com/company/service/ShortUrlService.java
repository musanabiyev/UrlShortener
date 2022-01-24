package com.company.service;

import com.company.exception.CodeAlreadyExists;
import com.company.exception.ShortUrlNotFoundException;
import com.company.model.ShortUrl;
import com.company.repository.ShortUrlRepository;
import com.company.util.RandomStringGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;
    private final RandomStringGenerator randomStringGenerator;

    public ShortUrlService(ShortUrlRepository shortUrlRepository, RandomStringGenerator randomStringGenerator) {
        this.shortUrlRepository = shortUrlRepository;
        this.randomStringGenerator = randomStringGenerator;
    }


    public List<ShortUrl> getAllShortUrl() {
        return shortUrlRepository.findAll();
    }

    public ShortUrl getUrlByCode(String code) {

        return shortUrlRepository.findAllByCode(code).orElseThrow(
                () -> new ShortUrlNotFoundException("url not found!")
        );
    }

    public ShortUrl create(ShortUrl shortUrl) {
        if (shortUrl.getCode() == null || shortUrl.getCode().isEmpty()) {
            shortUrl.setCode(generateCode());
        } else if (shortUrlRepository.findAllByCode(shortUrl.getCode()).isPresent()) {
            throw new CodeAlreadyExists("Code already exists");
        }
        shortUrl.setCode(shortUrl.getCode().toUpperCase());

        return shortUrlRepository.save(shortUrl);
    }

    private String generateCode() {
        String code;

        do {
            code = randomStringGenerator.generateRandomString();
        } while (shortUrlRepository.findAllByCode(code).isPresent());

        return code;
    }
}
