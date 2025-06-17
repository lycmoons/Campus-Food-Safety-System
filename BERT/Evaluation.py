import numpy as np
from Dataset_Partitioning import Cross_Validation, Hold_out, Bootstrapping
from sklearn.preprocessing import PolynomialFeatures
from sklearn.linear_model import LogisticRegression
from sklearn.pipeline import make_pipeline

def get_Best_M(train_data, Ms, method, parameters):
    if method == 'Cross Validation':
        T = parameters[0]
        K = parameters[1]
        return get_CV(train_data, Ms, T, K)
    elif method == 'Hold Out':
        test_ratio = parameters[0]
        return get_HO(train_data, Ms, test_ratio)
    elif method == 'Bootstrapping':
        times = parameters[0]
        return get_B(train_data, Ms, times)

# ==============================
# T 次 K 折交叉验证
# ==============================
def get_CV(train_data, Ms, T, K):
    best_M = None
    best_score = -1

    for M in Ms: # 遍历每个多项式阶数，比如 M=1,2,5
        scores = []
        for _ in range(T): # 做 T 次重复的 K 折交叉验证，提高稳定性，减少划分随机性带来的波动
            folds = Cross_Validation(train_data, K) # 每次调用 Cross_Validation 将 train_data 划分成 K 个互不重叠的子集
            for i in range(K):
                val_data = folds[i]
                train_folds = [folds[j] for j in range(K) if j != i]
                fold_train = np.vstack(train_folds)

                model = make_pipeline(PolynomialFeatures(degree=M), LogisticRegression())
                model.fit(fold_train[:, 1:], fold_train[:, 0])
                output = model.predict(val_data[:, 1:])
                acc = np.mean(output == val_data[:, 0])
                scores.append(acc)

        avg_score = np.mean(scores)
        if avg_score > best_score:
            best_score = avg_score
            best_M = M

    return best_M

# ==============================
# 留出法：随机划分训练集与验证集
# ==============================
def get_HO(train_data, Ms, test_ratio):
    best_M = None
    best_score = -1

    sub_train, val_data = Hold_out(train_data, test_ratio)

    for M in Ms:
        model = make_pipeline(PolynomialFeatures(degree=M), LogisticRegression())
        model.fit(sub_train[:, 1:], sub_train[:, 0])
        output = model.predict(val_data[:, 1:])
        acc = np.mean(output == val_data[:, 0])

        if acc > best_score:
            best_score = acc
            best_M = M

    return best_M

# ==============================
# 自助法（Bootstrapping）：有放回抽样
# ==============================
def get_B(train_data, Ms, times):
    best_M = None
    best_score = -1

    for M in Ms:
        scores = []
        for _ in range(times):
            sub_train, val_data = Bootstrapping(train_data, len(train_data))

            model = make_pipeline(PolynomialFeatures(degree=M), LogisticRegression())
            model.fit(sub_train[:, 1:], sub_train[:, 0])
            output = model.predict(val_data[:, 1:])
            acc = np.mean(output == val_data[:, 0])
            scores.append(acc)

        avg_score = np.mean(scores)
        if avg_score > best_score:
            best_score = avg_score
            best_M = M

    return best_M
