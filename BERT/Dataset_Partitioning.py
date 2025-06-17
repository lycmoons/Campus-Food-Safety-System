import random
import numpy as np
import math

# 由于是二分类问题，所以这里默认数据集label只有0和1，大家也可以挑战更高难度（未知标签情况）

# fold折交叉验证法的第k次（即取fold份数据中的第k份作为测试集,k从1开始）
'''
原理解释
K折交叉验证：将数据划分为 fold 份，每次用其中一份作为测试集，其余为训练集。
    - k 表示第几次交叉验证（从 1 开始），所以第 k 个 fold 被选为测试集。
    - 优点：相比单次留出法更稳定，能更充分利用数据。
    - math.ceil 是为了防止不能整除导致某些数据被遗漏。
'''
def Cross_Validation(data, fold, k):
    data = list(data)  # 将数据转为列表，方便打乱和切片
    random.shuffle(data)  # 打乱数据，确保每一折的样本随机

    fold_size = math.ceil(len(data) / fold)  # 每一折的样本数量（向上取整）
    test_start = (k - 1) * fold_size  # 第k折起始索引（k从1开始）
    test_end = min(k * fold_size, len(data))  # 第k折结束索引，注意不能越界

    test_data = data[test_start:test_end]  # 测试集是第k折
    train_data = data[:test_start] + data[test_end:]  # 剩余的作为训练集

    return np.array(train_data), np.array(test_data)


# 测试样本占比为test_ratio的留出法
# 留出法：直接将数据按比例（test_ratio）划分为训练集和测试集。
# 为了保持类别均衡，该函数分别对每一类样本（class 0 和 class 1）做划分。
def Hold_out(data, test_ratio):
    class0 = []
    class1 = []

    # 验证集划分
    for d in data:
        if d[0] == '0':
            class0.append(d)
        else:
            class1.append(d)

    train_data = []
    test_data = []

    # 验证集划分
    for i in range(len(class0)):
        if i < len(class0)*test_ratio:
            test_data.append(class0[i])
        else:
            train_data.append(class0[i])

    for i in range(len(class1)):
        if i < len(class1)*test_ratio:
            test_data.append(class1[i])
        else:
            train_data.append(class1[i])

    return np.array(train_data), np.array(test_data)

# 训练样本抽样times次的自助法
# 进行 times 次 有放回采样，每次都从原始数据集中随机采样生成一个训练集，同时构造相应的测试集（自助法）。
def Bootstrapping(data, times):
    data = list(data)
    n = len(data)
    
    bootstrap_samples = []
    for _ in range(times):
        # 从原始数据中有放回地随机抽取 n 个样本构成训练集
        train_data = [random.choice(data) for _ in range(n)]

        # 将训练集转成集合用于查重（元组可以作为集合元素）
        train_set = set(map(tuple, train_data))

        # 测试集 = 原始数据中不在训练集中的样本（即未被抽中的）
        test_data = [d for d in data if tuple(d) not in train_set]

        # 保存每一组划分结果
        bootstrap_samples.append((np.array(train_data), np.array(test_data)))

    return bootstrap_samples

