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
import rs.id.webzine.domain.Article;
import rs.id.webzine.domain.ArticleBookmark;
import rs.id.webzine.domain.User;

@RequestMapping("/articlebookmarks")
@Controller
public class ArticleBookmarkController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid ArticleBookmark articleBookmark,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, articleBookmark);
			return "articlebookmarks/create";
		}
		uiModel.asMap().clear();
		articleBookmark.persist();
		return "redirect:/articlebookmarks/"
				+ encodeUrlPathSegment(articleBookmark.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new ArticleBookmark());
		return "articlebookmarks/create";
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Integer id, Model uiModel) {
		addDateTimeFormatPatterns(uiModel);
		uiModel.addAttribute("articlebookmark",
				ArticleBookmark.findArticleBookmark(id));
		uiModel.addAttribute("itemId", id);
		return "articlebookmarks/show";
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
			uiModel.addAttribute("articlebookmarks", ArticleBookmark
					.findArticleBookmarkEntries(firstResult, sizeNo));
			float nrOfPages = (float) ArticleBookmark.countArticleBookmarks()
					/ sizeNo;
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else {
			uiModel.addAttribute("articlebookmarks",
					ArticleBookmark.findAllArticleBookmarks());
		}
		addDateTimeFormatPatterns(uiModel);
		return "articlebookmarks/list";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid ArticleBookmark articleBookmark,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, articleBookmark);
			return "articlebookmarks/update";
		}
		uiModel.asMap().clear();
		articleBookmark.merge();
		return "redirect:/articlebookmarks/"
				+ encodeUrlPathSegment(articleBookmark.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
		populateEditForm(uiModel, ArticleBookmark.findArticleBookmark(id));
		return "articlebookmarks/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Integer id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		ArticleBookmark articleBookmark = ArticleBookmark
				.findArticleBookmark(id);
		articleBookmark.remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/articlebookmarks";
	}

	void addDateTimeFormatPatterns(Model uiModel) {
		uiModel.addAttribute(
				"articleBookmark_dc_date_format",
				DateTimeFormat.patternForStyle("MM",
						LocaleContextHolder.getLocale()));
		uiModel.addAttribute(
				"articleBookmark_dm_date_format",
				DateTimeFormat.patternForStyle("MM",
						LocaleContextHolder.getLocale()));
	}

	void populateEditForm(Model uiModel, ArticleBookmark articleBookmark) {
		uiModel.addAttribute("articleBookmark", articleBookmark);
		addDateTimeFormatPatterns(uiModel);
		uiModel.addAttribute("articles", Article.findAllArticles());
		uiModel.addAttribute("users", User.findAll());
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
