package rs.id.webzine.service.magazine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import rs.id.webzine.entity.magazine.ArticleCategory;
import rs.id.webzine.entity.magazine.Category;
import rs.id.webzine.service.GenericService;

@Component
public class ArticleCategoryService extends GenericService<ArticleCategory> {

  @Autowired
  private CategoryService categoryService;

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

  public List<Category> findCategoryListForArticle(Integer articleId) {
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
      List<Category> categoryList = findCategoryListForArticle(articleId);
      if (categoryList.isEmpty()) {
        list = categoryService.findAll();
      } else {
        TypedQuery<Category> query = entityManager().createQuery(
            "SELECT c FROM Category c WHERE c NOT IN :categoryList", Category.class);
        query.setParameter("categoryList", categoryList);
        list = query.getResultList();
      }
      return list;
    }
  }

  @Override
  @Transactional
  public void create(ArticleCategory articleCategory) {

    articleCategory.setUc(currentUser());
    articleCategory.setDc(Calendar.getInstance());

    super.create(articleCategory);
  }
}
