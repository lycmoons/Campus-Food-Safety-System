import numpy as np
import matplotlib.pyplot as plt
from sklearn.preprocessing import PolynomialFeatures
from sklearn.linear_model import LogisticRegression
from sklearn.pipeline import make_pipeline

# matplotlib画图中中文显示会有问题，需要这两行设置默认字体可以显示中文
plt.rcParams['font.sans-serif'] = ['SimHei']
plt.rcParams['axes.unicode_minus'] = False

def drawing_data(data, title):
    Label = []
    Input1 = []
    Input2 = []

    for d in data:
        Label.append(d[0])
        Input1.append(d[1])
        Input2.append(d[2])

    plt.figure(title)

    for i in range(len(Label)):
        if Label[i] == 1:
            plt.scatter(Input1[i], Input2[i], c='r', marker='+')
        else:
            plt.scatter(Input1[i], Input2[i], c='b', marker='.')

    plt.title(title)
    plt.xlabel('坐标x')
    plt.ylabel('坐标y')
    plt.xlim(-8, 6)
    plt.ylim(-8, 6)

def drawing_model(data, model, title):
    Label = []
    Input1 = []
    Input2 = []

    for d in data:
        Label.append(d[0])
        Input1.append(d[1])
        Input2.append(d[2])

    plt.figure(title)
    xx, yy = np.meshgrid(np.arange(-8, 6, 0.01),
                         np.arange(-8, 6, 0.01))
    Z = model.predict(np.c_[xx.ravel(), yy.ravel()])
    Z = Z.reshape(xx.shape)
    plt.contourf(xx, yy, Z, alpha=0.3, cmap=plt.cm.coolwarm)
    #　plt.contour(xx, yy, Z, colors='k', linewidths=1.5)

    for i in range(len(Label)):
        if Label[i] == 1:
            plt.scatter(Input1[i], Input2[i], c='r', marker='+')
        else:
            plt.scatter(Input1[i], Input2[i], c='b', marker='.')

    plt.title(title)
    plt.xlabel('坐标x')
    plt.ylabel('坐标y')
    plt.xlim(-8, 6)
    plt.ylim(-8, 6)

def drawing_models(models, test_data, Ms, title):
    Label = []
    Input1 = []
    Input2 = []

    for d in test_data:
        Label.append(d[0])
        Input1.append(d[1])
        Input2.append(d[2])

    plt.figure(title)


    for i in range(len(Label)):
        if Label[i] == 1:
            plt.scatter(Input1[i], Input2[i], c='r', marker='+')
        else:
            plt.scatter(Input1[i], Input2[i], c='b', marker='.')

    xx, yy = np.meshgrid(np.arange(-8, 6, 0.01),
                         np.arange(-8, 6, 0.01))
    for M_model in models:
        Z = M_model.predict(np.c_[xx.ravel(), yy.ravel()])
        Z = Z.reshape(xx.shape)
        #plt.contourf(xx, yy, Z, alpha=0.3, cmap=plt.cm.coolwarm)
        plt.contour(xx, yy, Z, colors='k', linewidths=1.5)

    plt.title(title)
    plt.xlabel('坐标x')
    plt.ylabel('坐标y')
    plt.xlim(-8, 6)
    plt.ylim(-8, 6)

"""
    设0为True，1为false
        predict
    gt  0       1
    0   TP      FN
    1   FP      TN

"""
def drawing_PR(Label, Output, title):
    from sklearn.metrics import precision_recall_curve

    precision, recall, _ = precision_recall_curve(Label, Output)
    plt.figure(title)
    plt.plot(recall, precision, marker='.')
    plt.title(f'{title} - PR曲线')
    plt.xlabel('Recall（召回率）')
    plt.ylabel('Precision（精确率）')
    plt.grid(True)
    plt.xlim([0.0, 1.0])
    plt.ylim([0.0, 1.05])

def drawing_ROC(Label, Output, title):
    from sklearn.metrics import roc_curve, auc

    fpr, tpr, _ = roc_curve(Label, Output)
    roc_auc = auc(fpr, tpr)

    plt.figure(title)
    plt.plot(fpr, tpr, color='darkorange', lw=2, label=f'ROC曲线 (AUC = {roc_auc:.2f})')
    plt.plot([0, 1], [0, 1], color='navy', lw=1, linestyle='--')
    plt.xlim([0.0, 1.0])
    plt.ylim([0.0, 1.05])
    plt.xlabel('False Positive Rate（假阳性率）')
    plt.ylabel('True Positive Rate（真正率）')
    plt.title(f'{title} - ROC曲线')
    plt.legend(loc='lower right')
    plt.grid(True)

def drawing_PRs(outputs, test_data, Ms, title):
    from sklearn.metrics import precision_recall_curve

    Label = [d[0] for d in test_data]
    plt.figure(title)

    for i, output in enumerate(outputs):
        precision, recall, _ = precision_recall_curve(Label, output)
        plt.plot(recall, precision, lw=1.5, label=f'M={Ms[i]}')

    plt.title(f'{title} - 多模型 PR曲线')
    plt.xlabel('Recall（召回率）')
    plt.ylabel('Precision（精确率）')
    plt.xlim([0.0, 1.0])
    plt.ylim([0.0, 1.05])
    plt.grid(True)
    plt.legend()

def drawing_ROCs(outputs, test_data, Ms, title):
    from sklearn.metrics import roc_curve, auc

    Label = [d[0] for d in test_data]
    plt.figure(title)

    for i, output in enumerate(outputs):
        fpr, tpr, _ = roc_curve(Label, output)
        roc_auc = auc(fpr, tpr)
        plt.plot(fpr, tpr, lw=1.5, label=f'M={Ms[i]} (AUC={roc_auc:.2f})')

    plt.plot([0, 1], [0, 1], color='gray', linestyle='--')
    plt.title(f'{title} - 多模型 ROC曲线')
    plt.xlabel('False Positive Rate（假阳性率）')
    plt.ylabel('True Positive Rate（真正率）')
    plt.grid(True)
    plt.legend()
