package fu_de200312.main;

import fu_de200312.dao.DepartmentDAO;
import fu_de200312.dao.EmployeeDAO;
import fu_de200312.dao.ProjectDAO;
import fu_de200312.pojo.Department;
import fu_de200312.pojo.Employee;
import fu_de200312.pojo.Gender;
import fu_de200312.pojo.Project;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        EmployeeDAO employeeDAO = new EmployeeDAO();
        ProjectDAO projectDAO = new ProjectDAO();
        DepartmentDAO departmentDAO = new DepartmentDAO();

        Department department = new Department(
                "IT Department",
                "Da Nang"
        );

        departmentDAO.save(department);

        Employee employee1 = new Employee(
                "Nguyen Van A",
                "employee1@gmail.com",
                new BigDecimal("1500.00"),
                Gender.MALE,
                LocalDate.of(2022, 1, 10),
                true
        );

        Employee employee2 = new Employee(
                "Tran Thi B",
                "employee2@gmail.com",
                new BigDecimal("1800.00"),
                Gender.FEMALE,
                LocalDate.of(2023, 3, 15),
                true
        );

        Employee employee3 = new Employee(
                "Le Van C",
                "employee3@gmail.com",
                new BigDecimal("2000.00"),
                Gender.MALE,
                LocalDate.of(2024, 5, 20),
                true
        );

        employee1.setDepartment(department);
        employee2.setDepartment(department);
        employee3.setDepartment(department);

        employeeDAO.save(employee1);
        employeeDAO.save(employee2);
        employeeDAO.save(employee3);

        Project projectA = new Project(
                "PRJ-A",
                "Project A",
                new BigDecimal("100000.00"),
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31)
        );

        Project projectB = new Project(
                "PRJ-B",
                "Project B",
                new BigDecimal("150000.00"),
                LocalDate.of(2025, 2, 1),
                null
        );

        projectDAO.save(projectA);
        projectDAO.save(projectB);

        // NV1 tham gia Project A + Project B
        employeeDAO.assignEmployeeToProject(
                employee1.getId(),
                projectA.getId()
        );

        employeeDAO.assignEmployeeToProject(
                employee1.getId(),
                projectB.getId()
        );

        // NV2 tham gia Project B
        employeeDAO.assignEmployeeToProject(
                employee2.getId(),
                projectB.getId()
        );

        // NV3 tham gia Project A
        employeeDAO.assignEmployeeToProject(
                employee3.getId(),
                projectA.getId()
        );


        System.out.println(
                "===== DANH SACH PROJECT CUA TUNG NHAN VIEN ====="
        );

        employeeDAO.findAllWithProjects().forEach(employee -> {

            System.out.println(
                    employee.getFullName()
                            + " (" + employee.getEmail() + ")"
            );

            System.out.println("Projects:");

            employee.getProjects().forEach(project ->
                    System.out.println(
                            "  - "
                                    + project.getProjectCode()
                                    + " | "
                                    + project.getProjectName()
                    )
            );

            System.out.println();
        });

        System.out.println(
                "===== TODO 5.8: SO NV ACTIVE VA TONG SALARY THEO PROJECT ====="
        );

        projectDAO.countActiveEmployeesAndSumSalaryByProject()
                .forEach(row -> System.out.println(
                        "Project: " + row[0]
                                + " | So NV active: " + row[1]
                                + " | Tong salary: " + row[2]
                ));
    }
}