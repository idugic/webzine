package rs.id.webzine.controller.marketing;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import rs.id.webzine.controller.WebController;
import rs.id.webzine.entity.marketing.Advertiser;
import rs.id.webzine.entity.system.Address;
import rs.id.webzine.service.marketing.AdvertiserService;
import rs.id.webzine.service.system.AddressService;

@RequestMapping(AdvertiserController.PATH)
@Controller
public class AdvertiserController extends WebController {

  private static final Log log = LogFactory.getLog(AdvertiserController.class);

  public static final String PATH = "admin/marketing/advertiser";

  @Autowired
  AdvertiserService advertiserService;

  @Autowired
  AddressService addressService;

  @RequestMapping(produces = "text/html")
  public String list(@RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    try {
      List<Advertiser> advertiserList = prepareList(advertiserService, "advertiserList", page, size, uiModel);

      // prepare form list
      List<AdvertiserForm> advertiserFormList = new ArrayList<AdvertiserForm>();
      for (Advertiser advertiser : advertiserList) {
        AdvertiserForm advertiserForm = new AdvertiserForm();
        PropertyUtils.copyProperties(advertiserForm, advertiser);
        advertiserForm.setAdvertiserId(advertiser.getId());

        Address address = advertiser.getAddress();
        if (address != null) {
          PropertyUtils.copyProperties(advertiserForm, address);
        }

        advertiserFormList.add(advertiserForm);
      }

      uiModel.addAttribute("advertiserList", advertiserFormList);

      return PATH + "/" + LIST;
    } catch (Exception e) {
      log.error(e);
      throw new RuntimeException(e);
    }
  }

  @RequestMapping(params = "form", produces = "text/html")
  public String createForm(Model uiModel, HttpServletRequest httpServletRequest) {
    populateEditForm(uiModel, new AdvertiserForm(), httpServletRequest, true);
    return PATH + "/" + CREATE;
  }

  void populateEditForm(Model uiModel, AdvertiserForm advertiserForm, HttpServletRequest httpServletRequest,
      boolean create) {
    addDatePattern(uiModel);

    uiModel.addAttribute("advertiserForm", advertiserForm);
  }

  @RequestMapping(method = RequestMethod.POST, produces = "text/html")
  public String create(AdvertiserForm advertiserForm, BindingResult bindingResult, Model uiModel,
      HttpServletRequest httpServletRequest) {
    try {

      // validate...
      AdvertiserCreateValidator validator = new AdvertiserCreateValidator();
      validator.validate(advertiserForm, bindingResult);
      if (bindingResult.hasErrors()) {
        populateEditForm(uiModel, advertiserForm, httpServletRequest, true);
        return PATH + "/" + CREATE;
      }

      // address
      Address address = new Address();
      PropertyUtils.copyProperties(address, advertiserForm);

      // persist
      Advertiser advertiser = new Advertiser();
      PropertyUtils.copyProperties(advertiser, advertiserForm);

      advertiserService.create(advertiser, address);

      uiModel.asMap().clear();
      return REDIRECT + PATH + "/" + encodeUrlPathSegment(advertiser.getId().toString(), httpServletRequest);
    } catch (Exception e) {
      log.error(e);
      throw new RuntimeException(e);
    }

  }

  @RequestMapping(value = "/{id}", produces = "text/html")
  public String show(@PathVariable("id") Integer id, Model uiModel, HttpServletRequest httpServletRequest) {
    try {
      AdvertiserForm advertiserForm = new AdvertiserForm();

      Advertiser advertiser = advertiserService.find(id);
      PropertyUtils.copyProperties(advertiserForm, advertiser);
      advertiserForm.setAdvertiserId(id);

      if (advertiser.getAddress() != null) {
        PropertyUtils.copyProperties(advertiserForm, advertiser.getAddress());
      }

      uiModel.addAttribute("advertiserForm", advertiserForm);
      uiModel.addAttribute("itemId", id);

      return PATH + "/" + SHOW;
    } catch (Exception e) {
      log.error(e);
      throw new RuntimeException(e);
    }

  }

  @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
  public String updateForm(@PathVariable("id") Integer id, Model uiModel, HttpServletRequest httpServletRequest) {
    try {
      AdvertiserForm advertiserForm = new AdvertiserForm();

      Advertiser advertiser = advertiserService.find(id);
      PropertyUtils.copyProperties(advertiserForm, advertiser);
      advertiserForm.setAdvertiserId(advertiser.getId());

      if (advertiser.getAddress() != null) {
        PropertyUtils.copyProperties(advertiserForm, advertiser.getAddress());
      }

      populateEditForm(uiModel, advertiserForm, httpServletRequest, false);
      return PATH + "/" + UPDATE;
    } catch (Exception e) {
      log.error(e);
      throw new RuntimeException(e);
    }
  }

  @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
  public String update(AdvertiserForm advertiserForm, BindingResult bindingResult, Model uiModel,
      HttpServletRequest httpServletRequest) {
    try {
      // validate...
      AdvertiserUpdateValidator validator = new AdvertiserUpdateValidator();
      validator.validate(advertiserForm, bindingResult);
      if (bindingResult.hasErrors()) {
        populateEditForm(uiModel, advertiserForm, httpServletRequest, true);
        return PATH + "/" + UPDATE;
      }

      Advertiser advertiser = new Advertiser();
      PropertyUtils.copyProperties(advertiser, advertiserForm);

      Address address = new Address();
      PropertyUtils.copyProperties(address, advertiserForm);

      advertiserService.update(advertiserForm.getAdvertiserId(), advertiser, address);

      uiModel.asMap().clear();
      return REDIRECT + PATH + "/"
          + encodeUrlPathSegment(advertiserForm.getAdvertiserId().toString(), httpServletRequest);
    } catch (Exception e) {
      log.error(e);
      throw new RuntimeException(e);
    }
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
  public String delete(@PathVariable("id") Integer id, @RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    advertiserService.delete(id);

    uiModel.asMap().clear();
    return REDIRECT + PATH;
  }

  private class AdvertiserCreateValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
      return true;
    }

    @Override
    public void validate(Object target, Errors errors) {
      AdvertiserForm form = (AdvertiserForm) target;
      Advertiser advertiser = advertiserService.findForName(form.getName());
      if (advertiser != null) {
        errors.rejectValue("name", "validation_name_already_exists");
      }
    }
  }

  private class AdvertiserUpdateValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
      return true;
    }

    @Override
    public void validate(Object target, Errors errors) {
      AdvertiserForm form = (AdvertiserForm) target;
      Advertiser advertiser = advertiserService.findForName(form.getName());
      if (advertiser != null && advertiser.getId() != form.getAdvertiserId()) {
        errors.rejectValue("name", "validation_name_already_exists");
      }
    }
  }
}
