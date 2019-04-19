package com.zslin.web;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper {

	@Insert("insert into record(create_time,file_dir,open_id,record_time,record_content,answer_content,delete_flag) values(#{create_time},#{file_dir},#{open_id},#{record_time},#{record_content},#{answer_content},#{delete_flag})")
	@Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
	void insert(RecordBean recordBean);

	@Select("select create_time,file_dir,id,open_id,record_content,answer_content from record where open_id=#{open_id} and record_content != '' and delete_flag = 0 order by create_time desc")
	List<RecordBean> selectByOpenId(@Param("open_id") String open_id);
	
	@Select("update record set delete_flag = 1 where open_id=#{open_id} and file_dir=#{file_dir}")
	RecordBean deleteRecord(@Param("open_id") String open_id, @Param("file_dir") String file_dir);
	
}
