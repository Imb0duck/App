from torch.utils.data import Dataset
import numpy as np

class Data(Dataset):
    def __init__(self, dataLocation) -> None:
        #each record is 2052 bytes
        record_size = 2052 
        self.labels_list = {" A":0, " I":1, " U":2, " E":3, " O":4,
               "KA":5, "KI":6, "KU":7, "KE":8, "KO":9,
               "SA":10, "SI":11, "SU":12, "SE":13, "SO":14,
               "TA":15, "TI":16, "TU":17, "TE":18, "TO":19,
               "NA":20, "NI":21, "NU":22, "NE":23, "NO":24,
               "HA":25, "HI":26, "HU":27, "HE":28, "HO":29,
               "MA":30, "MI":31, "MU":32, "ME":33, "MO":34,
               "YA":35, "YU":36, "YO":37,
               "RA":38, "RI":39, "RU":40, "RE":41, "RO":42,
               "WA":43, "WO":44,
               " N":45,
               ",,":46, ",0":47
        }
        self.data = np.fromfile(dataLocation, dtype=np.uint8)
        self.n_samples = self.data.shape[0] // record_size
        self.data = self.data.reshape(self.n_samples, record_size)
        
    
    @staticmethod
    def normalize_data(data, threshold=7):
        # Extract the high and low 4-bit numbers from the entire data array
        high_bits = (data[:, 33:2049] >> 4) & 0x0F
        low_bits = data[:, 33:2049] & 0x0F
        
        # Normalize high and low bits using vectorized operations
        high_bits = np.where(high_bits > threshold, 0, 15)
        low_bits = np.where(low_bits > threshold, 0, 15)
        
        # Pack the high and low bits back into one byte
        normalized_data = (high_bits << 4) | low_bits
        
        # Update the original data array with the normalized values
        data[:, 33:2049] = normalized_data
        
        return data
    
    
    def expandData(self, dataLocation) -> None:
        record_size = 2052 
        additional_data = np.fromfile(dataLocation, dtype=np.uint8)
        n_samples = additional_data.shape[0] // record_size
        additional_data = additional_data.reshape(n_samples, record_size)
        self.data = np.append(self.data, additional_data, axis=0)
        self.n_samples += n_samples


    def __getitem__(self, index):
        label = self.data[index, 2:4]
        label = self.labels_list[str(chr(label[0]) + chr(label[1]))]
        # Extract the data slice 
        raw_data = self.data[index, 33:2049]
        
        # Convert each byte to two 4-bit values
        high_bits = raw_data >> 4  # Extracts the high 4 bits of each byte
        low_bits = raw_data & 0x0F  # Extracts the low 4 bits of each byte
        
        # Concatenate the high and low bits arrays to get a single array of 4-bit values
        pixels = np.empty(high_bits.size + low_bits.size, dtype=high_bits.dtype)
        pixels[0::2], pixels[1::2] = high_bits, low_bits
        pixels = pixels.astype(np.float32).reshape(1,63,64)
    
        return pixels, label
    
    def __len__(self):
        return self.n_samples