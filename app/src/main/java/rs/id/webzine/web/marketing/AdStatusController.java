package rs.id.webzine.web.marketing;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import rs.id.webzine.domain.marketing.AdStatus;
import rs.id.webzine.web.WebController;

@RequestMapping("admin/ad_status")
@Controller
public class AdStatusController extends WebController {

  @RequestMapping(value = "/{id}", produces = "text/html")
  public String show(@PathVariable("id") Integer id, Model uiModel) {
    uiModel.addAttribute("adStatus", AdStatus.find(id));
    uiModel.addAttribute("itemId", id);
    return "admin/ad_status/show";
  }

  @RequestMapping(produces = "text/html")
  public String list(@RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    if (page != null || size != null) {
      int sizeNo = size == null ? 10 : size.intValue();
      final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
      uiModel.addAttribute("adStatusList", AdStatus.findEntries(firstResult, sizeNo));
      float nrOfPages = (float) AdStatus.count() / sizeNo;
      uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
          : nrOfPages));
    } else {
      uiModel.addAttribute("adStatusList", AdStatus.findAll());
    }
    return "admin/ad_status/list";
  }

}
