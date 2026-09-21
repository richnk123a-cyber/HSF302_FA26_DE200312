package fu_de200312.dao;

import fu_de200312.pojo.Employee;
import fu_de200312.pojo.Project;
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

    public void delete(Long id) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Employee e = em.find(Employee.class, id);

            if (e != null) {
                em.remove(e);
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

    // TODO 5.6:
    // Tìm Employee và Project trong cùng một transaction,
    // sau đó gọi helper method assignToProject() để cập nhật
    // quan hệ ở cả hai phía.
    public void assignEmployeeToProject(Long employeeId, Long projectId) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Employee employee = em.find(Employee.class, employeeId);
            Project project = em.find(Project.class, projectId);

            if (employee == null) {
                throw new IllegalArgumentException(
                        "Employee not found: " + employeeId
                );
            }

            if (project == null) {
                throw new IllegalArgumentException(
                        "Project not found: " + projectId
                );
            }

            employee.assignToProject(project);

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

    public void unassignEmployeeFromProject(Long employeeId, Long projectId) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Employee employee = em.find(Employee.class, employeeId);
            Project project = em.find(Project.class, projectId);

            if (employee == null) {
                throw new IllegalArgumentException(
                        "Employee not found: " + employeeId
                );
            }

            if (project == null) {
                throw new IllegalArgumentException(
                        "Project not found: " + projectId
                );
            }

            employee.unassignFromProject(project);

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

    public java.util.List<Employee> findAllWithProjects() {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                    "SELECT DISTINCT e FROM Employee e LEFT JOIN FETCH e.projects",
                    Employee.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    public java.util.List<Employee> findActiveEmployeesInMultipleProjects() {
        EntityManager em = emf.createEntityManager();

        try {
            return em.createQuery(
                    "SELECT e FROM Employee e WHERE e.active = true AND SIZE(e.projects) > 1",
                    Employee.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    /*
     * TODO 5.11:
     * - Nhân viên nghỉ việc (deactivate / soft-delete) KHÔNG NÊN tự động bị gỡ khỏi tất cả project:
     *   + Cần giữ lại lịch sử tham gia (historical data / audit log) để biết nhân viên đó từng đóng góp cho các project nào.
     *   + Khi tính toán nhân sự hay chi phí hiện tại, chỉ cần lọc theo điều kiện (e.active = true) như ở TODO 5.8 & TODO 5.10.
     *
     * - Về Cascade:
     *   + Tuyệt đối KHÔNG dùng CascadeType.REMOVE trên quan hệ ManyToMany, vì việc xóa hoặc thao tác trên Employee có thể cascade xóa luôn cả Project (entity độc lập).
     *
     * - Cách xử lý phù hợp:
     *   + Sử dụng Soft Delete: cập nhật active = false (giữ nguyên liên kết trong bảng trung gian employee_project).
     *   + Nếu nghiệp vụ thực sự yêu cầu gỡ nhân viên khỏi dự án cụ thể, chủ động gọi method gỡ (unassignFromProject) trong transaction riêng biệt một cách tường minh.
     */
    public void deactivateEmployee(Long employeeId) {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Employee employee = em.find(Employee.class, employeeId);
            if (employee == null) {
                throw new IllegalArgumentException("Employee not found: " + employeeId);
            }

            employee.setActive(false);

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