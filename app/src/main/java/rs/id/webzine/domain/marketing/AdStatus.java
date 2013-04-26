package rs.id.webzine.domain.marketing;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Configurable;

import rs.id.webzine.domain.IdEntity;

@Configurable
@Entity
@Table(schema = "ADMIN", name = "AD_STATUS")
public class AdStatus extends IdEntity {

  private static Log log = LogFactory.getLog(AdStatus.class);

  public static final String CD_SUBMITTED = "1";
  public static final String CD_PUBLISHED = "100";

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
    return entityManager().createQuery("SELECT COUNT(o) FROM AdStatus o", Long.class).getSingleResult();
  }

  public static List<AdStatus> findAll() {
    return entityManager().createQuery("SELECT o FROM AdStatus o", AdStatus.class).getResultList();
  }

  public static AdStatus find(Integer id) {
    if (id == null)
      return null;
    return entityManager().find(AdStatus.class, id);
  }

  public static List<AdStatus> findEntries(int firstResult, int maxResults) {
    return entityManager().createQuery("SELECT o FROM AdStatus o", AdStatus.class).setFirstResult(firstResult)
        .setMaxResults(maxResults).getResultList();
  }

  public static AdStatus findForCd(String cd) {
    AdStatus articleStatus = null;

    try {
      if (cd != null) {
        TypedQuery<AdStatus> query = entityManager().createQuery("SELECT o FROM AdStatus o WHERE cd = :cd",
            AdStatus.class);
        query.setParameter("cd", cd);
        articleStatus = query.getSingleResult();
      }
    } catch (Exception e) {
      log.debug(e);
    }

    return articleStatus;
  }
}
