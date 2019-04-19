package com.zslin.web.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.zslin.web.bean.JsonRootBean;

import io.netty.handler.codec.http.HttpResponse;

/**
 * Http Client 工具类
 */
public class HttpUtil {
	/**
	 * 发送post请求，根据 Content-Type 返回不同的返回值
	 * 
	 * @param url
	 * @param header
	 * @param body
	 * @return
	 */
	public static Map<String, Object> doPost2(String url, Map<String, String> header, String body) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		PrintWriter out = null;
		try {
			// 设置 url
			URL realUrl = new URL(url);
			URLConnection connection = realUrl.openConnection();
			HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
			// 设置 header
			for (String key : header.keySet()) {
				httpURLConnection.setRequestProperty(key, header.get(key));
			}
			// 设置请求 body
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			out = new PrintWriter(httpURLConnection.getOutputStream());
			// 保存body
			out.print(body);
			// 发送body
			out.flush();
			if (HttpURLConnection.HTTP_OK != httpURLConnection.getResponseCode()) {
				System.out.println("Http 请求失败，状态码：" + httpURLConnection.getResponseCode());
				return null;
			}
			// 获取响应header
			String responseContentType = httpURLConnection.getHeaderField("Content-Type");
			if ("audio/mpeg".equals(responseContentType)) {
				// 获取响应body
				byte[] bytes = toByteArray(httpURLConnection.getInputStream());
				resultMap.put("Content-Type", "audio/mpeg");
				resultMap.put("sid", httpURLConnection.getHeaderField("sid"));
				resultMap.put("body", bytes);
				return resultMap;
			} else {
				// 设置请求 body
				BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
				String line;
				String result = "";
				while ((line = in.readLine()) != null) {
					result += line;
				}
				resultMap.put("Content-Type", "text/plain");
				resultMap.put("body", result);

				return resultMap;
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 发送post请求
	 * 
	 * @param url
	 * @param header
	 * @param body
	 * @return
	 */		
	
	public static String doPost1(String url, Map<String, String> header, String body) {
		String result = "";
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			// 设置 url
			URL realUrl = new URL(url);
			URLConnection connection = realUrl.openConnection();
			HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
			// 设置 header
			for (String key : header.keySet()) {
				httpURLConnection.setRequestProperty(key, header.get(key));
			}
			// 设置请求 body
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			out = new PrintWriter(httpURLConnection.getOutputStream());
			// 保存body
			out.print(body);
			// 发送body
			out.flush();
			if (HttpURLConnection.HTTP_OK != httpURLConnection.getResponseCode()) {
				System.out.println("Http 请求失败，状态码：" + httpURLConnection.getResponseCode());
				return null;
			}

			// 获取响应body
			in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			return null;
		}
		return result;
	}

	/**
	 * 流转二进制数组
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static byte[] toByteArray(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024 * 4];
		int n = 0;
		while ((n = in.read(buffer)) != -1) {
			out.write(buffer, 0, n);
		}
		return out.toByteArray();
	}
	
	
	
	public static JSONArray getAnswer(String urlSuffix){
		HttpURLConnection connection = null;
		JSONArray jsonResult = null;
		try {
			URL url = new URL(urlSuffix);
			connection = (HttpURLConnection) url.openConnection();
//			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("GET"); 
			connection.setRequestProperty("Charsert", "UTF-8");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Accept", "application/json");
			connection.connect();
			String lines;
			StringBuffer sb = new StringBuffer("");
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			while ((lines = reader.readLine()) != null) {
				lines = new String(lines.getBytes(), "utf-8");
				sb.append(lines);
			}
			jsonResult = new JSONArray(sb.toString());
			reader.close();
			connection.disconnect();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonResult;
	}
	

//	
	

	
	public static String getQA(String question){
//		JsonObject jsonResult = null;
		
		String result = "";

		InputStream inputStream = null;
		HttpURLConnection urlConnection = null;
		try {
			
			
		JsonObject json = new JsonObject();
		json.addProperty("reqType", 0);		
		JsonObject jsonText = new JsonObject();
		JsonObject jsonContent = new JsonObject();
		jsonContent.addProperty("text", question);
		jsonText.add("inputText", jsonContent);		
		json.add("perception", jsonText);			
		JsonObject jsonUser = new JsonObject();
		jsonUser.addProperty("apiKey", "XXXXXXXX");
		jsonUser.addProperty("userId", "xxxxx");		
		json.add("userInfo", jsonUser);
		
		
			URL url = new URL("http://openapi.tuling123.com/openapi/api/v2");
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestProperty("Content-Type", "application/json"); 
			urlConnection.setRequestProperty("Accept", "application/json");						
			urlConnection.setRequestMethod("POST");
			urlConnection.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
			Gson gson = new Gson();			
			String jsonString = gson.toJson(json); 			
//			wr.writeBytes();
			wr.write(jsonString.getBytes()); 
			wr.flush();
			wr.close();
			int statusCode = urlConnection.getResponseCode();
			if (statusCode == 200) {
				String lines;
				StringBuffer sb = new StringBuffer("");
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				
				while ((lines = reader.readLine()) != null) {
					lines = new String(lines.getBytes(), "utf-8");
					sb.append(lines);
				}
				
				
				JsonRootBean jsonRootBean = new JsonRootBean();
				jsonRootBean = gson.fromJson(sb.toString(), JsonRootBean.class);		
				
				result = jsonRootBean.getResults().get(0).getValues().getText();
				
				System.out.println(result);
				
				reader.close();
				urlConnection.disconnect();
				return result;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		return null;
		}
}
