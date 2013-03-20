package rs.id.webzine.domain;

import java.util.Calendar;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(schema = "ADMIN", name = "TASK")
public class Task {

	@PersistenceContext
	transient EntityManager entityManager;

	public static final EntityManager entityManager() {
		EntityManager em = new Task().entityManager;
		if (em == null)
			throw new IllegalStateException(
					"Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
		return em;
	}

	public static long countTasks() {
		return entityManager().createQuery("SELECT COUNT(o) FROM Task o",
				Long.class).getSingleResult();
	}

	public static List<Task> findAllTasks() {
		return entityManager().createQuery("SELECT o FROM Task o", Task.class)
				.getResultList();
	}

	public static Task findTask(Integer id) {
		if (id == null)
			return null;
		return entityManager().find(Task.class, id);
	}

	public static List<Task> findTaskEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM Task o", Task.class)
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
			Task attached = Task.findTask(this.id);
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
	public Task merge() {
		if (this.entityManager == null)
			this.entityManager = entityManager();
		Task merged = this.entityManager.merge(this);
		this.entityManager.flush();
		return merged;
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

	@OneToMany(mappedBy = "parentTaskId")
	private Set<Task> tasks;

	@OneToMany(mappedBy = "taskId")
	private Set<TaskAttachment> taskAttachments;

	@OneToMany(mappedBy = "taskId")
	private Set<TaskComment> taskComments;

	@ManyToOne
	@JoinColumn(name = "PARENT_TASK_ID", referencedColumnName = "ID", insertable = false, updatable = false)
	private Task parentTaskId;

	@ManyToOne
	@JoinColumn(name = "UC", referencedColumnName = "ID", nullable = false)
	private Users uc;

	@ManyToOne
	@JoinColumn(name = "OWNER_USER_ID", referencedColumnName = "ID")
	private Users ownerUserId;

	@ManyToOne
	@JoinColumn(name = "UM", referencedColumnName = "ID")
	private Users um;

	@Column(name = "STATUS")
	@NotNull
	private Integer status;

	@Column(name = "PRIORITY")
	@NotNull
	private Integer priority;

	@Column(name = "TITLE", length = 250)
	@NotNull
	private String title;

	@Column(name = "DESCRIPTION", length = 1000)
	private String description;

	@Column(name = "DC")
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "MM")
	private Calendar dc;

	@Column(name = "DM")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "MM")
	private Calendar dm;

	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}

	public Set<TaskAttachment> getTaskAttachments() {
		return taskAttachments;
	}

	public void setTaskAttachments(Set<TaskAttachment> taskAttachments) {
		this.taskAttachments = taskAttachments;
	}

	public Set<TaskComment> getTaskComments() {
		return taskComments;
	}

	public void setTaskComments(Set<TaskComment> taskComments) {
		this.taskComments = taskComments;
	}

	public Task getParentTaskId() {
		return parentTaskId;
	}

	public void setParentTaskId(Task parentTaskId) {
		this.parentTaskId = parentTaskId;
	}

	public Users getUc() {
		return uc;
	}

	public void setUc(Users uc) {
		this.uc = uc;
	}

	public Users getOwnerUserId() {
		return ownerUserId;
	}

	public void setOwnerUserId(Users ownerUserId) {
		this.ownerUserId = ownerUserId;
	}

	public Users getUm() {
		return um;
	}

	public void setUm(Users um) {
		this.um = um;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String toString() {
		return ReflectionToStringBuilder.toString(this,
				ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
