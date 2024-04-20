import torch
from HiraganaModelClass import HiraganaCNN
import numpy as np
from labels import reversed_labels_list
import sys
from torch.utils.data import DataLoader


def predict(image_bytes) -> str:
    MODEL_PATH = 'models/Hiragana.pth'
    model = HiraganaCNN(num_classes=48)
    model.load_state_dict(torch.load(MODEL_PATH, map_location=torch.device('cpu')))
    model.eval()
    high_bits = [i >> 4 for i in image_bytes]    # Extracts the high 4 bits of each byte
    low_bits = [i & 0x0F for i in image_bytes] # Extracts the low 4 bits of each byte
    # Concatenate the high and low bits arrays to get a single array of 4-bit values
    pixels = np.empty(len(high_bits) + len(low_bits))
    pixels[0::2], pixels[1::2] = high_bits, low_bits
    pixels = pixels.astype(np.float32).reshape(1, 1, 63, 64)
    pixels_tensor = torch.utils.data.default_convert(pixels)
    outputs = model(pixels_tensor)

    _, prediction = torch.max(outputs, 1)

    return reversed_labels_list[prediction]
