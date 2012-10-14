package nz.net.thoms.HttpServer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringBufferInputStream;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
@SuppressWarnings("restriction")

public class FileHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange t) throws IOException {
        InputStream is = t.getRequestBody();
        String path = t.getRequestURI().getPath();
        System.err.println("Got Request for " + path);
        try  {
            File file = new File(System.getProperty("user.dir") + path);
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                JSONArray array = new JSONArray();
                for (File dirFile : files) {
                	array.add(dirFile.getName());
                }
                String json = array.toJSONString();
                t.sendResponseHeaders(200, json.length());
                OutputStream os = t.getResponseBody();
                InputStream input = new BufferedInputStream(new StringBufferInputStream(json));

                IOUtils.copy(input, os); 	
                os.close();
            } else {
                t.sendResponseHeaders(200, file.length());
                OutputStream os = t.getResponseBody();
                InputStream input = new BufferedInputStream(new FileInputStream(file));

                IOUtils.copy(input, os); 	
                os.close();            	
            }
        } catch (Exception e) {
        	e.printStackTrace();        	
        	t.sendResponseHeaders(500, 0);
        }
	}

}
