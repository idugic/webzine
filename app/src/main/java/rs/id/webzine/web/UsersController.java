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

import rs.id.webzine.domain.Roles;
import rs.id.webzine.domain.Users;

@RequestMapping("/users")
@Controller
public class UsersController extends ModelController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid Users users, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, users);
			return "users/create";
		}
		uiModel.asMap().clear();
		users.persist();
		return "redirect:/users/"
				+ encodeUrlPathSegment(users.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new Users());
		return "users/create";
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Integer id, Model uiModel) {
		uiModel.addAttribute("users", Users.find(id));
		uiModel.addAttribute("itemId", id);
		return "users/show";
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
			uiModel.addAttribute("users",
					Users.findEntries(firstResult, sizeNo));
			float nrOfPages = (float) Users.count() / sizeNo;
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else {
			uiModel.addAttribute("users", Users.findAll());
		}
		return "users/list";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid Users users, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, users);
			return "users/update";
		}
		uiModel.asMap().clear();
		users.merge();
		return "redirect:/users/"
				+ encodeUrlPathSegment(users.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
		populateEditForm(uiModel, Users.find(id));
		return "users/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Integer id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		Users users = Users.find(id);
		users.remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/users";
	}

	void populateEditForm(Model uiModel, Users users) {
		uiModel.addAttribute("users", users);
		uiModel.addAttribute("roles", Roles.findAll());
	}

}
