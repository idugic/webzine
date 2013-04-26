package rs.id.webzine.web;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import rs.id.webzine.service.GenericService;

@Component
public class WebController {

  public static final String REDIRECT = "redirect:/";

  public static final String LIST = "list";

  public static final String SHOW = "show";

  public static final String CREATE = "create";

  public static final String UPDATE = "update";

  public static final String IMAGE = "image";

  public static final String ABSTRACT_MEDIA = "abstract_media";

  
  public <E> List<E> prepareList(GenericService<E> service, String listName, Integer page, Integer size, Model uiModel) {
    List<E> list;

    if (page != null || size != null) {
      int sizeNo = size == null ? 10 : size.intValue();
      final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;

      list = service.findForList(firstResult, sizeNo);

      float nrOfPages = (float) service.count() / sizeNo;
      uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
          : nrOfPages));
    } else {
      list = service.findAll();
    }

    uiModel.addAttribute(listName, service.findAll());

    return list;
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

  public void addDatePattern(Model uiModel) {
    uiModel.addAttribute("date_pattern", DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
  }

  public void addDateTimePattern(Model uiModel) {
    uiModel.addAttribute("date_time_pattern", DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
  }
}
