from regex import Matcher

def main():
    testcases = [
        ['a', 'a', True],
        ['a', 'b', False],
        ['c', '.', True],
        ['abcd', 'a.c', False],
        ['', '*', True],
        ['abc', 'c*', False],
        ['abc', '*c*', True],
        ['abc', '*d', False],
        ['bedc', '*b*c', True],
        ['abcd', 'a.c*', True],
        ['abcd', 'a.c*.', True],
        ['abcd', 'a*d', True],
        ['abcd', 'a****d', True],
        ['', '*.', False],
        ['abcabfxyza', '*ab*klm', False],
        ['abcabf', '*abf', True],
        ['abccccc', '*ccc', True]
    ]

    import time
    for case in testcases:
        print('Executing test case %s %s %s' % (case[0], case[1], case[2]))
        start = time.time()
        assert Matcher().match(case[0], case[1]) == case[2]
        print('Executed successfully in %.2fms' % ((time.time() - start) * 1000))

if __name__ == '__main__':
    main()
