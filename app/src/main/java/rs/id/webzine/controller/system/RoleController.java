package rs.id.webzine.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import rs.id.webzine.controller.WebController;
import rs.id.webzine.service.system.RoleService;

@RequestMapping(RoleController.PATH)
@Controller
public class RoleController extends WebController {

  public static final String PATH = "admin/system/role";

  @Autowired
  RoleService roleService;

  @RequestMapping(value = "/{id}", produces = "text/html")
  public String show(@PathVariable("id") Integer id, Model uiModel) {
    uiModel.addAttribute("role", roleService.find(id));
    uiModel.addAttribute("itemId", id);
    return PATH + "/" + SHOW;
  }

  @RequestMapping(produces = "text/html")
  public String list(Model uiModel) {
    uiModel.addAttribute("roleList", roleService.findAll());
    return PATH + "/" + LIST;
  }

}
