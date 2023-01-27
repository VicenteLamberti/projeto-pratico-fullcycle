package com.fullcycle.vicente.infrastructure.category.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryJPAEntity, String> {
    Page<CategoryJPAEntity> findAll(Specification<CategoryJPAEntity> whereClause, Pageable pageable);
}
