package nz.net.thoms.HttpServer;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringBufferInputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nz.net.thoms.HttpServer.SplitBlocks.Block;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

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
			byte[] bytes;
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				JSONArray array = new JSONArray();
				for (File dirFile : files) {
					array.add(dirFile.getName());
				}
				String json = array.toJSONString();
				bytes = json.getBytes();
			} else {
				bytes = getFileBytes(file);
			}
			List<Block> blocks = SplitBlocks.splitBlocks(bytes);
			JSONArray blockList = new JSONArray();
			JSONObject blockData = new JSONObject();
			if (t.getRequestMethod().equals("HEAD")) {
				for (Block block : blocks) {
					blockList.add(block.md5Digest);
				}
				t.getResponseHeaders().add("X-Blocks", blockList.toJSONString());
				t.sendResponseHeaders(200, -1);
			} else {
				String neededBlocksStr = t.getRequestHeaders().getFirst("X-Needed-Blocks");
				System.out.println(neededBlocksStr);
				Set<String> neededBlocks = new HashSet<String>();                	
				if (neededBlocksStr != null) {
					JSONArray array = (JSONArray) JSONValue.parseWithException(neededBlocksStr);
					for (Object digest : array) {
						neededBlocks.add((String) digest);
					}
				} else {
					for (Block block : blocks) {
						neededBlocks.add(block.md5Digest);
					}
				}


				for (Block block : blocks) {
					if (neededBlocks.contains(block.md5Digest)) {
						blockData.put(block.md5Digest, Base64.encodeBase64String(block.block));
					}
				}

				String result = blockData.toJSONString();
				t.sendResponseHeaders(200, result.length());
				OutputStream os = t.getResponseBody();                
				InputStream input = new BufferedInputStream(new StringBufferInputStream(result));
				IOUtils.copy(input, os); 	
				input.close();
				os.close();
			}
		}
		catch (Exception e) {
			e.printStackTrace();        	
			t.sendResponseHeaders(500, 0);
		}
	}


	public byte[] getFileBytes(File file) throws IOException {
		InputStream input = new BufferedInputStream(new FileInputStream(file));
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		IOUtils.copy(input, byteStream);
		input.close();
		byteStream.close();
		return byteStream.toByteArray();
	}
}
