package nz.net.thoms.HttpCache;
/*******************************************************************************
 * Copyright (c) 2011 Jordan Thoms.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/


import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;

public class HttpHeader {
	// This class handles the simple getting of a url resource and returning the result
	// Since it's a pain to write out all the IOUtils stuff all the time.
	private HttpHead httpget = null;
	public HttpHeader(URI uri) {
		httpget = new HttpHead(uri);
	}
	public HttpHeader(String uri) {
		httpget = new HttpHead(uri);
	}
	public HttpResponse execute() throws ClientProtocolException, IOException  {
		HttpClient httpClient = HttpClientSingleton.getInstance();
		HttpResponse serverresponse = null;
		serverresponse = httpClient.execute(httpget);
		return serverresponse;
	}
}
