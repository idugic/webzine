package rs.id.webzine.web;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;
import rs.id.webzine.domain.Ad;
import rs.id.webzine.domain.Article;
import rs.id.webzine.domain.ManagedContent;

@RequestMapping("/managedcontents")
@Controller
public class ManagedContentController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid ManagedContent managedContent,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, managedContent);
			return "managedcontents/create";
		}
		uiModel.asMap().clear();
		managedContent.persist();
		return "redirect:/managedcontents/"
				+ encodeUrlPathSegment(managedContent.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new ManagedContent());
		return "managedcontents/create";
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Integer id, Model uiModel) {
		uiModel.addAttribute("managedcontent",
				ManagedContent.findManagedContent(id));
		uiModel.addAttribute("itemId", id);
		return "managedcontents/show";
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
			uiModel.addAttribute("managedcontents", ManagedContent
					.findManagedContentEntries(firstResult, sizeNo));
			float nrOfPages = (float) ManagedContent.countManagedContents()
					/ sizeNo;
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else {
			uiModel.addAttribute("managedcontents",
					ManagedContent.findAllManagedContents());
		}
		return "managedcontents/list";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid ManagedContent managedContent,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, managedContent);
			return "managedcontents/update";
		}
		uiModel.asMap().clear();
		managedContent.merge();
		return "redirect:/managedcontents/"
				+ encodeUrlPathSegment(managedContent.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
		populateEditForm(uiModel, ManagedContent.findManagedContent(id));
		return "managedcontents/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Integer id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		ManagedContent managedContent = ManagedContent.findManagedContent(id);
		managedContent.remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/managedcontents";
	}

	void populateEditForm(Model uiModel, ManagedContent managedContent) {
		uiModel.addAttribute("managedContent", managedContent);
		uiModel.addAttribute("ads", Ad.findAllAds());
		uiModel.addAttribute("articles", Article.findAllArticles());
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
