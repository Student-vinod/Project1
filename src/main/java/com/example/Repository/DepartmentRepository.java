package com.example.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Model.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}

