from keras.models import Sequential
from keras import backend as K
from keras.layers import Embedding, Dropout, Dense, Flatten
from keras.utils import np_utils
from keras.preprocessing import sequence, text
from keras.callbacks import ModelCheckpoint
from keras.layers.convolutional import Conv1D
from keras.layers.pooling import GlobalMaxPooling1D, MaxPool1D
import csv
import random
import numpy as np

max_len = 4000
vocab_size = 5000

stop_words = []
with open("database/stop_words.txt") as txt:
    for line in txt.readlines():
        stop_words.append(line.strip())

def percent(text):
    count = 0
    text = "".join(c for c in text if c not in ('!','.',':','?',';'))
    words = text.split()
    for word in words:
        if word.lower() in stop_words:
            count +=1
    return count / len(words)

model = Sequential()
model.add(Embedding(vocab_size, 32, input_length=max_len))
model.add(Conv1D(filters=32, kernel_size=(16), padding='valid', activation='relu'))
model.add(MaxPool1D())
model.add(Conv1D(filters=32, kernel_size=(16), padding='valid', activation='relu'))
model.add(GlobalMaxPooling1D())
model.add(Dense(100, activation='sigmoid'))
model.add(Dense(1))
model.compile(loss='mean_squared_error', optimizer='adam', metrics=['mae'])

def generate(skip=False):
    with open('database/banki_ru_train.csv') as f:
        reader = csv.reader(f)
        first = True
        if skip:
            m = random.randint(1, 40000)
        for row in reader:
            if first:
                first = False
                continue
            x_train = text.one_hot(row[2], vocab_size)
            y_train = np.array(percent(row[2])).reshape(-1, 1)
            x_train = sequence.pad_sequences([x_train], maxlen=max_len)
            res = x_train, y_train
            yield res

def evalute(s):
    model.load_weights("database/checker2.hdf5")
    x = text.one_hot(s, vocab_size)
    x = sequence.pad_sequences([x], maxlen=max_len)
    return max(model.predict(x))[0]

if __name__ == "__main__":
    model.fit_generator(generate(), 10000, epochs=10, verbose=1, validation_data=generate(True), validation_steps=1000, 
    callbacks=[ModelCheckpoint("database/checker2.hdf5", monitor="loss", save_best_only=True, mode="auto", save_weights_only=True)])
