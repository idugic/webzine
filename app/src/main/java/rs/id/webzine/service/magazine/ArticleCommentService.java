package rs.id.webzine.service.magazine;

import java.util.Calendar;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import rs.id.webzine.entity.magazine.ArticleComment;
import rs.id.webzine.service.GenericService;

@Component
public class ArticleCommentService extends GenericService<ArticleComment> {

  public static final String CD_NEW = "new";

  public static final String CD_ACCEPTED = "accepted";

  public static final String CD_REJECTED = "rejected";

  public static final String CD_ARCHIVED = "archived";

  @Autowired
  ArticleCommentStatusService articleCommentStatusService;

  @Override
  @Transactional
  public void create(ArticleComment articleComment) {

    articleComment.setStatus(articleCommentStatusService.findForCd(ArticleCommentStatusService.CD_NEW));

    articleComment.setUc(currentUser());
    articleComment.setDc(Calendar.getInstance());

    super.create(articleComment);
  }

  @Transactional
  public void reject(Integer id) {
    ArticleComment articleComment = find(id);
    articleComment.setStatus(articleCommentStatusService.findForCd(ArticleCommentStatusService.CD_REJECTED));

    articleComment.setUm(currentUser());
    articleComment.setDm(Calendar.getInstance());

    super.delete(id);
  }

  @Transactional
  public void accept(Integer id) {
    ArticleComment articleComment = find(id);
    articleComment.setStatus(articleCommentStatusService.findForCd(ArticleCommentStatusService.CD_ACCEPTED));

    articleComment.setUm(currentUser());
    articleComment.setDm(Calendar.getInstance());

    super.delete(id);
  }

  public List<ArticleComment> findForArticle(Integer articleId) {
    if (articleId == null) {
      return null;
    } else {
      TypedQuery<ArticleComment> query = entityManager().createQuery(
          "SELECT ac FROM ArticleComment ac JOIN ac.article a WHERE a.id = :articleId", ArticleComment.class);
      query.setParameter("articleId", articleId);
      return query.getResultList();
    }
  }

}
