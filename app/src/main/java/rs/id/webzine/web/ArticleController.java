package rs.id.webzine.web;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;
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

import rs.id.webzine.domain.Article;
import rs.id.webzine.domain.ArticleCategory;
import rs.id.webzine.domain.ArticleComment;
import rs.id.webzine.domain.ArticleCommentStatus;
import rs.id.webzine.domain.ArticleStatus;
import rs.id.webzine.domain.Category;
import rs.id.webzine.domain.Content;
import rs.id.webzine.domain.ContentType;
import rs.id.webzine.domain.ManagedContent;
import rs.id.webzine.domain.User;

@RequestMapping("/admin/article")
@Controller
public class ArticleController extends ModelController {
  private static final Log log = LogFactory.getLog(ArticleController.class);

  // TODO move content Up/Down in the content list

  // TODO hide/unhide content text, media file based on type, or do it in 2
  // forms: one for text, another for media

  // TODO all CDs as Strings!

  // TODO universal date format!

  // TODO no publish status in update

  // TODO no changes to published comment
  @InitBinder
  protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws ServletException {
    binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
  }

  @RequestMapping(params = "form", produces = "text/html")
  public String createForm(Model uiModel) {
    populateEditForm(uiModel, new Article());
    return "admin/article/create";
  }

  @RequestMapping(method = RequestMethod.POST, produces = "text/html")
  public String create(Article article, @RequestParam("abstractMedia") MultipartFile abstractMedia,
      BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    try {
      /*
       * workaround (multipart forms are always sent using POST)
       */
      if (httpServletRequest.getParameter("_method") != null
          && httpServletRequest.getParameter("_method").equals("PUT")) {
        return this.update(article, abstractMedia, bindingResult, uiModel, httpServletRequest);
      }

      if (bindingResult.hasErrors()) {
        populateEditForm(uiModel, article);
        return "admin/article/create";
      }

      ManagedContent managedContent = new ManagedContent();
      managedContent.persist();

      article.setManagedContentId(managedContent);
      article.setStatusId(ArticleStatus.findForCd(ArticleStatus.CD_SUBMITTED));
      article.setUc(getCurrentUser());
      article.setDc(Calendar.getInstance());
      article.setAbstractMedia(abstractMedia.getBytes());
      article.setAbstractMediaContentType(abstractMedia.getContentType());
      uiModel.asMap().clear();
      article.persist();
      return "redirect:/admin/article/" + encodeUrlPathSegment(article.getId().toString(), httpServletRequest);
    } catch (Exception e) {
      log.error(e);
      throw new RuntimeException(e);
    }
  }

  @RequestMapping(value = "/{id}", produces = "text/html")
  public String show(@PathVariable("id") Integer id, Model uiModel, HttpServletRequest httpServletRequest) {
    addDateTimeFormatPatterns(uiModel);
    Article article = Article.find(id);
    String mediaUrl = httpServletRequest.getContextPath() + "/admin/article/abstractMedia/"
        + encodeUrlPathSegment(id.toString(), httpServletRequest);
    article.setAbstractMediaUrl(mediaUrl);
    uiModel.addAttribute("article", article);
    uiModel.addAttribute("articleStatusList", ArticleStatus.findAll());
    uiModel.addAttribute("articleCategoryList", ArticleCategory.findForArticle(article.getId()));
    uiModel.addAttribute("articleCommentList", ArticleComment.findForArticle(article.getId()));
    uiModel.addAttribute("managedContent", article.getManagedContentId());
    uiModel.addAttribute("articleContentList", Content.findForManagedContent(article.getManagedContentId().getId()));
    uiModel.addAttribute("userList", User.findAll());
    uiModel.addAttribute("itemId", id);
    return "admin/article/show";
  }

