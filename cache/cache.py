class Cache(object):
    def __init__(self, algorithm):
        self.algorithm = algorithm
        self.data = dict()
        self.algorithm.target = self

    def put(self, key, value):
        self.algorithm.on_put(key, value)
        self.data[key] = value

    def get(self, key):
        if key not in self.data:
            self.on_miss(key)
        self.algorithm.on_get(key)
        return self.data[key] if key in self.data else None

    def on_miss(self, key):
        # we don't care about this case for now
        return None
