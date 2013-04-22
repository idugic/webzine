package rs.id.webzine.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import rs.id.webzine.domain.magazine.Category;
import rs.id.webzine.service.magazine.CategoryService;
import rs.id.webzine.service.magazine.ReaderTypeService;

@RequestMapping(CategoryController.PATH)
@Controller
public class CategoryController extends WebController {

  @Autowired
  ReaderTypeService readerTypeService;

  public static final String PATH = "admin/magazine/category";

  @Autowired
  CategoryService categoryService;

  @RequestMapping(produces = "text/html")
  public String list(@RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
    prepareList(categoryService, "categoryList", page, size, uiModel);
    return PATH + "/" + LIST;
  }

  @RequestMapping(params = "form", produces = "text/html")
  public String createForm(Model uiModel) {
    populateEditForm(uiModel, new Category());
    return PATH + "/" + CREATE;
  }

  @RequestMapping(method = RequestMethod.POST, produces = "text/html")
  public String create(Category category, BindingResult bindingResult, Model uiModel,
      HttpServletRequest httpServletRequest) {

    // validate...
    CategoryCreateValidator validator = new CategoryCreateValidator();
    validator.validate(category, bindingResult);
    if (bindingResult.hasErrors()) {
      populateEditForm(uiModel, category);
      return PATH + "/" + CREATE;
    }

    categoryService.create(category);

    uiModel.asMap().clear();
    return REDIRECT + PATH + "/" + encodeUrlPathSegment(category.getId().toString(), httpServletRequest);
  }

  @RequestMapping(value = "/{id}", produces = "text/html")
  public String show(@PathVariable("id") Integer id, Model uiModel) {
    uiModel.addAttribute("category", categoryService.find(id));
    uiModel.addAttribute("itemId", id);
    return PATH + "/" + SHOW;
  }

  @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
  public String updateForm(@PathVariable("id") Integer id, Model uiModel) {
    populateEditForm(uiModel, categoryService.find(id));
    return PATH + "/" + UPDATE;
  }

  @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
  public String update(Category category, BindingResult bindingResult, Model uiModel,
      HttpServletRequest httpServletRequest) {
    // validate...
    CategoryUpdateValidator validator = new CategoryUpdateValidator();
    validator.validate(category, bindingResult);
    if (bindingResult.hasErrors()) {
      populateEditForm(uiModel, category);
      return PATH + "/" + UPDATE;
    }

    categoryService.update(category);

    uiModel.asMap().clear();
    return REDIRECT + PATH + "/" + encodeUrlPathSegment(category.getId().toString(), httpServletRequest);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
  public String delete(@PathVariable("id") Integer id, Model uiModel) {
    categoryService.delete(id);

    uiModel.asMap().clear();
    return REDIRECT + PATH;
  }

  void populateEditForm(Model uiModel, Category category) {
    uiModel.addAttribute("category", category);
    uiModel.addAttribute("readerTypeList", readerTypeService.findAll());
  }

  private class CategoryCreateValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
      return true;
    }

    @Override
    public void validate(Object target, Errors errors) {
      Category form = (Category) target;

      Category category = categoryService.find(form.getName(), form.getReaderType().getId());
      if (category != null) {
        errors.rejectValue("name", "validation_category_name_reader_type_unique");
      }
    }
  }

  private class CategoryUpdateValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
      return true;
    }

    @Override
    public void validate(Object target, Errors errors) {
      Category form = (Category) target;

      Category category = categoryService.find(form.getName(), form.getReaderType().getId());
      if (category != null && category.getId() != form.getId()) {
        errors.rejectValue("name", "validation_category_name_reader_type_unique");
      }
    }
  }
}
