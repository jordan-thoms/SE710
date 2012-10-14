package nz.net.thoms.HttpCache.cache;
import static org.junit.Assert.*;
import junit.framework.TestCase;

import org.junit.Test;


public class CacheTest extends TestCase {
	public Cache<String, Integer> cache;
	
	@Override
	public void setUp() {
		cache = new Cache<String, Integer>(2);
	}
	
	@Test
	public void testRetrieve() {
		cache.set("a", 1);
		assertEquals(1, cache.get("a").intValue());
	}
	
	@Test
	public void testEvictsOldValues() {
		cache.set("a", 1);
		cache.set("b", 2);
		cache.set("c", 3);
		cache.set("d", 6);
		assertEquals(null, cache.get("a"));
		assertEquals(null, cache.get("b"));
		assertEquals(3, cache.get("c").intValue());
		assertEquals(6, cache.get("d").intValue());
	}
	
	@Test
	public void testGetTouchesValueStart() {
		cache.set("a", 1);
		cache.set("b", 2);
		assertEquals(Integer.valueOf(1), cache.get("a"));
		cache.set("c", 3);
		assertEquals(Integer.valueOf(1), cache.get("a"));
	}
		
	@Test
	public void testGetMiddle() {
		cache.set("a", 1);
		cache.set("b", 2);
		cache.set("c", 3);
		assertEquals(Integer.valueOf(2), cache.get("b"));
		cache.set("d", 4);
		assertEquals(Integer.valueOf(2), cache.get("b"));
		assertEquals(null, cache.get("a"));
		assertEquals(null, cache.get("c"));
		assertEquals(Integer.valueOf(4), cache.get("d"));
	}
	
	@Test
	public void testGetEnd() {
		cache.set("a", 1);
		cache.set("b", 2);
		assertEquals(Integer.valueOf(2), cache.get("b"));
		cache.set("c", 3);
		assertEquals(Integer.valueOf(2), cache.get("b"));
		assertEquals(null, cache.get("a"));
	}
	
	@Test
	public void testLargerCache() {
		cache = new Cache<String, Integer>(4);
		cache.set("a", 1);
		cache.set("b", 2);
		cache.set("c", 3);
		cache.set("d", 4);
		cache.set("e", 5);
		assertEquals(null, cache.get("a"));
		assertEquals(Integer.valueOf(2), cache.get("b"));
		cache.set("f", 6);
		assertEquals(Integer.valueOf(2), cache.get("b"));
		assertEquals(null, cache.get("c"));
	}

}
