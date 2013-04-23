package rs.id.webzine.web.project_management;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import rs.id.webzine.domain.project_management.Task;
import rs.id.webzine.domain.project_management.TaskAttachment;
import rs.id.webzine.domain.project_management.TaskComment;
import rs.id.webzine.service.project_management.TaskAttachmentService;
import rs.id.webzine.service.project_management.TaskCommentService;
import rs.id.webzine.service.project_management.TaskPriorityService;
import rs.id.webzine.service.project_management.TaskService;
import rs.id.webzine.service.project_management.TaskStatusService;
import rs.id.webzine.service.system.UserService;
import rs.id.webzine.web.WebController;

@RequestMapping(TaskController.PATH)
@Controller
public class TaskController extends WebController {

  private static final Log log = LogFactory.getLog(TaskController.class);

  public static final String PATH = "admin/project_management/task";

  @Autowired
  TaskStatusService taskStatusService;

  @Autowired
  TaskPriorityService taskPriorityService;

  @Autowired
  TaskService taskService;

  @Autowired
  UserService userService;

  @Autowired
  TaskCommentService taskCommentService;

  @Autowired
  TaskAttachmentService taskAttachmentService;

  @InitBinder
  protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws ServletException {
    binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
  }

  @RequestMapping(produces = "text/html")
  public String list(@RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    prepareList(taskService, "taskList", page, size, uiModel);
    return PATH + "/" + LIST;
  }

  @RequestMapping(params = "form", produces = "text/html")
  public String createForm(Model uiModel) {
    populateEditForm(uiModel, new Task());
    return PATH + "/" + CREATE;
  }

  private void populateEditForm(Model uiModel, Task task) {
    addDateTimePattern(uiModel);

    uiModel.addAttribute("task", task);
    uiModel.addAttribute("parentTaskList", taskService.findAvailableAsParent(task.getId()));
    uiModel.addAttribute("taskStatusList", taskStatusService.findAll());
    uiModel.addAttribute("taskPriorityList", taskPriorityService.findAll());
    uiModel.addAttribute("userList", userService.findForSystem());
  }

  @RequestMapping(method = RequestMethod.POST, produces = "text/html")
  public String create(Task task, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    if (bindingResult.hasErrors()) {
      populateEditForm(uiModel, task);
      return PATH + "/" + CREATE;
    }

    taskService.create(task);

    uiModel.asMap().clear();
    return REDIRECT + PATH + "/" + encodeUrlPathSegment(task.getId().toString(), httpServletRequest);
  }

  @RequestMapping(value = "/{id}", produces = "text/html")
  public String show(@PathVariable("id") Integer id, Model uiModel) {
    populateShowForm(uiModel, id);
    return PATH + "/" + SHOW;
  }

  private void populateShowForm(Model uiModel, Integer taskId) {
    addDateTimePattern(uiModel);

    uiModel.addAttribute("task", taskService.find(taskId));
    uiModel.addAttribute("parentTaskList", taskService.findAvailableAsParent(taskId));
    uiModel.addAttribute("taskStatusList", taskStatusService.findAll());
    uiModel.addAttribute("taskPriorityList", taskPriorityService.findAll());
    uiModel.addAttribute("userList", userService.findForSystem());

    uiModel.addAttribute("taskCommentList", taskCommentService.findForTask(taskId));
    uiModel.addAttribute("taskAttachmentList", taskAttachmentService.findForTask(taskId));

    uiModel.addAttribute("taskComment", new TaskComment());
    uiModel.addAttribute("taskAttachment", new TaskAttachment());

    uiModel.addAttribute("itemId", taskId);
  }

