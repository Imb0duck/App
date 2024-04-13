from labels import reversed_labels_list
import matplotlib.pyplot as plt
from sklearn.model_selection import train_test_split
import torch
import torch.nn as nn
from torch.utils.data import DataLoader
from hiraganaDataClass import Data
from HiraganaModelClass import HiraganaCNN


#trainDataLocation = './data/DigitRecognition/train.csv'
dataLocation = 'data/ETL7(hiragana)/ETL7LC_1'
dataLocation2 = 'data/ETL7(hiragana)/ETL7LC_2'

device = torch.device('cuda' if torch.cuda.is_available() else 'cpu')
hiroganaData = Data(dataLocation)
hiroganaData.expandData(dataLocation2)
hiroganaData.data = hiroganaData.normalize_data(hiroganaData.data)

trainData,testData = train_test_split(hiroganaData,test_size=0.15, random_state=1234, shuffle=True)
#trainData = Data.normalize_data(trainData, 8)
n_inputfeatures = 63*64
n_classes = 48
batch_size = 100
n_hidden = 100
#print(digitData.xy.shape)
train_loader = DataLoader(dataset=trainData,batch_size= batch_size, shuffle=True)
test_loader = DataLoader(dataset=testData,batch_size= batch_size, shuffle=True)

example = iter(train_loader)
samples,labels = next(example)


model = HiraganaCNN(num_classes= n_classes).to(device)
learning_rate = 0.001
criterion = nn.CrossEntropyLoss()
optimizer = torch.optim.Adam(model.parameters(), lr = learning_rate)
n_epochs = 5
n_batches = len(trainData)/batch_size

for epoch in range(n_epochs):
    for i,(images, labels) in enumerate(train_loader):
        
        images = images.to(device)
        labels = labels.clone().detach().type(torch.long).to(device)
        #labels = torch.tensor(labels, dtype=torch.long).to(device)
        y_pred = model(images)
        labels = labels.view(labels.shape[0])
        labels = labels.type(torch.LongTensor).to(device)
        #print(y_pred.shape, labels.shape)
        loss = criterion(y_pred, labels)
        #-backward pass: gradients
        loss.backward()
        
        #-update weight
        optimizer.step()
        optimizer.zero_grad()
        if (i+1)%100 == 0:
            print(f'epoch:{epoch+1}/{n_epochs}, step = {i+1}, loss = {loss.item():.4f}')
    
with torch.no_grad():
    n_correct = 0
    n_samples = 0
    for images,labels in test_loader:
        images = images.to(device)
        labels = labels.view(labels.shape[0]).to(device)
        outputs = model(images)

        _, predictions = torch.max(outputs,1)
        n_samples += labels.shape[0]
        n_correct += (predictions == labels).sum().item()
        #print(n_samples,n_correct, predictions.shape, labels.shape)
    acc = n_correct/n_samples
    print(f'total accuracy = {acc}')

PATH = 'models/Hiragana.pth'
#torch.save(model.state_dict(), PATH)

example = iter(test_loader)
samples,labels = next(example)
for i in range(6):
    plt.subplot(2,3,i+1)
    plt.imshow(samples[i].view(63,64), cmap = 'gray')
    print(reversed_labels_list[labels[i]])
plt.show()
samples = samples.to(device)
out = model(samples)
_, predictions = torch.max(out,1)
for i in range(15):
    print(f'predicted:{predictions[i]}, real:{labels[i]}')