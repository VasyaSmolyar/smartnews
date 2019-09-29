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

model = Sequential()
model.add(Embedding(vocab_size, 32, input_length=max_len))
model.add(Conv1D(filters=32, kernel_size=(16), padding='valid', activation='relu'))
model.add(MaxPool1D())
model.add(Conv1D(filters=32, kernel_size=(16), padding='valid', activation='relu'))
model.add(GlobalMaxPooling1D())
model.add(Dense(100, activation='sigmoid'))
model.add(Dense(6, activation='sigmoid'))
model.compile(loss='categorical_crossentropy', optimizer='adam', metrics=['accuracy'])

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
            y_train = int(row[4])
            x_train = sequence.pad_sequences([x_train], maxlen=max_len)
            y_train = np_utils.to_categorical([y_train], 6)
            res = x_train, y_train
            yield res

def evalute(s):
    model.load_weights("database/banker2.hdf5")
    x = text.one_hot(s, vocab_size)
    x = sequence.pad_sequences([x], maxlen=max_len)
    res = np.argmax(model.predict(x))
    return res

if __name__ == "__main__":
    model.fit_generator(generate(), 10000, epochs=10, verbose=1, validation_data=generate(True), validation_steps=1000, 
    callbacks=[ModelCheckpoint("database/banker2.hdf5", monitor="loss", save_best_only=True, mode="auto", save_weights_only=True)])

