package myflink.model;


import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

public class User {

    private Integer staticKey = 1;

    private String empId;
    private String name;
    private String dept;
    private Long salary;

    public User(String name, String dept, Long salary) {
        this.name = name;
        this.dept = dept;
        this.salary = salary;
    }

    public User() {
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public Integer getStaticKey() {
        return staticKey;
    }

    public void setStaticKey(Integer staticKey) {
        this.staticKey = staticKey;
    }

    public String buldJson() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        String s2 = om.writeValueAsString(this);
        return s2;
    }

    public static User parseJson(String s) throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        return om.readValue(s,User.class);
    }

}
