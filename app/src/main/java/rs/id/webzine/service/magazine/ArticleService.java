package rs.id.webzine.service.magazine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import rs.id.webzine.entity.content_management.ManagedContent;
import rs.id.webzine.entity.magazine.Article;
import rs.id.webzine.service.GenericService;
import rs.id.webzine.service.ServiceException;
import rs.id.webzine.service.ServiceExceptionCode;
import rs.id.webzine.service.content_management.ManagedContentService;

@Component
public class ArticleService extends GenericService<Article> {

  @Autowired
  private ArticleStatusService articleStatusService;

  @Autowired
  private ManagedContentService managedContentService;

  @Override
  public List<Article> findAll() {
    List<Article> list = new ArrayList<Article>();
    TypedQuery<Article> query = entityManager().createQuery(
        "SELECT a FROM Article a JOIN a.status s WHERE s.cd != :cdDeleted", Article.class);
    query.setParameter("cdDeleted", ArticleStatusService.CD_DELETED);
    list = query.getResultList();
    return list;
  }

  @Override
  public List<Article> findForList(int firstResult, int maxResults) {
    List<Article> list = new ArrayList<Article>();
    TypedQuery<Article> query = entityManager()
        .createQuery("SELECT a FROM Article a JOIN a.status s WHERE s.cd != :cdDeleted", Article.class)
        .setFirstResult(firstResult).setMaxResults(maxResults);
    query.setParameter("cdDeleted", ArticleStatusService.CD_DELETED);
    list = query.getResultList();
    return list;
  }

  @Transactional
  public void publish(Integer id) {
    Article article = find(id);
    article.setStatus(articleStatusService.findForCd(ArticleStatusService.CD_PUBLISHED));

    article.setPublishedBy(currentUser());
    article.setPublishedAt(Calendar.getInstance());

    super.update(article);
  }

  @Transactional
  public void recall(Integer id) {
    Article article = find(id);
    article.setStatus(articleStatusService.findForCd(ArticleStatusService.CD_NEW));

    article.setPublishedBy(currentUser());
    article.setPublishedAt(Calendar.getInstance());

    super.update(article);
  }

  @Override
  @Transactional
  public void create(Article article) {
    ManagedContent managedContent = new ManagedContent();
    managedContentService.create(managedContent);

    article.setManagedContent(managedContent);
    article.setStatus(articleStatusService.findForCd(ArticleStatusService.CD_NEW));

    article.setUc(currentUser());
    article.setDc(Calendar.getInstance());

    super.create(article);
  }

  @Override
  @Transactional
  public void update(Article article) {
    if (ArticleStatusService.CD_PUBLISHED.equals(article.getStatus().getCd())) {
      throw new ServiceException(ServiceExceptionCode.OPERATION_NOT_AVAILABLE_ARTICLE_IS_PUBLISHED);
    }

    Article persistedArticle = find(article.getId());

    article.setManagedContent(persistedArticle.getManagedContent());

    // change abstract media only if provided
    byte[] abstractMedia = article.getAbstractMedia();
    if (abstractMedia == null || abstractMedia.length == 0) {
      article.setAbstractMedia(persistedArticle.getAbstractMedia());
      article.setAbstractMediaContentType(persistedArticle.getAbstractMediaContentType());
    }

    article.setUc(persistedArticle.getUc());
    article.setDc(persistedArticle.getDc());

    article.setUm(currentUser());
    article.setDm(Calendar.getInstance());

    super.update(article);
  }

  @Override
  @Transactional
  public void delete(Integer id) {
    if (ArticleStatusService.CD_PUBLISHED.equals(find(id).getStatus().getCd())) {
      throw new ServiceException(ServiceExceptionCode.OPERATION_NOT_AVAILABLE_ARTICLE_IS_PUBLISHED);
    }

    Article article = find(id);
    article.setStatus(articleStatusService.findForCd(ArticleStatusService.CD_DELETED));

    update(article);
  }

  @Transactional
  public void deleteAbstractMedia(Integer id) {
    if (ArticleStatusService.CD_PUBLISHED.equals(find(id).getStatus().getCd())) {
      throw new ServiceException(ServiceExceptionCode.OPERATION_NOT_AVAILABLE_ARTICLE_IS_PUBLISHED);
    }

    Article persistedArticle = find(id);
    persistedArticle.setAbstractMedia(null);
    persistedArticle.setAbstractMediaContentType(null);

    persistedArticle.setUc(persistedArticle.getUc());
    persistedArticle.setDc(persistedArticle.getDc());

    persistedArticle.setUm(currentUser());
    persistedArticle.setDm(Calendar.getInstance());

    super.update(persistedArticle);
  }

}
