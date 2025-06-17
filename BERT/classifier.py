import matplotlib.pyplot as plt
import numpy as np
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.neighbors import KNeighborsClassifier
from sklearn.linear_model import LogisticRegression
from sklearn.tree import DecisionTreeClassifier
from sklearn.svm import SVC

from sklearn.metrics import accuracy_score, precision_score, recall_score, f1_score

# 1. 加载 Iris 数据集
iris = load_iris()
X = iris.data
y = iris.target
class_names = iris.target_names

# 仅选取花瓣长度和宽度（第3、4列）用于可视化
X_vis = X[:, 2:4]

# 2. 分割数据集为训练集和测试集（固定 random_state 保证可复现）
X_train, X_test, y_train, y_test = train_test_split(X_vis, y, test_size=0.3, random_state=42)

# 3. Predict 函数：选择分类器
def Predict(classifier_type):
    if classifier_type == "KNN":
        model = KNeighborsClassifier(n_neighbors=3)
    elif classifier_type == "Logistic":
        model = LogisticRegression(max_iter=200)
    elif classifier_type == "DecisionTree":
        model = DecisionTreeClassifier(max_depth=4)
    elif classifier_type == "SVM":
        model = SVC(kernel='rbf', gamma='scale')
    else:
        raise ValueError("Unsupported classifier type")

    model.fit(X_train, y_train)
    y_pred = model.predict(X_test)

    # 评估
    acc = accuracy_score(y_test, y_pred)
    precision = precision_score(y_test, y_pred, average='macro')
    recall = recall_score(y_test, y_pred, average='macro')
    f1 = f1_score(y_test, y_pred, average='macro')

    print(f"\n=== {classifier_type} Evaluation ===")
    print(f"Accuracy : {acc:.4f}")
    print(f"Precision: {precision:.4f}")
    print(f"Recall   : {recall:.4f}")
    print(f"F1 Score : {f1:.4f}")

    VisClassifierResult(model, f"{classifier_type} Result")
    return model

# 4. 可视化原始数据（二维）
def Vis():
    plt.figure(figsize=(8, 6))
    for target in range(3):
        plt.scatter(
            X_vis[y == target, 0], X_vis[y == target, 1],  # 花瓣长度、宽度
            label=class_names[target]
        )
    plt.xlabel("Petal Length")
    plt.ylabel("Petal Width")
    plt.title("Iris Dataset Visualization (Original Data)")
    plt.legend()
    plt.grid(True)
    plt.show()

# 5. 可视化分类结果（决策边界 + 测试集预测）
def VisClassifierResult(model, title):
    x_min, x_max = X_vis[:, 0].min() - 0.5, X_vis[:, 0].max() + 0.5
    y_min, y_max = X_vis[:, 1].min() - 0.5, X_vis[:, 1].max() + 0.5
    xx, yy = np.meshgrid(np.arange(x_min, x_max, 0.02),
                         np.arange(y_min, y_max, 0.02))
    grid = np.c_[xx.ravel(), yy.ravel()]
    Z = model.predict(grid).reshape(xx.shape)

    plt.figure(figsize=(8, 6))
    plt.contourf(xx, yy, Z, alpha=0.3, cmap=plt.cm.coolwarm)
    scatter = plt.scatter(X_test[:, 0], X_test[:, 1], c=model.predict(X_test), edgecolor='k', cmap=plt.cm.coolwarm)
    plt.xlabel("Petal Length")
    plt.ylabel("Petal Width")
    plt.title(title)
    plt.grid(True)
    plt.show()

# ==== 示例 ====
Vis()
Predict("KNN")
Predict("Logistic")
Predict("DecisionTree")
Predict("SVM")
