package rs.id.webzine.entity.project_management;

import java.util.Calendar;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;

import rs.id.webzine.entity.system.User;

@Configurable
@Entity
@Table(schema = "ADMIN", name = "TASK_ATTACHMENT")
public class TaskAttachment {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID")
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "TASK_ID", referencedColumnName = "ID", nullable = false)
  @NotNull
  private Task task;

  @Column(name = "NAME", length = 100)
  @NotNull
  private String name;

  @Column(name = "CONTENT")
  @Lob
  @Basic(fetch = FetchType.LAZY)
  @NotNull
  private byte[] content;

  @Column(name = "CONTENT_TYPE")
  @NotNull
  private String contentType;

  @Column(name = "CONTENT_SIZE")
  @NotNull
  private Long contentSize;

  @ManyToOne
  @JoinColumn(name = "UC", referencedColumnName = "ID", nullable = false)
  @NotNull
  private User uc;

  @Column(name = "DC")
  @Temporal(TemporalType.TIMESTAMP)
  @DateTimeFormat(style = "MM")
  @NotNull
  private Calendar dc;

  @ManyToOne
  @JoinColumn(name = "UM", referencedColumnName = "ID")
  private User um;

  @Column(name = "DM")
  @Temporal(TemporalType.TIMESTAMP)
  @DateTimeFormat(style = "MM")
  private Calendar dm;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Task getTask() {
    return task;
  }

  public void setTask(Task task) {
    this.task = task;
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

  public User getUc() {
    return uc;
  }

  public void setUc(User uc) {
    this.uc = uc;
  }

  public Calendar getDc() {
    return dc;
  }

  public void setDc(Calendar dc) {
    this.dc = dc;
  }

  public User getUm() {
    return um;
  }

  public void setUm(User um) {
    this.um = um;
  }

  public Calendar getDm() {
    return dm;
  }

  public void setDm(Calendar dm) {
    this.dm = dm;
  }

}
