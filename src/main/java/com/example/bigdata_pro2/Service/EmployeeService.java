package com.example.bigdata_pro2.Service;

import com.example.bigdata_pro2.entity.Employee;
import com.example.bigdata_pro2.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Bu sınıfın bir Spring Servisi olduğunu belirtir
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    // "Dependency Injection": Spring, EmployeeRepository'yi otomatik olarak bulup bu servise bağlar.
    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // --- Proje Gereksinimi [a]: CRUD İşlemleri ---

    /**
     * READ (Tüm Çalışanlar)
     * Proje Gereksinimi [c]: Web sayfasında bilgileri gösterme
     * @Cacheable("employees"): Bu metodu ilk kez çalıştırır, sonucu veritabanından alır
     * ve "employees" adıyla Redis'e kaydeder. Sonraki çağrılarda veritabanına gitmez,
     * direkt Redis'ten sonucu döndürür.
     */
    @Cacheable("employees")
    public List<Employee> getAllEmployees() {
        System.out.println("Veritabanından 'getAllEmployees' çağrıldı...");
        return employeeRepository.findAll();
    }

    /**
     * READ (Tek Çalışan)
     * @Cacheable(value = "employees", key = "#id"): Sonucu "employees" cache'ine
     * çalışanın 'id'si ile kaydeder. (Örn: employees::7839)
     */
    @Cacheable(value = "employees", key = "#id")
    public Employee getEmployeeById(Integer id) {
        System.out.println("Veritabanından 'getEmployeeById' çağrıldı: " + id);
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Çalışan bulunamadı: " + id));
    }

    /**
     * CREATE (Yeni Çalışan)
     * @CacheEvict(value = "employees", allEntries = true): "employees" cache'indeki tüm
     * verileri siler. Çünkü yeni bir çalışan eklendiğinde liste (getAllEmployees) değişir,
     * cache'in temizlenip yeniden doldurulması gerekir.
     */
    @CacheEvict(value = "employees", allEntries = true)
    public Employee createEmployee(Employee employee) {
        System.out.println("Yeni çalışan oluşturuluyor ve cache temizleniyor...");
        return employeeRepository.save(employee);
    }

    /**
     * UPDATE (Çalışan Güncelleme)
     * @CachePut(value = "employees", key = "#id"): Veritabanını günceller VE
     * cache'i temizlemek yerine, bu ID'ye ait veriyi cache'te de günceller.
     */
    @CachePut(value = "employees", key = "#id")
    public Employee updateEmployee(Integer id, Employee employeeDetails) {
        System.out.println("Çalışan güncelleniyor ve cache'teki veri yenileniyor: " + id);
        Employee employee = getEmployeeById(id); // Önce veriyi bul

        // Alanları güncelle
        employee.setEName(employeeDetails.getEName());
        employee.setJob(employeeDetails.getJob());
        employee.setSal(employeeDetails.getSal());
        employee.setComm(employeeDetails.getComm());
        // (Diğer alanlar da eklenebilir...)

        return employeeRepository.save(employee); // Veritabanına kaydet
    }

    /**
     * DELETE (Çalışan Silme)
     * @CacheEvict(value = "employees", allEntries = true): Bir çalışan silindiğinde
     * liste değiştiği için tüm "employees" cache'ini temizler.
     */
    @CacheEvict(value = "employees", allEntries = true)
    public void deleteEmployee(Integer id) {
        System.out.println("Çalışan siliniyor ve cache temizleniyor: " + id);
        employeeRepository.deleteById(id);
    }
}
