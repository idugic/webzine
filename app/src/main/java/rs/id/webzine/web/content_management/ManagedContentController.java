package rs.id.webzine.web.content_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import rs.id.webzine.domain.content_management.ManagedContent;
import rs.id.webzine.service.content_management.ContentService;
import rs.id.webzine.service.content_management.ManagedContentService;
import rs.id.webzine.web.WebController;

@RequestMapping(ManagedContentController.PATH)
@Controller
public class ManagedContentController extends WebController {

  public static final String PATH = "admin/content_management/managed_content";

  @Autowired
  ManagedContentService managedContentService;

  @Autowired
  ContentService contentService;

  @RequestMapping(value = "/{id}", produces = "text/html")
  public String show(@PathVariable("id") Integer id, Model uiModel) {

    ManagedContent managedContent = managedContentService.find(id);

    uiModel.addAttribute("managedContent", managedContent);
    uiModel.addAttribute("contentList", contentService.findForManagedContent(managedContent.getId()));

    return PATH + "/" + SHOW;
  }

}
