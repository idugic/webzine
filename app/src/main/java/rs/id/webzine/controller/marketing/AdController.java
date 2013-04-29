package rs.id.webzine.controller.marketing;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import rs.id.webzine.controller.WebController;
import rs.id.webzine.entity.marketing.Ad;
import rs.id.webzine.entity.marketing.AdArticle;
import rs.id.webzine.service.marketing.AdArticleService;
import rs.id.webzine.service.marketing.AdService;
import rs.id.webzine.service.marketing.AdStatusService;

@RequestMapping(AdController.PATH)
@Controller
public class AdController extends WebController {

  public static final String PATH = "admin/marketing/ad";

  @Autowired
  private AdService adService;

  @Autowired
  private AdStatusService adStatusService;

  @Autowired
  private AdArticleService adArticleService;

  @RequestMapping(produces = "text/html")
  public String list(@RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    prepareList(adService, "adList", page, size, uiModel);
    return PATH + "/" + LIST;
  }

  @RequestMapping(params = "form", produces = "text/html")
  public String createForm(Model uiModel, HttpServletRequest httpServletRequest) {
    populateEditForm(uiModel, new Ad(), httpServletRequest, true);
    return PATH + "/" + CREATE;
  }

  void populateEditForm(Model uiModel, Ad ad, HttpServletRequest httpServletRequest, boolean create) {
    addDateTimePattern(uiModel);
    uiModel.addAttribute("ad", ad);
    uiModel.addAttribute("adStatusList", adStatusService.findAll());
  }

  @RequestMapping(method = RequestMethod.POST, produces = "text/html")
  public String create(Ad ad, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {

    if (bindingResult.hasErrors()) {
      populateEditForm(uiModel, ad, httpServletRequest, true);
      return PATH + "/" + CREATE;
    }

    adService.create(ad);

    uiModel.asMap().clear();
    return REDIRECT + PATH + "/" + encodeUrlPathSegment(ad.getId().toString(), httpServletRequest);
  }

  @RequestMapping(value = "/{id}", produces = "text/html")
  public String show(@PathVariable("id") Integer id, Model uiModel, HttpServletRequest httpServletRequest) {
    populateShowForm(uiModel, id, httpServletRequest);
    return PATH + "/" + SHOW;
  }

  private void populateShowForm(Model uiModel, Integer id, HttpServletRequest httpServletRequest) {
    addDateTimePattern(uiModel);

    Ad ad = adService.find(id);
    uiModel.addAttribute("ad", ad);

    uiModel.addAttribute("adStatusList", adStatusService.findAll());
    uiModel.addAttribute("availableArticleList", adArticleService.findAvailableArticleList(ad.getId()));

    uiModel.addAttribute("itemId", id);
  }

  @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
  public String updateForm(@PathVariable("id") Integer id, Model uiModel, HttpServletRequest httpServletRequest) {
    populateEditForm(uiModel, adService.find(id), httpServletRequest, false);
    return PATH + "/" + UPDATE;
  }

  @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
  public String update(Ad ad, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {

    if (bindingResult.hasErrors()) {
      populateEditForm(uiModel, ad, httpServletRequest, false);
      return PATH + "/" + UPDATE;
    }

    adService.update(ad);

    uiModel.asMap().clear();
    return REDIRECT + PATH + "/" + encodeUrlPathSegment(ad.getId().toString(), httpServletRequest);
  }

  @RequestMapping(value = "/publish", method = RequestMethod.PUT, produces = "text/html")
  public String publish(Ad ad, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    if (bindingResult.hasErrors()) {
      populateEditForm(uiModel, ad, httpServletRequest, false);
      return PATH + "/" + UPDATE;
    }

    adService.publish(ad.getId());

    uiModel.asMap().clear();
    return REDIRECT + PATH + "/" + encodeUrlPathSegment(ad.getId().toString(), httpServletRequest);
  }

  @RequestMapping(value = "/recall", method = RequestMethod.PUT, produces = "text/html")
  public String recall(Ad ad, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    if (bindingResult.hasErrors()) {
      populateEditForm(uiModel, ad, httpServletRequest, false);
      return PATH + "/" + UPDATE;
    }

    adService.recall(ad.getId());

    uiModel.asMap().clear();
    return REDIRECT + PATH + "/" + encodeUrlPathSegment(ad.getId().toString(), httpServletRequest);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
  public String delete(@PathVariable("id") Integer id, Model uiModel) {
    adService.delete(id);

    uiModel.asMap().clear();
    return REDIRECT + PATH;
  }

  @RequestMapping(value = "/article/{adId}", method = RequestMethod.POST, produces = "text/html")
  public String createAdArticle(@PathVariable("adId") Integer adId, AdArticle adArticle, BindingResult bindingResult,
      Model uiModel, HttpServletRequest httpServletRequest) {
    if (bindingResult.hasErrors()) {
      populateShowForm(uiModel, adId, httpServletRequest);
      return PATH + "/" + SHOW;
    }

    adArticle.setAd(adService.find(adId));
    adArticleService.create(adArticle);

    uiModel.asMap().clear();
    return REDIRECT + PATH + "/" + encodeUrlPathSegment(adId.toString(), httpServletRequest);
  }

  @RequestMapping(value = "/article/{adId}/{id}", method = RequestMethod.DELETE, produces = "text/html")
  public String deleteArticle(@PathVariable("adId") Integer adId, @PathVariable("id") Integer id, Model uiModel,
      HttpServletRequest httpServletRequest) {
    adArticleService.delete(id);

    uiModel.asMap().clear();
    return REDIRECT + PATH + "/" + encodeUrlPathSegment(adId.toString(), httpServletRequest);
  }

}
