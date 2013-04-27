package rs.id.webzine.service.project_management;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import rs.id.webzine.entity.project_management.TaskAttachment;
import rs.id.webzine.service.GenericService;

@Component
public class TaskAttachmentService extends GenericService<TaskAttachment> {

  public List<TaskAttachment> findForTask(Integer taskId) {
    if (taskId == null) {
      return null;
    } else {
      TypedQuery<TaskAttachment> query = entityManager().createQuery(
          "SELECT ta FROM TaskAttachment ta JOIN ta.task t WHERE t.id = :taskId", TaskAttachment.class);
      query.setParameter("taskId", taskId);
      return query.getResultList();
    }
  }

}
