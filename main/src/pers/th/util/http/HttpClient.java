package pers.th.util.http;

import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import pers.th.util.io.IOUtils;

import java.util.Map.Entry;

public class HttpClient {
	public final Set<Entry<Object, Object>> prop;
	public static final Charset charSet = Charset.forName("utf-8");

	public HttpClient() {
		prop = new HashSet<>();
	}

	public HttpClient(Set<Entry<Object, Object>> sets) {
		prop = sets;
	}

	public HttpClient(String path) {
		Properties properties = new Properties();
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(new File(path));
			properties.load(inputStream);
		} catch (Exception e) {
			throw new RuntimeException("can't load:" + path, e);
		} finally {
			IOUtils.close(inputStream);
		}
		prop = properties.entrySet();
	}

	public Set<Entry<Object, Object>> getProp() {
		return prop;
	}

	public void connect() {

	}

	public String get(String path) {
		try {
			HttpURLConnection huc = (HttpURLConnection) new URL(path).openConnection();
			for (Entry<Object, Object> item : prop) {
				huc.addRequestProperty(item.getKey().toString(), item.getValue().toString());
			}
			huc.setConnectTimeout(2000);
			huc.connect();
			final String result = IOUtils.toString(huc.getInputStream(), charSet);
			IOUtils.close(huc);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
