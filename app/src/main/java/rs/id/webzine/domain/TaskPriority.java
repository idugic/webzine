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
@Table(schema = "ADMIN", name = "TASK_PRIORITY")
public class TaskPriority extends IdEntity {
	private static Log log = LogFactory.getLog(TaskPriority.class);

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
		return entityManager().createQuery("SELECT COUNT(o) FROM TaskPriority o", Long.class).getSingleResult();
	}

	public static List<TaskPriority> findAll() {
		return entityManager().createQuery("SELECT o FROM TaskPriority o", TaskPriority.class).getResultList();
	}

	public static TaskPriority find(Integer id) {
		if (id == null)
			return null;
		return entityManager().find(TaskPriority.class, id);
	}

	public static List<TaskPriority> findEntries(int firstResult, int maxResults) {
		return entityManager().createQuery("SELECT o FROM TaskPriority o", TaskPriority.class)
		        .setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
	}

	public static TaskPriority findForCd(String cd) {
		TaskPriority taskPriority = null;

		try {
			if (cd != null) {
				TypedQuery<TaskPriority> query = entityManager().createQuery(
				        "SELECT o FROM TaskPriority o WHERE cd = :cd", TaskPriority.class);
				query.setParameter("cd", cd);
				taskPriority = query.getSingleResult();
			}
		} catch (Exception e) {
			log.debug(e);
		}

		return taskPriority;
	}
}
