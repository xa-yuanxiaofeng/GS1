package com.browse.gs;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;

import java.io.Serializable;
import java.util.Date;

//充装检查数据，实体类
@JSONType
public class CheckRecorderEntity implements Serializable {
    //车牌号
    private String plateNumber="";
    //气瓶号
    private String CylinderNumber="";
    //有效期
    private Date ValidityPeriod=new Date();
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
    //充装前余压
    private String pressBefore="";
    //充装开始时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime = new Date();
    //充装后压力
    private String pressAfter="";
    //充装结束时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date EndTime=new Date();
    //检查员
    private String CheckOperator="";
    //充装员
    private String fillOperator="";
    //司机签字文件名
    private String signFile="";

    public String getSignFile() {
        return signFile;
    }

    public void setSignFile(String signFile) {
        this.signFile = signFile;
    }


    public String getCylinderNumber() {
        return CylinderNumber;
    }

    public void setCylinderNumber(String cylinderNumber) {
        CylinderNumber = cylinderNumber;
    }

    public Date getValidityPeriod() {
        return ValidityPeriod;
    }

    public void setValidityPeriod(Date validityPeriod) {
        ValidityPeriod = validityPeriod;
    }

    public String getGunNumber() {
        return GunNumber;
    }

    public void setGunNumber(String gunNumber) {
        GunNumber = gunNumber;
    }



    public CheckRecorderEntity(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public int isSurfaceBefore() {
        return surfaceBefore;
    }

    public void setSurfaceBefore(int surfaceBefore) {
        this.surfaceBefore = surfaceBefore;
    }

    public int isLeakBefore() {
        return leakBefore;
    }

    public void setLeakBefore(int leakBefore) {
        this.leakBefore = leakBefore;
    }

    public int isSurfaceAfter() {
        return surfaceAfter;
    }

    public void setSurfaceAfter(int surfaceAfter) {
        this.surfaceAfter = surfaceAfter;
    }

    public int isLeakAfter() {
        return leakAfter;
    }

    public void setLeakAfter(int leakAfter) {
        this.leakAfter = leakAfter;
    }

    public String getPressBefore() {
        return pressBefore;
    }

    public void setPressBefore(String pressBefore) {
        this.pressBefore = pressBefore;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getPressAfter() {
        return pressAfter;
    }

    public void setPressAfter(String pressAfter) {
        this.pressAfter = pressAfter;
    }

    public Date getEndTime() {
        return EndTime;
    }

    public void setEndTime(Date endTime) {
        EndTime = endTime;
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
