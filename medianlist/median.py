class MedianList(object):
    def __init__(self):
        self.list = []
        self.median = 0

    def add_num(self, number):
        idx = self.binary_search(number)
        self.list.insert(idx, number)

    def get_median(self):
        if not self.list:
            return 0
        list_len = len(self.list)
        if list_len % 2 == 0:
            return float(self.list[list_len / 2 - 1] + self.list[list_len / 2]) / 2
        return self.list[(list_len - 1) / 2]

    def binary_search(self, number):
        """
        Search for an index in the list where the item equals to number
        or item is less than number and the next item is greater than number
        """
        if not self.list:
            return 0
        list_len = len(self.list)
        idx = int(list_len / 2)
        while True:
            if self.list[idx] < number and idx == list_len - 1:
                return idx + 1
            if self.list[idx] > number and idx == 0:
                return idx
            if self.list[idx] == number or (self.list[idx] < number and self.list[idx + 1] > number):
                return idx + 1
            if self.list[idx] < number:
                idx = int((list_len + idx) / 2)
            else:
                idx = int(idx / 2)
        return idx

def main():
    testcases = [
        [[1, 3, 9, 5, 7, 5, 0, 4, 3, 2, 9, 1, 0], 3],
        [[6, 1, 2], 2],
        [[3, 9, 2, 5, 2], 3]
    ]

    for case in testcases:
        median_list = MedianList()
        print('Executing test case %s' % case[0])
        for number in case[0]:
            median_list.add_num(number)
        assert median_list.get_median() == case[1]

if __name__ == '__main__':
    main()