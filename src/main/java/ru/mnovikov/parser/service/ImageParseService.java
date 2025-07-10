package ru.mnovikov.parser.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class ImageParseService {
    public static final String TEST_IMAGE_PATH = "src/main/resources/test_image.jpg";
    public static final String PROMPT = """
        Ты - продвинутый AI-редактор, который восстанавливает текст после OCR.
        Твоя задача - исправить ошибки, убрать лишние символы, восстановить пробелы и примести текст к читаемому виду.
        
        Твои действия:
        - Убери все ошибки OCR (например, неверные буквы, случайные символы).
        - Исправь неверные символы (например, `|`, `¬`, `©`, искажения букв).
        - Восстанови правильные слова и пробелы (если текст "развалился").
        - Сохрани структуру текста (если есть заголовки, абзацы).
        - Удали любые случайные числа, символы, не относящиеся к тексту.
        - Если встречаются нелогичные сочетания букв - попытайся угадать исходное слово.
        - Если слово искажено, исправь его в правильную форму.
        - Не меняй падежи и склонения слов.
        - Если в тексте перепутаны буквы (например, "нрвое" вместо "новое"), исправь это.
        - Не добавляй текст от себя, если его не было изначально. Не пиши от себя лишние слова и символы.
        - Давай только исправленный текст, без своих комментариев и без добавления стилизации.
        - Не меняй порядок слов в тексте.
        - Не переводи и не смешивай языки. Если текст на английском — отвечай только по-английски.
        - Если слово не можешь уверенно восстановить — оставь в максимально близком виде, без фантазии.
        
        Теперь исправь этот текст: %s
        """;

    private final ChatClient chatClient;
    private final TesseractImageParser parser;

    public ImageParseService(ChatClient.Builder chatClientBuilder,
                             TesseractImageParser parser) {
        this.chatClient = chatClientBuilder.build();
        this.parser = parser;
    }

    public String parseTestImage() {
        return parseImage(TEST_IMAGE_PATH);
    }

    public String parseImage(String imagePath) {
        final String imageText = parser.extractTextFromImage(imagePath);
        return chatClient.prompt(PROMPT.formatted(imageText))
            .call()
            .content();
    }

    public Map<String, String> parseFolder(String folderPath) {
        final File folder = new File(folderPath);
        final Map<String, String> result = new HashMap<>();
        if (!folder.exists() || !folder.isDirectory()) {
            return result;
        }

        final File[] files = folder.listFiles();
        if (files == null) {
            return result;
        }

        for (final File file : files) {
            if (file.isFile()) {
                final String text = parseImage(file.getAbsolutePath());
                result.put(file.getName(), text);
            }
        }
        return result;
    }
}
