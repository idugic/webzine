package rs.id.webzine.controller.marketing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import rs.id.webzine.controller.WebController;
import rs.id.webzine.service.marketing.AdStatusService;

@RequestMapping(AdStatusController.PATH)
@Controller
public class AdStatusController extends WebController {

  public static final String PATH = "admin/marketing/ad_status";

  @Autowired
  AdStatusService adStatusService;

  @RequestMapping(value = "/{id}", produces = "text/html")
  public String show(@PathVariable("id") Integer id, Model uiModel) {
    uiModel.addAttribute("adStatus", adStatusService.find(id));
    uiModel.addAttribute("itemId", id);
    return PATH + "/" + SHOW;
  }

  @RequestMapping(produces = "text/html")
  public String list(Model uiModel) {
    uiModel.addAttribute("adStatusList", adStatusService.findAll());
    return PATH + "/" + LIST;
  }

}
