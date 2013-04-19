package rs.id.webzine.web;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

import rs.id.webzine.domain.Ad;
import rs.id.webzine.domain.AdStatus;
import rs.id.webzine.domain.Article;
import rs.id.webzine.domain.ArticleBookmark;
import rs.id.webzine.domain.ArticleCategory;
import rs.id.webzine.domain.ArticleComment;
import rs.id.webzine.domain.ArticleCommentStatus;
import rs.id.webzine.domain.ArticleRate;
import rs.id.webzine.domain.ArticleStatus;
import rs.id.webzine.domain.Category;
import rs.id.webzine.domain.Content;
import rs.id.webzine.domain.ContentType;
import rs.id.webzine.domain.Customer;
import rs.id.webzine.domain.ManagedContent;
import rs.id.webzine.domain.ReaderType;
import rs.id.webzine.domain.UserArticle;
import rs.id.webzine.domain.UserArticleStatus;
import rs.id.webzine.domain.project_management.Task;
import rs.id.webzine.domain.project_management.TaskAttachment;
import rs.id.webzine.domain.project_management.TaskComment;
import rs.id.webzine.domain.project_management.TaskPriority;
import rs.id.webzine.domain.project_management.TaskStatus;
import rs.id.webzine.domain.system.Country;
import rs.id.webzine.domain.system.Role;
import rs.id.webzine.domain.system.User;
import rs.id.webzine.domain.system.UserStatus;
import rs.id.webzine.service.project_management.TaskAttachmentService;
import rs.id.webzine.service.project_management.TaskCommentService;
import rs.id.webzine.service.project_management.TaskPriorityService;
import rs.id.webzine.service.project_management.TaskService;
import rs.id.webzine.service.project_management.TaskStatusService;
import rs.id.webzine.service.system.CountryService;
import rs.id.webzine.service.system.RoleService;
import rs.id.webzine.service.system.UserService;
import rs.id.webzine.service.system.UserStatusService;
import rs.id.webzine.web.system.UserForm;

/**
 * A central place to register application converters and formatters.
 */
