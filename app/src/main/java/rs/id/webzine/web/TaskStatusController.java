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

import rs.id.webzine.domain.TaskStatus;

@RequestMapping("admin/task_status")
@Controller
public class TaskStatusController extends ModelController {

	class TaskStatusCreateValidator implements Validator {
		@Override
		public boolean supports(Class<?> clazz) {
			return true;
		}

		@Override
		public void validate(Object target, Errors errors) {
			TaskStatus form = (TaskStatus) target;
			TaskStatus taskStatus = TaskStatus.findForCd(form.getCd());
			if (taskStatus != null) {
				errors.rejectValue("cd", "validation.taskstatus.cd.duplicate");
			}
		}
	}

	@RequestMapping(params = "form", produces = "text/html")
	public String createForm(Model uiModel) {
		populateEditForm(uiModel, new TaskStatus());
		return "admin/task_status/create";
	}

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
	public String create(@Valid TaskStatus taskStatus, BindingResult bindingResult, Model uiModel,
	        HttpServletRequest httpServletRequest) {
		// bind
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, taskStatus);
			return "admin/task_status/create";
		}

		// validate
		TaskStatusCreateValidator validator = new TaskStatusCreateValidator();
		validator.validate(taskStatus, bindingResult);
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, taskStatus);
			return "admin/task_status/create";
		}

		// persist
		taskStatus.persist();

		// show
		uiModel.asMap().clear();
		return "redirect:/admin/task_status/" + encodeUrlPathSegment(taskStatus.getId().toString(), httpServletRequest);
	}

	@RequestMapping(value = "/{id}", produces = "text/html")
	public String show(@PathVariable("id") Integer id, Model uiModel) {
		uiModel.addAttribute("taskStatus", TaskStatus.find(id));
		uiModel.addAttribute("itemId", id);
		return "admin/task_status/show";
	}

	@RequestMapping(produces = "text/html")
	public String list(@RequestParam(value = "page", required = false) Integer page,
	        @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		if (page != null || size != null) {
			int sizeNo = size == null ? 10 : size.intValue();
			final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
			uiModel.addAttribute("taskStatus", TaskStatus.findEntries(firstResult, sizeNo));
			float nrOfPages = (float) TaskStatus.count() / sizeNo;
			uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
			        : nrOfPages));
		} else {
			uiModel.addAttribute("taskStatus", TaskStatus.findAll());
		}
		return "admin/task_status/list";
	}

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
	public String update(@Valid TaskStatus taskStatus, BindingResult bindingResult, Model uiModel,
	        HttpServletRequest httpServletRequest) {
		if (bindingResult.hasErrors()) {
			populateEditForm(uiModel, taskStatus);
			return "admin/task_status/update";
		}

		uiModel.asMap().clear();
		taskStatus.merge();
		return "redirect:/admin/task_status/" + encodeUrlPathSegment(taskStatus.getId().toString(), httpServletRequest);
	}

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
	public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
		populateEditForm(uiModel, TaskStatus.find(id));
		return "admin/task_status/update";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
	public String delete(@PathVariable("id") Integer id, @RequestParam(value = "page", required = false) Integer page,
	        @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
		TaskStatus taskStatus = TaskStatus.find(id);
		taskStatus.remove();
		uiModel.asMap().clear();
		uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
		uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
		return "redirect:/admin/task_status";
	}

	void populateEditForm(Model uiModel, TaskStatus taskStatus) {
		uiModel.addAttribute("taskStatus", taskStatus);
	}
}
