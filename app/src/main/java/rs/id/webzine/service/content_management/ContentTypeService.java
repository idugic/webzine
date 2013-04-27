package rs.id.webzine.service.content_management;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import rs.id.webzine.entity.content_management.ContentType;
import rs.id.webzine.service.GenericService;

@Component
public class ContentTypeService extends GenericService<ContentType> {

  public static final String CD_TEXT = "text";

  public static final String CD_MEDIA = "media";

  public ContentType findForCd(String cd) {
    ContentType contentType = null;

    if (cd != null) {
      TypedQuery<ContentType> query = entityManager().createQuery("SELECT o FROM ContentType o WHERE o.cd = :cd",
          ContentType.class);
      query.setParameter("cd", cd);
      List<ContentType> contentTypeList = query.getResultList();
      if (!contentTypeList.isEmpty()) {
        contentType = contentTypeList.get(0);
      }
    }

    return contentType;
  }
}
