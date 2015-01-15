package fx.excel.upload.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSON;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;

public class ExcelService {
	
	private static final String DOMAIN_URL = "http://localhost:8080";
	
	private static final String CONTEXT_NAME = "";
	
	private static final String POST_EXCEL_URL = DOMAIN_URL + CONTEXT_NAME + "/excel";
	
	private static final String GET_EXCEL_LIST_URL = DOMAIN_URL + CONTEXT_NAME + "/excel";
	
	private static final String GET_EXCEL_DETAIL_URL = DOMAIN_URL + CONTEXT_NAME + "/excel/{{excelFileId}}";
	
	public void insert(File excel) throws IOException {
		HttpPost httpPost = new HttpPost(POST_EXCEL_URL);
		
		MultipartEntityBuilder mBuilder = MultipartEntityBuilder.create();
		mBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		mBuilder.setCharset(StandardCharsets.UTF_8);
		mBuilder.addBinaryBody("uploadFile", excel, ContentType.create("application/excel"), excel.getName());
		httpPost.setEntity(mBuilder.build());
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = client.execute(httpPost);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			throw new RuntimeException("サーバとの通信に失敗しました。statu:" + statusCode);
		}
	}
	
	public List<Map<String, Object>> findAllExcelFileName() throws IOException {
		HttpGet httpget = new HttpGet(GET_EXCEL_LIST_URL);
		HttpEntity entity = null;
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = client.execute(httpget);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			throw new RuntimeException("サーバとの通信に失敗しました。statu:" + statusCode);
		}
		entity = response.getEntity();
		InputStream is = entity.getContent();
		
		if (is == null) {
			return new ArrayList<Map<String, Object>>();
		}
		return JSON.decode(is);
	}
	
	public List<List<String>> findByExcelFileId(String excelFileId) throws IOException {
		HttpGet httpget = new HttpGet(buildDetailUrl(GET_EXCEL_DETAIL_URL, excelFileId));
		HttpEntity entity = null;
		
		HttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = client.execute(httpget);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK) {
			throw new RuntimeException("サーバとの通信に失敗しました。statu:" + statusCode);
		}
		entity = response.getEntity();
		InputStream is = entity.getContent();
		
		if (is == null) {
			return new ArrayList<List<String>>();
		}
		return JSON.decode(is);
	}
	
	private String buildDetailUrl(String baseUrl, String excelFileId) {
		String url = new String(baseUrl);
		url = StringUtils.replace(url, "{{excelFileId}}", FilenameUtils.getBaseName(excelFileId));
		
		return url;
	}
}
