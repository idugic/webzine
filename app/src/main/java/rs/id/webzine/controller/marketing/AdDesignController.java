package rs.id.webzine.controller.marketing;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import rs.id.webzine.controller.content_management.DesignController;

@RequestMapping(AdDesignController.PATH)
@Controller
public class AdDesignController extends DesignController {

  public static final String PATH = "admin/marketing/ad/design";

  public AdDesignController() {
    super(PATH);
  }
}