  @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
  public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
    populateEditForm(uiModel, taskService.find(id));
    return PATH + "/" + UPDATE;
  }

  @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
  public String update(Task task, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    if (bindingResult.hasErrors()) {
      populateEditForm(uiModel, task);
      return PATH + "/" + UPDATE;
    }

    taskService.update(task);

    uiModel.asMap().clear();
    return REDIRECT + PATH + "/" + encodeUrlPathSegment(task.getId().toString(), httpServletRequest);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
  public String delete(@PathVariable("id") Integer id, Model uiModel) {
    taskService.delete(id);

    uiModel.asMap().clear();
    return REDIRECT + PATH;
  }

  @RequestMapping(value = "/comment/{taskId}", method = RequestMethod.POST, produces = "text/html")
  public String createComment(@PathVariable("taskId") Integer taskId, TaskComment taskComment,
      BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    if (bindingResult.hasErrors()) {
      populateShowForm(uiModel, taskId);
      return PATH + "/" + SHOW;
    }

    taskService.createComment(taskId, taskComment);

    uiModel.asMap().clear();
    return REDIRECT + PATH + "/" + encodeUrlPathSegment(taskId.toString(), httpServletRequest);
  }

  @RequestMapping(value = "/comment/{taskId}/{id}", method = RequestMethod.DELETE, produces = "text/html")
  public String deleteComment(@PathVariable("taskId") Integer taskId, @PathVariable("id") Integer id, Model uiModel,
      HttpServletRequest httpServletRequest) {
    taskCommentService.delete(id);

    return REDIRECT + PATH + "/" + encodeUrlPathSegment(taskId.toString(), httpServletRequest);
  }

  @RequestMapping(value = "/attachment/{taskId}", method = RequestMethod.POST, produces = "text/html")
  public String createAttachment(@PathVariable("taskId") Integer taskId, TaskAttachment taskAttachment,
      @RequestParam("content") MultipartFile content, BindingResult bindingResult, Model uiModel,
      HttpServletRequest httpServletRequest) {
    try {
      // bind
      if (bindingResult.hasErrors()) {
        populateShowForm(uiModel, taskId);
        return PATH + "/" + SHOW;
      }

      if (taskAttachment.getContent() == null || taskAttachment.getContent().length == 0) {
        populateShowForm(uiModel, taskId);
        uiModel.addAttribute("attachment_required");
        return PATH + "/" + SHOW;
      }

      taskAttachment.setName(content.getOriginalFilename());
      taskAttachment.setContentType(content.getContentType());
      taskAttachment.setContentSize(content.getSize());

      taskService.createAttachment(taskId, taskAttachment);

      uiModel.asMap().clear();
      return REDIRECT + PATH + "/" + encodeUrlPathSegment(taskId.toString(), httpServletRequest);
    } catch (Exception e) {
      log.error(e);
      throw new RuntimeException(e);
    }
  }

  @RequestMapping(value = "/attachment/{taskId}/{id}", method = RequestMethod.DELETE, produces = "text/html")
  public String deleteAttachment(@PathVariable("taskId") Integer taskId, @PathVariable("id") Integer id, Model uiModel,
      HttpServletRequest httpServletRequest) {
    taskAttachmentService.delete(id);

    uiModel.asMap().clear();
    return REDIRECT + PATH + "/" + encodeUrlPathSegment(taskId.toString(), httpServletRequest);
  }

  @RequestMapping(value = "/attachment/{taskId}/{id}", method = RequestMethod.GET)
  public String downloadAttachment(@PathVariable("taskId") Integer taskId, @PathVariable("id") Integer id,
      HttpServletResponse response, Model model) {
    try {
      TaskAttachment taskAttachment = taskAttachmentService.find(id);

      response.setHeader("Content-Disposition", "attachment; filename=\"" + taskAttachment.getName() + "\"");
      response.setContentType(taskAttachment.getContentType());

      OutputStream out = response.getOutputStream();
      IOUtils.copy(new ByteArrayInputStream(taskAttachment.getContent()), out);
      out.flush();

      return null;
    } catch (Exception e) {
      log.error(e);
      throw new RuntimeException(e);
    }
  }

}
