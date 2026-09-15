package fu_de200312.dao;

import fu_de200312.pojo.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EmployeeDAO {
    private final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("hsf302FU");

    public void save(Employee e) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            em.persist(e);

            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }

    public Employee findById(Long id) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.find(Employee.class, id);
        } finally {
            em.close();
        }
    }

    public java.util.List<Employee> findAll() {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                    "SELECT e FROM Employee e",
                    Employee.class
            ).getResultList();
        } finally {
            em.close();
        }
    }
    public Employee findByEmail(String email) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                            "SELECT e FROM Employee e WHERE e.email = :email",
                            Employee.class
                    )
                    .setParameter("email", email)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }

    public java.util.List<Employee> findBySalaryGreaterThan(
            java.math.BigDecimal salary) {

        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                            "SELECT e FROM Employee e WHERE e.salary > :salary",
                            Employee.class
                    )
                    .setParameter("salary", salary)
                    .getResultList();
        } finally {
            em.close();
        }
    }
    public void update(Employee e) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            e = em.merge(e);

            em.getTransaction().commit();
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw ex;
        } finally {
            em.close();
        }
    }
}
