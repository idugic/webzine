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
import rs.id.webzine.entity.project_management.TaskStatus;
import rs.id.webzine.service.project_management.TaskStatusService;

@RequestMapping(TaskStatusController.PATH)
@Controller
public class TaskStatusController extends WebController {

  public static final String PATH = "admin/project_management/task_status";

  @Autowired
  TaskStatusService taskStatusService;

  @RequestMapping(produces = "text/html")
  public String list(@RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    prepareList(taskStatusService, "taskStatusList", page, size, uiModel);
    return PATH + "/" + LIST;

  }

  @RequestMapping(params = "form", produces = "text/html")
  public String createForm(Model uiModel) {
    populateEditForm(uiModel, new TaskStatus());
    return PATH + "/" + CREATE;
  }

  @RequestMapping(method = RequestMethod.POST, produces = "text/html")
  public String create(@Valid TaskStatus taskStatus, BindingResult bindingResult, Model uiModel,
      HttpServletRequest httpServletRequest) {

    // validate
    TaskStatusCreateValidator validator = new TaskStatusCreateValidator();
    validator.validate(taskStatus, bindingResult);
    if (bindingResult.hasErrors()) {
      populateEditForm(uiModel, taskStatus);
      return PATH + "/" + CREATE;
    }

    taskStatusService.create(taskStatus);

    uiModel.asMap().clear();
    return REDIRECT + PATH + "/" + encodeUrlPathSegment(taskStatus.getId().toString(), httpServletRequest);
  }

  @RequestMapping(value = "/{id}", produces = "text/html")
  public String show(@PathVariable("id") Integer id, Model uiModel) {
    uiModel.addAttribute("taskStatus", taskStatusService.find(id));
    uiModel.addAttribute("itemId", id);
    return PATH + "/" + SHOW;
  }

  @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
  public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
    populateEditForm(uiModel, taskStatusService.find(id));
    return PATH + "/" + UPDATE;
  }

  @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
  public String update(@Valid TaskStatus taskStatus, BindingResult bindingResult, Model uiModel,
      HttpServletRequest httpServletRequest) {
    if (bindingResult.hasErrors()) {
      populateEditForm(uiModel, taskStatus);
      return PATH + "/" + UPDATE;
    }

    taskStatusService.update(taskStatus);

    uiModel.asMap().clear();
    return REDIRECT + PATH + "/" + encodeUrlPathSegment(taskStatus.getId().toString(), httpServletRequest);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
  public String delete(@PathVariable("id") Integer id, @RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    taskStatusService.delete(id);

    uiModel.asMap().clear();
    return REDIRECT + PATH;
  }

  void populateEditForm(Model uiModel, TaskStatus taskStatus) {
    uiModel.addAttribute("taskStatus", taskStatus);
  }

  private class TaskStatusCreateValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
      return true;
    }

    @Override
    public void validate(Object target, Errors errors) {
      TaskStatus form = (TaskStatus) target;

      TaskStatus taskStatus = taskStatusService.findForCd(form.getCd());
      if (taskStatus != null) {
        errors.rejectValue("cd", "validation_code_already_exists");
      }
    }
  }
}
