package rs.id.webzine.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class Pagination {

  public static final String COUNTRY = "Country";

  public static class Page {
    private Integer page;

    private Integer size;

    public Page(Integer page, Integer size) {
      this.page = page;
      this.size = size;
    }

    public Integer getPage() {
      return page;
    }

    public void setPage(Integer page) {
      this.page = page;
    }

    public Integer getSize() {
      return size;
    }

    public void setSize(Integer size) {
      this.size = size;
    }
  }

  private Map<String, Page> map = new HashMap<String, Page>();

  public Page getPage(String list) {
    if (!map.containsKey(list)) {
      map.put(list, new Page(0, 10));
    }

    return map.get(list);
  }

  public void setPage(String list, Page page) {
    map.put(list, page);
  }

}
