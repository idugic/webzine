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
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;

@Configurable
@Entity
@Table(schema = "ADMIN", name = "TASK")
public class Task extends IdEntity {

	@ManyToOne
	@JoinColumn(name = "STATUS_ID", referencedColumnName = "ID", nullable = false)
	private TaskStatus statusId;

	@ManyToOne
	@JoinColumn(name = "PRIORITY_ID", referencedColumnName = "ID", nullable = false)
	private TaskPriority priorityId;

	@Column(name = "TITLE", length = 250)
	@NotNull
	private String title;

	@Column(name = "DESCRIPTION", length = 1000)
	private String description;

	@ManyToOne
	@JoinColumn(name = "OWNER_USER_ID", referencedColumnName = "ID")
	private User ownerUserId;

	@ManyToOne
	@JoinColumn(name = "PARENT_TASK_ID", referencedColumnName = "ID", insertable = false, updatable = false)
	private Task parentTaskId;

	@ManyToOne
	@JoinColumn(name = "UC", referencedColumnName = "ID", nullable = false)
	private User uc;

	@Column(name = "DC")
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "MM")
	private Calendar dc;

	@ManyToOne
	@JoinColumn(name = "UM", referencedColumnName = "ID")
	private User um;

	@Column(name = "DM")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "MM")
	private Calendar dm;

	public TaskStatus getStatusId() {
		return statusId;
	}

	public void setStatusId(TaskStatus statusId) {
		this.statusId = statusId;
	}

	public TaskPriority getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(TaskPriority priorityId) {
		this.priorityId = priorityId;
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

	public User getOwnerUserId() {
		return ownerUserId;
	}

	public void setOwnerUserId(User ownerUserId) {
		this.ownerUserId = ownerUserId;
	}

	public Task getParentTaskId() {
		return parentTaskId;
	}

	public void setParentTaskId(Task parentTaskId) {
		this.parentTaskId = parentTaskId;
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

	public static long count() {
		return entityManager().createQuery("SELECT COUNT(o) FROM Task o", Long.class).getSingleResult();
	}

	public static List<Task> findAll() {
		return entityManager().createQuery("SELECT o FROM Task o", Task.class).getResultList();
	}

	public static Task find(Integer id) {
		if (id == null)
			return null;
		return entityManager().find(Task.class, id);
	}

	public static List<Task> findEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM Task o", Task.class).setFirstResult(firstResult)
		        .setMaxResults(maxResults).getResultList();
	}

}
