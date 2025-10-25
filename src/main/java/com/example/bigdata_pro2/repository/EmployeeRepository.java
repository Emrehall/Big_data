package com.example.bigdata_pro2.repository;

import com.example.bigdata_pro2.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Bu arayüzün bir Spring Data Repository'si olduğunu belirtir.
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    // Bu JpaRepository<Employee, Integer> sayesinde
    // 'emp' tablosu için tüm CRUD işlemleri otomatik olarak tanımlanmış oldu.
    // <Employee, Integer> kısmı "Employee entity'si ile çalış"
    // "ve bu entity'nin Primary Key'i (empNo) Integer tipindedir" anlamına gelir.
}