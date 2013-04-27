package rs.id.webzine.controller.magazine;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import rs.id.webzine.controller.WebController;
import rs.id.webzine.domain.magazine.Article;
import rs.id.webzine.domain.magazine.ArticleCategory;
import rs.id.webzine.domain.magazine.ArticleComment;
import rs.id.webzine.service.magazine.ArticleCategoryService;
import rs.id.webzine.service.magazine.ArticleCommentService;
import rs.id.webzine.service.magazine.ArticleService;
import rs.id.webzine.service.magazine.ArticleStatusService;

@RequestMapping(ArticleController.PATH)
@Controller
public class ArticleController extends WebController {
  private static final Log log = LogFactory.getLog(ArticleController.class);

  public static final String PATH = "admin/magazine/article";

  @Autowired
  private ArticleService articleService;

  @Autowired
  private ArticleStatusService articleStatusService;

  @Autowired
  private ArticleCategoryService articleCategoryService;

  @Autowired
  private ArticleCommentService articleCommentService;

  @InitBinder
  protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws ServletException {
    binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
  }

  @RequestMapping(produces = "text/html")
  public String list(@RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    prepareList(articleService, "articleList", page, size, uiModel);
    return PATH + "/" + LIST;
  }

  @RequestMapping(params = "form", produces = "text/html")
  public String createForm(Model uiModel, HttpServletRequest httpServletRequest) {
    populateEditForm(uiModel, new Article(), httpServletRequest, true);
    return PATH + "/" + CREATE;
  }

  void populateEditForm(Model uiModel, Article article, HttpServletRequest httpServletRequest, boolean create) {
    addDateTimePattern(uiModel);
    uiModel.addAttribute("article", article);
    uiModel.addAttribute("articleStatusList", articleStatusService.findAll());

    if (!create) {
      //
      if (article.getAbstractMedia() != null && article.getAbstractMedia().length != 0) {
        String mediaUrl = httpServletRequest.getContextPath() + "/" + PATH + "/" + ABSTRACT_MEDIA + "/"
            + encodeUrlPathSegment(article.getId().toString(), httpServletRequest);
        uiModel.addAttribute("abstractMediaUrl", mediaUrl);
        uiModel.addAttribute("abstractMediaContentType", article.getAbstractMediaContentType());
        article.setAbstractMedia(null);
        uiModel.addAttribute("itemId", article.getId());
      }

      uiModel.addAttribute("abstractMediaForm", article);
    }
  }

