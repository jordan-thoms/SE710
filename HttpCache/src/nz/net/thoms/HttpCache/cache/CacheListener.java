package nz.net.thoms.HttpCache.cache;

public interface CacheListener<K> {
	public void newEntry(K key);
	public void cleared();
	public void evicted(K key);
}
