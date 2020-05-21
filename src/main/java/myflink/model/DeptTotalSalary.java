package myflink.model;

public class DeptTotalSalary {

    private Integer staticKey = 1;

    public String dept;

    public Long salary;

    public DeptTotalSalary() {
    }

    public DeptTotalSalary(String dept, Long salary) {
        this.dept = dept;
        this.salary = salary;
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

    @Override
    public String toString() {
        return "DeptTotalSalary{" +
                "dept='" + dept + '\'' +
                ", salary=" + salary +
                '}';
    }
}
