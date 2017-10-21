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

def maxdiff(a):
    if len(a) < 2:
        return -1
    cur_max = -1
    cur_diff = 0

    diffs = convert_to_diffs(a)

    for i in range(len(diffs) - 1):
        item = diffs[i]
        if item < 0:
            # don't care about negative items
            # since that means a[i] > a[i + 1]
            continue

        cur_diff += item
        if cur_diff > cur_max:
            cur_max = cur_diff

        next_item = diffs[i + 1]
        cur_diff += next_item
        # if the next down slope is too high
        # that means we should skip the current up slope
        if cur_diff < 0:
            cur_diff = 0

    if diffs[len(diffs) - 1] + cur_diff > cur_max:
        cur_max = diffs[len(diffs) - 1] + cur_diff
    return cur_max

def convert_to_diffs(a):
    """
    convert the array into array of up-and-down slopes
    if the items are continously increase or decrease,
    they will be grouped together

    an array of [1, 3, 2] will be converted into [2, -1]
    while an array of [1, 2, 3] will be converted into [2]
    """
    diffs = []
    cur_sign = 0
    cur_acc = 0
    i = 0
    while i < len(a) - 1:
        sign = 1 if a[i + 1] > a[i] else -1
        acc = a[i + 1] - a[i]
        if cur_sign == 0:
            cur_sign = sign
        if cur_sign == sign:
            cur_acc += acc
        else:
            diffs.append(cur_acc)
            cur_acc = acc
        cur_sign = sign
        i += 1

    diffs.append(cur_acc)
    return diffs

if __name__ == '__main__':
    main()
