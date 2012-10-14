package nz.net.thoms.HttpCache;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import nz.net.thoms.HttpCache.cache.Cache;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
@SuppressWarnings("restriction")

public class CacheHandler implements HttpHandler {
	
	@Override
	public void handle(HttpExchange t) throws IOException {
        String path = t.getRequestURI().getPath();
        System.err.println("Got Request for " + path);
        try  {
        	CacheValue value;
        	if ((value = HttpMain.cache.get(path)) != null) {
	        	System.err.println("Hit for " + path + ", using cache");
	        	HttpMain.window.textArea.setText(HttpMain.window.textArea.getText() + "Hit for " + path + ", using cache\n");	        	
	    		OutputStream responseStream = t.getResponseBody();
	    		t.sendResponseHeaders(200, value.bytes.length);	    		
        		responseStream.write(value.bytes);
        		responseStream.close();
        	} else {
	        	System.err.println("Miss for " + path + ", fetching from upstream");
	        	HttpMain.window.textArea.setText(HttpMain.window.textArea.getText() + "Miss for " + path + ", fetching from upstream\n");
	        	HttpGet httpget = new HttpGet(HttpMain.UPSTREAM + path);
	    		HttpClient httpClient = HttpClientSingleton.getInstance();
	    		HttpResponse serverresponse = httpClient.execute(httpget);
	    		HttpEntity entity = serverresponse.getEntity();
	    		t.sendResponseHeaders(200, entity.getContentLength());
	    		InputStream is = entity.getContent();
	    		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
	    		IOUtils.copy(is, byteStream);
	    		is.close();
	    		byteStream.close();
	    		OutputStream responseStream = t.getResponseBody();
	    		byte[] output = byteStream.toByteArray();
	    		responseStream.write(output);
	    		HttpMain.cache.set(path, new CacheValue(output));
	    		responseStream.close();
        	}
        } catch (Exception e) {
        	e.printStackTrace();        	
        	t.sendResponseHeaders(500, 0);
        }
	}

}

class CacheValue {
	public byte[] bytes;
	
	public CacheValue(byte[] bytes) {
		this.bytes = bytes;
	}
}
