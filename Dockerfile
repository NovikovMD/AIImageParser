FROM python:3.9-slim

# Устанавливаем Tesseract и языковые пакеты
RUN apt-get update && apt-get install -y \
    tesseract-ocr \
    tesseract-ocr-rus \
    tesseract-ocr-eng \
    && rm -rf /var/lib/apt/lists/*

# Устанавливаем Python зависимости
RUN pip install flask pytesseract pillow

# Копируем API сервер
COPY src/main/resources/tesseract-api/tesseract_api.py /app/tesseract_api.py

WORKDIR /app

EXPOSE 5000

CMD ["python", "tesseract_api.py"]