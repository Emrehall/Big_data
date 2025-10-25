package com.example.bigdata_pro2.Service;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service // Bu sınıfın bir Spring Servisi olduğunu belirtir
public class HdfsService {

    private FileSystem fileSystem;
    // Resimlerin HDFS içinde saklanacağı klasör.
    private Path hdfsUploadDir = new Path("/user/uploads/images");

    // Bu 'Constructor', 'application.properties' dosyasındaki HDFS adresini okur
    // ve HDFS bağlantısını otomatik olarak başlatır.
    public HdfsService(@Value("${fs.defaultFS}") String hdfsUri) {
        try {
            Configuration conf = new Configuration();
            conf.set("fs.defaultFS", hdfsUri);
            this.fileSystem = FileSystem.get(conf);

            // HDFS'te /user/uploads/images klasörünün var olup olmadığını kontrol et, yoksa oluştur
            if (!fileSystem.exists(hdfsUploadDir)) {
                fileSystem.mkdirs(hdfsUploadDir);
                System.out.println("HDFS directory created: " + hdfsUploadDir);
            }
        } catch (IOException e) {
            throw new RuntimeException("HDFS FileSystem başlatılamadı!", e);
        }
    }

    /**
     * Proje Gereksinimi [d]: Kullanıcı resim yükleme
     * Web'den gelen dosyayı (MultipartFile) HDFS'e yükler.
     */
    public String uploadFile(MultipartFile file) throws IOException {
        // Dosya çakışmasını önlemek için benzersiz bir isim oluştur (örn: 1678886400000_avatar.png)
        String newFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path hdfsWritePath = new Path(hdfsUploadDir, newFileName);

        // Dosyayı HDFS'e yaz
        try (FSDataOutputStream outputStream = fileSystem.create(hdfsWritePath)) {
            outputStream.write(file.getBytes());
        }

        // Yüklenen dosyanın adını (örn: "1678886400000_avatar.png") döndürüyoruz.
        return newFileName;
    }

    /**
     * Proje Gereksinimi [b]: Resimleri HDFS'ten okuma
     * Dosya adını kullanarak HDFS'ten dosyayı okur ve byte dizisi olarak döndürür.
     */
    public byte[] readFile(String fileName) throws IOException {
        Path hdfsReadPath = new Path(hdfsUploadDir, fileName);

        if (!fileSystem.exists(hdfsReadPath)) {
            throw new RuntimeException("Dosya HDFS'te bulunamadı: " + fileName);
        }

        // Dosyayı HDFS'ten oku ve byte dizisi olarak döndür
        try (FSDataInputStream inputStream = fileSystem.open(hdfsReadPath)) {
            return inputStream.readAllBytes();
        }
    }
}
