package rs.id.webzine.web;

import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

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

import rs.id.webzine.domain.Ad;
import rs.id.webzine.domain.AdArticle;
import rs.id.webzine.domain.AdStatus;
import rs.id.webzine.domain.Customer;
import rs.id.webzine.domain.content_management.Content;
import rs.id.webzine.domain.content_management.ContentType;
import rs.id.webzine.domain.content_management.ManagedContent;
import rs.id.webzine.domain.magazine.Article;
import rs.id.webzine.domain.system.User;
import rs.id.webzine.service.Service;

@RequestMapping("/admin/ad")
@Controller
public class AdController extends WebController {
  private static final Log log = LogFactory.getLog(AdController.class);

  // TODO move content Up/Down in the content list

  // TODO hide/unhide content text, media file based on type, or do it in 2
  // forms: one for text, another for media

  // @InitBinder
  // protected void initBinder(HttpServletRequest request,
  // ServletRequestDataBinder binder) throws ServletException {
  // binder.registerCustomEditor(byte[].class, new
  // ByteArrayMultipartFileEditor());
  // }
  //
  // @RequestMapping(params = "form", produces = "text/html")
  // public String createForm(Model uiModel) {
  // populateEditForm(uiModel, new Ad());
  // return "admin/ad/create";
  // }
  //
  // @RequestMapping(method = RequestMethod.POST, produces = "text/html")
  // public String create(Ad ad, BindingResult bindingResult, Model uiModel,
  // HttpServletRequest httpServletRequest) {
  // try {
  // if (bindingResult.hasErrors()) {
  // populateEditForm(uiModel, ad);
  // return "admin/ad/create";
  // }
  //
  // ManagedContent managedContent = new ManagedContent();
  // managedContent.persist();
  //
  // ad.setManagedContentId(managedContent);
  // ad.setStatusId(AdStatus.findForCd(AdStatus.CD_SUBMITTED));
  // ad.setUc(Service.getCurrentUser());
  // ad.setDc(Calendar.getInstance());
  // uiModel.asMap().clear();
  // ad.persist();
  // return "redirect:/admin/ad/" + encodeUrlPathSegment(ad.getId().toString(),
  // httpServletRequest);
  // } catch (Exception e) {
  // log.error(e);
  // throw new RuntimeException(e);
  // }
  // }
  //
  // @RequestMapping(value = "/{id}", produces = "text/html")
  // public String show(@PathVariable("id") Integer id, Model uiModel,
  // HttpServletRequest httpServletRequest) {
  // addDateTimeFormatPatterns(uiModel);
  // Ad ad = Ad.find(id);
  // uiModel.addAttribute("ad", ad);
  // uiModel.addAttribute("customerList", Customer.findAll());
  // uiModel.addAttribute("adStatusList", AdStatus.findAll());
  // uiModel.addAttribute("adArticleList", AdArticle.findForAd(ad.getId()));
  // uiModel.addAttribute("managedContent", ad.getManagedContentId());
  // uiModel.addAttribute("adContentList",
  // Content.findForManagedContent(ad.getManagedContentId().getId()));
  // // uiModel.addAttribute("userList", User.findAll());
  // uiModel.addAttribute("itemId", id);
  // return "admin/ad/show";
  // }
  //
  // @RequestMapping(produces = "text/html")
  // public String list(@RequestParam(value = "page", required = false) Integer
  // page,
  // @RequestParam(value = "size", required = false) Integer size, Model
  // uiModel) {
  // if (page != null || size != null) {
  // int sizeNo = size == null ? 10 : size.intValue();
  // final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
  // uiModel.addAttribute("adList", Ad.findEntries(firstResult, sizeNo));
  // float nrOfPages = (float) Ad.count() / sizeNo;
  // uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages ||
  // nrOfPages == 0.0) ? nrOfPages + 1
  // : nrOfPages));
  // } else {
  // uiModel.addAttribute("adList", Ad.findAll());
  // }
  // addDateTimeFormatPatterns(uiModel);
  // return "admin/ad/list";
  // }
  //
  // @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
  // public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
  // populateEditForm(uiModel, Ad.find(id));
  // uiModel.addAttribute("adArticleList", AdArticle.findForAd(id));
  // uiModel.addAttribute("adArticle", new AdArticle());
  // uiModel.addAttribute("managedContent", new ManagedContent());
  // uiModel.addAttribute("content", new Content());
  // return "admin/ad/update";
  // }
  //
  // @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
  // public String update(Ad ad, BindingResult bindingResult, Model uiModel,
  // HttpServletRequest httpServletRequest) {
  // try {
  // if (bindingResult.hasErrors()) {
  // populateEditForm(uiModel, ad);
  // uiModel.addAttribute("adArticleList", AdArticle.findForAd(ad.getId()));
  // uiModel.addAttribute("adArticle", new AdArticle());
  // uiModel.addAttribute("managedContent", new ManagedContent());
  // uiModel.addAttribute("content", new Content());
  //
  // return "admin/ad/update";
  // }
  //
  // Ad oldAd = Ad.find(ad.getId());
  //
  // ad.setManagedContentId(oldAd.getManagedContentId());
  // ad.setUc(oldAd.getUc());
  // ad.setDc(oldAd.getDc());
  // ad.setUm(Service.getCurrentUser());
  // ad.setDm(Calendar.getInstance());
  //
  // uiModel.asMap().clear();
  // ad.merge();
  // return "redirect:/admin/ad/" + encodeUrlPathSegment(ad.getId().toString(),
  // httpServletRequest);
  // } catch (Exception e) {
  // log.error(e);
  // throw new RuntimeException(e);
  // }
  // }
  //
  // @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces =
  // "text/html")
  // public String delete(@PathVariable("id") Integer id, @RequestParam(value =
  // "page", required = false) Integer page,
  // @RequestParam(value = "size", required = false) Integer size, Model
  // uiModel) {
  //
  // // TODO delete in service layer
  // List<AdArticle> adArticleList = AdArticle.findForAd(id);
  // for (AdArticle adArticle : adArticleList) {
  // adArticle.remove();
  // }
  //
  // Ad ad = Ad.find(id);
  //
  // ManagedContent managedContent = ad.getManagedContentId();
  //
  // ad.remove();
  //
  // List<Content> contentList =
  // Content.findForManagedContent(managedContent.getId());
  // for (Content content : contentList) {
  // content.remove();
  // }
  // managedContent.remove();
  //
  // uiModel.asMap().clear();
  // uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
  // uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
  // return "redirect:/admin/ad";
  // }
  //
  // void addDateTimeFormatPatterns(Model uiModel) {
  // uiModel.addAttribute("date_format", DateTimeFormat.patternForStyle("MM",
  // LocaleContextHolder.getLocale()));
  // }
  //
  // void populateEditForm(Model uiModel, Ad ad) {
  // uiModel.addAttribute("ad", ad);
  // addDateTimeFormatPatterns(uiModel);
  // uiModel.addAttribute("customerList", Customer.findAll());
  // uiModel.addAttribute("adStatusList", AdStatus.findAll());
  // uiModel.addAttribute("availableArticleList",
  // Article.findAvailableForAd(ad.getId()));
  // uiModel.addAttribute("adArticleList", Article.findForAd(ad.getId()));
  // uiModel.addAttribute("managedContent", ad.getManagedContentId());
  // uiModel.addAttribute("contentTypeList", ContentType.findAll());
  // if (ad.getManagedContentId() != null) {
  // uiModel.addAttribute("adContentList",
  // Content.findForManagedContent(ad.getManagedContentId().getId()));
  // }
  // }
  //
  // @RequestMapping(value = "/article/{adId}", method = RequestMethod.POST,
  // produces = "text/html")
  // public String createAdArticle(@PathVariable("adId") Integer adId, AdArticle
  // adArticle, BindingResult bindingResult,
  // Model uiModel, HttpServletRequest httpServletRequest) {
  // if (bindingResult.hasErrors()) {
  // populateEditForm(uiModel, Ad.find(adId));
  // uiModel.addAttribute("adArticleList", AdArticle.findForAd(adId));
  // uiModel.addAttribute("adArticle", new AdArticle());
  // uiModel.addAttribute("managedContent", new ManagedContent());
  // uiModel.addAttribute("content", new Content());
  // return "admin/ad/update";
  // }
  // adArticle.setAdId(Ad.find(adId));
  // adArticle.setUc(Service.getCurrentUser());
  // adArticle.setDc(Calendar.getInstance());
  // adArticle.persist();
  //
  // uiModel.asMap().clear();
  // return "redirect:/admin/ad/" + encodeUrlPathSegment(adId.toString(),
  // httpServletRequest);
  // }
  //
  // @RequestMapping(value = "/article/{adId}/{id}", method =
  // RequestMethod.DELETE, produces = "text/html")
  // public String deleteAdArticle(@PathVariable("adId") Integer adId,
  // @PathVariable("id") Integer id, Model uiModel) {
  // AdArticle adArticle = AdArticle.find(id);
  // adArticle.remove();
  //
  // populateEditForm(uiModel, Ad.find(adId));
  // uiModel.addAttribute("adArticleList", AdArticle.findForAd(adId));
  // uiModel.addAttribute("adArticle", new AdArticle());
  // uiModel.addAttribute("managedContent", new ManagedContent());
  // uiModel.addAttribute("content", new Content());
  // return "admin/ad/update";
  // }
  //
  // @RequestMapping(value = "/managed_content/{adId}", method =
  // RequestMethod.PUT, produces = "text/html")
  // public String updateManagedContent(@PathVariable("adId") Integer adId,
  // ManagedContent managedContent,
  // BindingResult bindingResult, Model uiModel, HttpServletRequest
  // httpServletRequest) {
  // try {
  // if (bindingResult.hasErrors()) {
  // populateEditForm(uiModel, Ad.find(adId));
  // uiModel.addAttribute("adArticleList", AdArticle.findForAd(adId));
  // uiModel.addAttribute("adArticle", new AdArticle());
  // uiModel.addAttribute("managedContent", new ManagedContent());
  // uiModel.addAttribute("content", new Content());
  // return "admin/ad/update";
  // }
  //
  // Ad ad = Ad.find(adId);
  //
  // ManagedContent adManagedContent = ad.getManagedContentId();
  // adManagedContent.setCss(managedContent.getCss());
  //
  // uiModel.asMap().clear();
  // adManagedContent.merge();
  // return "redirect:/admin/ad/" + encodeUrlPathSegment(ad.getId().toString(),
  // httpServletRequest);
  // } catch (Exception e) {
  // log.error(e);
  // throw new RuntimeException(e);
  // }
  // }
  //
  // @RequestMapping(value = "/content/{adId}", method = RequestMethod.POST,
  // produces = "text/html")
  // public String createContent(@PathVariable("adId") Integer adId, Content
  // content,
  // @RequestParam("media") MultipartFile media, BindingResult bindingResult,
  // Model uiModel,
  // HttpServletRequest httpServletRequest) {
  // try {
  // if (bindingResult.hasErrors()) {
  // populateEditForm(uiModel, Ad.find(adId));
  // uiModel.addAttribute("adArticleList", AdArticle.findForAd(adId));
  // uiModel.addAttribute("adArticle", new AdArticle());
  // uiModel.addAttribute("managedContent", new ManagedContent());
  // uiModel.addAttribute("content", new Content());
  // return "admin/ad/update";
  // }
  //
  // Ad ad = Ad.find(adId);
  // content.setManagedContentId(ad.getManagedContentId());
  // // TODO synchronize
  // content.setOrderNo(Content.getNextOrderNoForManagedContent(ad.getManagedContentId().getId()));
  // if (media != null && media.getBytes() != null && media.getBytes().length >
  // 0) {
  // content.setMediaContentType(media.getContentType());
  // }
  //
  // // description
  // if (content.getDescription() == null || content.getDescription().isEmpty())
  // {
  // if (content.getText() != null && !content.getText().isEmpty()) {
  // content.setDescription(content.getText());
  // } else {
  // content.setDescription(media.getOriginalFilename());
  // }
  // }
  //
  // content.persist();
  //
  // uiModel.asMap().clear();
  // return "redirect:/admin/ad/" + encodeUrlPathSegment(adId.toString(),
  // httpServletRequest);
  // } catch (Exception e) {
  // log.error(e);
  // throw new RuntimeException(e);
  // }
  // }
  //
  // @RequestMapping(value = "/content/{adId}/{id}", method =
  // RequestMethod.DELETE, produces = "text/html")
  // public String deleteContent(@PathVariable("adId") Integer adId,
  // @PathVariable("id") Integer id, Model uiModel) {
  // Content content = Content.find(id);
  // content.remove();
  //
  // populateEditForm(uiModel, Ad.find(adId));
  // uiModel.addAttribute("adArticleList", AdArticle.findForAd(adId));
  // uiModel.addAttribute("adArticle", new AdArticle());
  // uiModel.addAttribute("managedContent", new ManagedContent());
  // uiModel.addAttribute("content", new Content());
  // return "admin/ad/update";
  // }

}
