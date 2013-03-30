package rs.id.webzine.domain;

import java.util.Calendar;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;

@Configurable
@Entity
@Table(schema = "ADMIN", name = "TASK_ATTACHMENT")
public class TaskAttachment extends IdEntity {

  @ManyToOne
  @JoinColumn(name = "TASK_ID", referencedColumnName = "ID", nullable = false)
  private Task taskId;

  @ManyToOne
  @JoinColumn(name = "UM", referencedColumnName = "ID")
  private User um;

  @ManyToOne
  @JoinColumn(name = "UC", referencedColumnName = "ID", nullable = false)
  private User uc;

  @Column(name = "NAME", length = 100)
  private String name;

  @Column(name = "CONTENT")
  @Lob
  @Basic(fetch = FetchType.LAZY)
  private byte[] content;

  @Column(name = "CONTENT_TYPE")
  private String contentType;

  @Column(name = "CONTENT_SIZE")
  private Long contentSize;

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

  public User getUm() {
    return um;
  }

  public void setUm(User um) {
    this.um = um;
  }

  public User getUc() {
    return uc;
  }

  public void setUc(User uc) {
    this.uc = uc;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public byte[] getContent() {
    return content;
  }

  public void setContent(byte[] content) {
    this.content = content;
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

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public Long getContentSize() {
    return contentSize;
  }

  public void setContentSize(Long contentSize) {
    this.contentSize = contentSize;
  }

  public static long count() {
    return entityManager().createQuery("SELECT COUNT(o) FROM TaskAttachment o", Long.class).getSingleResult();
  }

  public static List<TaskAttachment> findAll() {
    return entityManager().createQuery("SELECT o FROM TaskAttachment o", TaskAttachment.class).getResultList();
  }

  public static TaskAttachment find(Integer id) {
    if (id == null)
      return null;
    return entityManager().find(TaskAttachment.class, id);
  }

  public static List<TaskAttachment> findEntries(int firstResult, int maxResults) {
    return entityManager().createQuery("SELECT o FROM TaskAttachment o", TaskAttachment.class)
        .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
  }

  public static List<TaskAttachment> findForTask(Integer taskId) {
    if (taskId == null) {
      return null;
    } else {
      TypedQuery<TaskAttachment> query = entityManager().createQuery(
          "SELECT ta FROM TaskAttachment ta JOIN ta.taskId t WHERE t.id = :taskId", TaskAttachment.class);
      query.setParameter("taskId", taskId);
      return query.getResultList();
    }
  }

}
