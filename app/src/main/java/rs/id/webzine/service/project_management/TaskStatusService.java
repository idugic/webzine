package rs.id.webzine.service.project_management;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import rs.id.webzine.domain.project_management.TaskStatus;
import rs.id.webzine.service.GenericService;

@Component
public class TaskStatusService extends GenericService<TaskStatus> {

  public static String CD_NEW = "new";

  public static String CD_COMPLETED = "completed";

  public TaskStatus findForCd(String cd) {
    TaskStatus taskStatus = null;

    if (cd != null) {
      TypedQuery<TaskStatus> query = entityManager().createQuery("SELECT o FROM TaskStatus o WHERE o.cd = :cd",
          TaskStatus.class);
      query.setParameter("cd", cd);
      List<TaskStatus> taskStatusList = query.getResultList();
      if (!taskStatusList.isEmpty()) {
        taskStatus = taskStatusList.get(0);
      }
    }

    return taskStatus;
  }
}
