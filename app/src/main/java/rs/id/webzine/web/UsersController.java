package rs.id.webzine.web;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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

import rs.id.webzine.domain.Roles;
import rs.id.webzine.domain.UserStatus;
import rs.id.webzine.domain.Users;

@RequestMapping("/users")
@Controller
public class UsersController extends ModelController {
	private static final Log log = LogFactory.getLog(UsersController.class);

	@InitBinder
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws ServletException {
		binder.registerCustomEditor(byte[].class,
				new ByteArrayMultipartFileEditor());
	}

	void addDateTimeFormatPatterns(Model uiModel) {
		uiModel.addAttribute(
				"users_birthday_date_format",
				DateTimeFormat.patternForStyle("M-",
						LocaleContextHolder.getLocale()));
	}

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid Users users,
			@RequestParam("image") MultipartFile image,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, users);
			return "users/create";
		}
		users.setImageContentType(image.getContentType());
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
		Users users = Users.find(id);
		users.setImageUrl("http://localhost:8080/webzine/users/showimage/" + id); // TODO
		uiModel.addAttribute("users", users);
		uiModel.addAttribute("itemId", id);
		addDateTimeFormatPatterns(uiModel);
		return "users/show";
	}

	@RequestMapping(value = "/showimage/{id}", method = RequestMethod.GET)
	public String showImage(@PathVariable("id") Integer id,
			HttpServletResponse response, Model model) {
		Users users = Users.find(id);

		try {
			response.setHeader("Content-Disposition", "inline;");

			OutputStream out = response.getOutputStream();
			response.setContentType(users.getImageContentType());

			IOUtils.copy(new ByteArrayInputStream(users.getImage()), out);
			out.flush();
		} catch (Exception e) {
			log.error(e);
		}

		return null;
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
		addDateTimeFormatPatterns(uiModel);
		return "users/list";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid Users users,
			@RequestParam("image") MultipartFile image,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		try {
			if (bindingResult.hasErrors()) {
				populateEditForm(uiModel, users);
				return "users/update";
			}
			if (image == null || image.getBytes() == null
					|| image.getBytes().length == 0) {
				Users oldUsers = Users.find(users.getId());
				users.setImage(oldUsers.getImage());
				users.setImageContentType(oldUsers.getImageContentType());
			} else {
				users.setImageContentType(image.getContentType());
			}

			uiModel.asMap().clear();
			users.merge();
			return "redirect:/users/"
					+ encodeUrlPathSegment(users.getId().toString(),
							httpServletRequest);
		} catch (Exception e) {
			log.error(e);
		}
		return null;
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
		uiModel.addAttribute("userstatuses", UserStatus.findAll());
		addDateTimeFormatPatterns(uiModel);
	}

}
