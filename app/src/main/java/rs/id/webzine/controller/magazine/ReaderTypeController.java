package rs.id.webzine.controller.magazine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import rs.id.webzine.controller.WebController;
import rs.id.webzine.service.magazine.ReaderTypeService;

@RequestMapping(ReaderTypeController.PATH)
@Controller
public class ReaderTypeController extends WebController {

  public static final String PATH = "admin/magazine/reader_type";

  @Autowired
  ReaderTypeService readerTypeService;

  @RequestMapping(value = "/{id}", produces = "text/html")
  public String show(@PathVariable("id") Integer id, Model uiModel) {
    uiModel.addAttribute("readerType", readerTypeService.find(id));
    uiModel.addAttribute("itemId", id);
    return PATH + "/" + SHOW;
  }

  @RequestMapping(produces = "text/html")
  public String list(Model uiModel) {
    uiModel.addAttribute("readerTypeList", readerTypeService.findAll());
    return PATH + "/" + LIST;
  }

}
