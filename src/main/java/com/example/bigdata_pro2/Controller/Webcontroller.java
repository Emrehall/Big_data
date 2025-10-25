package com.example.bigdata_pro2.Controller;

import com.example.bigdata_pro2.entity.Employee;
import com.example.bigdata_pro2.Service.EmployeeService;
import com.example.bigdata_pro2.Service.HdfsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller // Bu sınıfın web isteklerini karşılayacağını belirtir (JSON değil, HTML döndürecek)
public class Webcontroller {

    private final EmployeeService employeeService;
    private final HdfsService hdfsService;

    // Dependency Injection: Gerekli servisleri otomatik olarak bu sınıfa bağla
    @Autowired
    public Webcontroller(EmployeeService employeeService, HdfsService hdfsService) {
        this.employeeService = employeeService;
        this.hdfsService = hdfsService;
    }

    /**
     * Proje Gereksinimi [c]: Tek bir web sayfası olacak
     * Ana sayfa isteğini (GET /) karşılar.
     * Tüm çalışanları veritabanından (veya cache'ten) çeker ve 'model' aracılığıyla HTML'e gönderir.
     */
    @GetMapping("/")
    public String showMainPage(Model model) {
        // EmployeeService'ten tüm çalışanları al (Cache'leme burada çalışacak)
        List<Employee> employeeList = employeeService.getAllEmployees();

        // Bu listeyi "employees" adıyla HTML (View) katmanına gönder
        model.addAttribute("employees", employeeList);

        // "index.html" dosyasını tarayıcıya göster
        return "index";
    }

    /**
     * Proje Gereksinimi [d]: Kullanıcı resim yükleme
     * HTML formundan gelen dosya yükleme isteğini (POST /upload) karşılar.
     */
    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            // Dosyayı HDFS'e yükle
            String fileName = hdfsService.uploadFile(file);
            redirectAttributes.addFlashAttribute("message", "Dosya başarıyla yüklendi: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Dosya yüklenemedi: " + e.getMessage());
        }

        // İşlem bittikten sonra kullanıcıyı ana sayfaya (GET /) yönlendir
        return "redirect:/";
    }

    /**
     * Proje Gereksinimi [b]: Resimleri HDFS'ten okuma
     * HTML'deki <img src="/images/dosyaadi.png"> gibi istekleri karşılar.
     * Dosya adını HdfsService'e gönderir ve dosyayı byte olarak tarayıcıya döndürür.
     */
    @GetMapping("/images/{filename:.+}")
    @ResponseBody // HTML değil, doğrudan veri (resim) döndür
    public ResponseEntity<byte[]> serveFile(@PathVariable String filename) {
        try {
            // Dosyayı HDFS'ten byte dizisi olarak oku
            byte[] fileBytes = hdfsService.readFile(filename);

            // Tarayıcıya "bu bir resim dosyasıdır" de ve byte'ları gönder
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(fileBytes);
        } catch (Exception e) {
            // Dosya bulunamazsa 404 hatası döndür
            return ResponseEntity.notFound().build();
        }
    }

    // Not: Proje CRUD operasyonları istiyor.
    // Bunlar için API endpointleri de (örn: /delete/{id}) buraya eklenebilir,
    // ancak şimdilik ana sayfa ve resim yükleme gereksinimleri tamamdır.
}