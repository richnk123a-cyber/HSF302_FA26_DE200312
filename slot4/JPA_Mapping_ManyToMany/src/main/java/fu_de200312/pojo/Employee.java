package fu_de200312.pojo;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(precision = 10, scale = 2)
    private BigDecimal salary;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate hireDate;

    @Column(nullable = false)
    private boolean active = true;

    @Transient
    private int yearsOfService;

    // Quan hệ ManyToOne với Department từ bài trước
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private fu_de200312.pojo.Department department;

    // TODO 5.2:
    // Employee là owning side của quan hệ ManyToMany.
    @ManyToMany
    @JoinTable(
            name = "employee_project",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private Set<Project> projects = new HashSet<>();

    public Employee() {
    }

    public Employee(String fullName, String email, BigDecimal salary,
                    Gender gender, LocalDate hireDate, boolean active) {
        this.fullName = fullName;
        this.email = email;
        this.salary = salary;
        this.gender = gender;
        this.hireDate = hireDate;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    // Tính số năm làm việc, không lưu trong DB
    public int getYearsOfService() {
        if (hireDate == null) {
            return 0;
        }
        return (int) ChronoUnit.YEARS.between(hireDate, LocalDate.now());
    }

    // TODO 5.5:
    // Thêm Employee vào cả hai phía của quan hệ ManyToMany.
    public void assignToProject(Project p) {
        if (p == null) {
            return;
        }

        this.projects.add(p);
        p.getEmployees().add(this);
    }

    public void unassignFromProject(Project p) {
        if (p == null) {
            return;
        }

        this.projects.remove(p);
        p.getEmployees().remove(this);
    }

    // TODO 5.4:
    // Dùng email làm business key, không dùng id.
    // id do Hibernate tự sinh và có thể thay đổi từ null -> giá trị
    // sau khi persist, gây vấn đề khi dùng HashSet/HashMap.
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Employee)) {
            return false;
        }

        Employee employee = (Employee) o;
        return Objects.equals(email, employee.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", salary=" + salary +
                ", gender=" + gender +
                ", hireDate=" + hireDate +
                ", active=" + active +
                ", yearsOfService=" + getYearsOfService() +
                '}';
    }
}