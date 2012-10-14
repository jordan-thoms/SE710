package nz.net.thoms.HttpCache;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nz.net.thoms.HttpCache.cache.Cache;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
@SuppressWarnings("restriction")

public class CacheHandler implements HttpHandler {
	
	@Override
	public void handle(HttpExchange t) throws IOException {
        String path = t.getRequestURI().getPath();
        System.err.println("Got Request for " + path);
        try  {
        	HttpMain.window.postMessage("Fetching head from upstream for" + path);
        	HttpHeader header = new HttpHeader(HttpMain.UPSTREAM + path);
        	HttpResponse response = header.execute();
        	String blocksJSON = response.getFirstHeader("X-Blocks").getValue();
        	List<String> blocks = (List<String>) JSONValue.parse(blocksJSON);
        	System.out.println(blocksJSON);
        	JSONArray missing = new JSONArray();
        	// check for missing blocks
        	for (String block : blocks) {
        		if (!HttpMain.cache.isCached(block)) {
        			missing.add(block);
        		}
        	}
        	Map<String, String> data = null;
        	if (missing.size() > 0) {
	        	HttpGet httpget = new HttpGet(HttpMain.UPSTREAM + path);
	        	httpget.addHeader("X-Needed-Blocks", missing.toJSONString());
	    		HttpClient httpClient = HttpClientSingleton.getInstance();
	    		HttpResponse serverresponse = httpClient.execute(httpget);
	    		HttpEntity entity = serverresponse.getEntity();
				StringWriter writer = new StringWriter();
				IOUtils.copy(entity.getContent(), writer);
				data = (Map<String, String>) JSONValue.parseWithException(writer.toString());
        	}
			
        	ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        	int cachedBytes = 0;
        	for (String block : blocks) {
        		byte[] value = null;
        		
        		CacheValue v;
        		if ((v = HttpMain.cache.get(block)) != null) {
        			value = v.bytes;
        			cachedBytes += value.length;
                	HttpMain.window.postMessage("Block " + block + " from cache, cachedBytes " + cachedBytes);
        		}
        		
        		if (value == null) {
        			value = Base64.decodeBase64(data.get(block));
        			HttpMain.cache.set(block, new CacheValue(value));
                	HttpMain.window.postMessage("Block " + block + " from server");        			
        		}
        		byteStream.write(value);
			}
        	byteStream.close();
    		byte[] output = byteStream.toByteArray();
    		t.sendResponseHeaders(200, output.length);
    		OutputStream responseStream = t.getResponseBody();
    		responseStream.write(output);
    		responseStream.close();
        	int totalBytes = output.length;
        	HttpMain.window.postMessage("Responded for " + path + " , cached bytes " + cachedBytes + " total bytes " + totalBytes + "cache efficency: " + ((float) cachedBytes / totalBytes) *100 + "%");
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
/*
OutputStream responseStream = t.getResponseBody();
t.sendResponseHeaders(200, value.bytes.length);	    		
responseStream.write(value.bytes);
responseStream.close();
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

*/