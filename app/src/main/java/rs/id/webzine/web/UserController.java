package rs.id.webzine.web;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
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

import rs.id.webzine.domain.Address;
import rs.id.webzine.domain.User;
import rs.id.webzine.service.system.RoleService;
import rs.id.webzine.service.system.UserStatusService;
import rs.id.webzine.web.backing.UserBacking;

@RequestMapping("admin/user")
@Controller
public class UserController extends WebController {

  private static final Log log = LogFactory.getLog(UserController.class);

  @Autowired
  RoleService roleService;

  @Autowired
  UserStatusService userStatusService;

  // TODO model attribute user(s) -> userList (also for other entities)

  class UserCreateValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
      return true;
    }

    @Override
    public void validate(Object target, Errors errors) {
      UserBacking form = (UserBacking) target;
      User user = User.findForUserName(form.getUserName());
      if (user != null) {
        errors.rejectValue("userName", "validation.user.userName.duplicate");
      }
    }
  }

  @InitBinder
  protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws ServletException {
    binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
  }

  void addDateTimeFormatPatterns(Model uiModel) {
    uiModel.addAttribute("user_birthdate_date_format",
        DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
  }

  @RequestMapping(params = "form", produces = "text/html")
  public String createForm(Model uiModel) {
    populateEditForm(uiModel, new UserBacking());
    return "admin/user/create";
  }

  @RequestMapping(method = RequestMethod.POST, produces = "text/html")
  public String create(@Valid UserBacking userBacking, @RequestParam("image") MultipartFile image,
      BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    try {
      /*
       * workaround (multipart forms are always sent using POST)
       */
      if (httpServletRequest.getParameter("_method") != null
          && httpServletRequest.getParameter("_method").equals("PUT")) {
        return this.update(userBacking, image, bindingResult, uiModel, httpServletRequest);
      }

      // bind
      if (bindingResult.hasErrors()) {
        populateEditForm(uiModel, userBacking);
        return "admin/user/create";
      }

      // validate
      UserCreateValidator validator = new UserCreateValidator();
      validator.validate(userBacking, bindingResult);
      if (bindingResult.hasErrors()) {
        populateEditForm(uiModel, userBacking);
        return "admin/user/create";
      }

      // address
      Address address = new Address();
      PropertyUtils.copyProperties(address, userBacking);
      address.persist();

      // persist
      User user = new User();
      PropertyUtils.copyProperties(user, userBacking);

      // TODO hash password

      user.setImageContentType(image.getContentType());
      user.setAddressId(address);
      user.persist();

      uiModel.asMap().clear();
      return "redirect:/admin/user/" + encodeUrlPathSegment(user.getId().toString(), httpServletRequest);
    } catch (Exception e) {
      log.error(e);
      throw new RuntimeException(e);
    }

  }

  @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
  public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
    try {
      UserBacking userBacking = new UserBacking();
      User user = User.find(id);
      PropertyUtils.copyProperties(userBacking, user);
      userBacking.setBackingId(user.getId());

      if (user.getAddressId() != null) {
        PropertyUtils.copyProperties(userBacking, user.getAddressId());
      }

      populateEditForm(uiModel, userBacking);
      return "admin/user/update";
    } catch (Exception e) {
      log.error(e);
      throw new RuntimeException(e);
    }
  }

  @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
  public String update(@Valid UserBacking userBacking, @RequestParam("image") MultipartFile image,
      BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
    try {
      if (bindingResult.hasErrors()) {
        populateEditForm(uiModel, userBacking);
        return "admin/user/update";
      }

      User user = User.find(userBacking.getBackingId());

      // collect OLD values
      String oldPassword = user.getPassword();
      byte[] oldImage = null;
      if (user.getImage() != null) {
        oldImage = user.getImage().clone();
      }
      String oldImageContentType = user.getImageContentType();

      PropertyUtils.copyProperties(user, userBacking);

      user.setId(userBacking.getBackingId());

      String newPassword = userBacking.getNewPassword();
      if (newPassword != null && !newPassword.isEmpty()) {
        // TODO hash password
        user.setPassword(newPassword);
      } else {
        user.setPassword(oldPassword);
      }

      if (image == null || image.getBytes() == null || image.getBytes().length == 0) {
        user.setImage(oldImage);
        user.setImageContentType(oldImageContentType);
      } else {
        user.setImage(image.getBytes());
        user.setImageContentType(image.getContentType());
      }

      // address
      if (user.getAddressId() != null) {
        PropertyUtils.copyProperties(user.getAddressId(), userBacking);
      } else {
        Address address = new Address();
        PropertyUtils.copyProperties(address, userBacking);
        address.persist();

        user.setAddressId(address);
      }

      uiModel.asMap().clear();
      user.merge();
      return "redirect:/admin/user/" + encodeUrlPathSegment(user.getId().toString(), httpServletRequest);
    } catch (Exception e) {
      log.error(e);
      throw new RuntimeException(e);
    }
  }

  @RequestMapping(value = "/{id}", produces = "text/html")
  public String show(@PathVariable("id") Integer id, Model uiModel, HttpServletRequest httpServletRequest) {
    try {
      UserBacking userBacking = new UserBacking();

      User user = User.find(id);

      if (user.getAddressId() != null) {
        PropertyUtils.copyProperties(userBacking, user.getAddressId());
        userBacking.setBackingId(id);
      }
      PropertyUtils.copyProperties(userBacking, user);

      String imageUrl = httpServletRequest.getContextPath() + "/admin/user/showimage/"
          + encodeUrlPathSegment(id.toString(), httpServletRequest);
      userBacking.setImageUrl(imageUrl);

      uiModel.addAttribute("userBacking", userBacking);
      uiModel.addAttribute("itemId", id);
      addDateTimeFormatPatterns(uiModel);
      return "admin/user/show";
    } catch (Exception e) {
      log.error(e);
      throw new RuntimeException(e);
    }

  }

  @RequestMapping(value = "/showimage/{id}", method = RequestMethod.GET)
  public String showImage(@PathVariable("id") Integer id, HttpServletResponse response, Model model) {
    try {
      User user = User.find(id);
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

  @RequestMapping(produces = "text/html")
  public String list(@RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    try {
      List<User> userList = new ArrayList<User>();
      if (page != null || size != null) {
        int sizeNo = size == null ? 10 : size.intValue();
        final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;

        userList = User.findEntries(firstResult, sizeNo);
        float nrOfPages = (float) User.count() / sizeNo;
        uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
            : nrOfPages));
      } else {
        userList = User.findAll();
      }
      addDateTimeFormatPatterns(uiModel);

      List<UserBacking> userBackingList = new ArrayList<UserBacking>();
      for (User user : userList) {
        UserBacking userBacking = new UserBacking();
        PropertyUtils.copyProperties(userBacking, user);
        userBacking.setBackingId(user.getId());

        Address address = user.getAddressId();
        if (address != null) {
          PropertyUtils.copyProperties(userBacking, address);
        }

        userBackingList.add(userBacking);
      }

      uiModel.addAttribute("userBacking", userBackingList);

      return "admin/user/list";
    } catch (Exception e) {
      log.error(e);
      throw new RuntimeException(e);
    }
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
  public String delete(@PathVariable("id") Integer id, @RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    User user = User.find(id);
    user.remove();
    uiModel.asMap().clear();
    uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
    uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
    return "redirect:/admin/user";
  }

  void populateEditForm(Model uiModel, UserBacking userBacking) {
    uiModel.addAttribute("userBacking", userBacking);
    uiModel.addAttribute("role", roleService.findAll());
    uiModel.addAttribute("userStatus", userStatusService.findAll());
    addDateTimeFormatPatterns(uiModel);
  }

}
