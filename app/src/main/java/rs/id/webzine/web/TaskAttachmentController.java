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
import rs.id.webzine.domain.Task;
import rs.id.webzine.domain.TaskAttachment;
import rs.id.webzine.domain.Users;

@RequestMapping("/taskattachments")
@Controller
public class TaskAttachmentController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid TaskAttachment taskAttachment,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, taskAttachment);
			return "taskattachments/create";
		}
		uiModel.asMap().clear();
		taskAttachment.persist();
		return "redirect:/taskattachments/"
				+ encodeUrlPathSegment(taskAttachment.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new TaskAttachment());
		return "taskattachments/create";
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Integer id, Model uiModel) {
		addDateTimeFormatPatterns(uiModel);
		uiModel.addAttribute("taskattachment",
				TaskAttachment.findTaskAttachment(id));
		uiModel.addAttribute("itemId", id);
		return "taskattachments/show";
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
			uiModel.addAttribute("taskattachments", TaskAttachment
					.findTaskAttachmentEntries(firstResult, sizeNo));
			float nrOfPages = (float) TaskAttachment.countTaskAttachments()
					/ sizeNo;
			uiModel.addAttribute(
					"maxPages",
					(int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
							: nrOfPages));
		} else {
			uiModel.addAttribute("taskattachments",
					TaskAttachment.findAllTaskAttachments());
		}
		addDateTimeFormatPatterns(uiModel);
		return "taskattachments/list";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid TaskAttachment taskAttachment,
			BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, taskAttachment);
			return "taskattachments/update";
		}
		uiModel.asMap().clear();
		taskAttachment.merge();
		return "redirect:/taskattachments/"
				+ encodeUrlPathSegment(taskAttachment.getId().toString(),
						httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
		populateEditForm(uiModel, TaskAttachment.findTaskAttachment(id));
		return "taskattachments/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Integer id,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size,
			Model uiModel) {
		TaskAttachment taskAttachment = TaskAttachment.findTaskAttachment(id);
		taskAttachment.remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/taskattachments";
	}

	void addDateTimeFormatPatterns(Model uiModel) {
		uiModel.addAttribute(
				"taskAttachment_dc_date_format",
				DateTimeFormat.patternForStyle("MM",
						LocaleContextHolder.getLocale()));
		uiModel.addAttribute(
				"taskAttachment_dm_date_format",
				DateTimeFormat.patternForStyle("MM",
						LocaleContextHolder.getLocale()));
	}

	void populateEditForm(Model uiModel, TaskAttachment taskAttachment) {
		uiModel.addAttribute("taskAttachment", taskAttachment);
		addDateTimeFormatPatterns(uiModel);
		uiModel.addAttribute("tasks", Task.findAllTasks());
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
