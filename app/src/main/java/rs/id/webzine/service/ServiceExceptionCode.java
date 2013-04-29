package rs.id.webzine.service;

public enum ServiceExceptionCode {

  OPERATION_NOT_AVAILABLE_ENTITY_IS_PUBLISHED("operation_not_available_entity_is_published");

  private String name;

  private ServiceExceptionCode(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
