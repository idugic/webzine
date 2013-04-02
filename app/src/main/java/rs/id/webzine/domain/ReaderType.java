package rs.id.webzine.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;

@Entity
@Table(schema = "ADMIN", name = "READER_TYPE")
@Configurable
public class ReaderType extends IdEntity {

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
    return entityManager().createQuery("SELECT COUNT(o) FROM ReaderType o", Long.class).getSingleResult();
  }

  public static List<ReaderType> findAll() {
    return entityManager().createQuery("SELECT o FROM ReaderType o", ReaderType.class).getResultList();
  }

  public static ReaderType find(Integer id) {
    if (id == null)
      return null;
    return entityManager().find(ReaderType.class, id);
  }

  public static List<ReaderType> findEntries(int firstResult, int maxResults) {
    return entityManager().createQuery("SELECT o FROM ReaderType o", ReaderType.class).setFirstResult(firstResult)
        .setMaxResults(maxResults).getResultList();
  }

}
