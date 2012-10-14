package nz.net.thoms.HttpClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class HttpClient {
	public static final String SERVER = "http://localhost:8001/";

	public static List<String> getFileList() {
		HttpGetter get = new HttpGetter(SERVER);
		String result;
		try {
			result = get.execute();
			Object obj = JSONValue.parse(result);
			JSONArray array=(JSONArray)obj;
			return array;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}
	
	public static String getFile(String path) {
		HttpGetter get = new HttpGetter(SERVER + path);
		try {
			return get.execute();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

}
