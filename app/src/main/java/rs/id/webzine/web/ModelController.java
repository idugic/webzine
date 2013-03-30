package rs.id.webzine.web;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import rs.id.webzine.domain.User;

public class ModelController {

  String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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

  User getCurrentUser() {
    User user = null;

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userName = authentication.getName();
    if (userName != null) {
      user = User.findForUserName(userName);
    }
    return user;
  }
}
