package rs.id.webzine.domain;

import java.util.Calendar;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@Entity
@Table(schema = "ADMIN", name = "TASK_COMMENT")
public class TaskComment {

	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}

	@ManyToOne
	@JoinColumn(name = "TASK_ID", referencedColumnName = "ID", nullable = false)
	private Task taskId;

	@ManyToOne
	@JoinColumn(name = "UC", referencedColumnName = "ID", nullable = false)
	private User uc;

	@ManyToOne
	@JoinColumn(name = "UM", referencedColumnName = "ID")
	private User um;

	@Column(name = "TEXT", length = 250)
	@NotNull
	private String text;

	@Column(name = "DC")
	@NotNull
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

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Integer id;

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@PersistenceContext
	transient EntityManager entityManager;

	public static final EntityManager entityManager() {
		EntityManager em = new TaskComment().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countTaskComments() {
		return entityManager().createQuery(
				"SELECT COUNT(o) FROM TaskComment o", Long.class)
				.getSingleResult();
	}

	public static List<TaskComment> findAllTaskComments() {
		return entityManager().createQuery("SELECT o FROM TaskComment o",
				TaskComment.class).getResultList();
	}

	public static TaskComment findTaskComment(Integer id) {
		if (id == null)
			return null;
		return entityManager().find(TaskComment.class, id);
	}

	public static List<TaskComment> findTaskCommentEntries(int firstResult,
			int maxResults) {
		return entityManager()
				.createQuery("SELECT o FROM TaskComment o", TaskComment.class)
				.setFirstResult(firstResult).setMaxResults(maxResults)
				.getResultList();
	}

	@Transactional
	public void persist() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.persist(this);
	}

	@Transactional
	public void remove() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		if (this.entityManager.contains(this)) {
			this.entityManager.remove(this);
		} else {
			TaskComment attached = TaskComment.findTaskComment(this.id);
			this.entityManager.remove(attached);
		}
	}

	@Transactional
	public void flush() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.flush();
	}

	@Transactional
	public void clear() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		this.entityManager.clear();
	}

	@Transactional
	public TaskComment merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		TaskComment merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
	}
}
