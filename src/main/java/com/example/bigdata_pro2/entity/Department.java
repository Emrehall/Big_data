package com.example.bigdata_pro2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity // Bu sınıfın bir veritabanı tablosuna karşılık geldiğini belirtir.
@Table(name = "DEPT") // Hangi tabloya karşılık geldiğini belirtir.
@Data // Lombok: Getter, setter, equals, hashCode metotlarını otomatik oluşturur.
@ToString // @Data'nın bazen sebep olduğu döngüsel sorunları çözmek için eklendi.
public class Department {

    @Id // Bu alanın "Primary Key" (Birincil Anahtar) olduğunu belirtir.
    @Column(name = "DEPTNO") // Hangi sütuna karşılık geldiğini belirtir.
    private Integer deptNo;

    @Column(name = "DNAME")
    private String dName;

    @Column(name = "LOC")
    private String loc;
}