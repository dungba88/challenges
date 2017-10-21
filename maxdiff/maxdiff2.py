def maxdiff(a):
    if len(a) < 2:
        return -1
    cur_max = -1
    max_value = None
    for i in reversed(range(len(a))):
        if max_value is not None:
            tmp = max_value - a[i]
            if tmp > cur_max:
                cur_max = tmp
        if max_value is None or a[i] > max_value:
            max_value = a[i]
    return cur_max
