package com.zslin.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zslin.web.util.HttpUtil;

@RestController
public class UpRecord {
	@Autowired
	private OrderMapper orderMapper;

	@PostMapping("/fileUpload")
	public String upload(HttpServletRequest request, @RequestParam("file") MultipartFile[] files,
			@RequestParam("open_id") String open_id, @RequestParam("record_time") long record_time) {
		if (files != null && files.length >= 1) {
			BufferedOutputStream bw = null;
			try {
				String fileName = files[0].getOriginalFilename();
				if (!fileName.equals("")) {
					byte[] pcmBytes = mp3Convertpcm(files[0].getInputStream());
					String recordContent = "";
					recordContent = WebIAT.speechBdApi(pcmBytes);
					if (recordContent.equals("")) {
						return "fail";
					} else {
						System.out.println(recordContent);
						String answer = "";
						answer = HttpUtil.getQA(recordContent);
						if (answer != null && !answer.equals("")) {
							System.out.println(answer);
						}
						Date date = new Date();
						RecordBean recordBean = new RecordBean();
						recordBean.setCreate_time(date);
						recordBean.setFile_dir(fileName);
						recordBean.setOpen_id(open_id);
						recordBean.setRecord_content(recordContent);
						recordBean.setAnswer_content(answer);
						recordBean.setDelete_flag(false);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date recordTime = new Date(record_time);
						recordBean.setRecord_time(recordTime);
						orderMapper.insert(recordBean);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (bw != null) {
						bw.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "success";
	}

	@RequestMapping(value = "/fileDelete", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
	public String deleteFileAction(HttpServletRequest request, @RequestParam("open_id") String open_id,
			@RequestParam("file_dir") String file_dir, HttpServletResponse response) {
		orderMapper.deleteRecord(open_id, file_dir);
		String content = "";
		Map<String, String> map = new HashMap<String, String>();
		ObjectMapper mapper = new ObjectMapper();
		map.put("message", "删除成功");
		try {
			content = mapper.writeValueAsString(map);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	public byte[] mp3Convertpcm(InputStream mp3Stream) throws Exception {
		InputStream bufferedIn = new BufferedInputStream(mp3Stream);
		AudioInputStream mp3audioStream = AudioSystem.getAudioInputStream(bufferedIn);
		AudioInputStream pcmaudioStream = AudioSystem.getAudioInputStream(AudioFormat.Encoding.PCM_SIGNED,
				mp3audioStream);
		byte[] pcmBytes = IOUtils.toByteArray(pcmaudioStream);
		pcmaudioStream.close();
		mp3audioStream.close();
		return pcmBytes;
	}
}