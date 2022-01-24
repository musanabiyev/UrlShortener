package com.company.dto.converter;

import com.company.dto.ShortUrlDTO;
import com.company.model.ShortUrl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ShortUrlDTOConverter {

    public ShortUrlDTO convertToDTO(ShortUrl shortUrl) {
        return ShortUrlDTO.builder().id(shortUrl.getId())
                .url(shortUrl.getUrl())
                .code(shortUrl.getCode()).build();
    }

    public List<ShortUrlDTO> convertToDTO(List<ShortUrl> shortUrl){
        return shortUrl.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

}
