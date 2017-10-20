"""
Implementation for LRU eviction algorithm.

The idea is to build a doubly linked list to store all cache items.
Whenever an item is accessed, it will be moved to the end of the list.
The doubly linked list makes sure put() and get() time complexity is
always O(1) - linear.
"""

class LinkedListNode(object):
    def __init__(self, value):
        self.value = value
        self.next = None
        self.prev = None

class DoubleLinkedList(object):
    def __init__(self):
        self.head = None
        self.tail = None

    def append(self, node):
        if not self.head:
            self.tail = self.head = node
            return
        node.next = None
        node.prev = self.tail
        self.tail.next = node
        self.tail = node

    def remove(self, node):
        if node == self.head and node == self.tail:
            self.head = self.tail = None
            return node
        if node == self.head:
            self.head = self.head.next
            self.head.prev = None
            return node
        if node == self.tail:
            self.tail = self.tail.prev
            self.tail.next = None
            return node
        node.prev.next = node.next
        node.next.prev = node.prev
        node.prev = node.next = None
        return node

    def move_to_end(self, node):
        self.remove(node)
        self.append(node)

    def log(self):
        if not self.head:
            return
        current = self.head
        text = ''
        while current:
            text += str(current.value) + ' -> '
            current = current.next
        print(text + ' EOL')

class LRUCacheAlgorithm(object):
    def __init__(self, max_item=3):
        self.target = None
        self.max_item = max_item
        self.index_map = dict()
        self.index_list = DoubleLinkedList()

    def on_get(self, key):
        if key not in self.index_map:
            return
        index = self.index_map[key]
        self.index_list.move_to_end(index)

    def on_put(self, key, _):
        if key in self.index_map:
            self.on_get(key)
            return
        if len(self.target.data) >= self.max_item:
            self._evict()
        node = LinkedListNode(key)
        self.index_list.append(node)
        self.index_map[key] = node

    def _evict(self):
        evicted = self.index_list.remove(self.index_list.head)
        del self.target.data[evicted.value]
        del self.index_map[evicted.value]
