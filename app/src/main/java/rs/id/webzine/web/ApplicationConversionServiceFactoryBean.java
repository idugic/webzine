package rs.id.webzine.web;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

import rs.id.webzine.domain.Ad;
import rs.id.webzine.domain.Address;
import rs.id.webzine.domain.Article;
import rs.id.webzine.domain.ArticleBookmark;
import rs.id.webzine.domain.ArticleCategory;
import rs.id.webzine.domain.ArticleComment;
import rs.id.webzine.domain.ArticleRate;
import rs.id.webzine.domain.Category;
import rs.id.webzine.domain.Content;
import rs.id.webzine.domain.ContentType;
import rs.id.webzine.domain.Country;
import rs.id.webzine.domain.Customer;
import rs.id.webzine.domain.ManagedContent;
import rs.id.webzine.domain.News;
import rs.id.webzine.domain.ReaderType;
import rs.id.webzine.domain.Roles;
import rs.id.webzine.domain.Task;
import rs.id.webzine.domain.TaskAttachment;
import rs.id.webzine.domain.TaskComment;
import rs.id.webzine.domain.UserArticle;
import rs.id.webzine.domain.UserProfile;
import rs.id.webzine.domain.UserStatus;
import rs.id.webzine.domain.Users;

@Configurable
/**
 * A central place to register application converters and formatters. 
 */
