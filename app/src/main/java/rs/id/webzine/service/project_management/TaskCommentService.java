package rs.id.webzine.service.project_management;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import rs.id.webzine.entity.project_management.TaskComment;
import rs.id.webzine.service.GenericService;

@Component
public class TaskCommentService extends GenericService<TaskComment> {

  public List<TaskComment> findForTask(Integer taskId) {
    if (taskId == null) {
      return null;
    } else {
      TypedQuery<TaskComment> query = entityManager().createQuery(
          "SELECT tc FROM TaskComment tc JOIN tc.task t WHERE t.id = :taskId", TaskComment.class);
      query.setParameter("taskId", taskId);
      return query.getResultList();
    }
  }

}
