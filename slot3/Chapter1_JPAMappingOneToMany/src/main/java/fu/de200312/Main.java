package fu.de200312;

import fu.de200312.pojo.Department;
import fu.de200312.pojo.Employee;
import fu.de200312.pojo.Gender;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {

        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("hsf302FU");

        EntityManager em = emf.createEntityManager();

        try {
            Department dept = new Department("IT", "Ha Noi");

            Employee emp = new Employee(
                    "test@company.com",
                    "Test",
                    Gender.OTHER,
                    new BigDecimal("1000"),
                    LocalDate.now()
            );

            dept.addEmployee(emp);

            em.getTransaction().begin();

            em.persist(dept);

            em.getTransaction().commit();

            System.out.println("Department ID: " + dept.getId());
            System.out.println("Employee ID: " + emp.getId());

        } catch (Exception e) {
            e.printStackTrace();

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

        } finally {
            em.close();
            emf.close();
        }
    }
}
