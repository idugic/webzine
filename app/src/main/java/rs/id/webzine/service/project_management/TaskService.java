package rs.id.webzine.service.project_management;

import java.util.Calendar;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import rs.id.webzine.domain.project_management.Task;
import rs.id.webzine.domain.project_management.TaskAttachment;
import rs.id.webzine.domain.project_management.TaskComment;
import rs.id.webzine.service.GenericService;

@Component
public class TaskService extends GenericService<Task> {

  @Autowired
  TaskStatusService taskStatusService;

  @Autowired
  TaskCommentService taskCommentService;

  @Autowired
  TaskAttachmentService taskAttachmentService;

  public List<Task> findAvailableAsParent(Integer id) {
    List<Task> taskList;
    if (id == null) {
      taskList = findAll();
    } else {
      TypedQuery<Task> query = entityManager().createQuery(
          "SELECT t FROM Task t JOIN t.status s WHERE t.id != :id AND s.cd != :cdCompleted", Task.class);
      query.setParameter("id", id);
      query.setParameter("cdCompleted", TaskStatusService.CD_COMPLETED);

      taskList = query.getResultList();
    }

    Task emptyTask = new Task();
    emptyTask.setId(-1);
    taskList.add(0, emptyTask); // first one is empty task

    return taskList;
  }

  @Override
  @Transactional
  public void create(Task task) {

    task.setStatus(taskStatusService.findForCd(TaskStatusService.CD_NEW));

    task.setUc(currentUser());
    task.setDc(Calendar.getInstance());

    if (task.getOwnerUser().getId() == -1) {
      task.setOwnerUser(null);
    }

    if (task.getParentTask().getId() == -1) {
      task.setParentTask(null);
    }

    super.create(task);
  }

  @Override
  @Transactional
  public void update(Task task) {
    Task persistedTask = find(task.getId());

    task.setUc(persistedTask.getUc());
    task.setDc(persistedTask.getDc());

    task.setUm(currentUser());
    task.setDm(Calendar.getInstance());

    if (task.getOwnerUser().getId() == -1) {
      task.setOwnerUser(null);
    }

    if (task.getParentTask().getId() == -1) {
      task.setParentTask(null);
    }

    super.update(task);
  }

  @Override
  @Transactional
  public void delete(Integer id) {
    Task persistedTask = find(id);
    persistedTask.setStatus(taskStatusService.findForCd(TaskStatusService.CD_DELETED));
  }

  @Transactional
  public void createComment(Integer taskId, TaskComment taskComment) {
    taskComment.setTask(find(taskId));

    taskComment.setUc(currentUser());
    taskComment.setDc(Calendar.getInstance());

    taskCommentService.create(taskComment);
  }

  @Transactional
  public void createAttachment(Integer taskId, TaskAttachment taskAttachment) {
    taskAttachment.setTask(find(taskId));

    taskAttachment.setUc(currentUser());
    taskAttachment.setDc(Calendar.getInstance());

    taskAttachmentService.create(taskAttachment);
  }
}
