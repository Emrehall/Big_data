package com.example.bigdata_pro2.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Entity // Bu sınıfın bir veritabanı tablosu olduğunu belirtir.
@Table(name = "EMP") // Hangi tabloya karşılık geldiğini belirtir.
@Data // Lombok: Getter, setter vb. oluşturur.
@ToString(exclude = {"manager", "department"}) // İlişkisel alanları ToString'den çıkar (sonsuz döngüleri önler)
public class Employee {

    @Id // Primary Key
    @Column(name = "EMPNO")
    private Integer empNo;

    @Column(name = "ENAME")
    private String eName;

    @Column(name = "JOB")
    private String job;

    @Column(name = "HIREDATE")
    @Temporal(TemporalType.DATE) // Veritabanındaki DATE tipini Java'daki Date tipi ile eşler.
    private Date hireDate;

    @Column(name = "SAL")
    private Double sal;

    @Column(name = "COMM")
    private Double comm;

    // --- İLİŞKİLER (JOIN İŞLEMLERİ İÇİN) ---

    // Proje Gereksinimi: Manager Adı
    // 'EMP' tablosu, 'MGR' sütunu ile kendi kendine (Employee) bağlanır.
    @ManyToOne(fetch = FetchType.LAZY) // Bir yönetici (manager) birden çok çalışana (employee) sahip olabilir.
    @JoinColumn(name = "MGR") // Hangi sütun üzerinden bağlanılacağını belirtir ('MGR' sütunu)
    private Employee manager;

    // Proje Gereksinimi: Department Adı
    // 'EMP' tablosu, 'DEPTNO' sütunu ile 'DEPT' tablosuna (Department) bağlanır.
    @ManyToOne(fetch = FetchType.LAZY) // Bir departman birden çok çalışana sahip olabilir.
    @JoinColumn(name = "DEPTNO") // Hangi sütun üzerinden bağlanılacağını belirtir ('DEPTNO' sütunu)
    private Department department;
}