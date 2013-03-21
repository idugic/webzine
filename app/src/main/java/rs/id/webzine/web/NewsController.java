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
import rs.id.webzine.domain.News;
import rs.id.webzine.domain.Users;

@RequestMapping("/news")
@Controller
public class NewsController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid News news, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, news);
			return "news/create";
		}
		uiModel.asMap().clear();
		news.persist();
		return "redirect:/news/"
				+ encodeUrlPathSegment(news.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new News());
		return "news/create";
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Integer id, Model uiModel) {
		addDateTimeFormatPatterns(uiModel);
		uiModel.addAttribute("news", News.findNews(id));
		uiModel.addAttribute("itemId", id);
		return "news/show";
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
			uiModel.addAttribute("newsitems",
					News.findNewsEntries(firstResult, sizeNo));
			float nrOfPages = (float) News.countNews() / sizeNo;
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else {
			uiModel.addAttribute("newsitems", News.findAllNews());
		}
		addDateTimeFormatPatterns(uiModel);
		return "news/list";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid News news, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, news);
			return "news/update";
		}
		uiModel.asMap().clear();
		news.merge();
		return "redirect:/news/"
				+ encodeUrlPathSegment(news.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
		populateEditForm(uiModel, News.findNews(id));
		return "news/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Integer id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		News news = News.findNews(id);
		news.remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/news";
	}

	void addDateTimeFormatPatterns(Model uiModel) {
		uiModel.addAttribute(
				"news_publishedat_date_format",
				DateTimeFormat.patternForStyle("MM",
						LocaleContextHolder.getLocale()));
		uiModel.addAttribute(
				"news_dc_date_format",
				DateTimeFormat.patternForStyle("MM",
						LocaleContextHolder.getLocale()));
		uiModel.addAttribute(
				"news_dm_date_format",
				DateTimeFormat.patternForStyle("MM",
						LocaleContextHolder.getLocale()));
	}

	void populateEditForm(Model uiModel, News news) {
		uiModel.addAttribute("news", news);
		addDateTimeFormatPatterns(uiModel);
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
