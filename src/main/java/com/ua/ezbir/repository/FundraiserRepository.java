package com.ua.ezbir.repository;

import com.ua.ezbir.domain.Fundraiser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FundraiserRepository extends JpaRepository<Fundraiser,Long> {
    @Query("SELECT f FROM Fundraiser f WHERE " +
            "(:name IS NULL OR LOWER(f.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:isClosed IS NULL OR f.isClosed = :isClosed)")
    Page<Fundraiser> searchFundraisers(
            @Param("name") String name,
            @Param("isClosed") Boolean isClosed,
            Pageable pageable);
}
