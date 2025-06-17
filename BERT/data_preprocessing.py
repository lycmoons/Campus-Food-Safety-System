from sklearn.model_selection import train_test_split, StratifiedShuffleSplit
from tensorflow.keras.preprocessing.text import Tokenizer
from tensorflow.keras.preprocessing.sequence import pad_sequences
from collections import Counter

# Stratified split into train/val/test in one go
sss = StratifiedShuffleSplit(n_splits=1, test_size=0.2, random_state=42)
train_idx, temp_idx = next(sss.split(df, df["labels"]))
train_df = df.iloc[train_idx]
temp_df = df.iloc[temp_idx]

sss2 = StratifiedShuffleSplit(n_splits=1, test_size=0.5, random_state=42)
val_idx, test_idx = next(sss2.split(temp_df, temp_df["labels"]))
val_df = temp_df.iloc[val_idx]
test_df = temp_df.iloc[test_idx]

# Modify tokenizer filter so that it doesn't remove '"', '?' and '!'
custom_punctuation_filter =  '#$%&()*+,-./:;=@[\\]\'^\'_`{|}~\t\n'

countTokens = Tokenizer(num_words=None,filters=custom_punctuation_filter)
countTokens.fit_on_texts(train_df["tweets"])

# Separate low-freq vs high-freq
low_freq = {w:c for w,c in countTokens.word_counts.items() if c <= 6}
high_freq = {w:c for w,c in countTokens.word_counts.items() if c > 6}

print(f"Words with ≤5 occurrences: {len(low_freq)}")
print(f"Words with >5 occurrences: {len(high_freq)}")

# Set num_words = size of high_freq + 1 for OOV
vocab_size = len(high_freq) + 1

# Re-fit tokenizer with OOV token and limited vocab
tk = Tokenizer(num_words=vocab_size, oov_token="<OOV>",
               filters=custom_punctuation_filter)
tk.fit_on_texts(train_df["tweets"])

#1.1 get the size of the dictionary
dico_size = len(tk.word_counts.items())
num_tokens = dico_size + 1
#2. building a sequneces
seqs_train = tk.texts_to_sequences(train_df["tweets"])

oov_idx = tk.word_index["<OOV>"]
total_tokens = sum(len(seq) for seq in seqs_train)
oov_tokens  = sum(token == oov_idx for seq in seqs_train for token in seq)

print(f"OOV tokens: {oov_tokens} / {total_tokens} = {oov_tokens/total_tokens:.2%}")

#2.1 calculate maxi length of tweets
# max_len = np.max(np.array([len(d) for d in seqs_train]))
# marg_len=10
# maxlen = max_len + marg_len
maxlen = 50

# ——————————————————————————
# 1) Build token counts from your TRAIN sequences
# (you already have `seqs_train`, `countTokens` and `low_freq`)

idx_to_word = {idx: w for w, idx in tk.word_index.items()}
word_counts = countTokens.word_counts  # dict: word → count
# 2) Assemble a DataFrame
rows = []
for word, count in word_counts.items():
    rows.append({
        "word":    word,
        "count":   count,
        "is_oov":  (count <= 6)
    })
df_tokens = pd.DataFrame(rows)

# sort so that OOV words come first, then by ascending count
df_tokens = df_tokens.sort_values(by=["is_oov", "count"], ascending=[False, True])

# 3) Save to CSV for inspection
df_tokens.to_csv("token_frequencies.csv", index=False, encoding="utf-8-sig")

print("✅ Saved all tokens → token_frequencies.csv.  You can now download and inspect which words ended up in OOV, their counts, etc.")