public class ApplicationConversionServiceFactoryBean extends
		FormattingConversionServiceFactoryBean {

	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
	}

	public Converter<Ad, String> getAdToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<rs.id.webzine.domain.Ad, java.lang.String>() {
			public String convert(Ad ad) {
				return new StringBuilder().append(ad.getStatus()).append(' ')
						.append(ad.getDescription()).append(' ')
						.append(ad.getValidFrom()).append(' ')
						.append(ad.getValidTo()).toString();
			}
		};
	}

	public Converter<Integer, Ad> getIdToAdConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Integer, rs.id.webzine.domain.Ad>() {
			public rs.id.webzine.domain.Ad convert(java.lang.Integer id) {
				return Ad.findAd(id);
			}
		};
	}

	public Converter<String, Ad> getStringToAdConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, rs.id.webzine.domain.Ad>() {
			public rs.id.webzine.domain.Ad convert(String id) {
				return getObject().convert(
						getObject().convert(id, Integer.class), Ad.class);
			}
		};
	}

	public Converter<Address, String> getAddressToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<rs.id.webzine.domain.Address, java.lang.String>() {
			public String convert(Address address) {
				return new StringBuilder().append(address.getEmail())
						.append(' ').append(address.getPhone()).append(' ')
						.append(address.getStreetLine()).append(' ')
						.append(address.getCity()).toString();
			}
		};
	}

	public Converter<Integer, Address> getIdToAddressConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Integer, rs.id.webzine.domain.Address>() {
			public rs.id.webzine.domain.Address convert(java.lang.Integer id) {
				return Address.findAddress(id);
			}
		};
	}

	public Converter<String, Address> getStringToAddressConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, rs.id.webzine.domain.Address>() {
			public rs.id.webzine.domain.Address convert(String id) {
				return getObject().convert(
						getObject().convert(id, Integer.class), Address.class);
			}
		};
	}

	public Converter<Article, String> getArticleToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<rs.id.webzine.domain.Article, java.lang.String>() {
			public String convert(Article article) {
				return new StringBuilder().append(article.getStatus())
						.append(' ').append(article.getTitle()).append(' ')
						.append(article.getAbstractText()).append(' ')
						.append(article.getAbstractImage()).toString();
			}
		};
	}

	public Converter<Integer, Article> getIdToArticleConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Integer, rs.id.webzine.domain.Article>() {
			public rs.id.webzine.domain.Article convert(java.lang.Integer id) {
				return Article.findArticle(id);
			}
		};
	}

	public Converter<String, Article> getStringToArticleConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, rs.id.webzine.domain.Article>() {
			public rs.id.webzine.domain.Article convert(String id) {
				return getObject().convert(
						getObject().convert(id, Integer.class), Article.class);
			}
		};
	}

	public Converter<ArticleBookmark, String> getArticleBookmarkToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<rs.id.webzine.domain.ArticleBookmark, java.lang.String>() {
			public String convert(ArticleBookmark articleBookmark) {
				return new StringBuilder().append(articleBookmark.getDc())
						.append(' ').append(articleBookmark.getDm()).toString();
			}
		};
	}

	public Converter<Integer, ArticleBookmark> getIdToArticleBookmarkConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Integer, rs.id.webzine.domain.ArticleBookmark>() {
			public rs.id.webzine.domain.ArticleBookmark convert(
					java.lang.Integer id) {
				return ArticleBookmark.findArticleBookmark(id);
			}
		};
	}

	public Converter<String, ArticleBookmark> getStringToArticleBookmarkConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, rs.id.webzine.domain.ArticleBookmark>() {
			public rs.id.webzine.domain.ArticleBookmark convert(String id) {
				return getObject().convert(
						getObject().convert(id, Integer.class),
						ArticleBookmark.class);
			}
		};
	}

	public Converter<ArticleCategory, String> getArticleCategoryToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<rs.id.webzine.domain.ArticleCategory, java.lang.String>() {
			public String convert(ArticleCategory articleCategory) {
				return new StringBuilder().append(articleCategory.getDc())
						.append(' ').append(articleCategory.getDm()).toString();
			}
		};
	}

	public Converter<Integer, ArticleCategory> getIdToArticleCategoryConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Integer, rs.id.webzine.domain.ArticleCategory>() {
			public rs.id.webzine.domain.ArticleCategory convert(
					java.lang.Integer id) {
				return ArticleCategory.findArticleCategory(id);
			}
		};
	}

	public Converter<String, ArticleCategory> getStringToArticleCategoryConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, rs.id.webzine.domain.ArticleCategory>() {
			public rs.id.webzine.domain.ArticleCategory convert(String id) {
				return getObject().convert(
						getObject().convert(id, Integer.class),
						ArticleCategory.class);
			}
		};
	}

	public Converter<ArticleComment, String> getArticleCommentToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<rs.id.webzine.domain.ArticleComment, java.lang.String>() {
			public String convert(ArticleComment articleComment) {
				return new StringBuilder().append(articleComment.getStatus())
						.append(' ').append(articleComment.getText())
						.append(' ').append(articleComment.getPublishedAt())
						.append(' ').append(articleComment.getDc()).toString();
			}
		};
	}

	public Converter<Integer, ArticleComment> getIdToArticleCommentConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Integer, rs.id.webzine.domain.ArticleComment>() {
			public rs.id.webzine.domain.ArticleComment convert(
					java.lang.Integer id) {
				return ArticleComment.findArticleComment(id);
			}
		};
	}

	public Converter<String, ArticleComment> getStringToArticleCommentConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, rs.id.webzine.domain.ArticleComment>() {
			public rs.id.webzine.domain.ArticleComment convert(String id) {
				return getObject().convert(
						getObject().convert(id, Integer.class),
						ArticleComment.class);
			}
		};
	}

	public Converter<ArticleRate, String> getArticleRateToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<rs.id.webzine.domain.ArticleRate, java.lang.String>() {
			public String convert(ArticleRate articleRate) {
				return new StringBuilder().append(articleRate.getRate())
						.append(' ').append(articleRate.getDc()).append(' ')
						.append(articleRate.getDm()).toString();
			}
		};
	}

	public Converter<Integer, ArticleRate> getIdToArticleRateConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Integer, rs.id.webzine.domain.ArticleRate>() {
			public rs.id.webzine.domain.ArticleRate convert(java.lang.Integer id) {
				return ArticleRate.findArticleRate(id);
			}
		};
	}

	public Converter<String, ArticleRate> getStringToArticleRateConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, rs.id.webzine.domain.ArticleRate>() {
			public rs.id.webzine.domain.ArticleRate convert(String id) {
				return getObject().convert(
						getObject().convert(id, Integer.class),
						ArticleRate.class);
			}
		};
	}

	public Converter<Category, String> getCategoryToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<rs.id.webzine.domain.Category, java.lang.String>() {
			public String convert(Category category) {
				return new StringBuilder().append(category.getName())
						.append(' ').append(category.getDc()).append(' ')
						.append(category.getDm()).toString();
			}
		};
	}

	public Converter<Integer, Category> getIdToCategoryConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Integer, rs.id.webzine.domain.Category>() {
			public rs.id.webzine.domain.Category convert(java.lang.Integer id) {
				return Category.findCategory(id);
			}
		};
	}

	public Converter<String, Category> getStringToCategoryConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, rs.id.webzine.domain.Category>() {
			public rs.id.webzine.domain.Category convert(String id) {
				return getObject().convert(
						getObject().convert(id, Integer.class), Category.class);
			}
		};
	}

	public Converter<Content, String> getContentToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<rs.id.webzine.domain.Content, java.lang.String>() {
			public String convert(Content content) {
				return new StringBuilder().append(content.getOrderNo())
						.append(' ').append(content.getDescription())
						.append(' ').append(content.getText()).append(' ')
						.append(content.getImage()).toString();
			}
		};
	}

	public Converter<Integer, Content> getIdToContentConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Integer, rs.id.webzine.domain.Content>() {
			public rs.id.webzine.domain.Content convert(java.lang.Integer id) {
				return Content.findContent(id);
			}
		};
	}

	public Converter<String, Content> getStringToContentConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, rs.id.webzine.domain.Content>() {
			public rs.id.webzine.domain.Content convert(String id) {
				return getObject().convert(
						getObject().convert(id, Integer.class), Content.class);
			}
		};
	}

	public Converter<ContentType, String> getContentTypeToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<rs.id.webzine.domain.ContentType, java.lang.String>() {
			public String convert(ContentType contentType) {
				return new StringBuilder().append(contentType.getCd())
						.append(' ').append(contentType.getName()).toString();
			}
		};
	}

	public Converter<Integer, ContentType> getIdToContentTypeConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Integer, rs.id.webzine.domain.ContentType>() {
			public rs.id.webzine.domain.ContentType convert(java.lang.Integer id) {
				return ContentType.findContentType(id);
			}
		};
	}

	public Converter<String, ContentType> getStringToContentTypeConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, rs.id.webzine.domain.ContentType>() {
			public rs.id.webzine.domain.ContentType convert(String id) {
				return getObject().convert(
						getObject().convert(id, Integer.class),
						ContentType.class);
			}
		};
	}

	public Converter<Country, String> getCountryToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<rs.id.webzine.domain.Country, java.lang.String>() {
			public String convert(Country country) {
				return new StringBuilder().append(country.getCd()).append(' ')
						.append(country.getName()).toString();
			}
		};
	}

	public Converter<Integer, Country> getIdToCountryConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Integer, rs.id.webzine.domain.Country>() {
			public rs.id.webzine.domain.Country convert(java.lang.Integer id) {
				return Country.find(id);
			}
		};
	}

	public Converter<String, Country> getStringToCountryConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, rs.id.webzine.domain.Country>() {
			public rs.id.webzine.domain.Country convert(String id) {
				return getObject().convert(
						getObject().convert(id, Integer.class), Country.class);
			}
		};
	}

	public Converter<Customer, String> getCustomerToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<rs.id.webzine.domain.Customer, java.lang.String>() {
			public String convert(Customer customer) {
				return new StringBuilder().append(customer.getName())
						.append(' ').append(customer.getDescription())
						.toString();
			}
		};
	}

	public Converter<Integer, Customer> getIdToCustomerConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Integer, rs.id.webzine.domain.Customer>() {
			public rs.id.webzine.domain.Customer convert(java.lang.Integer id) {
				return Customer.findCustomer(id);
			}
		};
	}

	public Converter<String, Customer> getStringToCustomerConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, rs.id.webzine.domain.Customer>() {
			public rs.id.webzine.domain.Customer convert(String id) {
				return getObject().convert(
						getObject().convert(id, Integer.class), Customer.class);
			}
		};
	}

	public Converter<ManagedContent, String> getManagedContentToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<rs.id.webzine.domain.ManagedContent, java.lang.String>() {
			public String convert(ManagedContent managedContent) {
				return new StringBuilder().append(managedContent.getCss())
						.toString();
			}
		};
	}

	public Converter<Integer, ManagedContent> getIdToManagedContentConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Integer, rs.id.webzine.domain.ManagedContent>() {
			public rs.id.webzine.domain.ManagedContent convert(
					java.lang.Integer id) {
				return ManagedContent.findManagedContent(id);
			}
		};
	}

	public Converter<String, ManagedContent> getStringToManagedContentConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, rs.id.webzine.domain.ManagedContent>() {
			public rs.id.webzine.domain.ManagedContent convert(String id) {
				return getObject().convert(
						getObject().convert(id, Integer.class),
						ManagedContent.class);
			}
		};
	}

	public Converter<News, String> getNewsToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<rs.id.webzine.domain.News, java.lang.String>() {
			public String convert(News news) {
				return new StringBuilder().append(news.getStatus()).append(' ')
						.append(news.getText()).append(' ')
						.append(news.getLink()).append(' ')
						.append(news.getLinkTarget()).toString();
			}
		};
	}

	public Converter<Integer, News> getIdToNewsConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Integer, rs.id.webzine.domain.News>() {
			public rs.id.webzine.domain.News convert(java.lang.Integer id) {
				return News.findNews(id);
			}
		};
	}

	public Converter<String, News> getStringToNewsConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, rs.id.webzine.domain.News>() {
			public rs.id.webzine.domain.News convert(String id) {
				return getObject().convert(
						getObject().convert(id, Integer.class), News.class);
			}
		};
	}

	public Converter<ReaderType, String> getReaderTypeToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<rs.id.webzine.domain.ReaderType, java.lang.String>() {
			public String convert(ReaderType readerType) {
				return new StringBuilder().append(readerType.getCd())
						.append(' ').append(readerType.getName()).toString();
			}
		};
	}

	public Converter<Integer, ReaderType> getIdToReaderTypeConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Integer, rs.id.webzine.domain.ReaderType>() {
			public rs.id.webzine.domain.ReaderType convert(java.lang.Integer id) {
				return ReaderType.findReaderType(id);
			}
		};
	}

	public Converter<String, ReaderType> getStringToReaderTypeConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, rs.id.webzine.domain.ReaderType>() {
			public rs.id.webzine.domain.ReaderType convert(String id) {
				return getObject().convert(
						getObject().convert(id, Integer.class),
						ReaderType.class);
			}
		};
	}

	public Converter<Roles, String> getRolesToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<rs.id.webzine.domain.Roles, java.lang.String>() {
			public String convert(Roles roles) {
				return new StringBuilder().append(roles.getName()).toString();
			}
		};
	}

	public Converter<Integer, Roles> getIdToRolesConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Integer, rs.id.webzine.domain.Roles>() {
			public rs.id.webzine.domain.Roles convert(java.lang.Integer id) {
				return Roles.find(id);
			}
		};
	}

	public Converter<String, Roles> getStringToRolesConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, rs.id.webzine.domain.Roles>() {
			public rs.id.webzine.domain.Roles convert(String id) {
				return getObject().convert(
						getObject().convert(id, Integer.class), Roles.class);
			}
		};
	}

	public Converter<UserStatus, String> getUserStatusToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<rs.id.webzine.domain.UserStatus, java.lang.String>() {
			public String convert(UserStatus userStatus) {
				return new StringBuilder().append(userStatus.getName())
						.toString();
			}
		};
	}

	public Converter<Integer, UserStatus> getIdToUserStatusConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Integer, rs.id.webzine.domain.UserStatus>() {
			public rs.id.webzine.domain.UserStatus convert(java.lang.Integer id) {
				return UserStatus.find(id);
			}
		};
	}

	public Converter<String, UserStatus> getStringToUserStatusConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, rs.id.webzine.domain.UserStatus>() {
			public rs.id.webzine.domain.UserStatus convert(String id) {
				return getObject().convert(
						getObject().convert(id, Integer.class),
						UserStatus.class);
			}
		};
	}

	public Converter<Task, String> getTaskToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<rs.id.webzine.domain.Task, java.lang.String>() {
			public String convert(Task task) {
				return new StringBuilder().append(task.getStatus()).append(' ')
						.append(task.getPriority()).append(' ')
						.append(task.getTitle()).append(' ')
						.append(task.getDescription()).toString();
			}
		};
	}

	public Converter<Integer, Task> getIdToTaskConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Integer, rs.id.webzine.domain.Task>() {
			public rs.id.webzine.domain.Task convert(java.lang.Integer id) {
				return Task.findTask(id);
			}
		};
	}

	public Converter<String, Task> getStringToTaskConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, rs.id.webzine.domain.Task>() {
			public rs.id.webzine.domain.Task convert(String id) {
				return getObject().convert(
						getObject().convert(id, Integer.class), Task.class);
			}
		};
	}

	public Converter<TaskAttachment, String> getTaskAttachmentToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<rs.id.webzine.domain.TaskAttachment, java.lang.String>() {
			public String convert(TaskAttachment taskAttachment) {
				return new StringBuilder().append(taskAttachment.getName())
						.append(' ').append(taskAttachment.getContent())
						.append(' ').append(taskAttachment.getDc()).append(' ')
						.append(taskAttachment.getDm()).toString();
			}
		};
	}

	public Converter<Integer, TaskAttachment> getIdToTaskAttachmentConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Integer, rs.id.webzine.domain.TaskAttachment>() {
			public rs.id.webzine.domain.TaskAttachment convert(
					java.lang.Integer id) {
				return TaskAttachment.findTaskAttachment(id);
			}
		};
	}

	public Converter<String, TaskAttachment> getStringToTaskAttachmentConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, rs.id.webzine.domain.TaskAttachment>() {
			public rs.id.webzine.domain.TaskAttachment convert(String id) {
				return getObject().convert(
						getObject().convert(id, Integer.class),
						TaskAttachment.class);
			}
		};
	}

	public Converter<TaskComment, String> getTaskCommentToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<rs.id.webzine.domain.TaskComment, java.lang.String>() {
			public String convert(TaskComment taskComment) {
				return new StringBuilder().append(taskComment.getText())
						.append(' ').append(taskComment.getDc()).append(' ')
						.append(taskComment.getDm()).toString();
			}
		};
	}

	public Converter<Integer, TaskComment> getIdToTaskCommentConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Integer, rs.id.webzine.domain.TaskComment>() {
			public rs.id.webzine.domain.TaskComment convert(java.lang.Integer id) {
				return TaskComment.findTaskComment(id);
			}
		};
	}

	public Converter<String, TaskComment> getStringToTaskCommentConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, rs.id.webzine.domain.TaskComment>() {
			public rs.id.webzine.domain.TaskComment convert(String id) {
				return getObject().convert(
						getObject().convert(id, Integer.class),
						TaskComment.class);
			}
		};
	}

	public Converter<UserArticle, String> getUserArticleToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<rs.id.webzine.domain.UserArticle, java.lang.String>() {
			public String convert(UserArticle userArticle) {
				return new StringBuilder().append(userArticle.getStatus())
						.append(' ').append(userArticle.getTitle()).append(' ')
						.append(userArticle.getText()).append(' ')
						.append(userArticle.getImage()).toString();
			}
		};
	}

	public Converter<Integer, UserArticle> getIdToUserArticleConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Integer, rs.id.webzine.domain.UserArticle>() {
			public rs.id.webzine.domain.UserArticle convert(java.lang.Integer id) {
				return UserArticle.findUserArticle(id);
			}
		};
	}

	public Converter<String, UserArticle> getStringToUserArticleConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, rs.id.webzine.domain.UserArticle>() {
			public rs.id.webzine.domain.UserArticle convert(String id) {
				return getObject().convert(
						getObject().convert(id, Integer.class),
						UserArticle.class);
			}
		};
	}

	public Converter<UserProfile, String> getUserProfileToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<rs.id.webzine.domain.UserProfile, java.lang.String>() {
			public String convert(UserProfile userProfile) {
				return new StringBuilder().append(userProfile.getFirstName())
						.append(' ').append(userProfile.getLastName())
						.append(' ').append(userProfile.getBirthday())
						.append(' ').append(userProfile.getImage()).toString();
			}
		};
	}

	public Converter<Integer, UserProfile> getIdToUserProfileConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Integer, rs.id.webzine.domain.UserProfile>() {
			public rs.id.webzine.domain.UserProfile convert(java.lang.Integer id) {
				return UserProfile.findUserProfile(id);
			}
		};
	}

	public Converter<String, UserProfile> getStringToUserProfileConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, rs.id.webzine.domain.UserProfile>() {
			public rs.id.webzine.domain.UserProfile convert(String id) {
				return getObject().convert(
						getObject().convert(id, Integer.class),
						UserProfile.class);
			}
		};
	}

	public Converter<Users, String> getUsersToStringConverter() {
		return new org.springframework.core.convert.converter.Converter<rs.id.webzine.domain.Users, java.lang.String>() {
			public String convert(Users users) {
				return new StringBuilder().append(users.getUserName())
						.toString();
			}
		};
	}

	public Converter<Integer, Users> getIdToUsersConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.Integer, rs.id.webzine.domain.Users>() {
			public rs.id.webzine.domain.Users convert(java.lang.Integer id) {
				return Users.find(id);
			}
		};
	}

	public Converter<String, Users> getStringToUsersConverter() {
		return new org.springframework.core.convert.converter.Converter<java.lang.String, rs.id.webzine.domain.Users>() {
			public rs.id.webzine.domain.Users convert(String id) {
				return getObject().convert(
						getObject().convert(id, Integer.class), Users.class);
			}
		};
	}

	public void installLabelConverters(FormatterRegistry registry) {
		registry.addConverter(getAdToStringConverter());
		registry.addConverter(getIdToAdConverter());
		registry.addConverter(getStringToAdConverter());
		registry.addConverter(getAddressToStringConverter());
		registry.addConverter(getIdToAddressConverter());
		registry.addConverter(getStringToAddressConverter());
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
		registry.addConverter(getCategoryToStringConverter());
		registry.addConverter(getIdToCategoryConverter());
		registry.addConverter(getStringToCategoryConverter());
		registry.addConverter(getContentToStringConverter());
		registry.addConverter(getIdToContentConverter());
		registry.addConverter(getStringToContentConverter());
		registry.addConverter(getContentTypeToStringConverter());
		registry.addConverter(getIdToContentTypeConverter());
		registry.addConverter(getStringToContentTypeConverter());
		registry.addConverter(getCountryToStringConverter());
		registry.addConverter(getIdToCountryConverter());
		registry.addConverter(getStringToCountryConverter());
		registry.addConverter(getCustomerToStringConverter());
		registry.addConverter(getIdToCustomerConverter());
		registry.addConverter(getStringToCustomerConverter());
		registry.addConverter(getManagedContentToStringConverter());
		registry.addConverter(getIdToManagedContentConverter());
		registry.addConverter(getStringToManagedContentConverter());
		registry.addConverter(getNewsToStringConverter());
		registry.addConverter(getIdToNewsConverter());
		registry.addConverter(getStringToNewsConverter());
		registry.addConverter(getReaderTypeToStringConverter());
		registry.addConverter(getIdToReaderTypeConverter());
		registry.addConverter(getStringToReaderTypeConverter());
		registry.addConverter(getRolesToStringConverter());
		registry.addConverter(getUserStatusToStringConverter());
		registry.addConverter(getIdToRolesConverter());
		registry.addConverter(getIdToUserStatusConverter());
		registry.addConverter(getStringToRolesConverter());
		registry.addConverter(getStringToUserStatusConverter());
		registry.addConverter(getTaskToStringConverter());
		registry.addConverter(getIdToTaskConverter());
		registry.addConverter(getStringToTaskConverter());
		registry.addConverter(getTaskAttachmentToStringConverter());
		registry.addConverter(getIdToTaskAttachmentConverter());
		registry.addConverter(getStringToTaskAttachmentConverter());
		registry.addConverter(getTaskCommentToStringConverter());
		registry.addConverter(getIdToTaskCommentConverter());
		registry.addConverter(getStringToTaskCommentConverter());
		registry.addConverter(getUserArticleToStringConverter());
		registry.addConverter(getIdToUserArticleConverter());
		registry.addConverter(getStringToUserArticleConverter());
		registry.addConverter(getUserProfileToStringConverter());
		registry.addConverter(getIdToUserProfileConverter());
		registry.addConverter(getStringToUserProfileConverter());
		registry.addConverter(getUsersToStringConverter());
		registry.addConverter(getIdToUsersConverter());
		registry.addConverter(getStringToUsersConverter());
	}

	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		installLabelConverters(getObject());
	}
}