  @RequestMapping(method = RequestMethod.POST, produces = "text/html")
  public String create(Article article, @RequestParam("abstractMedia") MultipartFile abstractMedia,
      BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {

    try {
      /*
       * workaround (file is always sent as POST)
       */
      if (httpServletRequest.getParameter("_method") != null
          && httpServletRequest.getParameter("_method").equals("PUT")) {
        return this.update(article, abstractMedia, bindingResult, uiModel, httpServletRequest);
      }

      if (bindingResult.hasErrors()) {
        populateEditForm(uiModel, article, httpServletRequest, true);
        return PATH + "/" + CREATE;
      }

      article.setAbstractMedia(abstractMedia.getBytes());
      article.setAbstractMediaContentType(abstractMedia.getContentType());

      articleService.create(article);

      uiModel.asMap().clear();
      return REDIRECT + PATH + "/" + encodeUrlPathSegment(article.getId().toString(), httpServletRequest);
    } catch (IOException e) {
      log.error(e);
      throw new RuntimeException(e);
    }
  }

  @RequestMapping(value = "/{id}", produces = "text/html")
  public String show(@PathVariable("id") Integer id, Model uiModel, HttpServletRequest httpServletRequest) {
    populateShowForm(uiModel, id, httpServletRequest);
    return PATH + "/" + SHOW;
  }

  private void populateShowForm(Model uiModel, Integer id, HttpServletRequest httpServletRequest) {
    addDateTimePattern(uiModel);

    Article article = articleService.find(id);
    uiModel.addAttribute("article", article);

    if (article.getAbstractMedia() != null && article.getAbstractMedia().length != 0) {
      String mediaUrl = httpServletRequest.getContextPath() + "/" + PATH + "/" + ABSTRACT_MEDIA + "/"
          + encodeUrlPathSegment(article.getId().toString(), httpServletRequest);
      uiModel.addAttribute("abstractMediaUrl", mediaUrl);
      uiModel.addAttribute("abstractMediaContentType", article.getAbstractMediaContentType());
      article.setAbstractMedia(null);
      uiModel.addAttribute("itemId", article.getId());
    }

    uiModel.addAttribute("statusList", articleStatusService.findAll());
    uiModel.addAttribute("availableCategoryList", articleCategoryService.findAvailableCategoryList(article.getId()));
    uiModel.addAttribute("articleCategoryList", articleCategoryService.findForArticle(article.getId()));
    uiModel.addAttribute("articleCommentList", articleCommentService.findForArticle(article.getId()));

    uiModel.addAttribute("articleCategory", new ArticleCategory());
    uiModel.addAttribute("articleComment", new ArticleComment());

    uiModel.addAttribute("itemId", id);
  }

  @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
  public String updateForm(@PathVariable("id") Integer id, Model uiModel, HttpServletRequest httpServletRequest) {
    populateEditForm(uiModel, articleService.find(id), httpServletRequest, false);
    return PATH + "/" + UPDATE;
  }

  @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
  public String update(Article article, @RequestParam("abstractMedia") MultipartFile abstractMedia,
      BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {

    try {
      if (bindingResult.hasErrors()) {
        populateEditForm(uiModel, article, httpServletRequest, false);
        return PATH + "/" + UPDATE;
      }

      article.setAbstractMedia(abstractMedia.getBytes());
      article.setAbstractMediaContentType(abstractMedia.getContentType());

      articleService.update(article);

      uiModel.asMap().clear();
      return REDIRECT + PATH + "/" + encodeUrlPathSegment(article.getId().toString(), httpServletRequest);
    } catch (IOException e) {
      log.error(e);
      throw new RuntimeException(e);
    }
  }

  @RequestMapping(value = "/publish", method = RequestMethod.PUT, produces = "text/html")
  public String publish(Article article, BindingResult bindingResult, Model uiModel,
      HttpServletRequest httpServletRequest) {
    if (bindingResult.hasErrors()) {
      populateEditForm(uiModel, article, httpServletRequest, false);
      return PATH + "/" + UPDATE;
    }

    articleService.publish(article.getId());

    uiModel.asMap().clear();
    return REDIRECT + PATH + "/" + encodeUrlPathSegment(article.getId().toString(), httpServletRequest);
  }

  @RequestMapping(value = "/recall", method = RequestMethod.PUT, produces = "text/html")
  public String recall(Article article, BindingResult bindingResult, Model uiModel,
      HttpServletRequest httpServletRequest) {
    if (bindingResult.hasErrors()) {
      populateEditForm(uiModel, article, httpServletRequest, false);
      return PATH + "/" + UPDATE;
    }

    articleService.recall(article.getId());

    uiModel.asMap().clear();
    return REDIRECT + PATH + "/" + encodeUrlPathSegment(article.getId().toString(), httpServletRequest);
  }

  @RequestMapping(value = "/abstract_media/{id}", method = RequestMethod.DELETE, produces = "text/html")
  public String deleteAbstractMedia(@PathVariable("id") Integer id, Model uiModel, HttpServletRequest httpServletRequest) {
    articleService.deleteAbstractMedia(id);

    uiModel.asMap().clear();
    return REDIRECT + PATH + "/" + encodeUrlPathSegment(id.toString(), httpServletRequest);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
  public String delete(@PathVariable("id") Integer id, Model uiModel) {
    articleService.delete(id);

    uiModel.asMap().clear();
    return REDIRECT + PATH;
  }

  @RequestMapping(value = "/abstract_media/{id}", method = RequestMethod.GET)
  public String image(@PathVariable("id") Integer id, HttpServletResponse response, Model model) {
    try {
      Article article = articleService.find(id);

      if (article.getAbstractMedia() != null && article.getAbstractMedia().length > 0) {
        response.setHeader("Content-Disposition", "inline;");
        response.setContentType(article.getAbstractMediaContentType());

        OutputStream out = response.getOutputStream();
        IOUtils.copy(new ByteArrayInputStream(article.getAbstractMedia()), out);
        out.flush();
      }

      return null;
    } catch (Exception e) {
      log.error(e);
      throw new RuntimeException(e);
    }
  }

  @RequestMapping(value = "/category/{articleId}", method = RequestMethod.POST, produces = "text/html")
  public String createArticleCategory(@PathVariable("articleId") Integer articleId, ArticleCategory articleCategory,
      BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    if (bindingResult.hasErrors()) {
      populateShowForm(uiModel, articleId, httpServletRequest);
      return PATH + "/" + SHOW;
    }

    articleCategory.setArticle(articleService.find(articleId));
    articleCategoryService.create(articleCategory);

    uiModel.asMap().clear();
    return REDIRECT + PATH + "/" + encodeUrlPathSegment(articleId.toString(), httpServletRequest);
  }

  @RequestMapping(value = "/category/{articleId}/{id}", method = RequestMethod.DELETE, produces = "text/html")
  public String deleteCategory(@PathVariable("articleId") Integer articleId, @PathVariable("id") Integer id,
      Model uiModel, HttpServletRequest httpServletRequest) {
    articleCategoryService.delete(id);

    uiModel.asMap().clear();
    return REDIRECT + PATH + "/" + encodeUrlPathSegment(articleId.toString(), httpServletRequest);
  }

  @RequestMapping(value = "/comment/{articleId}", method = RequestMethod.POST, produces = "text/html")
  public String createComment(@PathVariable("articleId") Integer articleId, ArticleComment articleComment,
      BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    if (bindingResult.hasErrors()) {
      populateShowForm(uiModel, articleId, httpServletRequest);
      return PATH + "/" + SHOW;
    }

    articleComment.setArticle(articleService.find(articleId));
    articleCommentService.create(articleComment);

    uiModel.asMap().clear();
    return REDIRECT + PATH + "/" + encodeUrlPathSegment(articleId.toString(), httpServletRequest);
  }

  @RequestMapping(value = "/comment/{articleId}/decline/{id}", produces = "text/html")
  public String declineComment(@PathVariable("articleId") Integer articleId, @PathVariable("id") Integer id,
      Model uiModel, HttpServletRequest httpServletRequest) {
    articleCommentService.decline(id);

    uiModel.asMap().clear();
    return REDIRECT + PATH + "/" + encodeUrlPathSegment(articleId.toString(), httpServletRequest);
  }

  @RequestMapping(value = "/comment/{articleId}/accept/{id}", produces = "text/html")
  public String acceptComment(@PathVariable("articleId") Integer articleId, @PathVariable("id") Integer id,
      Model uiModel, HttpServletRequest httpServletRequest) {
    articleCommentService.accept(id);

    uiModel.asMap().clear();
    return REDIRECT + PATH + "/" + encodeUrlPathSegment(articleId.toString(), httpServletRequest);
  }

}
