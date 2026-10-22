package com.vet_clinic.vet_app.model;

public class LabTest {

    private int testId;
    private int appointmentId;
    private Integer diagnosisId;          // Integer (not int) because the column is optional/NULL
    private String testType;
    private int orderedBy;
    private int performedBy;
    private String result;
    private String resultFileUrl;
    private java.sql.Timestamp orderedAt;
    private java.sql.Timestamp completedAt;

    public LabTest() { }

    // Used when inserting a new row (the database generates the id)
    public LabTest(int appointmentId, Integer diagnosisId, String testType, int orderedBy, int performedBy, java.sql.Timestamp orderedAt) 
    {
        this.appointmentId = appointmentId;
        this.diagnosisId = diagnosisId;
        this.testType = testType;
        this.orderedBy = orderedBy;
        this.performedBy = performedBy;
        this.orderedAt = orderedAt;
    }
//Setters and Getters
    public int getTestId() 
    { return testId; }
    
    public void setTestId(int testId)
    { this.testId = testId; }
    
    public int getAppointmentId() 
    { return appointmentId; }
    
    public void setAppointmentId(int appointmentId)
    { this.appointmentId = appointmentId; }
    
    public Integer getDiagnosisId() 
    { return diagnosisId; }
    
    public void setDiagnosisId(Integer diagnosisId)
    { this.diagnosisId = diagnosisId; }
    
    public String getTestType()
    { return testType; }
    
    public void setTestType(String testType)
    { this.testType = testType; }
    
    public int getOrderedBy()
    { return orderedBy; }
    
    public void setOrderedBy(int orderedBy) 
    { this.orderedBy = orderedBy; }
    
    public int getPerformedBy() 
    { return performedBy; }
    
    public void setPerformedBy(int performedBy)
    { this.performedBy = performedBy; }
    
    public String getResult() 
    { return result; }
    
    public void setResult(String result)
    { this.result = result; }
    
    public String getResultFileUrl()
    { return resultFileUrl; }
    
    public void setResultFileUrl(String resultFileUrl)
    { this.resultFileUrl = resultFileUrl; }
    
    public java.sql.Timestamp getOrderedAt() 
    { return orderedAt; }
    
    public void setOrderedAt(java.sql.Timestamp orderedAt)
    { this.orderedAt = orderedAt; }
    
    public java.sql.Timestamp getCompletedAt()
    { return completedAt; }
    
    public void setCompletedAt(java.sql.Timestamp completedAt) 
    { this.completedAt = completedAt; }

    @Override
    public String toString() {
        return "LabTest{testId=" + testId
                + ", appointmentId=" + appointmentId
                + ", diagnosisId=" + diagnosisId
                + ", testType=" + testType
                + ", orderedBy=" + orderedBy
                + ", performedBy=" + performedBy
                + ", result=" + result
                + ", resultFileUrl=" + resultFileUrl
                + ", orderedAt=" + orderedAt
                + ", completedAt=" + completedAt + "}";
    }
}