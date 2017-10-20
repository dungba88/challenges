class Cache(object):
    def __init__(self, algorithm):
        self.algorithm = algorithm
        self.data = dict()
        self.algorithm.target = self

    def put(self, key, value):
        self.data[key] = value
        self.algorithm.on_put(key, value)

    def get(self, key):
        if key not in self.data:
            return None
        self.algorithm.on_get(key)
        return self.data[key]
