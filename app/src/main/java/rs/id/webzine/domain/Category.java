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
@Table(schema = "ADMIN", name = "CATEGORY")
public class Category extends IdEntity {

  public static long count() {
    return entityManager().createQuery("SELECT COUNT(o) FROM Category o", Long.class).getSingleResult();
  }

  public static List<Category> findAll() {
    return entityManager().createQuery("SELECT o FROM Category o", Category.class).getResultList();
  }

  public static Category find(Integer id) {
    if (id == null)
      return null;
    return entityManager().find(Category.class, id);
  }

  public static List<Category> findEntries(int firstResult, int maxResults) {
    return entityManager().createQuery("SELECT o FROM Category o", Category.class).setFirstResult(firstResult)
        .setMaxResults(maxResults).getResultList();
  }

  @ManyToOne
  @JoinColumn(name = "READER_TYPE_ID", referencedColumnName = "ID", nullable = false)
  private ReaderType readerTypeId;

  @Column(name = "NAME", length = 75)
  @NotNull
  private String name;

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

  public ReaderType getReaderTypeId() {
    return readerTypeId;
  }

  public void setReaderTypeId(ReaderType readerTypeId) {
    this.readerTypeId = readerTypeId;
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
}
