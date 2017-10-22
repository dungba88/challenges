from min_window import min_window

def main():
    testcases = [
        ["ADOBECODEBANC", "ABC", "BANC"],
        ["ADOBECODEBANC", "ABBC", "BECODEBA"],
        ["ABC", "A", "A"],
        ["ABC", "AAC", None],
        ["AXXBXBXAXXAC", "AB", "BXA"],
    ]

    for case in testcases:
        print('Executing test case %s - %s' % (case[0], case[1]))
        assert min_window(case[0], case[1]) == case[2]

if __name__ == '__main__':
    main()
