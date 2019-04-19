package com.zslin.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.util.Base64Utils;
import org.springframework.util.DigestUtils;

import com.google.gson.JsonObject;
import com.zslin.web.util.FileUtil;
import com.zslin.web.util.HttpUtil;


/**
 * 语音听写 WebAPI 接口调用示例
 * 
 * 运行方法：直接运行 main() 即可
 * 
 * 结果： 控制台输出语音听写结果信息
 * 
 * @author iflytek
 * 
 */
public class WebIAT {
	// 合成webapi接口地址
	private static final String WEBIAT_URL = "http://api.xfyun.cn/v1/service/v1/iat";
	// 应用ID
	private static final String APPID = "XXXXXXXX";
	// 接口密钥
	private static final String API_KEY = "XXXXXXXXXX";
	// 音频编码
	private static final String AUE = "raw";
	// 引擎类型
	private static final String ENGINE_TYPE = "sms16k";
	// 音频文件地址
	private static final String AUDIO_PATH = "resource\\iat.pcm";

	
	
	
	/**
	 * 听写 WebAPI 调用示例程序
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static String speechBdApi(byte[] pcmBytes) throws IOException {
		String result="";
		Map<String, String> header = buildHttpHeader();
//		byte[] audioByteArray = FileUtil.read(AUDIO_PATH);
		String audioBase64 = new String(Base64.encodeBase64(pcmBytes), "UTF-8");
		String resultEnd = HttpUtil.doPost1(WEBIAT_URL, header, "audio=" + URLEncoder.encode(audioBase64, "UTF-8"));
		JSONObject jsonObj = new JSONObject(resultEnd);
		if(jsonObj.getString("code").equals("0")){
			result = jsonObj.getString("data");
		}
		return result;
	}

	/**
	 * 组装http请求头
	 */
	private static Map<String, String> buildHttpHeader() throws UnsupportedEncodingException {
		String curTime = System.currentTimeMillis() / 1000L + "";
		String param = "{\"aue\":\"" + AUE + "\",\"engine_type\":\"" + ENGINE_TYPE + "\"}";
		String paramBase64 = new String(Base64.encodeBase64(param.getBytes("UTF-8")));
		String checkSum = getMD5(API_KEY + curTime + paramBase64);
		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		header.put("X-Param", paramBase64);
		header.put("X-CurTime", curTime);
		header.put("X-CheckSum", checkSum);
		header.put("X-Appid", APPID);
		return header;
	}
	
	public static String getMD5(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			return new BigInteger(1, md.digest()).toString(16);
		} catch (Exception e) {
			return "";
		}
	}
}
