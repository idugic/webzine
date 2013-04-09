package rs.id.webzine.domain.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import rs.id.webzine.domain.User;

public class Session {
  public static User getCurrentUser() {
    User user = null;

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userName = authentication.getName();
    if (userName != null) {
      user = User.findForUserName(userName);
    }
    return user;
  }
}