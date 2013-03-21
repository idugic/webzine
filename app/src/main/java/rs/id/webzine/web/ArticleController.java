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
import rs.id.webzine.domain.Ad;
import rs.id.webzine.domain.Article;
import rs.id.webzine.domain.ArticleBookmark;
import rs.id.webzine.domain.ArticleCategory;
import rs.id.webzine.domain.ArticleComment;
import rs.id.webzine.domain.ArticleRate;
import rs.id.webzine.domain.ManagedContent;
import rs.id.webzine.domain.Users;

@RequestMapping("/articles")
@Controller
public class ArticleController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid Article article, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, article);
			return "articles/create";
		}
		uiModel.asMap().clear();
		article.persist();
		return "redirect:/articles/"
				+ encodeUrlPathSegment(article.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new Article());
		return "articles/create";
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Integer id, Model uiModel) {
		addDateTimeFormatPatterns(uiModel);
		uiModel.addAttribute("article", Article.findArticle(id));
		uiModel.addAttribute("itemId", id);
		return "articles/show";
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
			uiModel.addAttribute("articles",
					Article.findArticleEntries(firstResult, sizeNo));
			float nrOfPages = (float) Article.countArticles() / sizeNo;
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else {
			uiModel.addAttribute("articles", Article.findAllArticles());
		}
		addDateTimeFormatPatterns(uiModel);
		return "articles/list";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid Article article, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, article);
			return "articles/update";
		}
		uiModel.asMap().clear();
		article.merge();
		return "redirect:/articles/"
				+ encodeUrlPathSegment(article.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
		populateEditForm(uiModel, Article.findArticle(id));
		return "articles/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Integer id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		Article article = Article.findArticle(id);
		article.remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/articles";
	}

	void addDateTimeFormatPatterns(Model uiModel) {
		uiModel.addAttribute(
				"article_publishedat_date_format",
				DateTimeFormat.patternForStyle("MM",
						LocaleContextHolder.getLocale()));
		uiModel.addAttribute(
				"article_dc_date_format",
				DateTimeFormat.patternForStyle("MM",
						LocaleContextHolder.getLocale()));
		uiModel.addAttribute(
				"article_dm_date_format",
				DateTimeFormat.patternForStyle("MM",
						LocaleContextHolder.getLocale()));
	}

	void populateEditForm(Model uiModel, Article article) {
		uiModel.addAttribute("article", article);
		addDateTimeFormatPatterns(uiModel);
		uiModel.addAttribute("ads", Ad.findAllAds());
		uiModel.addAttribute("articlebookmarks",
				ArticleBookmark.findAllArticleBookmarks());
		uiModel.addAttribute("articlecategorys",
				ArticleCategory.findAllArticleCategorys());
		uiModel.addAttribute("articlecomments",
				ArticleComment.findAllArticleComments());
		uiModel.addAttribute("articlerates", ArticleRate.findAllArticleRates());
		uiModel.addAttribute("managedcontents",
				ManagedContent.findAllManagedContents());
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
