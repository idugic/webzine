package rs.id.webzine.service.content_management;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import rs.id.webzine.entity.content_management.ManagedContent;
import rs.id.webzine.service.GenericService;

@Component
public class ManagedContentService extends GenericService<ManagedContent> {

  @Transactional
  public void updateCss(Integer id, String css) {
    ManagedContent managedContent = find(id);
    managedContent.setCss(css);
    super.update(managedContent);
  }

  @Transactional
  public void updateScript(Integer id, String script) {
    ManagedContent managedContent = find(id);
    managedContent.setScript(script);
    super.update(managedContent);
  }

}
