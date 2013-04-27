package rs.id.webzine.service.content_management;

import java.util.List;

import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import rs.id.webzine.entity.content_management.Content;
import rs.id.webzine.service.GenericService;

@Component
public class ContentService extends GenericService<Content> {

  @Autowired
  ManagedContentService managedContentService;

  @Autowired
  ContentTypeService contentTypeService;

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

  @Transactional
  public synchronized void createTextContent(Integer managedContentId, String text, String description) {

    Content content = new Content();
    content.setManagedContent(managedContentService.find(managedContentId));

    content.setContentType(contentTypeService.findForCd(ContentTypeService.CD_TEXT));

    if (description == null || description.isEmpty()) {
      description = StringUtils.substring(text, 0, 32);
      if (text.length() > 32) {
        description = description + "...";
      }
    }
    content.setDescription(description);

    content.setOrderNo(getNextOrderNo(managedContentId));

    super.create(content);
  }

  @Transactional
  public synchronized void createMediaContent(Integer managedContentId, byte[] mediaContent, String mediaContentType,
      String fileName, String description) {

    Content content = new Content();
    content.setManagedContent(managedContentService.find(managedContentId));

    content.setContentType(contentTypeService.findForCd(ContentTypeService.CD_MEDIA));

    if (description == null || description.isEmpty()) {
      description = fileName;
    }
    content.setDescription(description);

    content.setOrderNo(getNextOrderNo(managedContentId));

    super.create(content);

  }

  @Transactional
  public synchronized void moveDown(Integer id) {
    Content c1 = find(id);

    List<Content> contentList = findForManagedContent(c1.getManagedContent().getId());

    // find content entity with minimal order number greater then starting
    // content entity order number
    Content c2 = null;
    for (Content content : contentList) {
      if (content.getOrderNo() > c1.getOrderNo() && !content.getId().equals(c1.getId())
          && (c2 == null || content.getOrderNo() < c2.getOrderNo())) {
        c2 = content;
      }
    }

    if (c2 != null) {
      // swap
      int c1OrderNo = c1.getOrderNo();
      int c2OrderNo = c2.getOrderNo();

      c2.setOrderNo(-1);
      update(c2);

      c1.setOrderNo(c2OrderNo);
      update(c1);

      c2.setOrderNo(c1OrderNo);
      update(c2);
    }
  }

  @Transactional
  public synchronized void moveUp(Integer id) {
    Content c1 = find(id);

    List<Content> contentList = findForManagedContent(c1.getManagedContent().getId());

    // find content entity with maximal order number lower then starting
    // content entity order number
    Content c2 = null;
    for (Content content : contentList) {
      if (content.getOrderNo() < c1.getOrderNo() && !content.getId().equals(c1.getId())
          && (c2 == null || content.getOrderNo() > c2.getOrderNo())) {
        c2 = content;
      }
    }

    if (c2 != null) {
      // swap
      int c1OrderNo = c1.getOrderNo();
      int c2OrderNo = c2.getOrderNo();

      c2.setOrderNo(-1);
      update(c2);

      c1.setOrderNo(c2OrderNo);
      update(c1);

      c2.setOrderNo(c1OrderNo);
      update(c2);
    }

  }

  private synchronized Integer getNextOrderNo(Integer managedContentId) {
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