@Configurable
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

  private static final Log log = LogFactory.getLog(ApplicationConversionServiceFactoryBean.class);

  @Autowired
  RoleService roleService;

  @Autowired
  CountryService countryService;

  @Autowired
  UserStatusService userStatusService;

  @Autowired
  UserService userService;

  @Autowired
  TaskStatusService taskStatusService;

  @Autowired
  TaskPriorityService taskPriorityService;

  @Autowired
  TaskService taskService;

  @Autowired
  TaskCommentService taskCommentService;

  @Autowired
  TaskAttachmentService taskAttachmentService;

  // role
  public Converter<Role, String> getRoleToStringConverter() {
    return new Converter<Role, java.lang.String>() {
      public String convert(Role role) {
        return role.getName();
      }
    };
  }

  public Converter<Integer, Role> getIdToRoleConverter() {
    return new Converter<java.lang.Integer, Role>() {
      public Role convert(java.lang.Integer id) {
        return roleService.find(id);
      }
    };
  }

  public Converter<String, Role> getStringToRoleConverter() {
    return new Converter<java.lang.String, Role>() {
      public Role convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), Role.class);
      }
    };
  }

  // user status
  public Converter<UserStatus, String> getUserStatusToStringConverter() {
    return new Converter<UserStatus, java.lang.String>() {
      public String convert(UserStatus userStatus) {
        return userStatus.getName();
      }
    };
  }

  public Converter<Integer, UserStatus> getIdToUserStatusConverter() {
    return new Converter<java.lang.Integer, UserStatus>() {
      public UserStatus convert(java.lang.Integer id) {
        return userStatusService.find(id);
      }
    };
  }

  public Converter<String, UserStatus> getStringToUserStatusConverter() {
    return new Converter<java.lang.String, UserStatus>() {
      public UserStatus convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), UserStatus.class);
      }
    };
  }

  // user
  public Converter<User, String> getUserToStringConverter() {
    return new Converter<User, java.lang.String>() {
      public String convert(User user) {
        if (user.getId() != -1) {
          return user.getUserName();
        } else {
          return "-";
        }
      }
    };
  }

  public Converter<Integer, User> getIdToUserConverter() {
    return new Converter<java.lang.Integer, User>() {
      public User convert(java.lang.Integer id) {
        if (id != -1) {
          return userService.find(id);
        } else {
          User emptyUser = new User();
          emptyUser.setId(-1);
          return emptyUser;
        }
      }
    };
  }

  public Converter<String, User> getStringToUserConverter() {
    return new Converter<java.lang.String, User>() {
      public User convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), User.class);
      }
    };
  }

  public Converter<UserForm, String> getUserFormToStringConverter() {
    return new Converter<UserForm, java.lang.String>() {
      public String convert(UserForm userForm) {
        return userForm.getUserName();
      }
    };
  }

  public Converter<Integer, UserForm> getIdToUserFormConverter() {
    return new Converter<java.lang.Integer, UserForm>() {
      public UserForm convert(java.lang.Integer id) {
        try {
          UserForm userForm = new UserForm();
          User user = userService.find(id);
          PropertyUtils.copyProperties(userForm, user);
          userForm.setUserId(user.getId());
          if (user.getAddress() != null) {
            PropertyUtils.copyProperties(userForm, user.getAddress());
          }
          return userForm;
        } catch (Exception e) {
          log.error(e);
          throw new RuntimeException(e);
        }
      }
    };
  }

  public Converter<String, UserForm> getStringToUserFormConverter() {
    return new Converter<java.lang.String, UserForm>() {
      public UserForm convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), UserForm.class);
      }
    };
  }

  // country
  public Converter<Country, String> getCountryToStringConverter() {
    return new Converter<Country, java.lang.String>() {
      public String convert(Country country) {
        return country.getName();
      }
    };
  }

  public Converter<Integer, Country> getIdToCountryConverter() {
    return new Converter<java.lang.Integer, Country>() {
      public Country convert(java.lang.Integer id) {
        return countryService.find(id);
      }
    };
  }

  public Converter<String, Country> getStringToCountryConverter() {
    return new Converter<java.lang.String, Country>() {
      public Country convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), Country.class);
      }
    };
  }

  // task status
  public Converter<TaskStatus, String> getTaskStatusToStringConverter() {
    return new Converter<TaskStatus, java.lang.String>() {
      public String convert(TaskStatus taskStatus) {
        return taskStatus.getName();
      }
    };
  }

  public Converter<Integer, TaskStatus> getIdToTaskStatusConverter() {
    return new Converter<java.lang.Integer, TaskStatus>() {
      public TaskStatus convert(java.lang.Integer id) {
        return taskStatusService.find(id);
      }
    };
  }

  public Converter<String, TaskStatus> getStringToTaskStatusConverter() {
    return new Converter<java.lang.String, TaskStatus>() {
      public TaskStatus convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), TaskStatus.class);
      }
    };
  }

  // task priority
  public Converter<TaskPriority, String> getTaskPriorityToStringConverter() {
    return new Converter<TaskPriority, java.lang.String>() {
      public String convert(TaskPriority taskPriority) {
        return taskPriority.getName();
      }
    };
  }

  public Converter<Integer, TaskPriority> getIdToTaskPriorityConverter() {
    return new Converter<java.lang.Integer, TaskPriority>() {
      public TaskPriority convert(java.lang.Integer id) {
        return taskPriorityService.find(id);
      }
    };
  }

  public Converter<String, TaskPriority> getStringToTaskPriorityConverter() {
    return new Converter<java.lang.String, TaskPriority>() {
      public TaskPriority convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), TaskPriority.class);
      }
    };
  }

  // task
  public Converter<Task, String> getTaskToStringConverter() {
    return new Converter<Task, java.lang.String>() {
      public String convert(Task task) {
        if (task.getId() != -1) {
          return task.getId() + " " + task.getTitle();
        } else {
          return "-";
        }
      }
    };
  }

  public Converter<Integer, Task> getIdToTaskConverter() {
    return new Converter<java.lang.Integer, Task>() {
      public Task convert(java.lang.Integer id) {
        if (id != -1) {
          return taskService.find(id);
        } else {
          Task emptyTask = new Task();
          emptyTask.setId(-1);
          return emptyTask;
        }
      }
    };
  }

  public Converter<String, Task> getStringToTaskConverter() {
    return new Converter<java.lang.String, Task>() {
      public Task convert(String id) {
        if (id != null && !id.isEmpty()) {
          return getObject().convert(getObject().convert(id, Integer.class), Task.class);
        } else {
          return new Task();
        }
      }
    };
  }

  // task comment
  public Converter<TaskComment, String> getTaskCommentToStringConverter() {
    return new Converter<TaskComment, java.lang.String>() {
      public String convert(TaskComment taskComment) {
        return taskComment.getId().toString();
      }
    };
  }

  public Converter<Integer, TaskComment> getIdToTaskCommentConverter() {
    return new Converter<java.lang.Integer, TaskComment>() {
      public TaskComment convert(java.lang.Integer id) {
        return taskCommentService.find(id);
      }
    };
  }

  public Converter<String, TaskComment> getStringToTaskCommentConverter() {
    return new Converter<java.lang.String, TaskComment>() {
      public TaskComment convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), TaskComment.class);
      }
    };
  }

  public Converter<TaskAttachment, String> getTaskAttachmentToStringConverter() {
    return new Converter<TaskAttachment, java.lang.String>() {
      public String convert(TaskAttachment taskAttachment) {
        return taskAttachment.getId().toString();
      }
    };
  }

  public Converter<Integer, TaskAttachment> getIdToTaskAttachmentConverter() {
    return new Converter<java.lang.Integer, TaskAttachment>() {
      public TaskAttachment convert(java.lang.Integer id) {
        return taskAttachmentService.find(id);
      }
    };
  }

  public Converter<String, TaskAttachment> getStringToTaskAttachmentConverter() {
    return new Converter<java.lang.String, TaskAttachment>() {
      public TaskAttachment convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), TaskAttachment.class);
      }
    };
  }

  // content type
  public Converter<ContentType, String> getContentTypeToStringConverter() {
    return new Converter<ContentType, java.lang.String>() {
      public String convert(ContentType contentType) {
        return new StringBuilder().append(contentType.getName()).toString();
      }
    };
  }

  public Converter<Integer, ContentType> getIdToContentTypeConverter() {
    return new Converter<java.lang.Integer, ContentType>() {
      public ContentType convert(java.lang.Integer id) {
        return ContentType.find(id);
      }
    };
  }

  public Converter<String, ContentType> getStringToContentTypeConverter() {
    return new Converter<java.lang.String, ContentType>() {
      public ContentType convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), ContentType.class);
      }
    };
  }

  // reader type
  public Converter<ReaderType, String> getReaderTypeToStringConverter() {
    return new Converter<ReaderType, java.lang.String>() {
      public String convert(ReaderType readerType) {
        return new StringBuilder().append(readerType.getName()).toString();
      }
    };
  }

  public Converter<Integer, ReaderType> getIdToReaderTypeConverter() {
    return new Converter<java.lang.Integer, ReaderType>() {
      public ReaderType convert(java.lang.Integer id) {
        return ReaderType.find(id);
      }
    };
  }

  public Converter<String, ReaderType> getStringToReaderTypeConverter() {
    return new Converter<java.lang.String, ReaderType>() {
      public ReaderType convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), ReaderType.class);
      }
    };
  }

  // category
  public Converter<Category, String> getCategoryToStringConverter() {
    return new Converter<Category, java.lang.String>() {
      public String convert(Category category) {
        return new StringBuilder().append(category.getName()).toString();
      }
    };
  }

  public Converter<Integer, Category> getIdToCategoryConverter() {
    return new Converter<java.lang.Integer, Category>() {
      public Category convert(java.lang.Integer id) {
        return Category.find(id);
      }
    };
  }

  public Converter<String, Category> getStringToCategoryConverter() {
    return new Converter<java.lang.String, Category>() {
      public Category convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), Category.class);
      }
    };
  }

  // article status
  public Converter<ArticleStatus, String> getArticleStatusToStringConverter() {
    return new Converter<ArticleStatus, java.lang.String>() {
      public String convert(ArticleStatus articleStatus) {
        return new StringBuilder().append(articleStatus.getName()).toString();
      }
    };
  }

  public Converter<Integer, ArticleStatus> getIdToArticleStatusConverter() {
    return new Converter<java.lang.Integer, ArticleStatus>() {
      public ArticleStatus convert(java.lang.Integer id) {
        return ArticleStatus.find(id);
      }
    };
  }

  public Converter<String, ArticleStatus> getStringToArticleStatusConverter() {
    return new Converter<java.lang.String, ArticleStatus>() {
      public ArticleStatus convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), ArticleStatus.class);
      }
    };
  }

  // article comment
  public Converter<ArticleCommentStatus, String> getArticleCommentStatusToStringConverter() {
    return new Converter<ArticleCommentStatus, java.lang.String>() {
      public String convert(ArticleCommentStatus articleStatus) {
        return new StringBuilder().append(articleStatus.getName()).toString();
      }
    };
  }

  public Converter<Integer, ArticleCommentStatus> getIdToArticleCommentStatusConverter() {
    return new Converter<java.lang.Integer, ArticleCommentStatus>() {
      public ArticleCommentStatus convert(java.lang.Integer id) {
        return ArticleCommentStatus.find(id);
      }
    };
  }

  public Converter<String, ArticleCommentStatus> getStringToArticleCommentStatusConverter() {
    return new Converter<java.lang.String, ArticleCommentStatus>() {
      public ArticleCommentStatus convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), ArticleCommentStatus.class);
      }
    };
  }

  // user article status
  public Converter<UserArticleStatus, String> getUserArticleStatusToStringConverter() {
    return new Converter<UserArticleStatus, java.lang.String>() {
      public String convert(UserArticleStatus articleStatus) {
        return new StringBuilder().append(articleStatus.getName()).toString();
      }
    };
  }

  public Converter<Integer, UserArticleStatus> getIdToUserArticleStatusConverter() {
    return new Converter<java.lang.Integer, UserArticleStatus>() {
      public UserArticleStatus convert(java.lang.Integer id) {
        return UserArticleStatus.find(id);
      }
    };
  }

  public Converter<String, UserArticleStatus> getStringToUserArticleStatusConverter() {
    return new Converter<java.lang.String, UserArticleStatus>() {
      public UserArticleStatus convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), UserArticleStatus.class);
      }
    };
  }

  // ad status
  public Converter<AdStatus, String> getAdStatusToStringConverter() {
    return new Converter<AdStatus, java.lang.String>() {
      public String convert(AdStatus adStatus) {
        return new StringBuilder().append(adStatus.getName()).toString();
      }
    };
  }

  public Converter<Integer, AdStatus> getIdToAdStatusConverter() {
    return new Converter<java.lang.Integer, AdStatus>() {
      public AdStatus convert(java.lang.Integer id) {
        return AdStatus.find(id);
      }
    };
  }

  public Converter<String, AdStatus> getStringToAdStatusConverter() {
    return new Converter<java.lang.String, AdStatus>() {
      public AdStatus convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), AdStatus.class);
      }
    };
  }

  public Converter<Ad, String> getAdToStringConverter() {
    return new Converter<Ad, java.lang.String>() {
      public String convert(Ad ad) {
        return new StringBuilder().append(ad.getName()).toString();
      }
    };
  }

  public Converter<Integer, Ad> getIdToAdConverter() {
    return new Converter<java.lang.Integer, Ad>() {
      public Ad convert(java.lang.Integer id) {
        return Ad.find(id);
      }
    };
  }

  public Converter<String, Ad> getStringToAdConverter() {
    return new Converter<java.lang.String, Ad>() {
      public Ad convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), Ad.class);
      }
    };
  }

  public Converter<Article, String> getArticleToStringConverter() {
    return new Converter<Article, java.lang.String>() {
      public String convert(Article article) {
        return new StringBuilder().append(article.getId()).toString();
      }
    };
  }

  public Converter<Integer, Article> getIdToArticleConverter() {
    return new Converter<java.lang.Integer, Article>() {
      public Article convert(java.lang.Integer id) {
        return Article.find(id);
      }
    };
  }

  public Converter<String, Article> getStringToArticleConverter() {
    return new Converter<java.lang.String, Article>() {
      public Article convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), Article.class);
      }
    };
  }

  public Converter<ArticleBookmark, String> getArticleBookmarkToStringConverter() {
    return new Converter<ArticleBookmark, java.lang.String>() {
      public String convert(ArticleBookmark articleBookmark) {
        return new StringBuilder().append(articleBookmark.getDc()).append(' ').append(articleBookmark.getDm())
            .toString();
      }
    };
  }

  public Converter<Integer, ArticleBookmark> getIdToArticleBookmarkConverter() {
    return new Converter<java.lang.Integer, ArticleBookmark>() {
      public ArticleBookmark convert(java.lang.Integer id) {
        return ArticleBookmark.findArticleBookmark(id);
      }
    };
  }

  public Converter<String, ArticleBookmark> getStringToArticleBookmarkConverter() {
    return new Converter<java.lang.String, ArticleBookmark>() {
      public ArticleBookmark convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), ArticleBookmark.class);
      }
    };
  }

  public Converter<ArticleCategory, String> getArticleCategoryToStringConverter() {
    return new Converter<ArticleCategory, java.lang.String>() {
      public String convert(ArticleCategory articleCategory) {
        return new StringBuilder().append(articleCategory.getDc()).append(' ').append(articleCategory.getDm())
            .toString();
      }
    };
  }

  public Converter<Integer, ArticleCategory> getIdToArticleCategoryConverter() {
    return new Converter<java.lang.Integer, ArticleCategory>() {
      public ArticleCategory convert(java.lang.Integer id) {
        return ArticleCategory.find(id);
      }
    };
  }

  public Converter<String, ArticleCategory> getStringToArticleCategoryConverter() {
    return new Converter<java.lang.String, ArticleCategory>() {
      public ArticleCategory convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), ArticleCategory.class);
      }
    };
  }

  public Converter<ArticleComment, String> getArticleCommentToStringConverter() {
    return new Converter<ArticleComment, java.lang.String>() {
      public String convert(ArticleComment articleComment) {
        return new StringBuilder().append(articleComment.getText()).append(' ').append(articleComment.getPublishedAt())
            .append(' ').append(articleComment.getDc()).toString();
      }
    };
  }

  public Converter<Integer, ArticleComment> getIdToArticleCommentConverter() {
    return new Converter<java.lang.Integer, ArticleComment>() {
      public ArticleComment convert(java.lang.Integer id) {
        return ArticleComment.find(id);
      }
    };
  }

  public Converter<String, ArticleComment> getStringToArticleCommentConverter() {
    return new Converter<java.lang.String, ArticleComment>() {
      public ArticleComment convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), ArticleComment.class);
      }
    };
  }

  public Converter<ArticleRate, String> getArticleRateToStringConverter() {
    return new Converter<ArticleRate, java.lang.String>() {
      public String convert(ArticleRate articleRate) {
        return new StringBuilder().append(articleRate.getRate()).append(' ').append(articleRate.getDc()).append(' ')
            .append(articleRate.getDm()).toString();
      }
    };
  }

  public Converter<Integer, ArticleRate> getIdToArticleRateConverter() {
    return new Converter<java.lang.Integer, ArticleRate>() {
      public ArticleRate convert(java.lang.Integer id) {
        return ArticleRate.findArticleRate(id);
      }
    };
  }

  public Converter<String, ArticleRate> getStringToArticleRateConverter() {
    return new Converter<java.lang.String, ArticleRate>() {
      public ArticleRate convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), ArticleRate.class);
      }
    };
  }

  public Converter<Content, String> getContentToStringConverter() {
    return new Converter<Content, java.lang.String>() {
      public String convert(Content content) {
        return new StringBuilder().append(content.getOrderNo()).append(' ').append(content.getDescription()).toString();
      }
    };
  }

  public Converter<Integer, Content> getIdToContentConverter() {
    return new Converter<java.lang.Integer, Content>() {
      public Content convert(java.lang.Integer id) {
        return Content.find(id);
      }
    };
  }

  public Converter<String, Content> getStringToContentConverter() {
    return new Converter<java.lang.String, Content>() {
      public Content convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), Content.class);
      }
    };
  }

  public Converter<Customer, String> getCustomerToStringConverter() {
    return new Converter<Customer, java.lang.String>() {
      public String convert(Customer customer) {
        return new StringBuilder().append(customer.getName()).append(' ').append(customer.getDescription()).toString();
      }
    };
  }

  public Converter<Integer, Customer> getIdToCustomerConverter() {
    return new Converter<java.lang.Integer, Customer>() {
      public Customer convert(java.lang.Integer id) {
        return Customer.find(id);
      }
    };
  }

  public Converter<String, Customer> getStringToCustomerConverter() {
    return new Converter<java.lang.String, Customer>() {
      public Customer convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), Customer.class);
      }
    };
  }

  public Converter<ManagedContent, String> getManagedContentToStringConverter() {
    return new Converter<ManagedContent, java.lang.String>() {
      public String convert(ManagedContent managedContent) {
        return new StringBuilder().append(managedContent.getCss()).toString();
      }
    };
  }

  public Converter<Integer, ManagedContent> getIdToManagedContentConverter() {
    return new Converter<java.lang.Integer, ManagedContent>() {
      public ManagedContent convert(java.lang.Integer id) {
        return ManagedContent.find(id);
      }
    };
  }

  public Converter<String, ManagedContent> getStringToManagedContentConverter() {
    return new Converter<java.lang.String, ManagedContent>() {
      public ManagedContent convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), ManagedContent.class);
      }
    };
  }

  public Converter<UserArticle, String> getUserArticleToStringConverter() {
    return new Converter<UserArticle, java.lang.String>() {
      public String convert(UserArticle userArticle) {
        return new StringBuilder().append(userArticle.getTitle()).append(' ').append(userArticle.getText()).append(' ')
            .toString();
      }
    };
  }

  public Converter<Integer, UserArticle> getIdToUserArticleConverter() {
    return new Converter<java.lang.Integer, UserArticle>() {
      public UserArticle convert(java.lang.Integer id) {
        return UserArticle.find(id);
      }
    };
  }

  public Converter<String, UserArticle> getStringToUserArticleConverter() {
    return new Converter<java.lang.String, UserArticle>() {
      public UserArticle convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), UserArticle.class);
      }
    };
  }

  public void installLabelConverters(FormatterRegistry registry) {
    // role
    registry.addConverter(getRoleToStringConverter());
    registry.addConverter(getIdToRoleConverter());
    registry.addConverter(getStringToRoleConverter());

    // user status
    registry.addConverter(getUserStatusToStringConverter());
    registry.addConverter(getIdToUserStatusConverter());
    registry.addConverter(getStringToUserStatusConverter());

    // user
    registry.addConverter(getUserToStringConverter());
    registry.addConverter(getIdToUserConverter());
    registry.addConverter(getStringToUserConverter());

    registry.addConverter(getUserFormToStringConverter());
    registry.addConverter(getIdToUserFormConverter());
    registry.addConverter(getStringToUserFormConverter());

    // country
    registry.addConverter(getCountryToStringConverter());
    registry.addConverter(getIdToCountryConverter());
    registry.addConverter(getStringToCountryConverter());

    // task status
    registry.addConverter(getTaskStatusToStringConverter());
    registry.addConverter(getIdToTaskStatusConverter());
    registry.addConverter(getStringToTaskStatusConverter());

    // task priority
    registry.addConverter(getTaskPriorityToStringConverter());
    registry.addConverter(getIdToTaskPriorityConverter());
    registry.addConverter(getStringToTaskPriorityConverter());

    // task
    registry.addConverter(getTaskToStringConverter());
    registry.addConverter(getIdToTaskConverter());
    registry.addConverter(getStringToTaskConverter());

    // task comment
    registry.addConverter(getTaskCommentToStringConverter());
    registry.addConverter(getIdToTaskCommentConverter());
    registry.addConverter(getStringToTaskCommentConverter());

    // task attachment
    registry.addConverter(getTaskAttachmentToStringConverter());
    registry.addConverter(getIdToTaskAttachmentConverter());
    registry.addConverter(getStringToTaskAttachmentConverter());

    // content type
    registry.addConverter(getContentTypeToStringConverter());
    registry.addConverter(getIdToContentTypeConverter());
    registry.addConverter(getStringToContentTypeConverter());

    // reader type
    registry.addConverter(getReaderTypeToStringConverter());
    registry.addConverter(getIdToReaderTypeConverter());
    registry.addConverter(getStringToReaderTypeConverter());

    // category
    registry.addConverter(getCategoryToStringConverter());
    registry.addConverter(getIdToCategoryConverter());
    registry.addConverter(getStringToCategoryConverter());

    // article status
    registry.addConverter(getArticleStatusToStringConverter());
    registry.addConverter(getIdToArticleStatusConverter());
    registry.addConverter(getStringToArticleStatusConverter());

    // article comment status
    registry.addConverter(getArticleCommentStatusToStringConverter());
    registry.addConverter(getIdToArticleCommentStatusConverter());
    registry.addConverter(getStringToArticleCommentStatusConverter());

    // user article status
    registry.addConverter(getUserArticleStatusToStringConverter());
    registry.addConverter(getIdToUserArticleStatusConverter());
    registry.addConverter(getStringToUserArticleStatusConverter());

    // user article
    registry.addConverter(getUserArticleToStringConverter());
    registry.addConverter(getIdToUserArticleConverter());
    registry.addConverter(getStringToUserArticleConverter());

    // ad status
    registry.addConverter(getAdStatusToStringConverter());
    registry.addConverter(getIdToAdStatusConverter());
    registry.addConverter(getStringToAdStatusConverter());

    registry.addConverter(getAdToStringConverter());
    registry.addConverter(getIdToAdConverter());
    registry.addConverter(getStringToAdConverter());
    registry.addConverter(getArticleToStringConverter());
    registry.addConverter(getIdToArticleConverter());
    registry.addConverter(getStringToArticleConverter());
    registry.addConverter(getArticleBookmarkToStringConverter());
    registry.addConverter(getIdToArticleBookmarkConverter());
    registry.addConverter(getStringToArticleBookmarkConverter());
    registry.addConverter(getArticleCategoryToStringConverter());
    registry.addConverter(getIdToArticleCategoryConverter());
    registry.addConverter(getStringToArticleCategoryConverter());
    registry.addConverter(getArticleCommentToStringConverter());
    registry.addConverter(getIdToArticleCommentConverter());
    registry.addConverter(getStringToArticleCommentConverter());
    registry.addConverter(getArticleRateToStringConverter());
    registry.addConverter(getIdToArticleRateConverter());
    registry.addConverter(getStringToArticleRateConverter());
    registry.addConverter(getContentToStringConverter());
    registry.addConverter(getIdToContentConverter());
    registry.addConverter(getStringToContentConverter());

    registry.addConverter(getCustomerToStringConverter());
    registry.addConverter(getIdToCustomerConverter());
    registry.addConverter(getStringToCustomerConverter());
    registry.addConverter(getManagedContentToStringConverter());
    registry.addConverter(getIdToManagedContentConverter());
    registry.addConverter(getStringToManagedContentConverter());

    registry.addConverter(getReaderTypeToStringConverter());
    registry.addConverter(getIdToReaderTypeConverter());
    registry.addConverter(getStringToReaderTypeConverter());

  }

  public void afterPropertiesSet() {
    super.afterPropertiesSet();
    installLabelConverters(getObject());
  }

  @SuppressWarnings("deprecation")
  @Override
  protected void installFormatters(FormatterRegistry registry) {
    super.installFormatters(registry);
  }
}
