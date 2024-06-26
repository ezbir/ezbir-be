package com.ua.ezbir.repository;

import com.ua.ezbir.domain.Fundraiser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface FundraiserRepository extends JpaRepository<Fundraiser,Long> {
    Stream<Fundraiser> streamAllBy();

    Stream<Fundraiser> streamAllByNameStartsWithIgnoreCase(String prefixName);
}
