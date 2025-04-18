package com.web.dopamine.repository;

import com.web.dopamine.entity.Constraint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConstraintRepository extends JpaRepository<Constraint, Integer> {
} 