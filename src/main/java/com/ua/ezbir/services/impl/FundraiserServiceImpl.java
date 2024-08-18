package com.ua.ezbir.services.impl;

import com.ua.ezbir.domain.Fundraiser;
import com.ua.ezbir.domain.User;
import com.ua.ezbir.domain.exceptions.BadRequestException;
import com.ua.ezbir.domain.exceptions.DatabaseException;
import com.ua.ezbir.domain.exceptions.FundraiserNotFoundException;
import com.ua.ezbir.domain.exceptions.UnauthorizedException;
import com.ua.ezbir.factories.FundraiserDtoFactory;
import com.ua.ezbir.repository.FundraiserRepository;
import com.ua.ezbir.services.FundraiserService;
import com.ua.ezbir.services.UserService;
import com.ua.ezbir.web.fundraiser.Category;
import com.ua.ezbir.web.fundraiser.FundraiserRequestDto;
import com.ua.ezbir.web.fundraiser.FundraiserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FundraiserServiceImpl implements FundraiserService {
    private final FundraiserRepository fundraiserRepository;
    private final FundraiserDtoFactory fundraiserDtoFactory;
    private final UserService userService;

    @Override
    @Transactional
    public FundraiserResponseDto addFundraiser(FundraiserRequestDto fundraiserRequestDto) {
        User user = userService.getUser();

        List<Fundraiser> fundraiserList = user.getFundraisers();

        Fundraiser fundraiser = Fundraiser.builder()
                .amount(fundraiserRequestDto.getAmount())
                .name(fundraiserRequestDto.getName())
                .jarLink(fundraiserRequestDto.getJarLink())
                .description(fundraiserRequestDto.getDescription())
                .categories(fundraiserRequestDto.getCategories())
                .user(user)
                .build();
        saveFundraiser(fundraiser);

        // add new fundraiser in user list
        fundraiserList.add(fundraiser);
        user.setFundraisers(fundraiserList);

        userService.saveUser(user); // re-save user with updated fundraiser list

        return fundraiserDtoFactory.makeFundraiserResponseDto(fundraiser);
    }

    @Override
    @Transactional
    public String deleteFundraiser(Long id) {
        Fundraiser fundraiser = fundraiserRepository
                .findById(id)
                .orElseThrow(FundraiserNotFoundException::new);

        User user = userService.getUser();

        checkFundraiserAccess(fundraiser, user);

        fundraiserRepository.delete(fundraiser);

        List<Fundraiser> fundraisers = user.getFundraisers();
        fundraisers.remove(fundraiser);
        user.setFundraisers(fundraisers);
        userService.saveUser(user);

        return "Fundraiser was successful deleted";
    }

    public void checkFundraiserAccess(Fundraiser fundraiser, User user) {
        if (!fundraiser.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You don't have access to this fundraiser");
        }
    }

    @Override
    @Transactional
    public FundraiserResponseDto updateFundraiser(
            Long id,
            Optional<Integer> optionalAmount,
            Optional<String> optionalName,
            Optional<String> optionalJarLink,
            Optional<String> optionalDescription,
            Optional<Boolean> optionalIsClosed,
            Optional<Set<Category>> optionalCategories
    ) {
        Fundraiser fundraiser = fundraiserRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Fundraiser not found"));

        checkFundraiserAccess(fundraiser, userService.getUser());

        optionalAmount.ifPresent(fundraiser::setAmount);
        optionalName.ifPresent(fundraiser::setName);
        optionalJarLink.ifPresent(fundraiser::setJarLink);
        optionalDescription.ifPresent(fundraiser::setDescription);
        optionalIsClosed.ifPresent(fundraiser::setClosed);
        optionalCategories.ifPresent(fundraiser::setCategories);

        saveFundraiser(fundraiser);

        return fundraiserDtoFactory.makeFundraiserResponseDto(fundraiser);
    }

    @Override
    public Page<FundraiserResponseDto> searchFundraisers(
            String name,
            Boolean isClosed,
            int page,
            int size,
            String sortBy,
            String sortDir
    ) {
        Sort sort = Sort.by(sortBy);
        if (sortDir.equalsIgnoreCase("asc")) {
            sort = sort.ascending();
        } else {
            sort = sort.descending();
        }

        PageRequest pageRequest = PageRequest.of(page, size, sort);
        Page<Fundraiser> fundraisers = fundraiserRepository.searchFundraisers(name, isClosed, pageRequest);

        return fundraisers.map(fundraiserDtoFactory::makeFundraiserResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Fundraiser findFundraiserById(Long id) {
        return fundraiserRepository
                .findById(id)
                .orElseThrow(FundraiserNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public FundraiserResponseDto getFundraiserResponseDto(Long id) {
        return fundraiserDtoFactory
                .makeFundraiserResponseDto(
                        findFundraiserById(id)
                );
    }

    @Override
    @Transactional
    public Fundraiser saveFundraiser(Fundraiser fundraiser) {
        try {
            return fundraiserRepository.save(fundraiser);
        } catch (Exception e) {
            throw new DatabaseException();
        }
    }

}
