package com.ua.ezbir.web.controllers;

import com.ua.ezbir.services.FundraiserService;
import com.ua.ezbir.web.fundraiser.Category;
import com.ua.ezbir.web.fundraiser.FundraiserRequestDto;
import com.ua.ezbir.web.fundraiser.FundraiserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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
    ) {
        return fundraiserService.getFundraiserResponseDto(id);
    }

    @GetMapping("/search")
    public Page<FundraiserResponseDto> searchFundraisers(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false) Boolean isClosed,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return fundraiserService.searchFundraisers(name, isClosed, page, size, sortBy, sortDir);
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
