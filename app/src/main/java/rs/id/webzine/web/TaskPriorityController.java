package rs.id.webzine.web;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import rs.id.webzine.domain.TaskPriority;

@RequestMapping("admin/task_priority")
@Controller
public class TaskPriorityController extends WebController {

	class TaskPriorityCreateValidator implements Validator {
		@Override
		public boolean supports(Class<?> clazz) {
			return true;
		}

		@Override
		public void validate(Object target, Errors errors) {
			TaskPriority form = (TaskPriority) target;
			TaskPriority taskPriority = TaskPriority.findForCd(form.getCd());
			if (taskPriority != null) {
				errors.rejectValue("cd", "validation.taskpriority.cd.duplicate");
			}
		}
	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new TaskPriority());
		return "admin/task_priority/create";
	}

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid TaskPriority taskPriority, BindingResult bindingResult, Model uiModel,
	        HttpServletRequest httpServletRequest) {
		// bind
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, taskPriority);
			return "admin/task_priority/create";
		}

		// validate
		TaskPriorityCreateValidator validator = new TaskPriorityCreateValidator();
		validator.validate(taskPriority, bindingResult);
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, taskPriority);
			return "admin/task_priority/create";
		}

		// persist
		taskPriority.persist();

		// show
		uiModel.asMap().clear();
		return "redirect:/admin/task_priority/"
		        + encodeUrlPathSegment(taskPriority.getId().toString(), httpServletRequest);
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Integer id, Model uiModel) {
		uiModel.addAttribute("taskPriority", TaskPriority.find(id));
		uiModel.addAttribute("itemId", id);
		return "admin/task_priority/show";
	}

	@RequestMapping(produces = "text/html")
	public String list(@RequestParam(value = "page", required = false) Integer page,
	        @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
			uiModel.addAttribute("taskPriority", TaskPriority.findEntries(firstResult, sizeNo));
			float nrOfPages = (float) TaskPriority.count() / sizeNo;
			uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
			        : nrOfPages));
		} else {
			uiModel.addAttribute("taskPriority", TaskPriority.findAll());
		}
		return "admin/task_priority/list";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid TaskPriority taskPriority, BindingResult bindingResult, Model uiModel,
	        HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, taskPriority);
			return "admin/task_priority/update";
		}

		uiModel.asMap().clear();
		taskPriority.merge();
		return "redirect:/admin/task_priority/"
		        + encodeUrlPathSegment(taskPriority.getId().toString(), httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
		populateEditForm(uiModel, TaskPriority.find(id));
		return "admin/task_priority/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Integer id, @RequestParam(value = "page", required = false) Integer page,
	        @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		TaskPriority taskPriority = TaskPriority.find(id);
		taskPriority.remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/admin/task_priority";
	}

	void populateEditForm(Model uiModel, TaskPriority taskPriority) {
		uiModel.addAttribute("taskPriority", taskPriority);
	}
}
