package com.ua.ezbir.services;

import com.ua.ezbir.domain.Fundraiser;
import com.ua.ezbir.domain.User;
import com.ua.ezbir.web.fundraiser.Category;
import com.ua.ezbir.web.fundraiser.FundraiserRequestDto;
import com.ua.ezbir.web.fundraiser.FundraiserResponseDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface FundraiserService {
    FundraiserResponseDto addFundraiser(FundraiserRequestDto fundraiserRequestDto);
    String deleteFundraiser(Long id);
    FundraiserResponseDto updateFundraiser(
            Long id,
            Optional<Integer> optionalAmount,
            Optional<String> optionalName,
            Optional<String> optionalJarLink,
            Optional<String> optionalDescription,
            Optional<Boolean> optionalIsClosed,
            Optional<Set<Category>> optionalCategories
    );
    List<FundraiserResponseDto> fetchFundraisers(Optional<String> optionalPrefixName);
    Fundraiser findFundraiserById(Long id);
    FundraiserResponseDto getFundraiserResponseDto(Long id);
    void checkFundraiserAccess(Fundraiser fundraiser, User user);
    Fundraiser saveFundraiser(Fundraiser fundraiser);
}
