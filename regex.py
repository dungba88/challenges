"""
A simple regex matcher which can accept:
- a-z
- . for any single character
- * for any string (including empty string)
"""

def match(s, pattern):
    return do_match(s, pattern, 0, 0)

def do_match(s, pattern, s_idx, pattern_idx):
    if s_idx >= len(s) or pattern_idx >= len(pattern):
        if pattern_idx == len(pattern) - 1: # * matches empty string
            return pattern[pattern_idx] == '*'
        return s_idx >= len(s) and pattern_idx >= len(pattern)
    if not is_match(s[s_idx], pattern[pattern_idx]):
        return False
    if pattern[pattern_idx] != '*':
        return do_match(s, pattern, s_idx + 1, pattern_idx + 1)

    # this is only for the case where consecutive * are allowed
    while pattern_idx < len(pattern) - 1 and pattern[pattern_idx + 1] == '*':
        pattern_idx += 1

    if pattern_idx == len(pattern) - 1:
        return True
    for i in range(s_idx, len(s)):
        if do_match(s, pattern, i, pattern_idx + 1):
            return True
    return False

def is_match(char, pattern):
    if pattern == '*' or pattern == '.':
        return True
    return char == pattern

def main():
    assert match('a', 'a') is True
    assert match('a', 'b') is False
    assert match('c', '.') is True
    assert match('abcd', 'a.c') is False
    assert match('', '*') is True
    assert match('abc', 'c*') is False
    assert match('abc', '*c*') is True
    assert match('abc', '*d') is False
    assert match('bedc', '*b*c') is True
    assert match('abcd', 'a.c*') is True
    assert match('abcd', 'a.c*.') is True
    assert match('abcd', 'a*d') is True
    assert match('', '*.') is False

if __name__ == '__main__':
    main()
