package rs.id.webzine.service.magazine;

import java.util.Calendar;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import rs.id.webzine.entity.magazine.Category;
import rs.id.webzine.service.GenericService;

@Component
public class CategoryService extends GenericService<Category> {

  @Override
  @Transactional
  public void create(Category category) {
    category.setUc(currentUser());
    category.setDc(Calendar.getInstance());

    super.create(category);
  }

  @Override
  @Transactional
  public void update(Category category) {
    Category persistedCategory = find(category.getId());

    category.setUc(persistedCategory.getUc());
    category.setDc(persistedCategory.getDc());
    
    category.setUm(currentUser());
    category.setDm(Calendar.getInstance());

    super.update(category);
  }

  public Category find(String name, Integer readerTypeId) {
    Category category = null;

    TypedQuery<Category> query = entityManager().createQuery(
        "SELECT c FROM Category c JOIN c.readerType rt WHERE c.name = :name AND rt.id = :readerTypeId", Category.class);
    query.setParameter("name", name);
    query.setParameter("readerTypeId", readerTypeId);

    List<Category> categoryList = query.getResultList();
    if (!categoryList.isEmpty()) {
      category = categoryList.get(0);
    }

    return category;
  }

}
