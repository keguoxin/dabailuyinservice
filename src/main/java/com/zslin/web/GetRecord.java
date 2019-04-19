package com.zslin.web;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetRecord {
	@Autowired
	private OrderMapper orderMapper;
	
	@RequestMapping(value="/getListByOpenId", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
	public List<RecordBean> getList(String open_id){
		List<RecordBean> recordList=orderMapper.selectByOpenId(open_id);
		for(int i=0;i<recordList.size();i++){
			SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
			recordList.get(i).setCreate_time_str(sdf.format(recordList.get(i).getCreate_time()));			
		}		
		return recordList;
	}	
}
