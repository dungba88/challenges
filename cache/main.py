from cache import Cache
from lru import LRUCacheAlgorithm

def main():
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
