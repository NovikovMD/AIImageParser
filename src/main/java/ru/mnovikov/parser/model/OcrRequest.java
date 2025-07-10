package ru.mnovikov.parser.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OcrRequest {
    private String image;
    private String language;
}
