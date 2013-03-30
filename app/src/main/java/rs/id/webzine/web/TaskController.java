package rs.id.webzine.web;

import java.util.Calendar;

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

import rs.id.webzine.domain.Task;
import rs.id.webzine.domain.TaskAttachment;
import rs.id.webzine.domain.TaskComment;
import rs.id.webzine.domain.TaskPriority;
import rs.id.webzine.domain.TaskStatus;
import rs.id.webzine.domain.User;

@RequestMapping("admin/task")
@Controller
public class TaskController extends ModelController {

  @RequestMapping(method = RequestMethod.POST, produces = "text/html")
  public String create(@Valid Task task, BindingResult bindingResult, Model uiModel,
      HttpServletRequest httpServletRequest) {
    if (bindingResult.hasErrors()) {
      populateEditForm(uiModel, task);
      return "admin/task/create";
    }
    uiModel.asMap().clear();

    task.setStatusId(TaskStatus.findForCd(TaskStatus.CD_ACTIVE));
    task.setUc(getCurrentUser());
    task.setDc(Calendar.getInstance());
    task.persist();
    return "redirect:/admin/task/" + encodeUrlPathSegment(task.getId().toString(), httpServletRequest);
  }

  @RequestMapping(params = "form", produces = "text/html")
  public String createForm(Model uiModel) {
    populateEditForm(uiModel, new Task());
    return "admin/task/create";
  }

  @RequestMapping(value = "/{id}", produces = "text/html")
  public String show(@PathVariable("id") Integer id, Model uiModel) {
    addDateTimeFormatPatterns(uiModel);
    uiModel.addAttribute("task", Task.find(id));
    uiModel.addAttribute("taskCommentList", TaskComment.findForTask(id));
    uiModel.addAttribute("itemId", id);
    return "admin/task/show";
  }

  @RequestMapping(produces = "text/html")
  public String list(@RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    if (page != null || size != null) {
      int sizeNo = size == null ? 10 : size.intValue();
      final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
      uiModel.addAttribute("task", Task.findEntries(firstResult, sizeNo));
      float nrOfPages = (float) Task.count() / sizeNo;
      uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
          : nrOfPages));
    } else {
      uiModel.addAttribute("task", Task.findAll());
    }
    addDateTimeFormatPatterns(uiModel);
    return "admin/task/list";
  }

  @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
  public String update(@Valid Task task, BindingResult bindingResult, Model uiModel,
      HttpServletRequest httpServletRequest) {
    if (bindingResult.hasErrors()) {
      populateEditForm(uiModel, task);
      uiModel.addAttribute("taskComment", new TaskComment());
      return "admin/task/update";
    }
    Task oldTask = Task.find(task.getId());

    task.setUc(oldTask.getUc());
    task.setDc(oldTask.getDc());
    task.setUm(getCurrentUser());
    task.setDm(Calendar.getInstance());
    task.merge();

    uiModel.asMap().clear();
    return "redirect:/admin/task/" + encodeUrlPathSegment(task.getId().toString(), httpServletRequest);
  }

  @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
  public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
    populateEditForm(uiModel, Task.find(id));
    uiModel.addAttribute("taskComment", new TaskComment());
    uiModel.addAttribute("taskAttachment", new TaskAttachment());
    return "admin/task/update";
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
  public String delete(@PathVariable("id") Integer id, @RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    Task task = Task.find(id);
    task.remove();
    uiModel.asMap().clear();
    uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
    uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
    return "redirect:/admin/task";
  }

  void addDateTimeFormatPatterns(Model uiModel) {
    uiModel.addAttribute("task_dc_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
    uiModel.addAttribute("task_dm_date_format", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
  }

  void populateEditForm(Model uiModel, Task task) {
    uiModel.addAttribute("task", task);
    addDateTimeFormatPatterns(uiModel);
    uiModel.addAttribute("parentTask", Task.findAvailableAsParent(task.getId()));
    uiModel.addAttribute("taskStatus", TaskStatus.findAll());
    uiModel.addAttribute("taskPriority", TaskPriority.findAll());
    uiModel.addAttribute("user", User.findAll());
    uiModel.addAttribute("taskCommentList", TaskComment.findForTask(task.getId()));
  }

  @RequestMapping(value = "/comment/{taskId}", method = RequestMethod.POST, produces = "text/html")
  public String create(@PathVariable("taskId") Integer taskId, @Valid TaskComment taskComment,
      BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    if (bindingResult.hasErrors()) {
      populateEditForm(uiModel, Task.find(taskId));
      uiModel.addAttribute("taskComment", taskComment);
      return "admin/task/create";
    }
    taskComment.setTaskId(Task.find(taskId));
    taskComment.setUc(getCurrentUser());
    taskComment.setDc(Calendar.getInstance());
    taskComment.persist();

    uiModel.asMap().clear();
    return "redirect:/admin/task/" + encodeUrlPathSegment(taskId.toString(), httpServletRequest);
  }

  @RequestMapping(value = "/comment/{taskId}/{id}", method = RequestMethod.DELETE, produces = "text/html")
  public String deleteComment(@PathVariable("taskId") Integer taskId, @PathVariable("id") Integer id, Model uiModel) {
    TaskComment taskComment = TaskComment.find(id);
    taskComment.remove();

    populateEditForm(uiModel, Task.find(taskId));
    uiModel.addAttribute("taskCommentList", TaskComment.findForTask(taskId));
    uiModel.addAttribute("taskComment", new TaskComment());
    uiModel.addAttribute("taskAttachment", new TaskAttachment());
    return "admin/task/update";
  }

}
