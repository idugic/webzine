package rs.id.webzine.service.marketing;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import rs.id.webzine.domain.magazine.Article;
import rs.id.webzine.domain.marketing.AdArticle;
import rs.id.webzine.service.GenericService;
import rs.id.webzine.service.magazine.ArticleService;

@Component
public class AdArticleService extends GenericService<AdArticle> {

  @Autowired
  ArticleService articleService;

  public List<AdArticle> findForAd(Integer adId) {
    if (adId == null) {
      return null;
    } else {
      TypedQuery<AdArticle> query = entityManager().createQuery(
          "SELECT aa FROM AdArticle aa JOIN aa.adId a WHERE a.id = :adId", AdArticle.class);
      query.setParameter("adId", adId);

      return query.getResultList();
    }
  }

  public List<Article> findAvailableArticleList(Integer adId) {
    if (adId == null) {
      return null;
    } else {
      List<Article> list = new ArrayList<Article>();
      List<AdArticle> adArticleList = findForAd(adId);
      if (adArticleList.isEmpty()) {
        list = articleService.findAll();
      } else {
        TypedQuery<Article> query = entityManager().createQuery(
            "SELECT aa.article FROM AdArticle aa WHERE aa NOT IN :adArticleList", Article.class);
        query.setParameter("adArticleList", adArticleList);

        list = query.getResultList();
      }

      return list;
    }
  }

}
