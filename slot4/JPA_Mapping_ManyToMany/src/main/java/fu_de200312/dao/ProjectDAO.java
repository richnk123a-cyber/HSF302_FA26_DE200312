package fu_de200312.dao;

import fu_de200312.pojo.Project;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class ProjectDAO {

    private final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("hsf302FU");

    public void save(Project p) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            em.persist(p);

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

    public Project findById(Long id) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.find(Project.class, id);
        } finally {
            em.close();
        }
    }

    public List<Object[]> countActiveEmployeesAndSumSalaryByProject() {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                    "SELECT p.projectName, COUNT(e), SUM(e.salary) " +
                    "FROM Project p JOIN p.employees e " +
                    "WHERE e.active = true " +
                    "GROUP BY p.projectName",
                    Object[].class
            ).getResultList();
        } finally {
            em.close();
        }
    }
}