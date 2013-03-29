package rs.id.webzine.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
@Entity
@Table(schema = "ADMIN", name = "TASK_STATUS")
public class TaskStatus extends IdEntity {
	private static Log log = LogFactory.getLog(TaskStatus.class);

	@Column(name = "CD", length = 15, unique = true)
	@NotNull
	private String cd;

	@Column(name = "NAME", length = 50)
	@NotNull
	private String name;

	public String getCd() {
		return cd;
	}

	public void setCd(String cd) {
		this.cd = cd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static long count() {
		return entityManager().createQuery("SELECT COUNT(o) FROM TaskStatus o", Long.class).getSingleResult();
	}

	public static List<TaskStatus> findAll() {
		return entityManager().createQuery("SELECT o FROM TaskStatus o", TaskStatus.class).getResultList();
	}

	public static TaskStatus find(Integer id) {
		if (id == null)
			return null;
		return entityManager().find(TaskStatus.class, id);
	}

	public static List<TaskStatus> findEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM TaskStatus o", TaskStatus.class).setFirstResult(firstResult)
		        .setMaxResults(maxResults).getResultList();
	}

	public static TaskStatus findForCd(String cd) {
		TaskStatus taskStatus = null;

		try {
			if (cd != null) {
				TypedQuery<TaskStatus> query = entityManager().createQuery("SELECT o FROM TaskStatus o WHERE cd = :cd",
				        TaskStatus.class);
				query.setParameter("cd", cd);
				taskStatus = query.getSingleResult();
			}
		} catch (Exception e) {
			log.debug(e);
		}

		return taskStatus;
	}
}
