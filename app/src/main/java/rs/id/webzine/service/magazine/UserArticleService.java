package rs.id.webzine.service.magazine;

import org.springframework.stereotype.Component;

import rs.id.webzine.domain.magazine.UserArticle;
import rs.id.webzine.service.GenericService;

@Component
public class UserArticleService extends GenericService<UserArticle> {

  public static final String CD_NEW = "new";

  public static final String CD_PUBLISHED = "published";

  public static final String CD_DELETED = "deleted";

}
