"""
A simple regex matcher which can accept:
- a-z
- . for any single character
- * for any string (including empty string)

This algorithm uses recursion with memoization approach.
"""

class Matcher(object):
    def __init__(self):
        self.caches = {}

    def match(self, s, pattern):
        return self.do_match_with_cache(s, pattern, 0, 0)

    def do_match_with_cache(self, s, pattern, s_idx, pattern_idx):
        key = str(s_idx) + '_' + str(pattern_idx)
        if not key in self.caches:
            self.caches[key] = self.do_match(s, pattern, s_idx, pattern_idx)
        return self.caches[key]

    def do_match(self, s, pattern, s_idx, pattern_idx):
        if s_idx >= len(s) or pattern_idx >= len(pattern):
            if pattern_idx == len(pattern) - 1: # * matches empty string
                return pattern[pattern_idx] == '*'
            return s_idx >= len(s) and pattern_idx >= len(pattern)
        if not self.is_match(s[s_idx], pattern[pattern_idx]):
            return False
        if pattern[pattern_idx] != '*':
            return self.do_match_with_cache(s, pattern, s_idx + 1, pattern_idx + 1)

        # this is only for the case where consecutive * are allowed
        while pattern_idx < len(pattern) - 1 and pattern[pattern_idx + 1] == '*':
            pattern_idx += 1

        if pattern_idx == len(pattern) - 1:
            return True
        for i in range(s_idx, len(s)):
            if self.do_match_with_cache(s, pattern, i, pattern_idx + 1):
                return True
        return False

    def is_match(self, char, pattern):
        return pattern == '*' or pattern == '.' or char == pattern

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
        ['', '*.', False],
        ['abcabfxyza', '*ab*klm', False],
        ['abcabf', '*ab*f', True]
    ]

    for case in testcases:
        print('Executing test case %s %s %s' % (case[0], case[1], case[2]))
        assert Matcher().match(case[0], case[1]) == case[2]

if __name__ == '__main__':
    main()
