package ru.mnovikov.parser.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OcrResponse {
    private String text;
    private String status;
    private String error;
}
