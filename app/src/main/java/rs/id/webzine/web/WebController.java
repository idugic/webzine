package rs.id.webzine.web;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@Component
public class WebController {

  @Autowired
  Pagination pagination;

  public static final String REDIRECT = "redirect:/";

  public static final String LIST = "list";

  public static final String SHOW = "show";

  public static final String CREATE = "create";

  public static final String UPDATE = "update";

  public Pagination getPagination() {
    return pagination;
  }

  public String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
    String enc = httpServletRequest.getCharacterEncoding();
    if (enc == null) {
      enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
    }
    try {
      pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
    } catch (UnsupportedEncodingException uee) {
    }
    return pathSegment;
  }

}
