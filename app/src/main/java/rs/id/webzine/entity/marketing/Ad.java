package rs.id.webzine.entity.marketing;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;

import rs.id.webzine.entity.content_management.ManagedContent;
import rs.id.webzine.entity.system.User;

@Entity
@Table(schema = "ADMIN", name = "AD")
@Configurable
public class Ad {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ID")
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "STATUS_ID", referencedColumnName = "ID")
  private AdStatus status;

  @ManyToOne
  @JoinColumn(name = "ADVERTISER_ID", referencedColumnName = "ID", nullable = false)
  private Advertiser advertiser;

  @Column(name = "NAME", length = 100)
  @NotNull
  private String name;

  @Column(name = "DESCRIPTION", length = 500)
  private String description;

  @ManyToOne
  @JoinColumn(name = "MANAGED_CONTENT_ID", referencedColumnName = "ID", nullable = false)
  private ManagedContent managedContent;

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

  public AdStatus getStatus() {
    return status;
  }

  public void setStatus(AdStatus status) {
    this.status = status;
  }

  public Advertiser getAdvertiser() {
    return advertiser;
  }

  public void setAdvertiser(Advertiser advertiser) {
    this.advertiser = advertiser;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ManagedContent getManagedContent() {
    return managedContent;
  }

  public void setManagedContent(ManagedContent managedContent) {
    this.managedContent = managedContent;
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
