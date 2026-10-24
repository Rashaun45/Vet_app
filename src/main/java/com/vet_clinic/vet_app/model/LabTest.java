package com.vet_clinic.vet_app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "test")  
public class LabTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_id")
    private int testId;

    
    @Column(name = "appointment_id")
    private int appointmentId;

    @Column(name = "diagnosis_id")
    private Integer diagnosisId;          // Integer (not int) because the column is optional/NULL

    @Column(name = "test_type")
    private String testType;

    @Column(name = "ordered_by")
    private int orderedBy;

    @Column(name = "performed_by")
    private int performedBy;

    @Column(name = "result")
    private String result;

    @Column(name = "result_file_url")
    private String resultFileUrl;

    @Column(name = "ordered_at")
    private java.sql.Timestamp orderedAt;

    @Column(name = "completed_at")
    private java.sql.Timestamp completedAt;

    // Relationships Hibernate
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "appointment_id", nullable = false)
    private AppointmentVisit appointment;

    @ManyToOne(fetch = FetchType.LAZY)   // optional: a test may not be tied to a diagnosis yet
    @JoinColumn(name = "diagnosis_id")
    private Diagnosis diagnosis;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ordered_by", nullable = false)
    private Vet orderedByVet;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "performed_by", nullable = false)
    private Staff performedByStaff;

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

//Relationship getters and setters (the setters keep the matching id field in sync)

    public AppointmentVisit getAppointment()
    { return appointment; }

    public void setAppointment(AppointmentVisit appointment)
    { this.appointment = appointment; if (appointment != null) this.appointmentId = appointment.getAppointmentId(); }

    public Diagnosis getDiagnosis()
    { return diagnosis; }

    public void setDiagnosis(Diagnosis diagnosis)
    { this.diagnosis = diagnosis; this.diagnosisId = (diagnosis == null) ? null : diagnosis.getDiagnosisId(); }

    public Vet getOrderedByVet()
    { return orderedByVet; }

    public void setOrderedByVet(Vet orderedByVet)
    { this.orderedByVet = orderedByVet; if (orderedByVet != null) this.orderedBy = orderedByVet.getVetId(); }

    public Staff getPerformedByStaff()
    { return performedByStaff; }

    public void setPerformedByStaff(Staff performedByStaff)
    { this.performedByStaff = performedByStaff; if (performedByStaff != null) this.performedBy = performedByStaff.getStaffId(); }

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