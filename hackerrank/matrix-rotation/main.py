from rotate import rotate

def main():
    testcases = [
        [
            [
                [1, 2, 3, 4],
                [5, 6, 7, 8],
                [9, 10, 11, 12],
                [13, 14, 15, 16]
            ],
            4, 4, 1,
            [
                [2, 3, 4, 8],
                [1, 7, 11, 12],
                [5, 6, 10, 16],
                [9, 13, 14, 15]
            ]
        ]
    ]

    for case in testcases:
        print('Executing test case: %s rotate %s' % (case[0], case[3]))
        rotated = rotate(case[0], case[1], case[2], case[3])
        assert compare_matrix(case[4], rotated)

def compare_matrix(matrix, rotated):
    if len(matrix) != len(rotated):
        return False
    for i in range(len(matrix)):
        result = compare_row(matrix[i], rotated[i])
        if not result:
            return False
    return True

def compare_row(row1, row2):
    if len(row1) != len(row2):
        return False
    for i in range(len(row1)):
        if row1[i] != row2[i]:
            return False
    return True

if __name__ == '__main__':
    main()
