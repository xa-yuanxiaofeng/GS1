package com.browse.gs;

import java.util.Date;

//充装检查数据，实体类
public class CheckRecorderEntity {
    //车牌号
    private String plateNumber;
    //气瓶号
    private String CylinderNumber;
    //有效期
    private Date ValidityPeriod;
    //枪编号
    private String GunNumber;
    //充装前外观检查
    private boolean surfaceBefore;
    //充装前泄漏检查
    private boolean leakBefore;
    //充装后外观检查
    private boolean surfaceAfter;
    //充装后泄漏检查
    private boolean leakAfter;
    //充装前余压
    private String pressBefore;
    //充装开始时间
    private Date startTime;
    //充装后压力
    private String pressAfter;
    //充装结束时间
    private Date EndTime;
    //检查员
    private String CheckOperator;
    //充装员
    private String fillOperator;
    //司机签字

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

    public boolean isSurfaceBefore() {
        return surfaceBefore;
    }

    public void setSurfaceBefore(boolean surfaceBefore) {
        this.surfaceBefore = surfaceBefore;
    }

    public boolean isLeakBefore() {
        return leakBefore;
    }

    public void setLeakBefore(boolean leakBefore) {
        this.leakBefore = leakBefore;
    }

    public boolean isSurfaceAfter() {
        return surfaceAfter;
    }

    public void setSurfaceAfter(boolean surfaceAfter) {
        this.surfaceAfter = surfaceAfter;
    }

    public boolean isLeakAfter() {
        return leakAfter;
    }

    public void setLeakAfter(boolean leakAfter) {
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
