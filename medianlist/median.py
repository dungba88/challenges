class MedianList(object):
    def __init__(self):
        self.list = []

    def add_num(self, number):
        idx = self.binary_search(number)
        self.list.insert(idx, number)

    def get_median(self):
        if not self.list:
            return 0
        list_len = len(self.list)
        if list_len % 2 == 0:
            return float(self.list[int(list_len / 2 - 1)] + self.list[int(list_len / 2)]) / 2
        return self.list[int((list_len - 1) / 2)]

    def binary_search(self, number):
        """
        Search for an index in the list where the item equals to number
        or item is less than number and the next item is greater than number
        """
        if not self.list:
            return 0
        list_len = len(self.list)
        low = 0
        high = list_len - 1
        while low <= high:
            idx = int((low + high) / 2)
            if self.list[idx] < number and idx == list_len - 1:
                return list_len
            if self.list[idx] == number or (self.list[idx] < number and self.list[idx + 1] > number):
                return idx + 1
            if self.list[idx] < number:
                low = idx + 1
            else:
                high = idx - 1
        return 0
