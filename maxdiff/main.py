from maxdiff2 import maxdiff

def main():
    inputs = [
        [],
        [1],
        [1, 2],
        [4, 7, 1, 3, 2, 9],
        [1, 2, 3, 4, 5, 6],
        [4, 3, 2, 1],
        [3, 3, 3],
        [-1, 7, 1, 3, 2, 9],
        [4, 1, 3, 9, 2, -2, 3]
    ]
    outputs = [
        -1,
        -1,
        1,
        8,
        5,
        -1,
        0,
        10,
        8
    ]
    for i in range(len(inputs)):
        print('Executing test case: %s' % inputs[i])
        assert maxdiff(inputs[i]) == outputs[i]

if __name__ == '__main__':
    main()
