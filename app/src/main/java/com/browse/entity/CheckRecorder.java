package com.browse.entity;

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
    private String gunCode ="";
    //加气机编号
    private String gasMachineCode;
    //加气站编号
    private String gasName;

    //充装前外观检查
    private int surfaceBefore=0;
    //充装前泄漏检查
    private int leakBefore=0;
    //充装后外观检查
    private int surfaceAfter=0;
    //充装后泄漏检查
    private int leakAfter=0;

    //充装记录id
    private int fillRecorderId=-1;

    //检查员
    private String checkOperator="";
    //充装员
    private String fillOperator="";
    //司机签字文件名
    private String signFile="";

    public String getGasMachineCode() {
        return gasMachineCode;
    }

    public void setGasMachineCode(String gasMachineCode) {
        this.gasMachineCode = gasMachineCode;
    }

    public String getGasName() {
        return gasName;
    }

    public void setGasName(String gasName) {
        this.gasName = gasName;
    }


    public int getFillRecorderId() {
        return fillRecorderId;
    }

    public void setFillRecorderId(int fillRecorderId) {
        this.fillRecorderId = fillRecorderId;
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

    public String getGunCode() {
        return gunCode;
    }

    public void setGunCode(String gunCode) {
        this.gunCode = gunCode;
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
