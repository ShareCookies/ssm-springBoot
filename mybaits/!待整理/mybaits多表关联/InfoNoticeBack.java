package com.rongji.egov.info.business.info.model;

/**
 * 通知反馈
 * 
 */
public class InfoNoticeBack {

	private String id;
	private String noticeId;
	private String unitNo;
	private String isRead;
	private String feedback;

	private InfoNotice infoNotice;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public String getUnitNo() {
		return unitNo;
	}

	public void setUnitNo(String unitNo) {
		this.unitNo = unitNo;
	}

	public String getIsRead() {
		return isRead;
	}

	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public InfoNotice getInfoNotice() {
		return infoNotice;
	}

	public void setInfoNotice(InfoNotice infoNotice) {
		this.infoNotice = infoNotice;
	}

}
