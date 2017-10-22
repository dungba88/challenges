import math

def min_window(s, t):
    n = len(s)

    cur_min_head = None
    cur_min = math.inf

    required_map = {}
    for c in t:
        required_map[c] = 1 if c not in required_map else required_map[c] + 1
    collected = len(required_map)

    i = j = 0
    last_j = -1

    while i < n and j < n:
        head = s[i]
        tail = s[j]
        if tail in required_map and last_j != j:
            last_j = j
            required_map[tail] -= 1
            if required_map[tail] == 0:
                collected -= 1
        if collected > 0:
            j += 1
        else:
            if cur_min > j - i:
                cur_min = j - i
                cur_min_head = i
            if head in required_map:
                required_map[head] += 1
                if required_map[head] > 0:
                    collected += 1
            i += 1

    if cur_min == math.inf:
        return None
    return s[cur_min_head:cur_min_head + cur_min + 1]
