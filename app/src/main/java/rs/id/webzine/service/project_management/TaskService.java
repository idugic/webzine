package rs.id.webzine.service.project_management;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import rs.id.webzine.domain.project_management.Task;
import rs.id.webzine.service.GenericService;

@Component
public class TaskService extends GenericService<Task> {

  public List<Task> findAvailableAsParent(Integer id) {
    if (id == null) {
      return findAll();
    } else {
      TypedQuery<Task> query = entityManager().createQuery(
          "SELECT t FROM Task t JOIN t.status s WHERE o.id != :id AND s.cd != :cdCompleted", Task.class);
      query.setParameter("id", id);
      query.setParameter("cdCompleted", TaskStatusService.CD_COMPLETED);

      return query.getResultList();
    }
  }

}
