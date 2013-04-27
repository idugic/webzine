package rs.id.webzine.controller.project_management;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import rs.id.webzine.controller.WebController;
import rs.id.webzine.entity.project_management.TaskPriority;
import rs.id.webzine.service.project_management.TaskPriorityService;

@RequestMapping(TaskPriorityController.PATH)
@Controller
public class TaskPriorityController extends WebController {

  public static final String PATH = "admin/project_management/task_priority";

  @Autowired
  TaskPriorityService taskPriorityService;

  @RequestMapping(produces = "text/html")
  public String list(@RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    prepareList(taskPriorityService, "taskPriorityList", page, size, uiModel);
    return PATH + "/" + LIST;

  }

  @RequestMapping(params = "form", produces = "text/html")
  public String createForm(Model uiModel) {
    populateEditForm(uiModel, new TaskPriority());
    return PATH + "/" + CREATE;
  }

  @RequestMapping(method = RequestMethod.POST, produces = "text/html")
  public String create(@Valid TaskPriority taskPriority, BindingResult bindingResult, Model uiModel,
      HttpServletRequest httpServletRequest) {

    // validate
    TaskPriorityCreateValidator validator = new TaskPriorityCreateValidator();
    validator.validate(taskPriority, bindingResult);
    if (bindingResult.hasErrors()) {
      populateEditForm(uiModel, taskPriority);
      return PATH + "/" + CREATE;
    }

    taskPriorityService.create(taskPriority);

    uiModel.asMap().clear();
    return REDIRECT + PATH + "/" + encodeUrlPathSegment(taskPriority.getId().toString(), httpServletRequest);
  }

  @RequestMapping(value = "/{id}", produces = "text/html")
  public String show(@PathVariable("id") Integer id, Model uiModel) {
    uiModel.addAttribute("taskPriority", taskPriorityService.find(id));
    uiModel.addAttribute("itemId", id);
    return PATH + "/" + SHOW;
  }

  @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
  public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
    populateEditForm(uiModel, taskPriorityService.find(id));
    return PATH + "/" + UPDATE;
  }

  @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
  public String update(@Valid TaskPriority taskPriority, BindingResult bindingResult, Model uiModel,
      HttpServletRequest httpServletRequest) {
    if (bindingResult.hasErrors()) {
      populateEditForm(uiModel, taskPriority);
      return PATH + "/" + UPDATE;
    }

    taskPriorityService.update(taskPriority);

    uiModel.asMap().clear();
    return REDIRECT + PATH + "/" + encodeUrlPathSegment(taskPriority.getId().toString(), httpServletRequest);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
  public String delete(@PathVariable("id") Integer id, @RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    taskPriorityService.delete(id);

    uiModel.asMap().clear();
    return REDIRECT + PATH;
  }

  void populateEditForm(Model uiModel, TaskPriority taskPriority) {
    uiModel.addAttribute("taskPriority", taskPriority);
  }

  private class TaskPriorityCreateValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
      return true;
    }

    @Override
    public void validate(Object target, Errors errors) {
      TaskPriority form = (TaskPriority) target;

      TaskPriority taskPriority = taskPriorityService.findForCd(form.getCd());
      if (taskPriority != null) {
        errors.rejectValue("cd", "validation_code_already_exists");
      }
    }
  }
}
