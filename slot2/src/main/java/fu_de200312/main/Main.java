package fu_de200312.main;

import fu_de200312.dao.EmployeeDAO;
import fu_de200312.pojo.Employee;
import fu_de200312.pojo.Gender;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        EmployeeDAO dao = new EmployeeDAO();

        // CREATE
        Employee employee = new Employee(
                "Nguyen Van A",
                "nguyenvana@gmail.com",
                new BigDecimal("1500.00"),
                Gender.MALE,
                LocalDate.of(2023, 1, 10),
                true
        );

        dao.save(employee);

        System.out.println("CREATE:");
        System.out.println(employee);

        // READ
        Employee found = dao.findById(employee.getId());

        System.out.println("\nREAD:");
        System.out.println(found);

        // UPDATE
        found.setSalary(new BigDecimal("2000.00"));

        dao.update(found);

        System.out.println("\nUPDATE:");
        System.out.println(dao.findById(employee.getId()));

        // READ ALL
        System.out.println("\nREAD ALL:");
        dao.findAll().forEach(System.out::println);

        // DELETE
        dao.delete(employee.getId());

        System.out.println("\nDELETE:");

        Employee deleted = dao.findById(employee.getId());

        System.out.println("Sau khi xoa: " + deleted);
        // TODO 9 - Test duplicate email

        Employee employee1 = new Employee(
                "Nguyen Van B",
                "duplicate@gmail.com",
                new BigDecimal("1800.00"),
                Gender.MALE,
                LocalDate.of(2024, 1, 1),
                true
        );

        Employee employee2 = new Employee(
                "Nguyen Van C",
                "duplicate@gmail.com",
                new BigDecimal("1900.00"),
                Gender.FEMALE,
                LocalDate.of(2024, 2, 1),
                true
        );

        dao.save(employee1);

        try {
            dao.save(employee2);
            System.out.println("Loi: Email trung van duoc them!");
        } catch (Exception e) {
            System.out.println("Email trung bi chan: " + e.getClass().getSimpleName());
        }

        dao.delete(employee1.getId());
    }
}
