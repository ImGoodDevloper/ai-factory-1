package com.kes.api.repository;

import com.kes.api.entity.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageRepository extends JpaRepository<Page, Long> {
    List<Page> findByParentIsNull();

    @Query("SELECT p FROM Page p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(CAST(p.content AS string)) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Page> searchPages(@Param("query") String query);
}
