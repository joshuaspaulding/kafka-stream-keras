import string
valid = ''.join([string.digits, string.ascii_lowercase, '-'])
valid_chars = {j:i+1 for i, j in enumerate(valid)}
maxlen = max([len(x) for x in "google"])
print(valid_chars)

from keras.preprocessing import sequence
