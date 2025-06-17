# 第一步：导入所需库
from sklearn.datasets import load_diabetes
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LinearRegression, SGDRegressor
from sklearn.metrics import mean_squared_error
import numpy as np
import matplotlib.pyplot as plt

plt.rcParams['font.sans-serif'] = ['SimHei']
plt.rcParams['axes.unicode_minus'] = False

# 第二步：加载糖尿病数据集
diabetes = load_diabetes()

# 数据介绍
# diabetes.data 是 shape = (442, 10) 的特征矩阵，共10个生理指标
# diabetes.target 是 shape = (442,) 的目标变量，即一年后的疾病发展程度

# 为了方便可视化，我们只选取一个特征（例如 BMI 对应的索引2）
X = diabetes.data[:, np.newaxis, 2]  # 选择 BMI 特征并添加一个维度
y = diabetes.target  # 目标值是一维向量

# 第三步：将数据划分为训练集和测试集（80% 训练，20% 测试）
X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.2, random_state=42
)

# 第四步：训练线性回归模型（最小二乘法）
lr_model = LinearRegression()               # 创建最小二乘模型
lr_model.fit(X_train, y_train)              # 拟合模型

# 第五步：训练线性回归模型（梯度下降法）
sgd_model = SGDRegressor(
    loss='squared_error',   # 使用平方误差损失函数
    penalty='l2',           # L2 正则化（Ridge）
    learning_rate='invscaling',  # 学习率调整策略
    eta0=0.01,              # 初始学习率
    max_iter=1000,          # 最多迭代次数
    random_state=42
)
sgd_model.fit(X_train, y_train)

# 第六步：模型预测
lr_pred = lr_model.predict(X_test)
sgd_pred = sgd_model.predict(X_test)

# 第七步：使用 sklearn 的 MSE 评估模型效果
lr_mse = mean_squared_error(y_test, lr_pred)
sgd_mse = mean_squared_error(y_test, sgd_pred)

print("最小二乘法 LinearRegression 的 MSE:", lr_mse)
print("梯度下降法 SGDRegressor 的 MSE:", sgd_mse)

# 第八步：手动实现 MSE（扩展要求）
def my_mse(y_true, y_pred):
    return np.mean((y_true - y_pred) ** 2)

print("自己实现的 MSE（LinearRegression）:", my_mse(y_test, lr_pred))
print("自己实现的 MSE（SGDRegressor）:", my_mse(y_test, sgd_pred))

# 第九步：结果可视化
plt.figure(figsize=(8, 6))
plt.scatter(X_test, y_test, color='black', label='测试数据点')
plt.plot(X_test, lr_pred, color='blue', linewidth=2, label='最小二乘法拟合线')
plt.plot(X_test, sgd_pred, color='red', linewidth=2, linestyle='--', label='梯度下降拟合线')
plt.xlabel('BMI（身体质量指数）')
plt.ylabel('一年后的糖尿病进展程度')
plt.title('线性回归模型对比：最小二乘 vs 梯度下降')
plt.legend()
plt.grid(True)
plt.show()
