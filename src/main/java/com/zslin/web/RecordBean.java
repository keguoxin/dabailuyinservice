package com.zslin.web;

import java.util.Date;

public class RecordBean {
	private int id;
	private String open_id;
	private String create_time_str;
	private Date record_time;
	private boolean delete_flag;
	public boolean isDelete_flag() {
		return delete_flag;
	}

	public void setDelete_flag(boolean delete_flag) {
		this.delete_flag = delete_flag;
	}

	private String record_content;
	private String answer_content;	

	public String getRecord_content() {
		return record_content;
	}

	public void setRecord_content(String record_content) {
		this.record_content = record_content;
	}
	
	public String getAnswer_content() {
		return answer_content;
	}

	public void setAnswer_content(String answer_content) {
		this.answer_content = answer_content;
	}
	
	public Date getRecord_time() {
		return record_time;
	}

	public void setRecord_time(Date record_time) {
		this.record_time = record_time;
	}

	public String getCreate_time_str() {
		return create_time_str;
	}

	public void setCreate_time_str(String create_time_str) {
		this.create_time_str = create_time_str;
	}

	public String getOpen_id() {
		return open_id;
	}

	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private Date create_time;
	private String file_dir;

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public String getFile_dir() {
		return file_dir;
	}

	public void setFile_dir(String file_dir) {
		this.file_dir = file_dir;
	}

}
