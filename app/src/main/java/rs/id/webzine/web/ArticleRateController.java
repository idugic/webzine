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
import rs.id.webzine.domain.magazine.Article;
import rs.id.webzine.domain.magazine.ArticleRate;
import rs.id.webzine.domain.system.User;

@RequestMapping("/articlerates")
@Controller
public class ArticleRateController {

//	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
//	public String create(@Valid ArticleRate articleRate,
//			BindingResult bindingResult, Model uiModel,
//			HttpServletRequest httpServletRequest) {
//		if (bindingResult.hasErrors()) {
//			populateEditForm(uiModel, articleRate);
//			return "articlerates/create";
//		}
//		uiModel.asMap().clear();
//		articleRate.persist();
//		return "redirect:/articlerates/"
//				+ encodeUrlPathSegment(articleRate.getId().toString(),
//						httpServletRequest);
//	}
//
//	@RequestMapping(params = "form", produces = "text/html")
//	public String createForm(Model uiModel) {
//		populateEditForm(uiModel, new ArticleRate());
//		return "articlerates/create";
//	}
//
//	@RequestMapping(value = "/{id}", produces = "text/html")
//	public String show(@PathVariable("id") Integer id, Model uiModel) {
//		addDateTimeFormatPatterns(uiModel);
//		uiModel.addAttribute("articlerate", ArticleRate.findArticleRate(id));
//		uiModel.addAttribute("itemId", id);
//		return "articlerates/show";
//	}
//
//	@RequestMapping(produces = "text/html")
//	public String list(
//			@RequestParam(value = "page", required = false) Integer page,
//			@RequestParam(value = "size", required = false) Integer size,
//			Model uiModel) {
//		if (page != null || size != null) {
//			int sizeNo = size == null ? 10 : size.intValue();
//			final int firstResult = page == null ? 0 : (page.intValue() - 1)
//					* sizeNo;
//			uiModel.addAttribute("articlerates",
//					ArticleRate.findArticleRateEntries(firstResult, sizeNo));
//			float nrOfPages = (float) ArticleRate.countArticleRates() / sizeNo;
//			uiModel.addAttribute(
//					"maxPages",
//					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
//							: nrOfPages));
//		} else {
//			uiModel.addAttribute("articlerates",
//					ArticleRate.findAllArticleRates());
//		}
//		addDateTimeFormatPatterns(uiModel);
//		return "articlerates/list";
//	}
//
//	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
//	public String update(@Valid ArticleRate articleRate,
//			BindingResult bindingResult, Model uiModel,
//			HttpServletRequest httpServletRequest) {
//		if (bindingResult.hasErrors()) {
//			populateEditForm(uiModel, articleRate);
//			return "articlerates/update";
//		}
//		uiModel.asMap().clear();
//		articleRate.merge();
//		return "redirect:/articlerates/"
//				+ encodeUrlPathSegment(articleRate.getId().toString(),
//						httpServletRequest);
//	}
//
//	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
//	public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
//		populateEditForm(uiModel, ArticleRate.findArticleRate(id));
//		return "articlerates/update";
//	}
//
//	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
//	public String delete(@PathVariable("id") Integer id,
//			@RequestParam(value = "page", required = false) Integer page,
//			@RequestParam(value = "size", required = false) Integer size,
//			Model uiModel) {
//		ArticleRate articleRate = ArticleRate.findArticleRate(id);
//		articleRate.remove();
//		uiModel.asMap().clear();
//		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
//		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
//		return "redirect:/articlerates";
//	}
//
//	void addDateTimeFormatPatterns(Model uiModel) {
//		uiModel.addAttribute(
//				"articleRate_dc_date_format",
//				DateTimeFormat.patternForStyle("MM",
//						LocaleContextHolder.getLocale()));
//		uiModel.addAttribute(
//				"articleRate_dm_date_format",
//				DateTimeFormat.patternForStyle("MM",
//						LocaleContextHolder.getLocale()));
//	}
//
//	void populateEditForm(Model uiModel, ArticleRate articleRate) {
//		uiModel.addAttribute("articleRate", articleRate);
//		addDateTimeFormatPatterns(uiModel);
//		uiModel.addAttribute("articles", Article.findAll());
//		// uiModel.addAttribute("users", User.findAll());
//	}
//
//	String encodeUrlPathSegment(String pathSegment,
//			HttpServletRequest httpServletRequest) {
//		String enc = httpServletRequest.getCharacterEncoding();
//		if (enc == null) {
//			enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
//		}
//		try {
//			pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
//		} catch (UnsupportedEncodingException uee) {
//		}
//		return pathSegment;
//	}
}