  @RequestMapping(produces = "text/html")
  public String list(@RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    if (page != null || size != null) {
      int sizeNo = size == null ? 10 : size.intValue();
      final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
      uiModel.addAttribute("articleList", Article.findEntries(firstResult, sizeNo));
      float nrOfPages = (float) Article.count() / sizeNo;
      uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
          : nrOfPages));
    } else {
      uiModel.addAttribute("articleList", Article.findAll());
    }
    addDateTimeFormatPatterns(uiModel);
    return "admin/article/list";
  }

  @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
  public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
    populateEditForm(uiModel, Article.find(id));
    uiModel.addAttribute("articleCategoryList", ArticleCategory.findForArticle(id));
    uiModel.addAttribute("articleCommentList", ArticleComment.findForArticle(id));
    uiModel.addAttribute("articleCategory", new ArticleCategory());
    uiModel.addAttribute("managedContent", new ManagedContent());
    uiModel.addAttribute("articleComment", new ArticleComment());
    uiModel.addAttribute("content", new Content());
    return "admin/article/update";
  }

  @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
  public String update(Article article, @RequestParam("abstractMedia") MultipartFile abstractMedia,
      BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    try {
      if (bindingResult.hasErrors()) {
        populateEditForm(uiModel, article);
        uiModel.addAttribute("articleCategoryList", ArticleCategory.findForArticle(article.getId()));
        uiModel.addAttribute("articleCommentList", ArticleComment.findForArticle(article.getId()));
        uiModel.addAttribute("articleCategory", new ArticleCategory());
        uiModel.addAttribute("managedContent", new ManagedContent());
        uiModel.addAttribute("articleComment", new ArticleComment());

        uiModel.addAttribute("content", new Content());

        return "admin/article/update";
      }

      Article oldArticle = Article.find(article.getId());

      article.setManagedContentId(oldArticle.getManagedContentId());
      article.setUc(oldArticle.getUc());
      article.setDc(oldArticle.getDc());
      article.setUm(getCurrentUser());
      article.setDm(Calendar.getInstance());

      if (abstractMedia == null || abstractMedia.getBytes() == null || abstractMedia.getBytes().length == 0) {
        article.setAbstractMedia(oldArticle.getAbstractMedia());
        article.setAbstractMediaContentType(oldArticle.getAbstractMediaContentType());
      } else {
        article.setAbstractMedia((abstractMedia.getBytes()));
        article.setAbstractMediaContentType((abstractMedia.getContentType()));
      }

      uiModel.asMap().clear();
      article.merge();
      return "redirect:/admin/article/" + encodeUrlPathSegment(article.getId().toString(), httpServletRequest);
    } catch (Exception e) {
      log.error(e);
      throw new RuntimeException(e);
    }
  }

  @RequestMapping(value = "/publish", method = RequestMethod.PUT, produces = "text/html")
  public String publish(Article article, BindingResult bindingResult, Model uiModel,
      HttpServletRequest httpServletRequest) {
    if (bindingResult.hasErrors()) {
      populateEditForm(uiModel, article);
      uiModel.addAttribute("articleCategoryList", ArticleCategory.findForArticle(article.getId()));
      uiModel.addAttribute("articleCommentList", ArticleComment.findForArticle(article.getId()));
      uiModel.addAttribute("articleCategory", new ArticleCategory());
      uiModel.addAttribute("managedContent", new ManagedContent());
      uiModel.addAttribute("articleComment", new ArticleComment());
      uiModel.addAttribute("content", new Content());
      return "admin/article/update";
    }

    Article.publish(article.getId());
    return "redirect:/admin/article/" + encodeUrlPathSegment(article.getId().toString(), httpServletRequest);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
  public String delete(@PathVariable("id") Integer id, @RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    Article article = Article.find(id);
    article.remove();
    uiModel.asMap().clear();
    uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
    uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
    return "redirect:/admin/article";
  }

  void addDateTimeFormatPatterns(Model uiModel) {
    uiModel.addAttribute("article_publishedat_date_format",
        DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    uiModel.addAttribute("article_dc_date_format",
        DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    uiModel.addAttribute("article_dm_date_format",
        DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
  }

  void populateEditForm(Model uiModel, Article article) {
    uiModel.addAttribute("article", article);
    addDateTimeFormatPatterns(uiModel);
    uiModel.addAttribute("articleStatusList", ArticleStatus.findAll());
    uiModel.addAttribute("availableCategoryList", Category.findAvailableForArticle(article.getId()));
    uiModel.addAttribute("articleCategoryList", Category.findForArticle(article.getId()));
    uiModel.addAttribute("managedContent", article.getManagedContentId());
    uiModel.addAttribute("contentTypeList", ContentType.findAll());
    uiModel.addAttribute("articleContentList", Content.findForManagedContent(article.getManagedContentId().getId()));
  }

  @RequestMapping(value = "/abstractMedia/{id}", method = RequestMethod.GET)
  public String showImage(@PathVariable("id") Integer id, HttpServletResponse response, Model model) {
    try {
      Article article = Article.find(id);
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
      populateEditForm(uiModel, Article.find(articleId));
      uiModel.addAttribute("articleCategoryList", ArticleCategory.findForArticle(articleId));
      uiModel.addAttribute("articleCommentList", ArticleComment.findForArticle(articleId));
      uiModel.addAttribute("articleCategory", new ArticleCategory());
      uiModel.addAttribute("managedContent", new ManagedContent());
      uiModel.addAttribute("articleComment", new ArticleComment());
      uiModel.addAttribute("content", new Content());
      return "admin/article/update";
    }
    articleCategory.setArticleId(Article.find(articleId));
    articleCategory.setUc(getCurrentUser());
    articleCategory.setDc(Calendar.getInstance());
    articleCategory.persist();

    uiModel.asMap().clear();
    return "redirect:/admin/article/" + encodeUrlPathSegment(articleId.toString(), httpServletRequest);
  }

  @RequestMapping(value = "/comment/{articleId}", method = RequestMethod.POST, produces = "text/html")
  public String createArticleComment(@PathVariable("articleId") Integer articleId, ArticleComment articleComment,
      BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    if (bindingResult.hasErrors()) {
      populateEditForm(uiModel, Article.find(articleId));
      uiModel.addAttribute("articleCategoryList", ArticleCategory.findForArticle(articleId));
      uiModel.addAttribute("articleCommentList", ArticleComment.findForArticle(articleId));
      uiModel.addAttribute("articleCategory", new ArticleCategory());
      uiModel.addAttribute("managedContent", new ManagedContent());
      uiModel.addAttribute("articleComment", new ArticleComment());
      uiModel.addAttribute("content", new Content());
      return "admin/article/update";
    }
    articleComment.setStatusId(ArticleCommentStatus.findForCd(ArticleCommentStatus.CD_SUBMITTED));
    articleComment.setArticleId(Article.find(articleId));
    articleComment.setUc(getCurrentUser());
    articleComment.setDc(Calendar.getInstance());
    articleComment.persist();

    ArticleComment.publish(articleComment.getId());

    uiModel.asMap().clear();
    return "redirect:/admin/article/" + encodeUrlPathSegment(articleId.toString(), httpServletRequest);
  }

  @RequestMapping(value = "/category/{articleId}/{id}", method = RequestMethod.DELETE, produces = "text/html")
  public String deleteCategory(@PathVariable("articleId") Integer articleId, @PathVariable("id") Integer id,
      Model uiModel) {
    ArticleCategory articleCategory = ArticleCategory.find(id);
    articleCategory.remove();

    populateEditForm(uiModel, Article.find(articleId));
    uiModel.addAttribute("articleCategoryList", ArticleCategory.findForArticle(articleId));
    uiModel.addAttribute("articleCommentList", ArticleComment.findForArticle(articleId));
    uiModel.addAttribute("articleCategory", new ArticleCategory());
    uiModel.addAttribute("managedContent", new ManagedContent());
    uiModel.addAttribute("articleComment", new ArticleComment());
    uiModel.addAttribute("content", new Content());
    return "admin/article/update";
  }

  @RequestMapping(value = "/comment/{articleId}/{id}", method = RequestMethod.DELETE, produces = "text/html")
  public String deleteComment(@PathVariable("articleId") Integer articleId, @PathVariable("id") Integer id,
      Model uiModel) {
    ArticleComment articleComment = ArticleComment.find(id);
    // logically delete article
    articleComment.setStatusId(ArticleCommentStatus.findForCd(ArticleCommentStatus.CD_DELETED));
    articleComment.setUm(getCurrentUser());
    articleComment.setDm(Calendar.getInstance());
    articleComment.merge();

    populateEditForm(uiModel, Article.find(articleId));
    uiModel.addAttribute("articleCategoryList", ArticleCategory.findForArticle(articleId));
    uiModel.addAttribute("articleCommentList", ArticleComment.findForArticle(articleId));
    uiModel.addAttribute("articleCategory", new ArticleCategory());
    uiModel.addAttribute("managedContent", new ManagedContent());
    uiModel.addAttribute("articleComment", new ArticleComment());
    uiModel.addAttribute("content", new Content());
    return "admin/article/update";
  }

  @RequestMapping(value = "/managed_content/{articleId}", method = RequestMethod.PUT, produces = "text/html")
  public String updateManagedContent(@PathVariable("articleId") Integer articleId, ManagedContent managedContent,
      BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    try {
      if (bindingResult.hasErrors()) {
        populateEditForm(uiModel, Article.find(articleId));
        uiModel.addAttribute("articleCategoryList", ArticleCategory.findForArticle(articleId));
        uiModel.addAttribute("articleCommentList", ArticleComment.findForArticle(articleId));
        uiModel.addAttribute("articleCategory", new ArticleCategory());
        uiModel.addAttribute("managedContent", new ManagedContent());
        uiModel.addAttribute("articleComment", new ArticleComment());
        uiModel.addAttribute("content", new Content());
        return "admin/article/update";
      }

      Article article = Article.find(articleId);

      ManagedContent articleManagedContent = article.getManagedContentId();
      articleManagedContent.setCss(managedContent.getCss());

      uiModel.asMap().clear();
      articleManagedContent.merge();
      return "redirect:/admin/article/" + encodeUrlPathSegment(article.getId().toString(), httpServletRequest);
    } catch (Exception e) {
      log.error(e);
      throw new RuntimeException(e);
    }
  }

  @RequestMapping(value = "/content/{articleId}", method = RequestMethod.POST, produces = "text/html")
  public String createContent(@PathVariable("articleId") Integer articleId, Content content,
      @RequestParam("media") MultipartFile media, BindingResult bindingResult, Model uiModel,
      HttpServletRequest httpServletRequest) {
    try {
      if (bindingResult.hasErrors()) {
        populateEditForm(uiModel, Article.find(articleId));
        uiModel.addAttribute("articleCategoryList", ArticleCategory.findForArticle(articleId));
        uiModel.addAttribute("articleCommentList", ArticleComment.findForArticle(articleId));
        uiModel.addAttribute("articleCategory", new ArticleCategory());
        uiModel.addAttribute("managedContent", new ManagedContent());
        uiModel.addAttribute("articleComment", new ArticleComment());
        uiModel.addAttribute("content", new Content());
        return "admin/article/update";
      }

      Article article = Article.find(articleId);
      content.setManagedContentId(article.getManagedContentId());
      // TODO synchronize
      content.setOrderNo(Content.getNextOrderNoForManagedContent(article.getManagedContentId().getId()));
      if (media != null && media.getBytes() != null && media.getBytes().length > 0) {
        content.setMediaContentType(media.getContentType());
      }

      // description
      if (content.getDescription() == null || content.getDescription().isEmpty()) {
        if (content.getText() != null && !content.getText().isEmpty()) {
          content.setDescription(content.getText());
        } else {
          content.setDescription(media.getOriginalFilename());
        }
      }

      content.persist();

      uiModel.asMap().clear();
      return "redirect:/admin/article/" + encodeUrlPathSegment(articleId.toString(), httpServletRequest);
    } catch (Exception e) {
      log.error(e);
      throw new RuntimeException(e);
    }
  }

  @RequestMapping(value = "/content/{articleId}/{id}", method = RequestMethod.DELETE, produces = "text/html")
  public String deleteContent(@PathVariable("articleId") Integer articleId, @PathVariable("id") Integer id,
      Model uiModel) {
    Content content = Content.find(id);
    content.remove();

    populateEditForm(uiModel, Article.find(articleId));
    uiModel.addAttribute("articleCategoryList", ArticleCategory.findForArticle(articleId));
    uiModel.addAttribute("articleCommentList", ArticleComment.findForArticle(articleId));
    uiModel.addAttribute("articleCategory", new ArticleCategory());
    uiModel.addAttribute("managedContent", new ManagedContent());
    uiModel.addAttribute("articleComment", new ArticleComment());
    uiModel.addAttribute("content", new Content());
    return "admin/article/update";
  }

}
