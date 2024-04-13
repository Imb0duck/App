import torch
from HiraganaModelClass import HiraganaCNN
import psutil
import numpy as np
from labels import reversed_labels_list
import sys
import matplotlib.pyplot as plt
from torch.utils.data import DataLoader
from torch.utils.data import Dataset



def get_label():
    image_path = sys.argv[1]
    MODEL_PATH = 'Ml/models/Hiragana.pth'
    model = HiraganaCNN(num_classes=48)
    model.load_state_dict(torch.load(MODEL_PATH,map_location=torch.device('cpu')))
    model.eval()

    with open(image_path, 'rb') as file:
        image_bytes = file.read()
    image_bytes = np.frombuffer(image_bytes, dtype=np.uint8)

    high_bits = image_bytes >> 4  # Extracts the high 4 bits of each byte
    low_bits = image_bytes & 0x0F  # Extracts the low 4 bits of each byte
    # Concatenate the high and low bits arrays to get a single array of 4-bit values
    pixels = np.empty(high_bits.size + low_bits.size, dtype=high_bits.dtype)
    pixels[0::2], pixels[1::2] = high_bits, low_bits
    pixels = pixels.astype(np.float32).reshape(1, 1, 63, 64)
    pixels_tensor = torch.utils.data.default_convert(pixels)
    outputs = model(pixels_tensor)

    _, prediction = torch.max(outputs, 1)

    print(reversed_labels_list[prediction])
