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
import rs.id.webzine.domain.Article;
import rs.id.webzine.domain.ArticleBookmark;
import rs.id.webzine.domain.ArticleCategory;
import rs.id.webzine.domain.ArticleComment;
import rs.id.webzine.domain.ArticleRate;
import rs.id.webzine.domain.Category;
import rs.id.webzine.domain.News;
import rs.id.webzine.domain.Roles;
import rs.id.webzine.domain.Task;
import rs.id.webzine.domain.TaskAttachment;
import rs.id.webzine.domain.TaskComment;
import rs.id.webzine.domain.UserArticle;
import rs.id.webzine.domain.UserProfile;
import rs.id.webzine.domain.Users;

@RequestMapping("/userses")
@Controller
public class UsersController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid Users users, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, users);
			return "userses/create";
		}
		uiModel.asMap().clear();
		users.persist();
		return "redirect:/userses/"
				+ encodeUrlPathSegment(users.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new Users());
		return "userses/create";
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Integer id, Model uiModel) {
		uiModel.addAttribute("users", Users.findUsers(id));
		uiModel.addAttribute("itemId", id);
		return "userses/show";
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
			uiModel.addAttribute("userses",
					Users.findUsersEntries(firstResult, sizeNo));
			float nrOfPages = (float) Users.countUserses() / sizeNo;
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else {
			uiModel.addAttribute("userses", Users.findAllUserses());
		}
		return "userses/list";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid Users users, BindingResult bindingResult,
			Model uiModel, HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, users);
			return "userses/update";
		}
		uiModel.asMap().clear();
		users.merge();
		return "redirect:/userses/"
				+ encodeUrlPathSegment(users.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
		populateEditForm(uiModel, Users.findUsers(id));
		return "userses/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Integer id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		Users users = Users.findUsers(id);
		users.remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/userses";
	}

	void populateEditForm(Model uiModel, Users users) {
		uiModel.addAttribute("users", users);
		uiModel.addAttribute("articles", Article.findAllArticles());
		uiModel.addAttribute("articlebookmarks",
				ArticleBookmark.findAllArticleBookmarks());
		uiModel.addAttribute("articlecategorys",
				ArticleCategory.findAllArticleCategorys());
		uiModel.addAttribute("articlecomments",
				ArticleComment.findAllArticleComments());
		uiModel.addAttribute("articlerates", ArticleRate.findAllArticleRates());
		uiModel.addAttribute("categorys", Category.findAllCategorys());
		uiModel.addAttribute("newsitems", News.findAllNews());
		uiModel.addAttribute("roles", Roles.findAllRoles());
		uiModel.addAttribute("tasks", Task.findAllTasks());
		uiModel.addAttribute("taskattachments",
				TaskAttachment.findAllTaskAttachments());
		uiModel.addAttribute("taskcomments", TaskComment.findAllTaskComments());
		uiModel.addAttribute("userarticles", UserArticle.findAllUserArticles());
		uiModel.addAttribute("userprofiles", UserProfile.findAllUserProfiles());
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
