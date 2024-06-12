package com.ua.ezbir.factories;

import com.ua.ezbir.domain.Fundraiser;
import com.ua.ezbir.web.fundraiser.FundraiserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FundraiserDtoFactory {
    private final PostDtoFactory postDtoFactory;

    public FundraiserResponseDto makeFundraiserResponseDto(Fundraiser fundraiser) {
        return FundraiserResponseDto.builder()
                .name(fundraiser.getName())
                .id(fundraiser.getId())
                .amount(fundraiser.getAmount())
                .jarLink(fundraiser.getJarLink())
                .description(fundraiser.getDescription())
                .isClosed(fundraiser.isClosed())
                .createdAt(fundraiser.getCreatedAt())
                .categories(fundraiser.getCategories())
                .posts(
                        fundraiser
                                .getPosts()
                                .stream()
                                .map(postDtoFactory::makePostResponseDto)
                                .collect(Collectors.toList())
                )
                .username(fundraiser.getUser().getUsername())
                .userId(fundraiser.getUser().getId())
                .build();
    }
}
