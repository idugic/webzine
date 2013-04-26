package rs.id.webzine.web.magazine;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

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

import rs.id.webzine.domain.content_management.Content;
import rs.id.webzine.domain.content_management.ManagedContent;
import rs.id.webzine.domain.magazine.Article;
import rs.id.webzine.service.content_management.ContentService;
import rs.id.webzine.service.content_management.ManagedContentService;
import rs.id.webzine.service.magazine.ArticleService;
import rs.id.webzine.web.WebController;

@RequestMapping(ArticleDesignController.PATH)
@Controller
public class ArticleDesignController extends WebController {
  private static final Log log = LogFactory.getLog(ArticleDesignController.class);

  public static final String PATH = "admin/magazine/article/design";

  @Autowired
  private ArticleService articleService;

  @Autowired
  private ManagedContentService managedContentService;

  @Autowired
  private ContentService contentService;

  @InitBinder
  protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws ServletException {
    binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
  }

  @RequestMapping(value = "/{articleId}", produces = "text/html")
  public String designForm(@PathVariable("articleId") Integer articleId, Model uiModel,
      HttpServletRequest httpServletRequest) {
    populateDesignForm(uiModel, articleId, httpServletRequest);
    return PATH;
  }

  private void populateDesignForm(Model uiModel, Integer articleId, HttpServletRequest httpServletRequest) {
    Article article = articleService.find(articleId);
    uiModel.addAttribute("article", article);

    uiModel.addAttribute("managedContent", article.getManagedContent());
    uiModel.addAttribute("contentList", contentService.findForManagedContent(article.getManagedContent().getId()));
    uiModel.addAttribute("content", new Content());

    uiModel.addAttribute("itemId", articleId);
  }

  @RequestMapping(value = "/managed_content/css/{articleId}", method = RequestMethod.PUT, produces = "text/html")
  public String updateManagedContentCss(@PathVariable("articleId") Integer articleId, ManagedContent managedContent,
      BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    if (bindingResult.hasErrors()) {
      populateDesignForm(uiModel, articleId, httpServletRequest);
      return PATH;
    }

    Article article = articleService.find(articleId);

    managedContentService.updateCss(article.getManagedContent().getId(), managedContent.getCss());

    uiModel.asMap().clear();
    return REDIRECT + PATH + "/" + encodeUrlPathSegment(articleId.toString(), httpServletRequest);
  }

  @RequestMapping(value = "/managed_content/script/{articleId}", method = RequestMethod.PUT, produces = "text/html")
  public String updateManagedContentScript(@PathVariable("articleId") Integer articleId, ManagedContent managedContent,
      BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    if (bindingResult.hasErrors()) {
      populateDesignForm(uiModel, articleId, httpServletRequest);
      return PATH;
    }

    Article article = articleService.find(articleId);

    managedContentService.updateScript(article.getManagedContent().getId(), managedContent.getScript());

    uiModel.asMap().clear();
    return REDIRECT + PATH + "/" + encodeUrlPathSegment(articleId.toString(), httpServletRequest);
  }

  @RequestMapping(value = "/content/{articleId}", method = RequestMethod.POST, produces = "text/html")
  public String createContent(@PathVariable("articleId") Integer articleId, Content content,
      @RequestParam("media") MultipartFile media, BindingResult bindingResult, Model uiModel,
      HttpServletRequest httpServletRequest) {

    try {
      if (bindingResult.hasErrors()) {
        populateDesignForm(uiModel, articleId, httpServletRequest);
        return PATH;
      }

      Article article = articleService.find(articleId);

      if (content.getText() != null && !content.getText().isEmpty()) {
        contentService.createTextContent(article.getManagedContent().getId(), content.getText(),
            content.getDescription());
      }

      if (media.getBytes() != null && media.getBytes().length > 0) {
        contentService.createMediaContent(article.getManagedContent().getId(), media.getBytes(),
            media.getContentType(), media.getOriginalFilename(), content.getDescription());
      }

      uiModel.asMap().clear();
      return REDIRECT + PATH + "/" + encodeUrlPathSegment(articleId.toString(), httpServletRequest);
    } catch (IOException e) {
      log.error(e);
      throw new RuntimeException(e);
    }
  }

  @RequestMapping(value = "/content/{articleId}/{id}", method = RequestMethod.DELETE, produces = "text/html")
  public String deleteContent(@PathVariable("articleId") Integer articleId, @PathVariable("id") Integer id,
      Model uiModel, HttpServletRequest httpServletRequest) {
    contentService.delete(id);

    uiModel.asMap().clear();
    return REDIRECT + PATH + "/" + encodeUrlPathSegment(articleId.toString(), httpServletRequest);

  }

  @RequestMapping(value = "content/up/{articleId}/{id}", method = RequestMethod.PUT, produces = "text/html")
  public String moveContentUp(@PathVariable("articleId") Integer articleId, @PathVariable("id") Integer id,
      Model uiModel, HttpServletRequest httpServletRequest) {
    contentService.moveUp(id);

    uiModel.asMap().clear();
    return REDIRECT + PATH + "/" + encodeUrlPathSegment(articleId.toString(), httpServletRequest);
  }

  @RequestMapping(value = "/content/down/{articleId}/{id}", method = RequestMethod.PUT, produces = "text/html")
  public String moveContentDown(@PathVariable("articleId") Integer articleId, @PathVariable("id") Integer id,
      Model uiModel, HttpServletRequest httpServletRequest) {
    contentService.moveDown(id);

    uiModel.asMap().clear();
    return REDIRECT + PATH + "/" + encodeUrlPathSegment(articleId.toString(), httpServletRequest);
  }
}
