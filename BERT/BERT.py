import pandas as pd
import torch
from sklearn.model_selection import train_test_split
from sklearn.metrics import classification_report
from torch.utils.data import Dataset, DataLoader
from transformers import BertTokenizer, BertForSequenceClassification, get_scheduler
from torch.optim import AdamW
import math
from transformers import AutoTokenizer, AutoModelForMaskedLM
from transformers import AutoModelForSequenceClassification
import os

# =====================
# 数据预处理
# =====================
df = pd.read_csv("file_preprocessed.csv", on_bad_lines='skip', quoting=1, engine='python')
df = df[['tweets', 'labels']]  # 保留需要的列
df.columns = ['text', 'label']  # 重命名方便后续处理

# 划分训练集和测试集
train_texts, val_texts, train_labels, val_labels = train_test_split(
    df['text'].tolist(), df['label'].tolist(), test_size=0.2, random_state=42
)

# 加载 BERT 分词器
# tokenizer = AutoTokenizer.from_pretrained("google-bert/bert-base-uncased")
tokenizer = AutoTokenizer.from_pretrained("bert-base-uncased")

# 编码数据
def is_valid_string(s):
    return isinstance(s, str) and s.strip() != "" and not isinstance(s, float)

train_texts = [str(text) for text in train_texts if is_valid_string(text)]
val_texts = [str(text) for text in val_texts if is_valid_string(text)]

print(type(train_texts))
print(type(train_texts[0]))
train_encodings = tokenizer(train_texts, truncation=True, padding=True, max_length=128)
val_encodings = tokenizer(val_texts, truncation=True, padding=True, max_length=128)

# 创建 Dataset
class SentimentDataset(Dataset):
    def __init__(self, encodings, labels):
        self.encodings = encodings
        self.labels = labels
    def __len__(self):
        return len(self.labels)
    def __getitem__(self, idx):
        return {
            'input_ids': torch.tensor(self.encodings['input_ids'][idx]),
            'attention_mask': torch.tensor(self.encodings['attention_mask'][idx]),
            'labels': torch.tensor(self.labels[idx])
        }

train_dataset = SentimentDataset(train_encodings, train_labels)
val_dataset = SentimentDataset(val_encodings, val_labels)

# =====================
# 模型设置
# =====================
device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

# model = AutoModelForMaskedLM.from_pretrained("google-bert/bert-base-uncased")
model = AutoModelForSequenceClassification.from_pretrained("bert-base-uncased", num_labels=3)
model.to(device)

train_loader = DataLoader(train_dataset, batch_size=8, shuffle=True)
val_loader = DataLoader(val_dataset, batch_size=8)

optimizer = AdamW(model.parameters(), lr=5e-5)

num_epochs = 3
num_training_steps = num_epochs * len(train_loader)
lr_scheduler = get_scheduler(
    "linear",
    optimizer=optimizer,
    num_warmup_steps=0,
    num_training_steps=num_training_steps,
)

# =====================
# 模型训练
# =====================
model.train()
for epoch in range(num_epochs):
    total_loss = 0
    for batch in train_loader:
        batch = {k: v.to(device) for k, v in batch.items()}
        outputs = model(**batch)
        loss = outputs.loss
        total_loss += loss.item()
        loss.backward()
        optimizer.step()
        lr_scheduler.step()
        optimizer.zero_grad()
    print(f"Epoch {epoch+1}/{num_epochs}, Loss: {total_loss/len(train_loader)}")

# =====================
# 模型评估
# =====================
model.eval()
preds, true_labels = [], []

with torch.no_grad():
    for batch in val_loader:
        batch = {k: v.to(device) for k, v in batch.items()}
        outputs = model(**batch)
        logits = outputs.logits
        predictions = torch.argmax(logits, dim=-1)
        preds.extend(predictions.cpu().numpy())
        true_labels.extend(batch['labels'].cpu().numpy())

print("Classification Report:")
print(classification_report(true_labels, preds, target_names=['bad', 'neutral', 'good']))

model.save_pretrained('./bert_sentiment_model')
tokenizer.save_pretrained('./bert_sentiment_model')
