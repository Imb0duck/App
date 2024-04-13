import torch
from HiraganaModelClass import HiraganaCNN
import time
import psutil

import torch
from labels import reversed_labels_list


def get_label(image):
    MODEL_PATH = 'models/Hiragana.pth'
    model = HiraganaCNN(num_classes=48)
    model.load_state_dict(torch.load(MODEL_PATH))
    model.eval()
    #tart = time.time()

    outputs = model(image)

    _, prediction = torch.max(outputs, 1)
    print(f'prediction = {reversed_labels_list[prediction]}')
    #end = time.time()
    #print("Execution time:", end - start, "seconds")
    return prediction


if __name__ == "__main__":  ##
    image = torch.load("array1.pt")

    get_label(image)

    # Get system resource usage
    process = psutil.Process()
    memory_usage = process.memory_info().rss / (1024 ** 2)  # Memory usage in MB
    cpu_usage = process.cpu_percent(interval=1)  # CPU usage in percentage

    print("Memory usage:", memory_usage, "MB")
    print("CPU usage:", cpu_usage, "%")
