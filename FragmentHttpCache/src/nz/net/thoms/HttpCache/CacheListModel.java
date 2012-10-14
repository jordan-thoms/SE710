package nz.net.thoms.HttpCache;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

import nz.net.thoms.HttpCache.cache.CacheListener;

public class CacheListModel extends AbstractListModel implements CacheListener<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<String> cacheKeys = new ArrayList<String>();
		
	@Override
	public Object getElementAt(int index) {
		return cacheKeys.get(index);
	}

	@Override
	public int getSize() {
		return cacheKeys.size();
	}

	@Override
	public void newEntry(String key) {
		cacheKeys.add(key);
		fireContentsChanged(this, 0, getSize());
	}

	@Override
	public void cleared() {
		cacheKeys.clear();
		fireContentsChanged(this, 0, getSize());
	}

	@Override
	public void evicted(String key) {
		cacheKeys.remove(key);
		fireContentsChanged(this, 0, getSize());		
	}
}
