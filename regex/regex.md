# Regular Expression Matcher

## problem statement

Build a regular expression matcher which can accept:

- a-z  : matches respective character
- .    : matches any single character
- \*   : matches any string (including empty string)

# example

```
match(a, a) = true;
match(a, b) = false;
match(c, .) = true;
match(de, *) = true;
match(abc, c*) = false;
```
