from transformers import AutoModelForSequenceClassification, AutoTokenizer

model = AutoModelForSequenceClassification.from_pretrained("bert-base-uncased", num_labels=3)
tokenizer = AutoTokenizer.from_pretrained("bert-base-uncased")

model.save_pretrained("./bert-base-uncased")
tokenizer.save_pretrained("./bert-base-uncased")
