import torch.nn as nn

class HiraganaCNN(nn.Module):
    def __init__(self, num_classes):
        super(HiraganaCNN, self).__init__()
        
        self.conv1 = nn.Conv2d(1, 32, kernel_size=5, stride=1, padding=2)
        self.conv2 = nn.Conv2d(32, 64, kernel_size=5, stride=1, padding=2)
        self.pool = nn.MaxPool2d(kernel_size=2, stride=2, padding=0)
        
        # Calculate the size of the features after the convolution and pooling layers
        # This example assumes the input images are 63x64 pixels
        # Adjust the size if your image dimensions are different
        #feature_size = 64 * 15 * 16 
        feature_size = 15360
        
        self.fc1 = nn.Linear(feature_size, 1024)
        self.fc2 = nn.Linear(1024, num_classes)
        
    def forward(self, x):
        # Apply the first convolution and pooling layers
        x = self.pool(nn.functional.relu(self.conv1(x)))
        
        # Apply the second convolution and pooling layers
        x = self.pool(nn.functional.relu(self.conv2(x)))
        
        # Flatten the features for the fully connected layer
        x = x.view(x.size(0), -1)
        
        # Apply the first fully connected layer with ReLU activation
        x = nn.functional.relu(self.fc1(x))
        
        # Apply the second fully connected layer to get class scores
        x = self.fc2(x)
        
        return x