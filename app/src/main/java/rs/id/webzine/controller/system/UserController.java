package rs.id.webzine.controller.system;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import rs.id.webzine.entity.system.User;
import rs.id.webzine.service.system.RoleService;
import rs.id.webzine.service.system.UserService;
import rs.id.webzine.service.system.UserStatusService;

@RequestMapping(UserController.PATH)
@Controller
public class UserController extends WebController {

  private static final Log log = LogFactory.getLog(UserController.class);

  public static final String PATH = "admin/system/user";

  @Autowired
  RoleService roleService;

  @Autowired
  UserStatusService userStatusService;

  @Autowired
  UserService userService;

  @RequestMapping(produces = "text/html")
  public String list(@RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    prepareList(userService, "userList", page, size, uiModel);
    return PATH + "/" + LIST;
  }

  @RequestMapping(params = "form", produces = "text/html")
  public String createForm(Model uiModel, HttpServletRequest httpServletRequest) {
    populateEditForm(uiModel, new User(), httpServletRequest, true);
    return PATH + "/" + CREATE;
  }

  @RequestMapping(method = RequestMethod.POST, produces = "text/html")
  public String create(User user, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    try {
      // validate...
      UserCreateValidator validator = new UserCreateValidator();
      validator.validate(user, bindingResult);
      if (bindingResult.hasErrors()) {
        populateEditForm(uiModel, user, httpServletRequest, true);
        return PATH + "/" + CREATE;
      }

      userService.createUser(user);

      uiModel.asMap().clear();
      return REDIRECT + PATH + "/" + encodeUrlPathSegment(user.getId().toString(), httpServletRequest);
    } catch (Exception e) {
      log.error(e);
      throw new RuntimeException(e);
    }
  }

  @RequestMapping(value = "/{id}", produces = "text/html")
  public String showForm(@PathVariable("id") Integer id, Model uiModel, HttpServletRequest httpServletRequest) {
    User user = userService.find(id);

    uiModel.addAttribute("user", user);
    uiModel.addAttribute("itemId", id);

    return PATH + "/" + SHOW;
  }

  @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
  public String updateForm(@PathVariable("id") Integer id, Model uiModel, HttpServletRequest httpServletRequest) {
    User user = userService.find(id);

    populateEditForm(uiModel, user, httpServletRequest, false);
    return PATH + "/" + UPDATE;
  }

  @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
  public String update(User user, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    try {
      if (bindingResult.hasErrors()) {
        populateEditForm(uiModel, user, httpServletRequest, false);
        return PATH + "/" + UPDATE;
      }

      userService.updateUser(user);

      uiModel.asMap().clear();
      return REDIRECT + PATH + "/" + encodeUrlPathSegment(user.getId().toString(), httpServletRequest);
    } catch (Exception e) {
      log.error(e);
      throw new RuntimeException(e);
    }
  }

  @RequestMapping(value = "/{id}/password", method = RequestMethod.PUT, produces = "text/html")
  public String changePassword(@PathVariable("id") Integer id, User userPasswordForm, BindingResult bindingResult,
      Model uiModel, HttpServletRequest httpServletRequest) {
    try {
      if (bindingResult.hasErrors()) {
        User user = userService.find(id);
        populateEditForm(uiModel, user, httpServletRequest, false);
        return PATH + "/" + UPDATE;
      }

      userService.updatePassword(id, userPasswordForm.getPassword());

      uiModel.asMap().clear();
      return REDIRECT + PATH + "/" + encodeUrlPathSegment(userPasswordForm.getId().toString(), httpServletRequest);
    } catch (Exception e) {
      log.error(e);
      throw new RuntimeException(e);
    }
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
  public String delete(@PathVariable("id") Integer id, @RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    userService.delete(id);

    uiModel.asMap().clear();
    return REDIRECT + PATH;
  }

  private void populateEditForm(Model uiModel, User user, HttpServletRequest httpServletRequest, boolean create) {
    uiModel.addAttribute("user", user);
    uiModel.addAttribute("userStatusList", userStatusService.findAll());
    uiModel.addAttribute("roleList", roleService.findAvailable());

    if (!create) {
      // password form
      uiModel.addAttribute("userPasswordForm", user);
    }
  }

  private class UserCreateValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
      return true;
    }

    @Override
    public void validate(Object target, Errors errors) {
      User form = (User) target;
      User user = userService.findForUserName(form.getUserName());
      if (user != null) {
        errors.reject("validation_username_already_exists");
      }
    }
  }
}
