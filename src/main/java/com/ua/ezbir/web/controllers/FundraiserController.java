package com.ua.ezbir.web.controllers;

import com.ua.ezbir.services.FundraiserService;
import com.ua.ezbir.web.fundraiser.Category;
import com.ua.ezbir.web.fundraiser.FundraiserRequestDto;
import com.ua.ezbir.web.fundraiser.FundraiserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fundraisers")
public class FundraiserController {
    private final FundraiserService fundraiserService;

    @GetMapping("/{id}")
    public FundraiserResponseDto findFundraiserById(
            @PathVariable("id") Long id
    ){
        return fundraiserService.findFundraiserById(id);
    }

    @GetMapping
    public List<FundraiserResponseDto> fetchFundraisers(
            @RequestParam("prefix_name") Optional<String> optionalPrefixName
            ) {
        return fundraiserService.fetchFundraisers(optionalPrefixName);
    }

    @PostMapping("/add")
    public FundraiserResponseDto addFundraiser(
            @RequestBody @Valid FundraiserRequestDto fundraiserRequestDto
    ) {
        return fundraiserService.addFundraiser(fundraiserRequestDto);
    }

    @DeleteMapping("/{id}")
    public String deleteFundraiser(
            @PathVariable("id") Long id
    ) {
        return fundraiserService.deleteFundraiser(id);
    }

    @PatchMapping("/{id}/update")
    public FundraiserResponseDto updateFundraiser(
            @PathVariable("id") Long id,
            @RequestParam("amount") Optional<Integer> optionalAmount,
            @RequestParam("name") Optional<String> optionalName,
            @RequestParam("jar_link") Optional<String> optionalJarLink,
            @RequestParam("description") Optional<String> optionalDescription,
            @RequestParam("is_closed") Optional<Boolean> optionalIsClosed,
            @RequestParam("categories") Optional<Set<Category>> optionalCategories
    ) {
        return fundraiserService.updateFundraiser(
                id,
                optionalAmount,
                optionalName,
                optionalJarLink,
                optionalDescription,
                optionalIsClosed,
                optionalCategories
        );
    }

}
