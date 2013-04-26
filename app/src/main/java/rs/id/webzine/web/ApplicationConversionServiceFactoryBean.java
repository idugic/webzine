package rs.id.webzine.web;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

import rs.id.webzine.domain.content_management.Content;
import rs.id.webzine.domain.content_management.ContentType;
import rs.id.webzine.domain.content_management.ManagedContent;
import rs.id.webzine.domain.magazine.Article;
import rs.id.webzine.domain.magazine.ArticleBookmark;
import rs.id.webzine.domain.magazine.ArticleCategory;
import rs.id.webzine.domain.magazine.ArticleComment;
import rs.id.webzine.domain.magazine.ArticleCommentStatus;
import rs.id.webzine.domain.magazine.ArticleRate;
import rs.id.webzine.domain.magazine.ArticleStatus;
import rs.id.webzine.domain.magazine.Category;
import rs.id.webzine.domain.magazine.ReaderType;
import rs.id.webzine.domain.marketing.Ad;
import rs.id.webzine.domain.marketing.AdStatus;
import rs.id.webzine.domain.marketing.Advertiser;
import rs.id.webzine.domain.project_management.Task;
import rs.id.webzine.domain.project_management.TaskAttachment;
import rs.id.webzine.domain.project_management.TaskComment;
import rs.id.webzine.domain.project_management.TaskPriority;
import rs.id.webzine.domain.project_management.TaskStatus;
import rs.id.webzine.domain.system.Country;
import rs.id.webzine.domain.system.Role;
import rs.id.webzine.domain.system.User;
import rs.id.webzine.domain.system.UserStatus;
import rs.id.webzine.service.content_management.ContentService;
import rs.id.webzine.service.content_management.ContentTypeService;
import rs.id.webzine.service.content_management.ManagedContentService;
import rs.id.webzine.service.magazine.ArticleBookmarkService;
import rs.id.webzine.service.magazine.ArticleCategoryService;
import rs.id.webzine.service.magazine.ArticleCommentService;
import rs.id.webzine.service.magazine.ArticleCommentStatusService;
import rs.id.webzine.service.magazine.ArticleRateService;
import rs.id.webzine.service.magazine.ArticleService;
import rs.id.webzine.service.magazine.ArticleStatusService;
import rs.id.webzine.service.magazine.CategoryService;
import rs.id.webzine.service.magazine.ReaderTypeService;
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

  @Autowired
  ManagedContentService managedContentService;

  @Autowired
  ContentTypeService contentTypeService;

  @Autowired
  ContentService contentService;

  @Autowired
  ReaderTypeService readerTypeService;

  @Autowired
  CategoryService categoryService;

  @Autowired
  ArticleStatusService articleStatusService;

  @Autowired
  ArticleService articleService;

  @Autowired
  ArticleCategoryService articleCategoryService;

  @Autowired
  ArticleRateService articleRateService;

  @Autowired
  ArticleCommentStatusService articleCommentStatusService;

  @Autowired
  ArticleCommentService articleCommentService;

  @Autowired
  ArticleBookmarkService articleBookmarkService;

  // role
  public Converter<Role, String> getRoleToStringConverter() {
    return new Converter<Role, String>() {
      public String convert(Role role) {
        return role.getName();
      }
    };
  }

  public Converter<Integer, Role> getIdToRoleConverter() {
    return new Converter<Integer, Role>() {
      public Role convert(Integer id) {
        return roleService.find(id);
      }
    };
  }

  public Converter<String, Role> getStringToRoleConverter() {
    return new Converter<String, Role>() {
      public Role convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), Role.class);
      }
    };
  }

  // user status
  public Converter<UserStatus, String> getUserStatusToStringConverter() {
    return new Converter<UserStatus, String>() {
      public String convert(UserStatus userStatus) {
        return userStatus.getName();
      }
    };
  }

  public Converter<Integer, UserStatus> getIdToUserStatusConverter() {
    return new Converter<Integer, UserStatus>() {
      public UserStatus convert(Integer id) {
        return userStatusService.find(id);
      }
    };
  }

  public Converter<String, UserStatus> getStringToUserStatusConverter() {
    return new Converter<String, UserStatus>() {
      public UserStatus convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), UserStatus.class);
      }
    };
  }

  // user
  public Converter<User, String> getUserToStringConverter() {
    return new Converter<User, String>() {
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
    return new Converter<Integer, User>() {
      public User convert(Integer id) {
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
    return new Converter<String, User>() {
      public User convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), User.class);
      }
    };
  }

  public Converter<UserForm, String> getUserFormToStringConverter() {
    return new Converter<UserForm, String>() {
      public String convert(UserForm userForm) {
        return userForm.getUserName();
      }
    };
  }

  public Converter<Integer, UserForm> getIdToUserFormConverter() {
    return new Converter<Integer, UserForm>() {
      public UserForm convert(Integer id) {
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
    return new Converter<String, UserForm>() {
      public UserForm convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), UserForm.class);
      }
    };
  }

  // country
  public Converter<Country, String> getCountryToStringConverter() {
    return new Converter<Country, String>() {
      public String convert(Country country) {
        return country.getName();
      }
    };
  }

  public Converter<Integer, Country> getIdToCountryConverter() {
    return new Converter<Integer, Country>() {
      public Country convert(Integer id) {
        return countryService.find(id);
      }
    };
  }

  public Converter<String, Country> getStringToCountryConverter() {
    return new Converter<String, Country>() {
      public Country convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), Country.class);
      }
    };
  }

  // task status
  public Converter<TaskStatus, String> getTaskStatusToStringConverter() {
    return new Converter<TaskStatus, String>() {
      public String convert(TaskStatus taskStatus) {
        return taskStatus.getName();
      }
    };
  }

  public Converter<Integer, TaskStatus> getIdToTaskStatusConverter() {
    return new Converter<Integer, TaskStatus>() {
      public TaskStatus convert(Integer id) {
        return taskStatusService.find(id);
      }
    };
  }

  public Converter<String, TaskStatus> getStringToTaskStatusConverter() {
    return new Converter<String, TaskStatus>() {
      public TaskStatus convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), TaskStatus.class);
      }
    };
  }

  // task priority
  public Converter<TaskPriority, String> getTaskPriorityToStringConverter() {
    return new Converter<TaskPriority, String>() {
      public String convert(TaskPriority taskPriority) {
        return taskPriority.getName();
      }
    };
  }

  public Converter<Integer, TaskPriority> getIdToTaskPriorityConverter() {
    return new Converter<Integer, TaskPriority>() {
      public TaskPriority convert(Integer id) {
        return taskPriorityService.find(id);
      }
    };
  }

  public Converter<String, TaskPriority> getStringToTaskPriorityConverter() {
    return new Converter<String, TaskPriority>() {
      public TaskPriority convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), TaskPriority.class);
      }
    };
  }

  // task
  public Converter<Task, String> getTaskToStringConverter() {
    return new Converter<Task, String>() {
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
    return new Converter<Integer, Task>() {
      public Task convert(Integer id) {
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
    return new Converter<String, Task>() {
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
    return new Converter<TaskComment, String>() {
      public String convert(TaskComment taskComment) {
        return taskComment.getId().toString();
      }
    };
  }

  public Converter<Integer, TaskComment> getIdToTaskCommentConverter() {
    return new Converter<Integer, TaskComment>() {
      public TaskComment convert(Integer id) {
        return taskCommentService.find(id);
      }
    };
  }

  public Converter<String, TaskComment> getStringToTaskCommentConverter() {
    return new Converter<String, TaskComment>() {
      public TaskComment convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), TaskComment.class);
      }
    };
  }

  // task attachment
  public Converter<TaskAttachment, String> getTaskAttachmentToStringConverter() {
    return new Converter<TaskAttachment, String>() {
      public String convert(TaskAttachment taskAttachment) {
        return taskAttachment.getId().toString();
      }
    };
  }

  public Converter<Integer, TaskAttachment> getIdToTaskAttachmentConverter() {
    return new Converter<Integer, TaskAttachment>() {
      public TaskAttachment convert(Integer id) {
        return taskAttachmentService.find(id);
      }
    };
  }

  public Converter<String, TaskAttachment> getStringToTaskAttachmentConverter() {
    return new Converter<String, TaskAttachment>() {
      public TaskAttachment convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), TaskAttachment.class);
      }
    };
  }

  // managed content
  public Converter<ManagedContent, String> getManagedContentToStringConverter() {
    return new Converter<ManagedContent, String>() {
      public String convert(ManagedContent managedContent) {
        return managedContent.getId().toString();
      }
    };
  }

  public Converter<Integer, ManagedContent> getIdToManagedContentConverter() {
    return new Converter<Integer, ManagedContent>() {
      public ManagedContent convert(Integer id) {
        return managedContentService.find(id);
      }
    };
  }

  public Converter<String, ManagedContent> getStringToManagedContentConverter() {
    return new Converter<String, ManagedContent>() {
      public ManagedContent convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), ManagedContent.class);
      }
    };
  }

  // content type
  public Converter<ContentType, String> getContentTypeToStringConverter() {
    return new Converter<ContentType, String>() {
      public String convert(ContentType contentType) {
        return contentType.getName();
      }
    };
  }

  public Converter<Integer, ContentType> getIdToContentTypeConverter() {
    return new Converter<Integer, ContentType>() {
      public ContentType convert(Integer id) {
        return contentTypeService.find(id);
      }
    };
  }

  public Converter<String, ContentType> getStringToContentTypeConverter() {
    return new Converter<String, ContentType>() {
      public ContentType convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), ContentType.class);
      }
    };
  }

  // content
  public Converter<Content, String> getContentToStringConverter() {
    return new Converter<Content, String>() {
      public String convert(Content content) {
        return content.getId().toString();
      }
    };
  }

  public Converter<Integer, Content> getIdToContentConverter() {
    return new Converter<Integer, Content>() {
      public Content convert(Integer id) {
        return contentService.find(id);
      }
    };
  }

  public Converter<String, Content> getStringToContentConverter() {
    return new Converter<String, Content>() {
      public Content convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), Content.class);
      }
    };
  }

  // reader type
  public Converter<ReaderType, String> getReaderTypeToStringConverter() {
    return new Converter<ReaderType, String>() {
      public String convert(ReaderType readerType) {
        return new StringBuilder().append(readerType.getName()).toString();
      }
    };
  }

  public Converter<Integer, ReaderType> getIdToReaderTypeConverter() {
    return new Converter<Integer, ReaderType>() {
      public ReaderType convert(Integer id) {
        return readerTypeService.find(id);
      }
    };
  }

  public Converter<String, ReaderType> getStringToReaderTypeConverter() {
    return new Converter<String, ReaderType>() {
      public ReaderType convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), ReaderType.class);
      }
    };
  }

  // category
  public Converter<Category, String> getCategoryToStringConverter() {
    return new Converter<Category, String>() {
      public String convert(Category category) {
        return new StringBuilder().append(category.getName()).toString();
      }
    };
  }

  public Converter<Integer, Category> getIdToCategoryConverter() {
    return new Converter<Integer, Category>() {
      public Category convert(Integer id) {
        return categoryService.find(id);
      }
    };
  }

  public Converter<String, Category> getStringToCategoryConverter() {
    return new Converter<String, Category>() {
      public Category convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), Category.class);
      }
    };
  }

  // article status
  public Converter<ArticleStatus, String> getArticleStatusToStringConverter() {
    return new Converter<ArticleStatus, String>() {
      public String convert(ArticleStatus articleStatus) {
        return new StringBuilder().append(articleStatus.getName()).toString();
      }
    };
  }

  public Converter<Integer, ArticleStatus> getIdToArticleStatusConverter() {
    return new Converter<Integer, ArticleStatus>() {
      public ArticleStatus convert(Integer id) {
        return articleStatusService.find(id);
      }
    };
  }

  public Converter<String, ArticleStatus> getStringToArticleStatusConverter() {
    return new Converter<String, ArticleStatus>() {
      public ArticleStatus convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), ArticleStatus.class);
      }
    };
  }

  // article
  public Converter<Article, String> getArticleToStringConverter() {
    return new Converter<Article, String>() {
      public String convert(Article article) {
        return new StringBuilder().append(article.getId()).toString();
      }
    };
  }

  public Converter<Integer, Article> getIdToArticleConverter() {
    return new Converter<Integer, Article>() {
      public Article convert(Integer id) {
        return articleService.find(id);
      }
    };
  }

  public Converter<String, Article> getStringToArticleConverter() {
    return new Converter<String, Article>() {
      public Article convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), Article.class);
      }
    };
  }

  public Converter<ArticleCategory, String> getArticleCategoryToStringConverter() {
    return new Converter<ArticleCategory, String>() {
      public String convert(ArticleCategory articleCategory) {
        return new StringBuilder().append(articleCategory.getDc()).append(' ').append(articleCategory.getDm())
            .toString();
      }
    };
  }

  // article category
  public Converter<Integer, ArticleCategory> getIdToArticleCategoryConverter() {
    return new Converter<Integer, ArticleCategory>() {
      public ArticleCategory convert(Integer id) {
        return articleCategoryService.find(id);
      }
    };
  }

  public Converter<String, ArticleCategory> getStringToArticleCategoryConverter() {
    return new Converter<String, ArticleCategory>() {
      public ArticleCategory convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), ArticleCategory.class);
      }
    };
  }

  // article rate
  public Converter<ArticleRate, String> getArticleRateToStringConverter() {
    return new Converter<ArticleRate, String>() {
      public String convert(ArticleRate articleRate) {
        return new StringBuilder().append(articleRate.getRate()).append(' ').append(articleRate.getDc()).append(' ')
            .append(articleRate.getDm()).toString();
      }
    };
  }

  public Converter<Integer, ArticleRate> getIdToArticleRateConverter() {
    return new Converter<Integer, ArticleRate>() {
      public ArticleRate convert(Integer id) {
        return articleRateService.find(id);
      }
    };
  }

  public Converter<String, ArticleRate> getStringToArticleRateConverter() {
    return new Converter<String, ArticleRate>() {
      public ArticleRate convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), ArticleRate.class);
      }
    };
  }

  // article comment status
  public Converter<ArticleCommentStatus, String> getArticleCommentStatusToStringConverter() {
    return new Converter<ArticleCommentStatus, String>() {
      public String convert(ArticleCommentStatus articleStatus) {
        return new StringBuilder().append(articleStatus.getName()).toString();
      }
    };
  }

  public Converter<Integer, ArticleCommentStatus> getIdToArticleCommentStatusConverter() {
    return new Converter<Integer, ArticleCommentStatus>() {
      public ArticleCommentStatus convert(Integer id) {
        return articleCommentStatusService.find(id);
      }
    };
  }

  public Converter<String, ArticleCommentStatus> getStringToArticleCommentStatusConverter() {
    return new Converter<String, ArticleCommentStatus>() {
      public ArticleCommentStatus convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), ArticleCommentStatus.class);
      }
    };
  }

  // article comment
  public Converter<ArticleComment, String> getArticleCommentToStringConverter() {
    return new Converter<ArticleComment, String>() {
      public String convert(ArticleComment articleComment) {
        return new StringBuilder().append(articleComment.getText()).append(' ').append(articleComment.getPublishedAt())
            .append(' ').append(articleComment.getDc()).toString();
      }
    };
  }

  public Converter<Integer, ArticleComment> getIdToArticleCommentConverter() {
    return new Converter<Integer, ArticleComment>() {
      public ArticleComment convert(Integer id) {
        return articleCommentService.find(id);
      }
    };
  }

  public Converter<String, ArticleComment> getStringToArticleCommentConverter() {
    return new Converter<String, ArticleComment>() {
      public ArticleComment convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), ArticleComment.class);
      }
    };
  }

  // article bookmark
  public Converter<ArticleBookmark, String> getArticleBookmarkToStringConverter() {
    return new Converter<ArticleBookmark, String>() {
      public String convert(ArticleBookmark articleBookmark) {
        return new StringBuilder().append(articleBookmark.getId()).toString();
      }
    };
  }

  public Converter<Integer, ArticleBookmark> getIdToArticleBookmarkConverter() {
    return new Converter<Integer, ArticleBookmark>() {
      public ArticleBookmark convert(Integer id) {
        return articleBookmarkService.find(id);
      }
    };
  }

  public Converter<String, ArticleBookmark> getStringToArticleBookmarkConverter() {
    return new Converter<String, ArticleBookmark>() {
      public ArticleBookmark convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), ArticleBookmark.class);
      }
    };
  }

  // ad status
  public Converter<AdStatus, String> getAdStatusToStringConverter() {
    return new Converter<AdStatus, String>() {
      public String convert(AdStatus adStatus) {
        return new StringBuilder().append(adStatus.getName()).toString();
      }
    };
  }

  public Converter<Integer, AdStatus> getIdToAdStatusConverter() {
    return new Converter<Integer, AdStatus>() {
      public AdStatus convert(Integer id) {
        return AdStatus.find(id);
      }
    };
  }

  public Converter<String, AdStatus> getStringToAdStatusConverter() {
    return new Converter<String, AdStatus>() {
      public AdStatus convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), AdStatus.class);
      }
    };
  }

  public Converter<Ad, String> getAdToStringConverter() {
    return new Converter<Ad, String>() {
      public String convert(Ad ad) {
        return new StringBuilder().append(ad.getName()).toString();
      }
    };
  }

  public Converter<Integer, Ad> getIdToAdConverter() {
    return new Converter<Integer, Ad>() {
      public Ad convert(Integer id) {
        return Ad.find(id);
      }
    };
  }

  public Converter<String, Ad> getStringToAdConverter() {
    return new Converter<String, Ad>() {
      public Ad convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), Ad.class);
      }
    };
  }

  public Converter<Advertiser, String> getCustomerToStringConverter() {
    return new Converter<Advertiser, String>() {
      public String convert(Advertiser customer) {
        return new StringBuilder().append(customer.getName()).append(' ').append(customer.getDescription()).toString();
      }
    };
  }

  public Converter<Integer, Advertiser> getIdToCustomerConverter() {
    return new Converter<Integer, Advertiser>() {
      public Advertiser convert(Integer id) {
        return Advertiser.find(id);
      }
    };
  }

  public Converter<String, Advertiser> getStringToCustomerConverter() {
    return new Converter<String, Advertiser>() {
      public Advertiser convert(String id) {
        return getObject().convert(getObject().convert(id, Integer.class), Advertiser.class);
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

    // managed content
    registry.addConverter(getManagedContentToStringConverter());
    registry.addConverter(getIdToManagedContentConverter());
    registry.addConverter(getStringToManagedContentConverter());

    // content type
    registry.addConverter(getContentTypeToStringConverter());
    registry.addConverter(getIdToContentTypeConverter());
    registry.addConverter(getStringToContentTypeConverter());

    // content
    registry.addConverter(getContentToStringConverter());
    registry.addConverter(getIdToContentConverter());
    registry.addConverter(getStringToContentConverter());

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

    // article
    registry.addConverter(getArticleToStringConverter());
    registry.addConverter(getIdToArticleConverter());
    registry.addConverter(getStringToArticleConverter());

    // article category
    registry.addConverter(getArticleCategoryToStringConverter());
    registry.addConverter(getIdToArticleCategoryConverter());
    registry.addConverter(getStringToArticleCategoryConverter());

    // article rate
    registry.addConverter(getArticleRateToStringConverter());
    registry.addConverter(getIdToArticleRateConverter());
    registry.addConverter(getStringToArticleRateConverter());

    // article comment status
    registry.addConverter(getArticleCommentStatusToStringConverter());
    registry.addConverter(getIdToArticleCommentStatusConverter());
    registry.addConverter(getStringToArticleCommentStatusConverter());

    // article comment
    registry.addConverter(getArticleCommentToStringConverter());
    registry.addConverter(getIdToArticleCommentConverter());
    registry.addConverter(getStringToArticleCommentConverter());

    // article bookmark
    registry.addConverter(getArticleBookmarkToStringConverter());
    registry.addConverter(getIdToArticleBookmarkConverter());
    registry.addConverter(getStringToArticleBookmarkConverter());

    // ad status
    registry.addConverter(getAdStatusToStringConverter());
    registry.addConverter(getIdToAdStatusConverter());
    registry.addConverter(getStringToAdStatusConverter());

    registry.addConverter(getAdToStringConverter());
    registry.addConverter(getIdToAdConverter());
    registry.addConverter(getStringToAdConverter());

    registry.addConverter(getCustomerToStringConverter());
    registry.addConverter(getIdToCustomerConverter());
    registry.addConverter(getStringToCustomerConverter());

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
