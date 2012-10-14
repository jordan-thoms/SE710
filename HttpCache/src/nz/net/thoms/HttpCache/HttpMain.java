package nz.net.thoms.HttpCache;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.InetSocketAddress;

import nz.net.thoms.HttpCache.cache.Cache;

import com.sun.net.httpserver.HttpServer;
@SuppressWarnings("restriction")


public class HttpMain {
	public static final String UPSTREAM = "http://localhost:8000/";

	public static Cache<String, CacheValue> cache;
	public static MainWindow window;
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		final CacheListModel model = new CacheListModel();
		
		cache = new Cache<String, CacheValue>(10000, model);
 
		HttpServer server = HttpServer.create(new InetSocketAddress(8001), 0);
		server.createContext("/", new CacheHandler());
		server.setExecutor(null); // creates a default executor
		server.start();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new MainWindow(model);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
