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

def main():
    testcases = [
        [[1, 3, 9, 5, 7, 5, 0, 4, 3, 2, 9, 1, 0], [1, 2, 3, 4, 5, 5, 5, 4.5, 4, 3.5, 4, 3.5, 3]],
        [[6, 1, 2], [6, 3.5, 2]],
        [[3, 9, 2, 5, 2], [3, 6, 3, 4, 3]],
        [[5, 5, 5, 5], [5, 5, 5, 5]],
        [[9, 6, 4, 3, 1], [9, 7.5, 6, 5, 4]],
        [[1, 2, 3, 4], [1, 1.5, 2, 2.5]],
        [[1, 3, 2], [1, 2, 2]],
        [[1], [1]]
    ]

    for case in testcases:
        median_list = MedianList()
        print('Executing test case %s' % case[0])
        for k, number in enumerate(case[0]):
            median_list.add_num(number)
            assert median_list.get_median() == case[1][k]

if __name__ == '__main__':
    main()
