package com.example.bigdata_pro2.repository;

import com.example.bigdata_pro2.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Bu arayüzün bir Spring Data Repository'si olduğunu belirtir.
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    // JpaRepository<Department, Integer> sayesinde
    // save(), findById(), findAll(), deleteById() gibi metotlar otomatik olarak gelir.
    // <Department, Integer> kısmı "Department entity'si ile çalış"
    // "ve bu entity'nin Primary Key'i (deptNo) Integer tipindedir" anlamına gelir.
}