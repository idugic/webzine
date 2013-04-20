package rs.id.webzine.service.content_management;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import rs.id.webzine.domain.content_management.Content;
import rs.id.webzine.service.GenericService;

@Component
public class ContentService extends GenericService<Content> {

  public List<Content> findForManagedContent(Integer managedContentId) {
    if (managedContentId == null) {
      return null;
    } else {
      TypedQuery<Content> query = entityManager().createQuery(
          "SELECT c FROM Content c JOIN c.managedContent mc WHERE mc.id = :managedContentId ORDER BY c.orderNo",
          Content.class);
      query.setParameter("managedContentId", managedContentId);
      return query.getResultList();
    }
  }

  // TODO synchronize
  private Integer getNextOrderNoForManagedContent(Integer managedContentId) {
    if (findForManagedContent(managedContentId).isEmpty()) {
      return 1;
    } else {
      TypedQuery<Integer> query = entityManager().createQuery(
          "SELECT max(c.orderNo) FROM Content c  JOIN c.managedContent mc WHERE mc.id = :managedContentId",
          Integer.class);
      query.setParameter("managedContentId", managedContentId);
      Integer maxOrderNo = query.getSingleResult();
      return ++maxOrderNo;
    }
  }
}
