package com.browse.entity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;

import java.io.Serializable;
import java.util.Date;

//充装检查数据，实体类
@JSONType
public class CheckRecorder implements Serializable {
    //车牌号
    private String plateNumber="";
    //气瓶号
    private String cylinderNumber="";
    //有效期
    @JSONField(format = "yyyy-MM-dd")
    private Date validDate;
    //枪编号
    private String gunNumber="";
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
    private String checkOperator="";
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
        this.cylinderNumber = cylinderNumber;
    }

    public Date getValidDate() {
        return validDate;
    }

    public void setValidDate(Date validDate) {
        this.validDate = validDate;
    }

    public String getGunNumber() {
        return gunNumber;
    }

    public void setGunNumber(String gunNumber) {
        this.gunNumber = gunNumber;
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
        return checkOperator;
    }

    public void setCheckOperator(String checkOperator) {
        this.checkOperator = checkOperator;
    }

    public String getFillOperator() {
        return fillOperator;
    }

    public void setFillOperator(String fillOperator) {
        this.fillOperator = fillOperator;
    }
    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

}
