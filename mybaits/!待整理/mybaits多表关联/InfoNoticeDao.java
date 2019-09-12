package com.rongji.egov.info.business.info.mapper;

import com.rongji.egov.info.business.info.model.InfoNotice;
import com.rongji.egov.info.business.info.model.InfoNoticeBack;
import com.rongji.egov.utils.api.paging.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface InfoNoticeDao {

	int insertInfoNotice(InfoNotice infoNotice);

	int insertInfoNoticeBack(InfoNoticeBack infoNoticeBack);

	int updateInfoNotice(InfoNotice infoNotice);

	int updateInfoNoticeBack(InfoNoticeBack infoNoticeBack);

	int deleteInfoNotice(String id);

	int deleteInfoNoticeByIds(@Param("ids") String[] ids);
	
	int deleteInfoNoticeBackById(String id);

	int countInfoNotice(Map<String, Object> map);

	int countInfoNoticeBack(Map<String, Object> map);

	Page<Map<String, Object>> listInfoNotice4Page(Map<String, Object> map);

    Page<Map<String,Object>> listInfoNoticeBack4Page(Map<String, Object> map);

	List<InfoNoticeBack> listUnitUnReadNotice(Map<String, Object> map);
	List<String> listNoticeBackByIdAndNo(Map<String, Object> map);

	InfoNoticeBack getInfoNoticeBackById(String id);

	InfoNotice getInfoNoticeById(String id);

}
