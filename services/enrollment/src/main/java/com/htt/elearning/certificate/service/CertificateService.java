package com.htt.elearning.certificate.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

@Service
public class CertificateService {
    private final TemplateEngine templateEngine;

    public CertificateService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generateCertificate(String studentName, String courseName) throws Exception {
        // 1️⃣ Chuẩn bị dữ liệu cho Thymeleaf
        Context context = new Context();
        context.setVariable("studentName", studentName);
        context.setVariable("courseName", courseName);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        String formattedDate = LocalDate.now().format(formatter);

        context.setVariable("issueDate", formattedDate);

        // 2️⃣ Load ảnh logo từ `static/logo.png`
        Resource resource = new ClassPathResource("static/logo.png");
        byte[] logoBytes = StreamUtils.copyToByteArray(resource.getInputStream());
        String logoBase64 = Base64.getEncoder().encodeToString(logoBytes);
        context.setVariable("logo", "data:image/png;base64," + logoBase64);

        // 3️⃣ Render HTML từ template `certificate.html`
        String htmlContent = templateEngine.process("certificate", context);

        // 4️⃣ Chuyển đổi HTML → PDF
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(htmlContent, new File("src/main/resources/static/").toURI().toString());
            builder.toStream(os);
            builder.run();
            return os.toByteArray();
        }
    }
}
