package rs.id.webzine.service;

public class ServiceException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private ServiceExceptionCode exceptionCode;

  public ServiceException() {
    super();
  }

  public ServiceException(String message) {
    super(message);
  }

  public ServiceException(Throwable cause) {
    super(cause);
  }

  public ServiceException(String message, Throwable cause) {
    super(message, cause);
  }

  public ServiceException(ServiceExceptionCode exceptionCode) {
    super();
    this.exceptionCode = exceptionCode;
  }

  public ServiceExceptionCode getExceptionCode() {
    return exceptionCode;
  }

  public void setExceptionCode(ServiceExceptionCode exceptionCode) {
    this.exceptionCode = exceptionCode;
  }

}
