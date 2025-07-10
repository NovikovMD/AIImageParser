from flask import Flask, request, jsonify
import pytesseract
from PIL import Image
import io
import base64

app = Flask(__name__)

@app.route('/ocr', methods=['POST'])
def extract_text():
    try:
        data = request.json
        image_data = base64.b64decode(data['image'])

        image = Image.open(io.BytesIO(image_data))

        language = data.get('language', 'rus+eng')
        text = pytesseract.image_to_string(image, lang=language)

        return jsonify({
            'text': text,
            'status': 'success'
        })
    except Exception as e:
        return jsonify({
            'error': str(e),
            'status': 'error'
        }), 500

@app.route('/health', methods=['GET'])
def health_check():
    return jsonify({'status': 'healthy'})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)