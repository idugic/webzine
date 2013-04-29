package rs.id.webzine.service.marketing;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import rs.id.webzine.entity.content_management.ManagedContent;
import rs.id.webzine.entity.marketing.Ad;
import rs.id.webzine.service.GenericService;
import rs.id.webzine.service.ServiceException;
import rs.id.webzine.service.ServiceExceptionCode;
import rs.id.webzine.service.content_management.ManagedContentService;

@Component
public class AdService extends GenericService<Ad> {

  @Autowired
  private AdStatusService adStatusService;

  @Autowired
  private ManagedContentService managedContentService;

  @Override
  public List<Ad> findAll() {
    List<Ad> list = new ArrayList<Ad>();
    TypedQuery<Ad> query = entityManager().createQuery("SELECT a FROM Ad a JOIN a.status s WHERE s.cd != :cdDeleted",
        Ad.class);
    query.setParameter("cdDeleted", AdStatusService.CD_DELETED);
    list = query.getResultList();
    return list;
  }

  @Override
  public List<Ad> findForList(int firstResult, int maxResults) {
    List<Ad> list = new ArrayList<Ad>();
    TypedQuery<Ad> query = entityManager()
        .createQuery("SELECT a FROM Ad a JOIN a.status s WHERE s.cd != :cdDeleted", Ad.class)
        .setFirstResult(firstResult).setMaxResults(maxResults);
    query.setParameter("cdDeleted", AdStatusService.CD_DELETED);
    list = query.getResultList();
    return list;
  }

  @Transactional
  public void publish(Integer id) {
    Ad ad = find(id);
    ad.setStatus(adStatusService.findForCd(AdStatusService.CD_PUBLISHED));

    ad.setPublishedBy(currentUser());
    ad.setPublishedAt(Calendar.getInstance());

    super.update(ad);
  }

  @Transactional
  public void recall(Integer id) {
    Ad ad = find(id);
    ad.setStatus(adStatusService.findForCd(AdStatusService.CD_NEW));

    ad.setPublishedBy(currentUser());
    ad.setPublishedAt(Calendar.getInstance());

    super.update(ad);
  }

  @Override
  @Transactional
  public void create(Ad ad) {
    ManagedContent managedContent = new ManagedContent();
    managedContentService.create(managedContent);

    ad.setManagedContent(managedContent);
    ad.setStatus(adStatusService.findForCd(AdStatusService.CD_NEW));

    ad.setUc(currentUser());
    ad.setDc(Calendar.getInstance());

    super.create(ad);
  }

  @Override
  @Transactional
  public void update(Ad ad) {
    if (AdStatusService.CD_PUBLISHED.equals(ad.getStatus().getCd())) {
      throw new ServiceException(ServiceExceptionCode.OPERATION_NOT_AVAILABLE_ENTITY_IS_PUBLISHED);
    }

    Ad persistedAd = find(ad.getId());

    ad.setManagedContent(persistedAd.getManagedContent());

    ad.setUc(persistedAd.getUc());
    ad.setDc(persistedAd.getDc());

    ad.setUm(currentUser());
    ad.setDm(Calendar.getInstance());

    super.update(ad);
  }

  @Override
  @Transactional
  public void delete(Integer id) {
    if (AdStatusService.CD_PUBLISHED.equals(find(id).getStatus().getCd())) {
      throw new ServiceException(ServiceExceptionCode.OPERATION_NOT_AVAILABLE_ENTITY_IS_PUBLISHED);
    }

    Ad ad = find(id);
    ad.setStatus(adStatusService.findForCd(AdStatusService.CD_DELETED));

    update(ad);
  }

}
