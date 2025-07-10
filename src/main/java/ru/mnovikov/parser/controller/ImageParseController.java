package ru.mnovikov.parser.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mnovikov.parser.service.ImageParseService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ImageParseController {

    private final ImageParseService imageParseService;

    @GetMapping("/test")
    public ResponseEntity<String> parseTestData() {
        return ResponseEntity.ok(imageParseService.parseTestImage());
    }

    @GetMapping("/parse")
    public ResponseEntity<String> parseImage(@RequestParam("path") String filePath) {
        return ResponseEntity.ok(imageParseService.parseImage(filePath));
    }

    @GetMapping("/parse-folder")
    public ResponseEntity<Map<String, String>> parseFolder(@RequestParam("path") String folderPath) {
        return ResponseEntity.ok(imageParseService.parseFolder(folderPath));
    }
}
