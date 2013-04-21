package rs.id.webzine.service.magazine;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import rs.id.webzine.domain.magazine.ArticleCategory;
import rs.id.webzine.service.GenericService;

@Component
public class ArticleCategoryService extends GenericService<ArticleCategory> {

  public List<ArticleCategory> findForArticle(Integer articleId) {
    if (articleId == null) {
      return null;
    } else {
      TypedQuery<ArticleCategory> query = entityManager().createQuery(
          "SELECT ac FROM ArticleCategory ac JOIN ac.article a WHERE a.id = :articleId", ArticleCategory.class);
      query.setParameter("articleId", articleId);
      return query.getResultList();
    }
  }
}
