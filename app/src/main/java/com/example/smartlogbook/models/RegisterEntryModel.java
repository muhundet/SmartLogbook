package com.example.smartlogbook.models;

public class RegisterEntryModel {
    private String registerEntryId;
    private String employeeId;
    private String employeeName;
    private String employeeSurname;
    private String employeeDepartment;
    private String date;
    private String time_in;
    private String time_out;
    private String status;

    public RegisterEntryModel(String registerEntryId, String employeeId, String employeeName, String employeeSurname, String employeeDepartment, String date, String time_in, String time_out, String status) {
        this.registerEntryId = registerEntryId;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeSurname = employeeSurname;
        this.employeeDepartment = employeeDepartment;
        this.date = date;
        this.time_in = time_in;
        this.time_out = time_out;
        this.status = status;
    }

    public String getRegisterEntryId() {
        return registerEntryId;
    }

    public void setRegisterEntryId(String registerEntryId) {
        this.registerEntryId = registerEntryId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeSurname() {
        return employeeSurname;
    }

    public void setEmployeeSurname(String employeeSurname) {
        this.employeeSurname = employeeSurname;
    }

    public String getEmployeeDepartment() {
        return employeeDepartment;
    }

    public void setEmployeeDepartment(String employeeDepartment) {
        this.employeeDepartment = employeeDepartment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime_in() {
        return time_in;
    }

    public void setTime_in(String time_in) {
        this.time_in = time_in;
    }

    public String getTime_out() {
        return time_out;
    }

    public void setTime_out(String time_out) {
        this.time_out = time_out;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
