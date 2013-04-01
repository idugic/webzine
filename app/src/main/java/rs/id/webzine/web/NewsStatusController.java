package rs.id.webzine.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import rs.id.webzine.domain.NewsStatus;

@RequestMapping("admin/news_status")
@Controller
public class NewsStatusController extends ModelController {

  @RequestMapping(value = "/{id}", produces = "text/html")
  public String show(@PathVariable("id") Integer id, Model uiModel) {
    uiModel.addAttribute("newsStatus", NewsStatus.find(id));
    uiModel.addAttribute("itemId", id);
    return "admin/news_status/show";
  }

  @RequestMapping(produces = "text/html")
  public String list(@RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    if (page != null || size != null) {
      int sizeNo = size == null ? 10 : size.intValue();
      final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
      uiModel.addAttribute("newsStatusList", NewsStatus.findEntries(firstResult, sizeNo));
      float nrOfPages = (float) NewsStatus.count() / sizeNo;
      uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
          : nrOfPages));
    } else {
      uiModel.addAttribute("newsStatusList", NewsStatus.findAll());
    }
    return "admin/news_status/list";
  }

}
