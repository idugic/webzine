package rs.id.webzine.service.magazine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import rs.id.webzine.domain.magazine.Article;
import rs.id.webzine.domain.magazine.Category;
import rs.id.webzine.domain.util.Session;
import rs.id.webzine.service.GenericService;
import rs.id.webzine.service.ServiceException;
import rs.id.webzine.service.ServiceExceptionCode;

@Component
public class ArticleService extends GenericService<Article> {

  @Autowired
  CategoryService categoryService;

  @Autowired
  ArticleStatusService articleStatusService;

  public List<Category> findCategories(Integer articleId) {
    if (articleId == null) {
      return null;
    } else {
      TypedQuery<Category> query = entityManager().createQuery(
          "SELECT ac.category FROM ArticleCategory ac JOIN ac.article a WHERE a.id = :articleId", Category.class);
      query.setParameter("articleId", articleId);
      return query.getResultList();
    }
  }

  public List<Category> findAvailableCategoryList(Integer articleId) {
    if (articleId == null) {
      return null;
    } else {
      List<Category> list = new ArrayList<Category>();
      List<Category> articleCategoryList = findCategories(articleId);
      if (articleCategoryList.isEmpty()) {
        list = categoryService.findAll();
      } else {
        TypedQuery<Category> query = entityManager().createQuery(
            "SELECT c FROM Category c WHERE c NOT IN :articleCategoryList", Category.class);
        query.setParameter("articleCategoryList", articleCategoryList);
        list = query.getResultList();
      }
      return list;
    }
  }

  @Transactional
  public void publish(Integer id) {
    Article article = find(id);
    article.setStatus(articleStatusService.findForCd(ArticleStatusService.CD_PUBLISHED));

    article.setPublishedBy(Session.getCurrentUser());
    article.setPublishedAt(Calendar.getInstance());

    super.update(article);
  }

  @Transactional
  public void unpublish(Integer id) {
    Article article = find(id);
    article.setStatus(articleStatusService.findForCd(ArticleStatusService.CD_NEW));

    article.setPublishedBy(Session.getCurrentUser());
    article.setPublishedAt(Calendar.getInstance());

    super.update(article);
  }

  @Override
  @Transactional
  public void update(Article article) {
    if (ArticleStatusService.CD_PUBLISHED.equals(article.getStatus().getCd())) {
      throw new ServiceException(ServiceExceptionCode.OPERATION_NOT_AVAILABLE_ARTICLE_IS_PUBLISHED);
    }
    super.update(article);
  }

  @Override
  @Transactional
  public void delete(Integer id) {
    if (ArticleStatusService.CD_PUBLISHED.equals(find(id).getStatus().getCd())) {
      throw new ServiceException(ServiceExceptionCode.OPERATION_NOT_AVAILABLE_ARTICLE_IS_PUBLISHED);
    }
    super.delete(id);
  }
}
