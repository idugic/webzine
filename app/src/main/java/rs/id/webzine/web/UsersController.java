package rs.id.webzine.web;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.beanutils.PropertyUtils;
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

import rs.id.webzine.domain.Address;
import rs.id.webzine.domain.Roles;
import rs.id.webzine.domain.UserStatus;
import rs.id.webzine.domain.Users;
import rs.id.webzine.web.backing.UserBacking;

@RequestMapping("/users")
@Controller
public class UsersController extends ModelController {
	private static final Log log = LogFactory.getLog(UsersController.class);

	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws ServletException {
		binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
	}

	void addDateTimeFormatPatterns(Model uiModel) {
		uiModel.addAttribute("users_birthday_date_format",
		        DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
	}

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid UserBacking userBacking, @RequestParam("image") MultipartFile image,
	        BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
		try {
			if (bindingResult.hasErrors()) {
				populateEditForm(uiModel, userBacking);
				return "users/create";
			}
			uiModel.asMap().clear();

			// address
			Address address = new Address();
			PropertyUtils.copyProperties(address, userBacking);
			address.persist();

			Users user = new Users();
			PropertyUtils.copyProperties(user, userBacking);
			user.setImageContentType(image.getContentType());
			user.setAddressId(address);
			user.persist();

			return "redirect:/users/" + encodeUrlPathSegment(user.getId().toString(), httpServletRequest);
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}

	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new UserBacking());
		return "users/create";
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Integer id, Model uiModel) {
		try {
			UserBacking userBacking = new UserBacking();

			Users user = Users.find(id);

			if (user.getAddressId() != null) {
				PropertyUtils.copyProperties(userBacking, user.getAddressId());
			}
			PropertyUtils.copyProperties(userBacking, user);

			userBacking.setImageUrl("http://localhost:8080/webzine/users/showimage/" + id); // TODO

			uiModel.addAttribute("userBacking", userBacking);
			uiModel.addAttribute("itemId", id);
			addDateTimeFormatPatterns(uiModel);
			return "users/show";
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}

	}

	@RequestMapping(value = "/showimage/{id}", method = RequestMethod.GET)
	public String showImage(@PathVariable("id") Integer id, HttpServletResponse response, Model model) {
		try {
			response.setHeader("Content-Disposition", "inline;");

			Users users = Users.find(id);
			response.setContentType(users.getImageContentType());

			OutputStream out = response.getOutputStream();
			IOUtils.copy(new ByteArrayInputStream(users.getImage()), out);
			out.flush();

			return null;
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	@RequestMapping(produces = "text/html")
	public String list(@RequestParam(value = "page", required = false) Integer page,
	        @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
			uiModel.addAttribute("users", Users.findEntries(firstResult, sizeNo));
			float nrOfPages = (float) Users.count() / sizeNo;
			uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
			        : nrOfPages));
		} else {
			uiModel.addAttribute("users", Users.findAll());
		}
		addDateTimeFormatPatterns(uiModel);
		return "users/list";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid UserBacking userBacking, @RequestParam("image") MultipartFile image,
	        BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
		try {
			if (bindingResult.hasErrors()) {
				populateEditForm(uiModel, userBacking);
				return "users/update";
			}

			// user
			Users user = Users.find(userBacking.getUserId());
			PropertyUtils.copyProperties(user, userBacking);
			user.setId(userBacking.getUserId());

			// image
			if (image == null || image.getBytes() == null || image.getBytes().length == 0) {
				Users oldUsers = Users.find(userBacking.getUserId());
				user.setImage(oldUsers.getImage());
				user.setImageContentType(oldUsers.getImageContentType());
			} else {
				user.setImageContentType(image.getContentType());
			}

			// address
			if (user.getAddressId() != null) {
				PropertyUtils.copyProperties(user.getAddressId(), userBacking);
			} else {
				Address address = new Address();
				PropertyUtils.copyProperties(address, userBacking);
				address.persist();

				user.setAddressId(address);
			}

			uiModel.asMap().clear();
			user.merge();
			return "redirect:/users/" + encodeUrlPathSegment(user.getId().toString(), httpServletRequest);
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
		try {
			UserBacking userBacking = new UserBacking();
			Users user = Users.find(id);
			PropertyUtils.copyProperties(userBacking, user);
			userBacking.setUserId(user.getId());

			if (user.getAddressId() != null) {
				PropertyUtils.copyProperties(userBacking, user.getAddressId());
			}

			populateEditForm(uiModel, userBacking);
			return "users/update";
		} catch (Exception e) {
			log.error(e);
			throw new RuntimeException(e);
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Integer id, @RequestParam(value = "page", required = false) Integer page,
	        @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		Users users = Users.find(id);
		users.remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/users";
	}

	void populateEditForm(Model uiModel, UserBacking userBacking) {
		uiModel.addAttribute("userBacking", userBacking);
		uiModel.addAttribute("roles", Roles.findAll());
		uiModel.addAttribute("userstatuses", UserStatus.findAll());
		addDateTimeFormatPatterns(uiModel);
	}

}
