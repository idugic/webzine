package rs.id.webzine.web;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import rs.id.webzine.domain.Country;

@RequestMapping("admin/country")
@Controller
public class CountryController extends ModelController {

	class CountryCreateValidator implements Validator {

		@Override
		public boolean supports(Class<?> clazz) {
			return true;
		}

		@Override
		public void validate(Object target, Errors errors) {
			Country form = (Country) target;
			Country country = Country.findForCd(form.getCd());
			if (country != null) {
				errors.rejectValue("cd", "validation.country.cd.duplicate");
			}
		}

	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new Country());
		return "admin/country/create";
	}

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid Country country, BindingResult bindingResult, Model uiModel,
	        HttpServletRequest httpServletRequest) {
		// bind
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, country);
			return "admin/country/create";
		}

		// validate
		CountryCreateValidator validator = new CountryCreateValidator();
		validator.validate(country, bindingResult);
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, country);
			return "admin/country/create";
		}

		// persist
		country.persist();

		// show
		uiModel.asMap().clear();
		return "redirect:/admin/country/" + encodeUrlPathSegment(country.getId().toString(), httpServletRequest);
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Integer id, Model uiModel) {
		uiModel.addAttribute("country", Country.find(id));
		uiModel.addAttribute("itemId", id);
		return "admin/country/show";
	}

	@RequestMapping(produces = "text/html")
	public String list(@RequestParam(value = "page", required = false) Integer page,
	        @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
			uiModel.addAttribute("country", Country.findEntries(firstResult, sizeNo));
			float nrOfPages = (float) Country.count() / sizeNo;
			uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
			        : nrOfPages));
		} else {
			uiModel.addAttribute("country", Country.findAll());
		}
		return "admin/country/list";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid Country country, BindingResult bindingResult, Model uiModel,
	        HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, country);
			return "admin/country/update";
		}

		// TODO Server side validation (check on duplicate CD)

		uiModel.asMap().clear();
		country.merge();
		return "redirect:/admin/country/" + encodeUrlPathSegment(country.getId().toString(), httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
		populateEditForm(uiModel, Country.find(id));
		return "admin/country/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Integer id, @RequestParam(value = "page", required = false) Integer page,
	        @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		Country country = Country.find(id);
		country.remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/admin/country";
	}

	void populateEditForm(Model uiModel, Country country) {
		uiModel.addAttribute("country", country);
	}
}
