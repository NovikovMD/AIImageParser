package ru.mnovikov.parser.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.mnovikov.parser.model.OcrRequest;
import ru.mnovikov.parser.model.OcrResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@Component
@Slf4j
public class TesseractImageParser {

    private final WebClient webClient;

    public TesseractImageParser(@Value("${tesseract.api.url}") String tesseractApiUrl) {
        this.webClient = WebClient.builder()
            .baseUrl(tesseractApiUrl)
            .build();
    }

    public String extractTextFromImage(String imagePath) {
        try {
            final byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
            final String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            final OcrRequest request = new OcrRequest(base64Image, "rus+eng");

            final OcrResponse response = webClient.post()
                .uri("/ocr")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(OcrResponse.class)
                .block();
            log.info("response: {}", response);

            if (response != null && "success".equals(response.getStatus())) {
                return response.getText();
            }
            throw new RuntimeException("Ошибка при обработке изображения в Tesseract API");
        } catch (IOException e) {
            throw new RuntimeException("Не удалось обработать изображение", e);
        }
    }
}
