package rs.id.webzine.web.system;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

import rs.id.webzine.domain.system.Country;
import rs.id.webzine.service.system.CountryService;
import rs.id.webzine.web.WebController;

@RequestMapping(CountryController.PATH)
@Controller
public class CountryController extends WebController {

  // TODO filter, pagination, sort

  public static final String PATH = "admin/system/country";

  @Autowired
  CountryService countryService;

  @RequestMapping(produces = "text/html")
  public String list(@RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    prepareList(countryService, "countryList", page, size, uiModel);
    return PATH + "/" + LIST;
  }

  @RequestMapping(params = "form", produces = "text/html")
  public String createForm(Model uiModel) {
    populateEditForm(uiModel, new Country());
    return PATH + "/" + CREATE;
  }

  @RequestMapping(method = RequestMethod.POST, produces = "text/html")
  public String create(@Valid Country country, BindingResult bindingResult, Model uiModel,
      HttpServletRequest httpServletRequest) {

    // validate...
    CountryCreateValidator validator = new CountryCreateValidator();
    validator.validate(country, bindingResult);
    if (bindingResult.hasErrors()) {
      populateEditForm(uiModel, country);
      return PATH + "/" + CREATE;
    }

    countryService.create(country);

    uiModel.asMap().clear();
    return REDIRECT + PATH + "/" + encodeUrlPathSegment(country.getId().toString(), httpServletRequest);
  }

  @RequestMapping(value = "/{id}", produces = "text/html")
  public String show(@PathVariable("id") Integer id, Model uiModel) {
    uiModel.addAttribute("country", countryService.find(id));
    uiModel.addAttribute("itemId", id);
    return PATH + "/" + SHOW;
  }

  @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
  public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
    populateEditForm(uiModel, countryService.find(id));
    return PATH + "/" + UPDATE;
  }

  @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
  public String update(@Valid Country country, BindingResult bindingResult, Model uiModel,
      HttpServletRequest httpServletRequest) {
    if (bindingResult.hasErrors()) {
      populateEditForm(uiModel, country);
      return PATH + "/" + UPDATE;
    }

    countryService.update(country);

    uiModel.asMap().clear();
    return REDIRECT + PATH + "/" + encodeUrlPathSegment(country.getId().toString(), httpServletRequest);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
  public String delete(@PathVariable("id") Integer id, Model uiModel) {
    countryService.delete(id);

    uiModel.asMap().clear();
    return REDIRECT + PATH;
  }

  void populateEditForm(Model uiModel, Country country) {
    uiModel.addAttribute("country", country);
  }

  private class CountryCreateValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
      return true;
    }

    @Override
    public void validate(Object target, Errors errors) {
      Country form = (Country) target;

      Country country = countryService.findForCd(form.getCd());
      if (country != null) {
        errors.rejectValue("cd", "validation_code_already_exists");
      }
    }
  }

}
