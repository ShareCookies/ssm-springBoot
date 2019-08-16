package com.rongji.egov.info.business.info.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;
import java.util.List;

/**
 * 通知
 * 
 */
public class InfoNotice {

    private String id;
    private String subject;
    /**
     * 通知时间
     */
    @JsonFormat(pattern ="yyyy-MM-dd",timezone="GMT+8")
    private Timestamp noticeTime;
    /**
     * 有效期
     */
    @JsonFormat(pattern ="yyyy-MM-dd",timezone="GMT+8")
    private Timestamp expiryTime;
    private String noticeType;
    /**
     * 密级
     */
    private String secLevel;
    /**
     * 接收单位类型（1、全部单位2、指定单位）
     */
    private String recUnitType;
    /**
     * 接收单位
     */
    private List<String> recUnit;
    /**
     * 通知人
     */
    private String noticeUnit;
    private String noticeUnitTel;
    private String noticeContent;
    private String createUser;
    private String createUserNo;
    @JsonFormat(pattern ="yyyy-MM-dd",timezone="GMT+8")
    private Timestamp createTime;
    /**
     * 状态（0:待处理；  1:待审核;  2:待发布;  3:已发布）
     */
    private String status;
    private String attId;
    /**
     *save:保存； publish：发布； unpublish：取消发布
     */
    private String act;

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getCreateUserNo() {
        return createUserNo;
    }

    public void setCreateUserNo(String createUserNo) {
        this.createUserNo = createUserNo;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getNoticeContent() {
        return noticeContent;
    }

    public void setNoticeContent(String noticeContent) {
        this.noticeContent = noticeContent;
    }

    public String getNoticeUnit() {
        return noticeUnit;
    }

    public void setNoticeUnit(String noticeUnit) {
        this.noticeUnit = noticeUnit;
    }

    public String getNoticeUnitTel() {
        return noticeUnitTel;
    }

    public void setNoticeUnitTel(String noticeUnitTel) {
        this.noticeUnitTel = noticeUnitTel;
    }

    public String getRecUnitType() {
        return recUnitType;
    }

    public void setRecUnitType(String recUnitType) {
        this.recUnitType = recUnitType;
    }

    public String getSecLevel() {
        return secLevel;
    }

    public void setSecLevel(String secLevel) {
        this.secLevel = secLevel;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public Timestamp getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Timestamp expiryTime) {
        this.expiryTime = expiryTime;
    }

    public Timestamp getNoticeTime() {
        return noticeTime;
    }

    public void setNoticeTime(Timestamp noticeTime) {
        this.noticeTime = noticeTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getRecUnit() {
        return recUnit;
    }

    public void setRecUnit(List<String> recUnit) {
        this.recUnit = recUnit;
    }

    public String getAttId() {
        return attId;
    }

    public void setAttId(String attId) {
        this.attId = attId;
    }

}