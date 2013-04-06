package rs.id.webzine.web;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import rs.id.webzine.domain.Article;
import rs.id.webzine.domain.ArticleComment;
import rs.id.webzine.domain.ArticleCommentStatus;
import rs.id.webzine.domain.User;

@RequestMapping("/admin/article_comment")
@Controller
public class ArticleCommentController extends ModelController {

  // TODO no publish status in update
  
  // TODO no changes to published comment

  @RequestMapping(method = RequestMethod.POST, produces = "text/html")
  public String create(ArticleComment articleComment, BindingResult bindingResult, Model uiModel,
      HttpServletRequest httpServletRequest) {
    if (bindingResult.hasErrors()) {
      populateEditForm(uiModel, articleComment);
      return "admin/article_comment/create";
    }
    uiModel.asMap().clear();
    articleComment.setUc(getCurrentUser());
    articleComment.setDc(Calendar.getInstance());
    articleComment.setStatusId(ArticleCommentStatus.findForCd(ArticleCommentStatus.CD_SUBMITTED));
    articleComment.persist();
    return "redirect:/admin/article_comment/"
        + encodeUrlPathSegment(articleComment.getId().toString(), httpServletRequest);
  }

  @RequestMapping(params = "form", produces = "text/html")
  public String createForm(Model uiModel) {
    populateEditForm(uiModel, new ArticleComment());
    return "admin/article_comment/create";
  }

  @RequestMapping(value = "/{id}", produces = "text/html")
  public String show(@PathVariable("id") Integer id, Model uiModel) {
    addDateTimeFormatPatterns(uiModel);
    uiModel.addAttribute("articleComment", ArticleComment.find(id));
    uiModel.addAttribute("itemId", id);
    return "admin/article_comment/show";
  }

  @RequestMapping(produces = "text/html")
  public String list(@RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    if (page != null || size != null) {
      int sizeNo = size == null ? 10 : size.intValue();
      final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
      uiModel.addAttribute("articleCommentList", ArticleComment.findEntries(firstResult, sizeNo));
      float nrOfPages = (float) ArticleComment.count() / sizeNo;
      uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
          : nrOfPages));
    } else {
      uiModel.addAttribute("articleCommentList", ArticleComment.findAll());
    }
    addDateTimeFormatPatterns(uiModel);
    return "admin/article_comment/list";
  }

  @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
  public String update(ArticleComment articleComment, BindingResult bindingResult, Model uiModel,
      HttpServletRequest httpServletRequest) {
    if (bindingResult.hasErrors()) {
      populateEditForm(uiModel, articleComment);
      return "admin/article_comment/update";
    }
    uiModel.asMap().clear();
    ArticleComment oldComment = ArticleComment.find(articleComment.getId());
    articleComment.setUc(oldComment.getUc());
    articleComment.setDc(oldComment.getDc());
    articleComment.setUm(getCurrentUser());
    articleComment.setDm(Calendar.getInstance());
    articleComment.merge();
    return "redirect:/admin/article_comment/"
        + encodeUrlPathSegment(articleComment.getId().toString(), httpServletRequest);
  }

  @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
  public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
    populateEditForm(uiModel, ArticleComment.find(id));
    return "admin/article_comment/update";
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
  public String delete(@PathVariable("id") Integer id, @RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    ArticleComment articleComment = ArticleComment.find(id);
    articleComment.remove();
    uiModel.asMap().clear();
    uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
    uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
    return "redirect:/admin/article_comment";
  }

  void addDateTimeFormatPatterns(Model uiModel) {
    uiModel.addAttribute("articleComment_publishedat_date_format",
        DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    uiModel.addAttribute("articleComment_dc_date_format",
        DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    uiModel.addAttribute("articleComment_dm_date_format",
        DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
  }

  void populateEditForm(Model uiModel, ArticleComment articleComment) {
    uiModel.addAttribute("articleComment", articleComment);
    addDateTimeFormatPatterns(uiModel);
    uiModel.addAttribute("articleCommentStatusList", ArticleCommentStatus.findAll());
    uiModel.addAttribute("articleList", Article.findAll());
    uiModel.addAttribute("userList", User.findAll());
  }

  @RequestMapping(value = "/publish", method = RequestMethod.PUT, produces = "text/html")
  public String publish(ArticleComment articleComment, BindingResult bindingResult, Model uiModel,
      HttpServletRequest httpServletRequest) {
    if (bindingResult.hasErrors()) {
      populateEditForm(uiModel, articleComment);
      return "admin/article_comment/update";
    }

    ArticleComment.publish(articleComment.getId());
    return "redirect:/admin/article_comment/"
        + encodeUrlPathSegment(articleComment.getId().toString(), httpServletRequest);
  }

}
