package rs.id.webzine.web.system;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import rs.id.webzine.domain.system.Address;
import rs.id.webzine.domain.system.User;
import rs.id.webzine.service.system.AddressService;
import rs.id.webzine.service.system.RoleService;
import rs.id.webzine.service.system.UserService;
import rs.id.webzine.service.system.UserStatusService;
import rs.id.webzine.web.WebController;
import rs.id.webzine.web.form.UserForm;

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

  @Autowired
  AddressService addressService;

  @InitBinder
  protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws ServletException {
    binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
  }

  @RequestMapping(produces = "text/html")
  public String list(@RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    try {
      List<User> userList = prepareList(userService, "userList", page, size, uiModel);

      // prepare form list
      List<UserForm> userFormList = new ArrayList<UserForm>();
      for (User user : userList) {
        UserForm userForm = new UserForm();
        PropertyUtils.copyProperties(userForm, user);
        userForm.setUserId(user.getId());

        Address address = user.getAddress();
        if (address != null) {
          PropertyUtils.copyProperties(userForm, address);
        }

        userFormList.add(userForm);
      }

      uiModel.addAttribute("userList", userFormList);

      return PATH + "/" + LIST;
    } catch (Exception e) {
      log.error(e);
      throw new RuntimeException(e);
    }
  }

  @RequestMapping(params = "form", produces = "text/html")
  public String createForm(Model uiModel) {
    populateEditForm(uiModel, new UserForm());
    return PATH + "/" + CREATE;
  }

  void populateEditForm(Model uiModel, UserForm userForm) {
    uiModel.addAttribute("userForm", userForm);
    uiModel.addAttribute("roleList", roleService.findAll());
    uiModel.addAttribute("userStatusList", userStatusService.findAll());
    addDateFormatPattern(uiModel);
  }

  @RequestMapping(method = RequestMethod.POST, produces = "text/html")
  public String create(UserForm userForm, @RequestParam("image") MultipartFile image, BindingResult bindingResult,
      Model uiModel, HttpServletRequest httpServletRequest) {
    try {

      /*
       * workaround (file is always sent as POST)
       */
      if (httpServletRequest.getParameter("_method") != null
          && httpServletRequest.getParameter("_method").equals("PUT")) {
        return this.update(userForm, image, bindingResult, uiModel, httpServletRequest);
      }

      // validate...
      UserCreateValidator validator = new UserCreateValidator();
      validator.validate(userForm, bindingResult);
      if (bindingResult.hasErrors()) {
        populateEditForm(uiModel, userForm);
        return PATH + "/" + CREATE;
      }

      // address
      Address address = new Address();
      PropertyUtils.copyProperties(address, userForm);

      // persist
      User user = new User();
      PropertyUtils.copyProperties(user, userForm);
      user.setImage(image.getBytes());
      user.setImageContentType(image.getContentType());

      userService.persist(user, address);

      uiModel.asMap().clear();
      return REDIRECT + PATH + "/" + encodeUrlPathSegment(user.getId().toString(), httpServletRequest);
    } catch (Exception e) {
      log.error(e);
      throw new RuntimeException(e);
    }

  }

  @RequestMapping(value = "/{id}", produces = "text/html")
  public String show(@PathVariable("id") Integer id, Model uiModel, HttpServletRequest httpServletRequest) {
    try {
      UserForm userForm = new UserForm();

      User user = userService.find(id);
      PropertyUtils.copyProperties(userForm, user);
      userForm.setUserId(id);

      if (user.getAddress() != null) {
        PropertyUtils.copyProperties(userForm, user.getAddress());
      }

      String imageUrl = httpServletRequest.getContextPath() + PATH + "/" + IMAGE + "/"
          + encodeUrlPathSegment(id.toString(), httpServletRequest);
      userForm.setImageUrl(imageUrl);

      uiModel.addAttribute("userForm", userForm);
      uiModel.addAttribute("itemId", id);
      addDateFormatPattern(uiModel);

      return PATH + "/" + SHOW;
    } catch (Exception e) {
      log.error(e);
      throw new RuntimeException(e);
    }

  }

  @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
  public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
    try {
      UserForm userForm = new UserForm();

      User user = userService.find(id);
      PropertyUtils.copyProperties(userForm, user);
      userForm.setUserId(user.getId());

      if (user.getAddress() != null) {
        PropertyUtils.copyProperties(userForm, user.getAddress());
      }

      populateEditForm(uiModel, userForm);
      return PATH + "/" + UPDATE;
    } catch (Exception e) {
      log.error(e);
      throw new RuntimeException(e);
    }
  }

  @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
  public String update(UserForm userForm, @RequestParam("image") MultipartFile image, BindingResult bindingResult,
      Model uiModel, HttpServletRequest httpServletRequest) {
    try {
      if (bindingResult.hasErrors()) {
        populateEditForm(uiModel, userForm);
        return PATH + "/" + UPDATE;
      }

      User user = new User();
      PropertyUtils.copyProperties(user, userForm);

      Address address = new Address();
      PropertyUtils.copyProperties(address, userForm);

      userService.merge(userForm.getUserId(), user, address);

      uiModel.asMap().clear();
      return REDIRECT + PATH + "/" + encodeUrlPathSegment(userForm.getUserId().toString(), httpServletRequest);
    } catch (Exception e) {
      log.error(e);
      throw new RuntimeException(e);
    }
  }

  @RequestMapping(value = "/image/{id}", method = RequestMethod.GET)
  public String image(@PathVariable("id") Integer id, HttpServletResponse response, Model model) {
    try {
      User user = userService.find(id);

      if (user.getImage() != null && user.getImage().length > 0) {
        response.setHeader("Content-Disposition", "inline;");
        response.setContentType(user.getImageContentType());

        OutputStream out = response.getOutputStream();
        IOUtils.copy(new ByteArrayInputStream(user.getImage()), out);
        out.flush();
      }

      return null;
    } catch (Exception e) {
      log.error(e);
      throw new RuntimeException(e);
    }
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
  public String delete(@PathVariable("id") Integer id, @RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    userService.remove(id);

    uiModel.asMap().clear();
    return REDIRECT + PATH;
  }

  private class UserCreateValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
      return true;
    }

    @Override
    public void validate(Object target, Errors errors) {
      UserForm form = (UserForm) target;
      User user = userService.findForUserName(form.getUserName());
      if (user != null) {
        errors.rejectValue("userName", "validation.user.userName.duplicate");
      }
    }
  }
}
