package rs.id.webzine.web;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import rs.id.webzine.domain.magazine.Category;
import rs.id.webzine.domain.magazine.ReaderType;
import rs.id.webzine.service.Service;

@RequestMapping("/admin/category")
@Controller
public class CategoryController extends WebController {

//  @RequestMapping(method = RequestMethod.POST, produces = "text/html")
//  public String create(Category category, BindingResult bindingResult, Model uiModel,
//      HttpServletRequest httpServletRequest) {
//    if (bindingResult.hasErrors()) {
//      populateEditForm(uiModel, category);
//      return "admin/category/create";
//    }
//
//    uiModel.asMap().clear();
//    category.setUc(Service.getCurrentUser());
//    category.setDc(Calendar.getInstance());
//    category.persist();
//    return "redirect:/admin/category/" + encodeUrlPathSegment(category.getId().toString(), httpServletRequest);
//  }
//
//  @RequestMapping(params = "form", produces = "text/html")
//  public String createForm(Model uiModel) {
//    populateEditForm(uiModel, new Category());
//    return "admin/category/create";
//  }
//
//  @RequestMapping(value = "/{id}", produces = "text/html")
//  public String show(@PathVariable("id") Integer id, Model uiModel) {
//    addDateTimeFormatPatterns(uiModel);
//    uiModel.addAttribute("category", Category.find(id));
//    uiModel.addAttribute("itemId", id);
//    return "admin/category/show";
//  }
//
//  @RequestMapping(produces = "text/html")
//  public String list(@RequestParam(value = "page", required = false) Integer page,
//      @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
//    if (page != null || size != null) {
//      int sizeNo = size == null ? 10 : size.intValue();
//      final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
//      uiModel.addAttribute("categoryList", Category.findEntries(firstResult, sizeNo));
//      float nrOfPages = (float) Category.count() / sizeNo;
//      uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1
//          : nrOfPages));
//    } else {
//      uiModel.addAttribute("categoryList", Category.findAll());
//    }
//    addDateTimeFormatPatterns(uiModel);
//    return "admin/category/list";
//  }
//
//  @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
//  public String update(Category category, BindingResult bindingResult, Model uiModel,
//      HttpServletRequest httpServletRequest) {
//    if (bindingResult.hasErrors()) {
//      populateEditForm(uiModel, category);
//      return "admin/category/update";
//    }
//    uiModel.asMap().clear();
//    Category oldCategory = Category.find(category.getId());
//
//    category.setUc(oldCategory.getUc());
//    category.setDc(oldCategory.getDc());
//    category.setUm(Service.getCurrentUser());
//    category.setDm(Calendar.getInstance());
//    category.merge();
//    return "redirect:/admin/category/" + encodeUrlPathSegment(category.getId().toString(), httpServletRequest);
//  }
//
//  @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
//  public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
//    populateEditForm(uiModel, Category.find(id));
//    return "admin/category/update";
//  }
//
//  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
//  public String delete(@PathVariable("id") Integer id, @RequestParam(value = "page", required = false) Integer page,
//      @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
//    Category category = Category.find(id);
//    category.remove();
//    uiModel.asMap().clear();
//    uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
//    uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
//    return "redirect:/admin/category";
//  }
//
//  void addDateTimeFormatPatterns(Model uiModel) {
//    uiModel.addAttribute("category_dc_date_format",
//        DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
//    uiModel.addAttribute("category_dm_date_format",
//        DateTimeFormat.patternForStyle("MM", LocaleContextHolder.getLocale()));
//  }
//
//  void populateEditForm(Model uiModel, Category category) {
//    uiModel.addAttribute("category", category);
//    addDateTimeFormatPatterns(uiModel);
//    uiModel.addAttribute("readerTypeList", ReaderType.findAll());
//
//  }
}
