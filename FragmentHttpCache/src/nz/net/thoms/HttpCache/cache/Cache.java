package nz.net.thoms.HttpCache.cache;
import java.util.HashMap;
import java.util.Map;
/**
 * LRU Cache, with O(1) set and get operations.
 * (I implemented this for a job interview).
 * @author Jordan Thoms
 *
 * @param <K>
 * @param <V>
 */

public class Cache<K, V> {
	class Node {
		Node next;
		Node previous;
		K key;
		V value;
	}
	
	private Node head;
	private Node tail;
	
	private CacheListener<K> listener;
	
	private int capacity;
	
	public Cache(int size) {
		this.capacity = size;
	}
	
	public Cache(int size, CacheListener<K> listener) {
		this.capacity = size;
		this.listener = listener;
	}

	public Map<K,Node> map = new HashMap<K,Node>();
	
	public V get(K key) {
		Node result =  map.get(key);
		if (result != null) {
			if (head != result) {
				// Remove from current position
				if (result.previous != null) {
					result.previous.next = result.next;
				}
				if (result.next != null) {
					result.next.previous = result.previous;
				}
				if (tail == result) {
					tail = result.previous;
				}
				// place at the head of the list
				head.previous = result;
				result.next = head;
				head = result;
			}
			return result.value;
		} else {
			return null;
		}
	}
	
	public boolean isCached(K key) {
		return map.containsKey(key);
	}
	
	public void set(K key, V value) {
		Node node = new Node();
		node.key = key;
		node.value = value;
		if (map.size() == 0) {
			head = node;
			tail = node;
		} else {
			// place at the head of the list
			head.previous = node;
			node.next = head;
			head = node;
		}
		map.put(key, node);
		
		if (listener != null) {
			listener.newEntry(key);
		}
		
		//evict if needed
		if (map.size() > capacity) {
			// pop off the tail
			Node end = tail;
			tail = tail.previous;
			tail.next = null;
			// Remove it from the map too
			map.remove(end.key);
			
			if (listener != null) {
				listener.evicted(key);
			}
		}
	}
	
	public void clear() {
		map.clear();
		head = null;
		tail = null;  
		
		if (listener != null) {
			listener.cleared();
		}
	}
	
}
