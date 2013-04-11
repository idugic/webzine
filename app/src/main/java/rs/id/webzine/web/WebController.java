package rs.id.webzine.web;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

public class WebController {

  public static final String REDIRECT = "redirect:/";

  public static final String LIST = "list";

  public static final String SHOW = "show";

  public static final String CREATE = "create";
  
  public static final String UPDATE = "update";

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
