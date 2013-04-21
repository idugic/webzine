package rs.id.webzine.service.project_management;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import rs.id.webzine.domain.project_management.TaskPriority;
import rs.id.webzine.service.GenericService;

@Component
public class TaskPriorityService extends GenericService<TaskPriority> {

  public static final String CD_NORMAL = "normal";

  public static final String CD_HIGH = "high";

  public static final String CD_LOW = "low";

  public TaskPriority findForCd(String cd) {
    TaskPriority taskPriority = null;

    if (cd != null) {
      TypedQuery<TaskPriority> query = entityManager().createQuery("SELECT o FROM TaskPriority o WHERE o.cd = :cd",
          TaskPriority.class);
      query.setParameter("cd", cd);
      List<TaskPriority> taskPriorityList = query.getResultList();
      if (!taskPriorityList.isEmpty()) {
        taskPriority = taskPriorityList.get(0);
      }
    }

    return taskPriority;
  }
}
