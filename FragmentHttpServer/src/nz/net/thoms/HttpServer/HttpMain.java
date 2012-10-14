package nz.net.thoms.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;
@SuppressWarnings("restriction")

public class HttpMain {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
	   HttpServer server = HttpServer.create(new InetSocketAddress(8010), 0);
	   server.createContext("/", new FileHandler());
	   server.setExecutor(null); // creates a default executor
	   server.start();
	}

}
