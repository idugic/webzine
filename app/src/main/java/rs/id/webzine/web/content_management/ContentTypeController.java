package rs.id.webzine.web.content_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import rs.id.webzine.service.content_management.ContentTypeService;
import rs.id.webzine.web.WebController;

@RequestMapping(ContentTypeController.PATH)
@Controller
public class ContentTypeController extends WebController {

  public static final String PATH = "admin/content_management/content_type";

  @Autowired
  ContentTypeService contentTypeService;

  @RequestMapping(value = "/{id}", produces = "text/html")
  public String show(@PathVariable("id") Integer id, Model uiModel) {
    uiModel.addAttribute("contentType", contentTypeService.find(id));
    uiModel.addAttribute("itemId", id);
    return PATH + "/" + SHOW;
  }

  @RequestMapping(produces = "text/html")
  public String list(Model uiModel) {
    uiModel.addAttribute("contentTypeList", contentTypeService.findAll());
    return PATH + "/" + LIST;
  }

}
