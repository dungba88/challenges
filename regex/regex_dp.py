"""
A simple regex matcher which can accept:
- a-z
- . for any single character
- * for any string (including empty string)
"""

class Matcher(object):

    def match(self, s, pattern):
        s_len = len(s)
        pattern_len = len(pattern)
        
        s_idx = 0
        pattern_idx = 0
        last_asterisk_pos = -1

        while s_idx < s_len and pattern_idx < pattern_len:
            if not self.is_match(s[s_idx], pattern[pattern_idx]):
                if last_asterisk_pos == -1:
                    return False
                if last_asterisk_pos == pattern_idx - 1: # find until match
                    s_idx += 1
                else: # current sequence not good, reset from last position
                    s_idx = last_asterisk_pos + 1
                continue

            if pattern[pattern_idx] != '*':
                s_idx += 1
            else:
                if pattern_idx == pattern_len - 1:
                    return True
                last_asterisk_pos = pattern_idx
            pattern_idx += 1

        return s_idx == s_len and (pattern_idx == pattern_len or (pattern_idx == pattern_len - 1 and pattern[pattern_idx] == '*'))

    def is_match(self, char, pattern):
        if pattern == '*' or pattern == '.':
            return True
        return char == pattern

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
        ['d', '*', True],
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
