from cache import Cache
from lru import LRUCacheAlgorithm
from lfu import LFUCacheAlgorithm

def main():
    print('testing LRU')
    test_lru()
    print('passed')

    print('\n------------\n')

    print('testing LFU')
    test_lfu()
    print('passed')

def test_lfu():
    algorithm = LFUCacheAlgorithm(max_item=3)
    cache = Cache(algorithm=algorithm)

    cache.put(1, 0) # 1(0)
    cache.get(1) # 1(1)
    algorithm.log()
    cache.put(2, 0) # 1(1) 2(0)
    cache.put(3, 0) # 1(1) 2(0) 3(0)
    algorithm.log()
    assert len(cache.data) == 3

    cache.put(4, 0) # 1(1) 3(0) 4(0)
    algorithm.log()
    assert cache.get(2) is None

    for _ in range(0, 3):
        cache.get(4) # 1(1) 3(0) 4(3)
    for _ in range(0, 2):
        cache.get(3) # 1(1) 3(2) 4(3)

    for _ in range(0, 2):
        cache.put(5, 0) # 3(2) 4(3) 5(1)
    algorithm.log()
    assert cache.get(1) is None

def test_lru():
    algorithm = LRUCacheAlgorithm(max_item=3)
    cache = Cache(algorithm=algorithm)

    cache.put(1, 0) # 1
    cache.get(1)
    cache.put(2, 0) # 1 2
    cache.put(3, 0) # 1 2 3
    algorithm.index_list.log()
    assert len(cache.data) == 3

    cache.put(4, 0) # 2 3 4
    algorithm.index_list.log()
    assert len(cache.data) == 3
    assert cache.get(1) is None

    cache.get(2)  # 3 4 2
    cache.put(5, 0) # 4 2 5
    algorithm.index_list.log()
    assert cache.get(3) is None

    cache.put(4, 0) # 2 5 4
    cache.put(3, 0) # 5 4 3
    algorithm.index_list.log()
    assert cache.get(2) is None

if __name__ == '__main__':
    main()
