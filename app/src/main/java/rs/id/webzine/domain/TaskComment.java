package rs.id.webzine.domain;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;

@Configurable
@Entity
@Table(schema = "ADMIN", name = "TASK_COMMENT")
public class TaskComment extends IdEntity {

  @ManyToOne
  @JoinColumn(name = "TASK_ID", referencedColumnName = "ID", nullable = false)
  private Task taskId;

  @ManyToOne
  @JoinColumn(name = "UC", referencedColumnName = "ID", nullable = false)
  private User uc;

  @ManyToOne
  @JoinColumn(name = "UM", referencedColumnName = "ID")
  private User um;

  @Column(name = "TEXT", length = 500)
  @NotNull
  private String text;

  @Column(name = "DC")
  @Temporal(TemporalType.TIMESTAMP)
  @DateTimeFormat(style = "MM")
  private Calendar dc;

  @Column(name = "DM")
  @Temporal(TemporalType.TIMESTAMP)
  @DateTimeFormat(style = "MM")
  private Calendar dm;

  public Task getTaskId() {
    return taskId;
  }

  public void setTaskId(Task taskId) {
    this.taskId = taskId;
  }

  public User getUc() {
    return uc;
  }

  public void setUc(User uc) {
    this.uc = uc;
  }

  public User getUm() {
    return um;
  }

  public void setUm(User um) {
    this.um = um;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Calendar getDc() {
    return dc;
  }

  public void setDc(Calendar dc) {
    this.dc = dc;
  }

  public Calendar getDm() {
    return dm;
  }

  public void setDm(Calendar dm) {
    this.dm = dm;
  }

  public static long count() {
    return entityManager().createQuery("SELECT COUNT(o) FROM TaskComment o", Long.class).getSingleResult();
  }

  public static List<TaskComment> findAll() {
    return entityManager().createQuery("SELECT o FROM TaskComment o", TaskComment.class).getResultList();
  }

  public static TaskComment find(Integer id) {
    if (id == null)
      return null;
    return entityManager().find(TaskComment.class, id);
  }

  public static List<TaskComment> findEntries(int firstResult, int maxResults) {
    return entityManager().createQuery("SELECT o FROM TaskComment o", TaskComment.class).setFirstResult(firstResult)
        .setMaxResults(maxResults).getResultList();
  }

  public static List<TaskComment> findForTask(Integer taskId) {
    if (taskId == null) {
      return null;
    } else {
      TypedQuery<TaskComment> query = entityManager().createQuery(
          "SELECT tc FROM TaskComment tc JOIN tc.taskId t WHERE t.id = :taskId", TaskComment.class);
      query.setParameter("taskId", taskId);
      return query.getResultList();
    }
  }

}
