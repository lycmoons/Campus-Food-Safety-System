from transformers import BertTokenizer, BertForSequenceClassification
import torch

# 模型文件夹路径
model_dir = "./bert_sentiment_model"

# 1. 加载分词器和模型（自动识别safetensors格式）
tokenizer = BertTokenizer.from_pretrained(model_dir)
model = BertForSequenceClassification.from_pretrained(model_dir)
model.eval()

# 2. 要预测的文本列表
texts = [
    "This product is amazing!",
    "I hate this experience.",
    "It was okay, not great but not bad either.",
    "openai announced chatgpt a model optimized for dialogue URLLINK"
]

# 3. 文本转为模型输入
inputs = tokenizer(texts, padding=True, truncation=True, return_tensors="pt")

# 4. 推理
with torch.no_grad():
    outputs = model(**inputs)
    logits = outputs.logits
    predictions = torch.argmax(logits, dim=1)

# 5. 映射标签
label_map = {
    0: "bad",
    1: "neutral",
    2: "good"
}

# 6. 打印结果
for text, pred in zip(texts, predictions):
    label = label_map[pred.item()]
    print(f"Text: {text}\nPredicted Sentiment: {label}\n")