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
        node.prev = self.tail
        self.tail.next = node
        self.tail = node

    def remove(self, node):
        if node == self.head and node == self.tail:
            self.head = self.tail = None
            return node
        if node == self.head:
            self.head = self.head.next
            return node
        if node == self.tail:
            self.tail = self.tail.prev
            self.tail.next = None
            return node
        node.prev.next = node.next
        node.next.prev = node.prev
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
        if not index.prev:
            return
        self.index_list.move_to_end(index)

    def on_put(self, key, _):
        if key in self.index_map:
            self.index_list.move_to_end(self.index_map[key])
            return
        node = LinkedListNode(key)
        self.index_list.append(node)
        self.index_map[key] = node
        if len(self.target.data) > self.max_item:
            evicted = self.index_list.remove(self.index_list.head)
            del self.target.data[evicted.value]
            del self.index_map[evicted.value]
