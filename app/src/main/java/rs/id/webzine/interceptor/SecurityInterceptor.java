package rs.id.webzine.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import rs.id.webzine.service.system.UserService;

public class SecurityInterceptor extends HandlerInterceptorAdapter {

  @Autowired
  private UserService userService;

  @Override
  public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
      final ModelAndView modelAndView) throws Exception {

    if (modelAndView != null && modelAndView.getModelMap() != null) {
      modelAndView.getModelMap().addAttribute("currentUser", userService.currentUser());
    }
  }
}
