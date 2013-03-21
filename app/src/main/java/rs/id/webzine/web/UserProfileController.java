package rs.id.webzine.web;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;
import rs.id.webzine.domain.Address;
import rs.id.webzine.domain.UserProfile; 
import rs.id.webzine.domain.Users;

@RequestMapping("/userprofiles")
@Controller
public class UserProfileController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid UserProfile userProfile,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, userProfile);
			return "userprofiles/create";
		}
		uiModel.asMap().clear();
		userProfile.persist();
		return "redirect:/userprofiles/"
				+ encodeUrlPathSegment(userProfile.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new UserProfile());
		return "userprofiles/create";
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Integer id, Model uiModel) {
		addDateTimeFormatPatterns(uiModel);
		uiModel.addAttribute("userprofile", UserProfile.findUserProfile(id));
		uiModel.addAttribute("itemId", id);
		return "userprofiles/show";
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
			uiModel.addAttribute("userprofiles",
					UserProfile.findUserProfileEntries(firstResult, sizeNo));
			float nrOfPages = (float) UserProfile.countUserProfiles() / sizeNo;
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else {
			uiModel.addAttribute("userprofiles",
					UserProfile.findAllUserProfiles());
		}
		addDateTimeFormatPatterns(uiModel);
		return "userprofiles/list";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid UserProfile userProfile,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, userProfile);
			return "userprofiles/update";
		}
		uiModel.asMap().clear();
		userProfile.merge();
		return "redirect:/userprofiles/"
				+ encodeUrlPathSegment(userProfile.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
		populateEditForm(uiModel, UserProfile.findUserProfile(id));
		return "userprofiles/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Integer id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		UserProfile userProfile = UserProfile.findUserProfile(id);
		userProfile.remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/userprofiles";
	}

	void addDateTimeFormatPatterns(Model uiModel) {
		uiModel.addAttribute(
				"userProfile_birthday_date_format",
				DateTimeFormat.patternForStyle("M-",
						LocaleContextHolder.getLocale()));
	}

	void populateEditForm(Model uiModel, UserProfile userProfile) {
		uiModel.addAttribute("userProfile", userProfile);
		addDateTimeFormatPatterns(uiModel);
		uiModel.addAttribute("addresses", Address.findAllAddresses());
		uiModel.addAttribute("users", Users.findAllUsers());
	}

	String encodeUrlPathSegment(String pathSegment,
			HttpServletRequest httpServletRequest) {
		String enc = httpServletRequest.getCharacterEncoding();
		if (enc == null) {
			enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
		}
		try {
			pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
		} catch (UnsupportedEncodingException uee) {
		}
		return pathSegment;
	}
}
