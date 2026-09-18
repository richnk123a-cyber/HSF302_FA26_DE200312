package fu_de200312.dao;

import fu_de200312.pojo.Department;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class DepartmentDAO {

    private final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("hsf302FU");

    public void save(Department d) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            em.persist(d);

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

    public List<Department> findAll() {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                    "SELECT d FROM Department d",
                    Department.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    public Department findById(Long id) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.find(Department.class, id);
        } finally {
            em.close();
        }
    }

    public void update(Department d) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            em.merge(d);

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

    public void delete(Long id) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Department d = em.find(Department.class, id);

            if (d != null) {
                em.remove(d);
            }

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

    public Department findByIdWithEmployees(Long id) {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                            "SELECT d FROM Department d " +
                                    "JOIN FETCH d.employees " +
                                    "WHERE d.id = :id",
                            Department.class
                    )
                    .setParameter("id", id)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    public List<Department> findAllWithEmployees() {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                    "SELECT DISTINCT d FROM Department d " +
                            "JOIN FETCH d.employees",
                    Department.class
            ).getResultList();
        } finally {
            em.close();
        }
    }
}