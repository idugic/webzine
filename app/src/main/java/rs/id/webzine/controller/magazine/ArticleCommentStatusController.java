package rs.id.webzine.controller.magazine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import rs.id.webzine.controller.WebController;
import rs.id.webzine.service.magazine.ArticleCommentStatusService;

@RequestMapping(ArticleCommentStatusController.PATH)
@Controller
public class ArticleCommentStatusController extends WebController {

  public static final String PATH = "admin/magazine/article_comment_status";

  @Autowired
  ArticleCommentStatusService articleCommentStatusService;

  @RequestMapping(value = "/{id}", produces = "text/html")
  public String show(@PathVariable("id") Integer id, Model uiModel) {
    uiModel.addAttribute("articleCommentStatus", articleCommentStatusService.find(id));
    uiModel.addAttribute("itemId", id);
    return PATH + "/" + SHOW;
  }

  @RequestMapping(produces = "text/html")
  public String list(Model uiModel) {
    uiModel.addAttribute("articleCommentStatusList", articleCommentStatusService.findAll());
    return PATH + "/" + LIST;
  }

}
