package com.browse.entity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

//充装检查数据，实体类
@JSONType
public class CheckRecorder implements Serializable {
    //车牌号
    private String plateNumber="";
    //气瓶号
    private String cylinderNumber="";
    //有效期
    @JSONField(format = "yyyyMMdd")
    private Date validityPeriod;
    //枪编号
    private String GunNumber="";
    //充装前外观检查
    private int surfaceBefore=0;
    //充装前泄漏检查
    private int leakBefore=0;
    //充装后外观检查
    private int surfaceAfter=0;
    //充装后泄漏检查
    private int leakAfter=0;

    //充装数据id
    private String fillDataId;
    //充装显示数据,不用存储到数据库中，仅显示使用
    private JSONObject fillData;


    //检查员
    private String CheckOperator="";
    //充装员
    private String fillOperator="";
    //司机签字文件名
    private String signFile="";

    public JSONObject getFillData() {
        return fillData;
    }

    public void setFillData(JSONObject fillData) {
        this.fillData = fillData;
    }

    public String getFillDataId() {
        return fillDataId;
    }

    public void setFillDataId(String fillDataId) {
        this.fillDataId = fillDataId;
    }

    public String getSignFile() {
        return signFile;
    }

    public void setSignFile(String signFile) {
        this.signFile = signFile;
    }


    public String getCylinderNumber() {
        return cylinderNumber;
    }

    public void setCylinderNumber(String cylinderNumber) {
        cylinderNumber = cylinderNumber;
    }

    public Date getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(Date validityPeriod) {
        validityPeriod = validityPeriod;
    }

    public String getGunNumber() {
        return GunNumber;
    }

    public void setGunNumber(String gunNumber) {
        GunNumber = gunNumber;
    }

    public CheckRecorder(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public int getSurfaceBefore() {
        return surfaceBefore;
    }

    public void setSurfaceBefore(int surfaceBefore) {
        this.surfaceBefore = surfaceBefore;
    }

    public int getLeakBefore() {
        return leakBefore;
    }

    public void setLeakBefore(int leakBefore) {
        this.leakBefore = leakBefore;
    }

    public int getSurfaceAfter() {
        return surfaceAfter;
    }

    public void setSurfaceAfter(int surfaceAfter) {
        this.surfaceAfter = surfaceAfter;
    }

    public int getLeakAfter() {
        return leakAfter;
    }

    public void setLeakAfter(int leakAfter) {
        this.leakAfter = leakAfter;
    }

    public String getCheckOperator() {
        return CheckOperator;
    }

    public void setCheckOperator(String checkOperator) {
        CheckOperator = checkOperator;
    }

    public String getFillOperator() {
        return fillOperator;
    }

    public void setFillOperator(String fillOperator) {
        this.fillOperator = fillOperator;
    }
    public String getNumber() {
        return plateNumber;
    }

    public void setNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

}