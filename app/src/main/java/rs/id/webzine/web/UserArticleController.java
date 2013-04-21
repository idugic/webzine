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

import rs.id.webzine.domain.magazine.UserArticle;
import rs.id.webzine.domain.magazine.UserArticleStatus;
import rs.id.webzine.domain.system.User;
import rs.id.webzine.service.Service;

@RequestMapping("/admin/user_article")
@Controller
public class UserArticleController extends WebController {
//  private static final Log log = LogFactory.getLog(UserArticleController.class);
//
//  @InitBinder
//  protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws ServletException {
//    binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
//  }
//
//  @RequestMapping(params = "form", produces = "text/html")
//  public String createForm(Model uiModel) {
//    populateEditForm(uiModel, new UserArticle());
//    return "admin/user_article/create";
//  }
//
//  @RequestMapping(method = RequestMethod.POST, produces = "text/html")
//  public String create(UserArticle userArticle, @RequestParam("media") MultipartFile media,
//      BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
//    try {
//      /*
//       * workaround (multipart forms are always sent using POST)
//       */
//      if (httpServletRequest.getParameter("_method") != null
//          && httpServletRequest.getParameter("_method").equals("PUT")) {
//        return this.update(userArticle, media, bindingResult, uiModel, httpServletRequest);
//      }
//
//      if (bindingResult.hasErrors()) {
//        populateEditForm(uiModel, userArticle);
//        return "admin/user_article/create";
//      }
//
//      userArticle.setStatusId(UserArticleStatus.findForCd(UserArticleStatus.CD_SUBMITTED));
//      userArticle.setUc(Service.getCurrentUser());
//      userArticle.setDc(Calendar.getInstance());
//      userArticle.setMedia(media.getBytes());
//      userArticle.setMediaContentType(media.getContentType());
//      uiModel.asMap().clear();
//      userArticle.persist();
//      return "redirect:/admin/user_article/" + encodeUrlPathSegment(userArticle.getId().toString(), httpServletRequest);
//    } catch (Exception e) {
//      log.error(e);
//      throw new RuntimeException(e);
//    }
//  }
//
//  @RequestMapping(value = "/{id}", produces = "text/html")
//  public String show(@PathVariable("id") Integer id, Model uiModel, HttpServletRequest httpServletRequest) {
//    addDateTimeFormatPatterns(uiModel);
//    UserArticle userArticle = UserArticle.find(id);
//    String mediaUrl = httpServletRequest.getContextPath() + "/admin/user_article/media/"
//        + encodeUrlPathSegment(id.toString(), httpServletRequest);
//    userArticle.setMediaUrl(mediaUrl);
//    uiModel.addAttribute("userArticle", userArticle);
//    uiModel.addAttribute("userArticleStatusList", UserArticleStatus.findAll());
//    // uiModel.addAttribute("userList", User.findAll());
//    uiModel.addAttribute("itemId", id);
//    return "admin/user_article/show";
//  }
//
//  @RequestMapping(produces = "text/html")
//  public String list(@RequestParam(value = "page", required = false) Integer page,
//      @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
//    if (page != null || size != null) {
//      int sizeNo = size == null ? 10 : size.intValue();
//      final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
//      uiModel.addAttribute("userArticleList", UserArticle.findEntries(firstResult, sizeNo));
//      float nrOfPages = (float) UserArticle.count() / sizeNo;
//      uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
//          : nrOfPages));
//    } else {
//      uiModel.addAttribute("userArticleList", UserArticle.findAll());
//    }
//    addDateTimeFormatPatterns(uiModel);
//    return "admin/user_article/list";
//  }
//
//  @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
//  public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
//    populateEditForm(uiModel, UserArticle.find(id));
//    return "admin/user_article/update";
//  }
//
//  @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
//  public String update(UserArticle userArticle, @RequestParam("media") MultipartFile media,
//      BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
//    try {
//      if (bindingResult.hasErrors()) {
//        populateEditForm(uiModel, userArticle);
//        return "admin/user_article/update";
//      }
//
//      UserArticle oldUserArticle = UserArticle.find(userArticle.getId());
//
//      userArticle.setUc(oldUserArticle.getUc());
//      userArticle.setDc(oldUserArticle.getDc());
//      userArticle.setUm(Service.getCurrentUser());
//      userArticle.setDm(Calendar.getInstance());
//
//      if (media == null || media.getBytes() == null || media.getBytes().length == 0) {
//        userArticle.setMedia(oldUserArticle.getMedia());
//        userArticle.setMediaContentType(oldUserArticle.getMediaContentType());
//      } else {
//        userArticle.setMedia((media.getBytes()));
//        userArticle.setMediaContentType((media.getContentType()));
//      }
//
//      uiModel.asMap().clear();
//      userArticle.merge();
//      return "redirect:/admin/user_article/" + encodeUrlPathSegment(userArticle.getId().toString(), httpServletRequest);
//    } catch (Exception e) {
//      log.error(e);
//      throw new RuntimeException(e);
//    }
//  }
//
//  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
//  public String delete(@PathVariable("id") Integer id, @RequestParam(value = "page", required = false) Integer page,
//      @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
//    UserArticle userArticle = UserArticle.find(id);
//    userArticle.remove();
//    uiModel.asMap().clear();
//    uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
//    uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
//    return "redirect:/admin/user_article";
//  }
//
//  void addDateTimeFormatPatterns(Model uiModel) {
//    uiModel.addAttribute("userArticle_dc_date_format",
//        DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
//    uiModel.addAttribute("userArticle_dm_date_format",
//        DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
//  }
//
//  void populateEditForm(Model uiModel, UserArticle userArticle) {
//    uiModel.addAttribute("userArticle", userArticle);
//    addDateTimeFormatPatterns(uiModel);
//    uiModel.addAttribute("userArticleStatusList", UserArticleStatus.findAll());
//  }
//
//  @RequestMapping(value = "/media/{id}", method = RequestMethod.GET)
//  public String showMedia(@PathVariable("id") Integer id, HttpServletResponse response, Model model) {
//    try {
//      UserArticle userArticle = UserArticle.find(id);
//      if (userArticle.getMedia() != null && userArticle.getMedia().length > 0) {
//        response.setHeader("Content-Disposition", "inline;");
//        response.setContentType(userArticle.getMediaContentType());
//
//        OutputStream out = response.getOutputStream();
//        IOUtils.copy(new ByteArrayInputStream(userArticle.getMedia()), out);
//        out.flush();
//      }
//
//      return null;
//    } catch (Exception e) {
//      log.error(e);
//      throw new RuntimeException(e);
//    }
//  }

}
