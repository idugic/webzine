package rs.id.webzine.web;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import rs.id.webzine.domain.UserStatus;

@RequestMapping("/userstatuses")
@Controller
public class UserStatusController extends ModelController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid UserStatus userStatus,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, userStatus);
			return "userstatuses/create";
		}
		uiModel.asMap().clear();
		userStatus.persist();
		return "redirect:/userstatuses/"
				+ encodeUrlPathSegment(userStatus.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new UserStatus());
		return "userstatuses/create";
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Integer id, Model uiModel) {
		uiModel.addAttribute("userstatuses", UserStatus.find(id));
		uiModel.addAttribute("itemId", id);
		return "userstatuses/show";
	}

	@RequestMapping(produces = "text/html")
	public String list(
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			final int firstResult = page == null ? 0 : (page.intValue() - 1)
					* sizeNo;
			uiModel.addAttribute("userstatuses",
					UserStatus.findEntries(firstResult, sizeNo));
			float nrOfPages = (float) UserStatus.count() / sizeNo;
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else {
			uiModel.addAttribute("userstatuses", UserStatus.findAll());
		}
		return "userstatuses/list";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid UserStatus userStatus,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, userStatus);
			return "userstatuses/update";
		}
		uiModel.asMap().clear();
		userStatus.merge();
		return "redirect:/userstatuses/"
				+ encodeUrlPathSegment(userStatus.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
		populateEditForm(uiModel, UserStatus.find(id));
		return "userstatuses/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Integer id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		UserStatus userstatuses = UserStatus.find(id);
		userstatuses.remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/userstatuses";
	}

	void populateEditForm(Model uiModel, UserStatus userStatus) {
		uiModel.addAttribute("userstatuses", userStatus);
	}

}
