"""
Implementation for LFU eviction algorithm.

The idea is to build a map from frequency to item, and store the minimum
access frequency. When eviction happens, we just need to remove the item
with access frequency equals to the minimum.

When we put new item to the cache, the minimum will be reset to 0. When
we access existing item, the frequency for that item will be increased
by 1. If there is no item with access frequency equals to the minimum,
we will increase the minimum by 1.
"""

class LFUCacheAlgorithm(object):
    def __init__(self, max_item=3):
        self.target = None
        self.max_item = max_item
        self.min_accessed_times = 0
        self.times_map = dict()
        self.nodes_map = dict()

    def _update_map(self, key, times):
        """update times and nodes map with new times value"""
        self.times_map[key] = times
        if times not in self.nodes_map:
            self.nodes_map[times] = dict()
        self.nodes_map[times][key] = 1

    def on_get(self, key):
        if key not in self.times_map:
            return
        # remove the old access frequency and increase it by 1
        times = self.times_map[key]
        del self.nodes_map[times][key]
        self._update_map(key, self.times_map[key] + 1)

        # if there is no nodes with minimum access frequency, increase it by 1
        if not self.nodes_map[times] and times == self.min_accessed_times:
            self.min_accessed_times += 1

    def on_put(self, key, _):
        if key in self.times_map:
            self.on_get(key)
            return
        if len(self.target.data) >= self.max_item:
            self._evict()
        self._update_map(key, 0)
        self.min_accessed_times = 0

    def _evict(self):
        evicted = next(iter(self.nodes_map[self.min_accessed_times]))
        del self.target.data[evicted]
        del self.nodes_map[self.min_accessed_times][evicted]
        del self.times_map[evicted]

    def log(self):
        keys = sorted(self.times_map)
        text = ''
        for k in keys:
            text += str(k) + '(' + str(self.times_map[k]) + ') '
        text += ' (Min access frequency: ' + str(self.min_accessed_times) + ')'
        print(text)
