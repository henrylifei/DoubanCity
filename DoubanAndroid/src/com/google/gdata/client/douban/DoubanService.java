package com.google.gdata.client.douban;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthMessage;
import net.oauth.OAuthServiceProvider;
import net.oauth.client.HttpClientPool;
import net.oauth.client.OAuthClient;
import net.oauth.client.OAuthHttpClient;

import com.google.gdata.client.Query;
import com.google.gdata.client.Service;
import com.google.gdata.client.Query.CustomParameter;
import com.google.gdata.client.http.GoogleGDataRequest;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.TextConstruct;
import com.google.gdata.data.douban.Attribute;
import com.google.gdata.data.douban.CollectionEntry;
import com.google.gdata.data.douban.CollectionFeed;
import com.google.gdata.data.douban.MiniblogEntry;
import com.google.gdata.data.douban.MiniblogFeed;
import com.google.gdata.data.douban.Namespaces;
import com.google.gdata.data.douban.NoteEntry;
import com.google.gdata.data.douban.NoteFeed;
import com.google.gdata.data.douban.ReviewEntry;
import com.google.gdata.data.douban.ReviewFeed;
import com.google.gdata.data.douban.Status;
import com.google.gdata.data.douban.Subject;
import com.google.gdata.data.douban.SubjectEntry;
import com.google.gdata.data.douban.SubjectFeed;
import com.google.gdata.data.douban.Tag;
import com.google.gdata.data.douban.TagEntry;
import com.google.gdata.data.douban.TagFeed;
import com.google.gdata.data.douban.UserEntry;
import com.google.gdata.data.douban.UserFeed;
import com.google.gdata.data.extensions.Rating;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.ServiceException;

public class DoubanService extends Service {

	protected String apiKey;
	protected String apiParam;
	protected String secret;
	protected boolean private_read;

	public static OAuthClient CLIENT = new OAuthHttpClient(
			new HttpClientPool() {
				public HttpClient getHttpClient(URL paramURL) {
					return new HttpClient();
				}
			});
	protected OAuthAccessor accessor;
	protected OAuthAccessor requestAccessor;
	protected List<Map.Entry<String, String>> parameters;
	protected OAuthConsumer client;

	static String requestTokenURL = "http://www.douban.com/service/auth/request_token";
	static String userAuthorizationURL = "http://www.douban.com/service/auth/authorize";
	static String accessTokenURL = "http://www.douban.com/service/auth/access_token";

	/**
	 * 
	 * 
	 * @param applicationName
	 * 
	 * @param apiKey
	 *            doubanapi key
	 */
	public DoubanService(String applicationName, String apiKey) {
		ExtensionProfile profile = getExtensionProfile();
		this.apiKey = apiKey;
		this.apiParam = "apikey=" + apiKey;

		profile.addDeclarations(new UserEntry());
		profile.addDeclarations(new SubjectEntry());
		profile.addDeclarations(new ReviewEntry());
		profile.addDeclarations(new CollectionEntry());
		profile.addDeclarations(new TagEntry());
		profile.addDeclarations(new NoteEntry());
		profile.addDeclarations(new MiniblogEntry());

		requestFactory = new GoogleGDataRequest.Factory();
		this.accessor = null;

		if (applicationName != null) {
			requestFactory.setHeader("User-Agent", applicationName + " "
					+ getServiceVersion());
		} else {
			requestFactory.setHeader("User-Agent", getServiceVersion());
		}
		this.private_read = false;
	}

	/**
	 * oauth
	 * 
	 * @param applicationName
	 * 
	 * @param apiKey
	 *            doubanapi key
	 * @param secret
	 *            doubanapi
	 */
	public DoubanService(String applicationName, String apiKey, String secret) {
		this(applicationName, apiKey);

		this.apiKey = apiKey;
		this.secret = secret;
		OAuthServiceProvider provider = new OAuthServiceProvider(
				requestTokenURL, userAuthorizationURL, accessTokenURL);

		this.client = new OAuthConsumer(null, apiKey, secret, provider);
		this.accessor = new OAuthAccessor(this.client);

		this.requestAccessor = new OAuthAccessor(this.client);

		this.client.setProperty("oauth_signature_method", OAuth.HMAC_SHA1);
		this.private_read = false;
	}

	/**
	 * 
	 * 
	 * @param applicationName
	 * 
	 * @param apiKey
	 *            doubanapi key
	 * @param secret
	 *            doubanapi
	 * @param private_read
	 * 
	 */
	public DoubanService(String applicationName, String apiKey, String secret,
			boolean private_read) {
		this(applicationName, apiKey);

		this.apiKey = apiKey;
		this.secret = secret;
		OAuthServiceProvider provider = new OAuthServiceProvider(
				requestTokenURL, userAuthorizationURL, accessTokenURL);

		this.client = new OAuthConsumer(null, apiKey, secret, provider);
		this.accessor = new OAuthAccessor(this.client);

		this.requestAccessor = new OAuthAccessor(this.client);

		this.client.setProperty("oauth_signature_method", OAuth.HMAC_SHA1);
		this.private_read = private_read;
	}

	/**
	 * URL URLcallbackURL
	 * 
	 * @param callback
	 *            URL
	 */
	public String getAuthorizationUrl(String callback) {
		String authorization_url = null;
		try {
			CLIENT.getRequestToken(this.requestAccessor);

			authorization_url = accessor.consumer.serviceProvider.userAuthorizationURL
					+ "?" + "oauth_token=" + this.requestAccessor.requestToken;
			if (callback != null)
				authorization_url += "&oauth_callback=" + callback;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return authorization_url;
	}

	public String getRequestToken() {
		return this.requestAccessor.requestToken;
	}

	public void setRequestToken(String token) {
		this.requestAccessor.requestToken = token;
	}

	public String getRequestTokenSecret() {
		return this.requestAccessor.tokenSecret;
	}

	public void setRequestTokenSecret(String tokenSecret) {
		this.requestAccessor.tokenSecret = tokenSecret;
	}

	/**
	 * token
	 * 
	 * 
	 * @param oauth_token
	 *            token
	 * @param oauth_token_secret
	 *            token
	 */
	public ArrayList<String> setAccessToken(String oauth_token,
			String oauth_token_secret) {
		this.accessor.accessToken = oauth_token;
		this.accessor.tokenSecret = oauth_token_secret;
		ArrayList<String> tokens = new ArrayList<String>(2);
		tokens.add(this.accessor.accessToken);
		tokens.add(this.accessor.tokenSecret);
		return tokens;
	}

	/**
	 * token
	 * 
	 */
	public ArrayList<String> getAccessToken() {
		OAuthMessage result;

		try {
			result = CLIENT.invoke(this.requestAccessor,
					accessor.consumer.serviceProvider.accessTokenURL, OAuth
							.newList("oauth_token",
									this.requestAccessor.requestToken));

			Map<String, String> responseParameters = OAuth.newMap(result
					.getParameters());

			this.accessor.accessToken = responseParameters.get("oauth_token");
			this.accessor.tokenSecret = responseParameters
					.get("oauth_token_secret");

			ArrayList<String> tokens = new ArrayList<String>(2);
			tokens.add(this.accessor.accessToken);
			tokens.add(this.accessor.tokenSecret);
			return tokens;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static final String toString(Object from) {
		return (from == null) ? null : from.toString();
	}

	public GDataRequest createFeedRequest(Query query) throws IOException,
			ServiceException {
		GDataRequest request = null;
		OAuthMessage oauthRequest = null;
		setTimeouts(request);

		if (this.accessor == null) {
			List<CustomParameter> customParams = query.getCustomParameters();
			customParams.add(new CustomParameter("apikey", this.apiKey));
			request = super.requestFactory.getRequest(query,
					super.getContentType());
			return request;
		}

		request = super.requestFactory
				.getRequest(query, super.getContentType());
		try {
			Collection<Map.Entry<String, String>> p = new ArrayList<Map.Entry<String, String>>();

			p.add(new OAuth.Parameter("oauth_version", "1.0"));
			String methodType = "GET";
			GDataRequest.RequestType type = GDataRequest.RequestType.QUERY;

			switch (type) {

			case INSERT:
				methodType = "POST";
				break;
			case UPDATE:
				methodType = "PUT";
				break;

			case DELETE:
				methodType = "DELETE";
				break;

			}
			if (this.private_read == true) {
				oauthRequest = this.requestAccessor.newRequestMessage(
						methodType, query.getUrl().toString(), p);
			} else {
				oauthRequest = this.accessor.newRequestMessage(methodType,
						query.getUrl().toString(), p);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		parameters = oauthRequest.getParameters();

		String url = "OAuth realm=\"\"";

		for (Map.Entry<String, String> parameter : parameters) {
			url += ", ";
			url += OAuth.percentEncode(toString(parameter.getKey()));
			url += "=\"";
			url += OAuth.percentEncode(toString(parameter.getValue()));
			url += "\"";
		}

		request.setHeader("Authorization", url);
		return request;
	}

	@Override
	public GDataRequest createRequest(GDataRequest.RequestType type,
			URL requestUrl, ContentType contentType) throws IOException,
			ServiceException {
		GDataRequest request = null;
		OAuthMessage oauthRequest = null;

		if (this.accessor == null) {
			String url = requestUrl.toString();
			if (url.indexOf('?') == -1) {
				url = url + "?" + this.apiParam;
			} else {
				url = url + "&" + this.apiParam;
			}
			request = super.createRequest(type, new URL(url), contentType);
			return request;
		}

		request = super.createRequest(type, requestUrl, contentType);
		try {
			Collection<Map.Entry<String, String>> p = new ArrayList<Map.Entry<String, String>>();

			p.add(new OAuth.Parameter("oauth_version", "1.0"));
			String methodType = "GET";

			switch (type) {

			case INSERT:
				methodType = "POST";
				break;
			case UPDATE:
				methodType = "PUT";
				break;

			case DELETE:
				methodType = "DELETE";
				break;
			}

			if (this.private_read == true) {
				oauthRequest = this.requestAccessor.newRequestMessage(
						methodType, requestUrl.toString(), p);
			} else {
				oauthRequest = this.accessor.newRequestMessage(methodType,
						requestUrl.toString(), p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		parameters = oauthRequest.getParameters();

		String url = "OAuth realm=\"\"";

		for (Map.Entry<String, String> parameter : parameters) {
			url += ", ";
			url += OAuth.percentEncode(toString(parameter.getKey()));
			url += "=\"";
			url += OAuth.percentEncode(toString(parameter.getValue()));
			url += "\"";
		}

		request.setHeader("Authorization", url);
		return request;
	}

	public <E extends BaseEntry<?>> E getEntry(String entryUrl,
			Class<E> entryClass) throws IOException, ServiceException {
		return super.getEntry(new URL(entryUrl), entryClass);
	}

	/**
	 * 
	 * 
	 * @param userId
	 *            id
	 */
	public UserEntry getUser(String userId) throws IOException,
			ServiceException {
		String url = Namespaces.userURL + "/" + userId;
		return getEntry(url, UserEntry.class);
	}

	public UserEntry getAuthorizedUser() throws IOException, ServiceException {
		String url = Namespaces.userURL + "/%40me";
		return getEntry(url, UserEntry.class);
	}

	/**
	 * 
	 * 
	 * @param q
	 * 
	 * @param startIndex
	 * 
	 * @param maxResult
	 * 
	 */
	public UserFeed findUser(String q, int startIndex, int maxResult)
			throws IOException, ServiceException {
		DoubanQuery query = new DoubanQuery(new URL(Namespaces.userURL));
		query.setFullTextQuery(q);
		query.setStartIndex(startIndex);
		query.setMaxResults(maxResult);

		return query(query, UserFeed.class);
	}

	/**
	 * 
	 * 
	 * @param userId
	 *            id
	 * @param startIndex
	 * 
	 * @param maxResult
	 * 
	 */
	public UserFeed getUserFriends(String userId, int startIndex, int maxResult)
			throws IOException, ServiceException {
		String url = Namespaces.userURL + "/" + userId + "/friends";
		DoubanQuery query = new DoubanQuery(new URL(url));
		query.setStartIndex(startIndex);
		query.setMaxResults(maxResult);
		return getFeed(query, UserFeed.class);
	}

	/**
	 * 
	 * 
	 * @param bookId
	 *            id
	 */
	public SubjectEntry getBook(String bookId) throws IOException,
			ServiceException {
		String url = bookId;
		if (url.lastIndexOf("http") != 0) {
			url = Namespaces.bookSubjectURL + "/" + bookId;
		}
		System.out.println(url);
		System.out
				.println("************************************************************");
		return getEntry(url, SubjectEntry.class);
	}

	public SubjectFeed findInterestActivity(String ActicityId, String type,
			int startIndex, int maxResult) throws IOException, ServiceException {
		SubjectQuery query = new SubjectQuery(new URL(Namespaces.userURL + "/"
				+ ActicityId + "/events/" + type));
		query.setStartIndex(startIndex);
		query.setMaxResults(maxResult);
		return query(query, SubjectFeed.class);

	}

	public SubjectEntry getActivity(String ActicityId) throws IOException,
			ServiceException {
		String url = ActicityId;
		if (url.lastIndexOf("http") != 0) {
			url = Namespaces.activitySubjectURL + "/" + ActicityId;
		}
		System.out.println(url);
		System.out
				.println("************************************************************");
		return getEntry(url, SubjectEntry.class);
	}

	/**
	 * 
	 * 
	 * @param bookId
	 *            id
	 */
	public SubjectEntry getBook(int bookId) throws IOException,
			ServiceException {
		String url = Namespaces.bookSubjectURL + "/" + bookId;
		return getEntry(url, SubjectEntry.class);
	}

	/**
	 * 
	 * 
	 * @param musicId
	 *            id
	 */
	public SubjectEntry getMusic(String musicId) throws IOException,
			ServiceException {
		String url = musicId;
		if (url.lastIndexOf("http") != 0) {
			url = Namespaces.musicSubjectURL + "/" + musicId;
		}
		return getEntry(url, SubjectEntry.class);
	}

	/**
	 * 
	 * 
	 * @param musicId
	 *            id
	 */
	public SubjectEntry getMusic(int musicId) throws IOException,
			ServiceException {
		String url = Namespaces.musicSubjectURL + "/" + musicId;
		return getEntry(url, SubjectEntry.class);
	}

	/**
	 * 
	 * 
	 * @param movieId
	 *            id
	 */
	public SubjectEntry getMovie(String movieId) throws IOException,
			ServiceException {
		String url = movieId;
		if (url.lastIndexOf("http") != 0) {
			url = Namespaces.movieSubjectURL + "/" + movieId;
		}
		return getEntry(url, SubjectEntry.class);
	}

	/**
	 * 
	 * 
	 * @param movieId
	 *            id
	 */
	public SubjectEntry getMovie(int movieId) throws IOException,
			ServiceException {
		String url = Namespaces.movieSubjectURL + "/" + movieId;
		return getEntry(url, SubjectEntry.class);
	}

	/**
	 * 
	 * 
	 * @param noteId
	 *            id
	 */
	public NoteEntry getNote(String noteId) throws IOException,
			ServiceException {
		String url = Namespaces.noteURL + "/" + noteId;
		return getEntry(url, NoteEntry.class);
	}

	/**
	 * 
	 * 
	 * @param userId
	 *            id
	 * @param startIndex
	 * 
	 * @param maxResult
	 * 
	 */
	public NoteFeed getUserNotes(String userId, int startIndex, int maxResult)
			throws IOException, ServiceException {
		String url = Namespaces.userURL + "/" + userId + "/notes";
		DoubanQuery query = new DoubanQuery(new URL(url));
		query.setStartIndex(startIndex);
		query.setMaxResults(maxResult);

		return query(query, NoteFeed.class);
	}

	/**
	 * 
	 * 
	 * @param title
	 * 
	 * @param content
	 * 
	 * @param privacy
	 *            public|private|friend
	 * @param can_reply
	 *            yes|no
	 */
	public NoteEntry createNote(TextConstruct title, TextConstruct content,
			String privacy, String can_reply) throws IOException,
			ServiceException {
		String url = Namespaces.noteCreateURL;
		NoteEntry ne = new NoteEntry();

		if (title != null) {
			ne.setTitle(title);
		}
		if (content != null) {
			ne.setContent(content);
		}
		ArrayList<Attribute> atts = new ArrayList<Attribute>(2);
		Attribute a1 = new Attribute();
		a1.setName("privacy");
		a1.setContent(privacy);
		Attribute a2 = new Attribute();
		a2.setName("can_reply");
		a2.setContent(can_reply);
		atts.add(a1);
		atts.add(a2);
		ne.setAttributes(atts);

		return insert(new URL(url), ne);
	}

	public NoteEntry participateActivity(String ActivityId) throws IOException,
			ServiceException {
		String url = Namespaces.participateActivityURL + ActivityId
				+ "/participants";
		NoteEntry ne = new NoteEntry();
		return insert(new URL(url),ne);
	}

	public void unparticipateActivity(String ActivityId) throws IOException,
			ServiceException {
		String url = Namespaces.participateActivityURL + ActivityId
				+ "/participants";
		delete(new URL(url));
	}

	public NoteEntry wishActivity(String ActivityId) throws IOException,
			ServiceException {
		String url = Namespaces.participateActivityURL + ActivityId
				+ "/wishers";
		NoteEntry ne = new NoteEntry();
		return insert(new URL(url),ne);
	}

	public void unwishActivity(String ActivityId) throws IOException,
			ServiceException {
		String url = Namespaces.participateActivityURL + ActivityId
				+ "/wishers";
		delete(new URL(url));
	}

	/**
	 * 
	 * 
	 * @param ne
	 * 
	 * @param title
	 * 
	 * @param content
	 * 
	 * @param privacy
	 *            public|private|friend
	 * @param can_reply
	 *            yes|no
	 */
	public NoteEntry updateNote(NoteEntry ne, TextConstruct title,
			TextConstruct content, String privacy, String can_reply)
			throws MalformedURLException, IOException, ServiceException {

		if (title != null) {
			ne.setTitle(title);
		}
		if (content != null) {
			ne.setContent(content);
		}
		ArrayList<Attribute> atts = new ArrayList<Attribute>(2);
		Attribute a1 = new Attribute();
		a1.setName("privacy");
		a1.setContent(privacy);
		Attribute a2 = new Attribute();
		a2.setName("can_reply");
		a2.setContent(can_reply);
		atts.add(a1);
		atts.add(a2);
		ne.setAttributes(atts);

		return update(new URL(ne.getId()), ne);
	}

	/**
	 * 
	 * 
	 * @param ne
	 * 
	 */
	public void deleteNote(NoteEntry ne) throws MalformedURLException,
			IOException, ServiceException {
		delete(new URL(ne.getId()));
	}

	/**
	 * 
	 * 
	 * @param userId
	 *            id
	 * @param startIndex
	 * 
	 * @param maxResult
	 * 
	 */
	public MiniblogFeed getUserMiniblogs(String userId, int startIndex,
			int maxResult) throws IOException, ServiceException {
		String url = Namespaces.userURL + "/" + userId + "/miniblog";
		DoubanQuery query = new DoubanQuery(new URL(url));
		query.setStartIndex(startIndex);
		query.setMaxResults(maxResult);

		return query(query, MiniblogFeed.class);
	}

	/**
	 * 
	 * 
	 * @param userId
	 *            id
	 * @param startIndex
	 * 
	 * @param maxResult
	 * 
	 */
	public MiniblogFeed getContactsMiniblogs(String userId, int startIndex,
			int maxResult) throws IOException, ServiceException {
		String url = Namespaces.userURL + "/" + userId + "/miniblog/contacts";
		DoubanQuery query = new DoubanQuery(new URL(url));
		query.setStartIndex(startIndex);
		query.setMaxResults(maxResult);

		return query(query, MiniblogFeed.class);
	}

	/**
	 * 
	 * 
	 * @param content
	 * 
	 */
	public MiniblogEntry createSaying(TextConstruct content)
			throws IOException, ServiceException {
		String url = Namespaces.sayingCreateURL;
		MiniblogEntry me = new MiniblogEntry();

		if (content != null) {
			me.setContent(content);
		}

		return insert(new URL(url), me);
	}

	/**
	 * 
	 * 
	 * @param me
	 * 
	 */
	public void deleteMiniblog(MiniblogEntry me) throws MalformedURLException,
			IOException, ServiceException {
		delete(new URL(me.getId()));
	}

	/**
	 * 
	 * 
	 * @param q
	 * 
	 * @param tag
	 *            tag
	 * @param startIndex
	 * 
	 * @param maxResult
	 * 
	 */
	public SubjectFeed findBook(String q, String tag, int startIndex,
			int maxResult) throws IOException, ServiceException {
		SubjectQuery query = new SubjectQuery(new URL(
				Namespaces.bookSubjectsURL));
		query.setFullTextQuery(q);
		query.setStartIndex(startIndex);
		query.setMaxResults(maxResult);
		query.setTag(tag);

		return query(query, SubjectFeed.class);
	}

	public SubjectFeed findActivity(String city, String type, int startIndex,
			int maxResult) throws IOException, ServiceException {
		SubjectQuery query = new SubjectQuery(new URL(
				Namespaces.activitySubjectsURL + city));
		query.setStartIndex(startIndex);
		query.setMaxResults(maxResult);
		query.setType(type);

		return query(query, SubjectFeed.class);
	}

	/**
	 * 
	 * 
	 * @param q
	 * 
	 * @param tag
	 *            tag
	 * @param startIndex
	 * 
	 * @param maxResult
	 * 
	 */
	public SubjectFeed findMovie(String q, String tag, int startIndex,
			int maxResult) throws IOException, ServiceException {
		SubjectQuery query = new SubjectQuery(new URL(
				Namespaces.movieSubjectsURL));
		query.setFullTextQuery(q);
		query.setStartIndex(startIndex);
		query.setMaxResults(maxResult);
		query.setTag(tag);

		return query(query, SubjectFeed.class);
	}

	/**
	 * 
	 * 
	 * @param q
	 * 
	 * @param tag
	 *            tag
	 * @param startIndex
	 * 
	 * @param maxResult
	 * 
	 */
	public SubjectFeed findMusic(String q, String tag, int startIndex,
			int maxResult) throws IOException, ServiceException {
		SubjectQuery query = new SubjectQuery(new URL(
				Namespaces.musicSubjectsURL));
		query.setFullTextQuery(q);
		query.setStartIndex(startIndex);
		query.setMaxResults(maxResult);
		query.setTag(tag);

		return query(query, SubjectFeed.class);
	}

	/**
	 * @deprecated apidouban
	 * 
	 * @param bookId
	 *            id
	 * @param startIndex
	 * 
	 * @param maxResult
	 * 
	 */
	public SubjectFeed getBookRelated(String bookId, int startIndex,
			int maxResult) throws IOException, ServiceException {
		SubjectQuery query = new SubjectQuery(new URL(Namespaces.bookSubjectURL
				+ "/" + bookId + "/related"));

		query.setStartIndex(startIndex);
		query.setMaxResults(maxResult);

		return query(query, SubjectFeed.class);
	}

	/**
	 * @deprecated apidouban
	 * 
	 * @param movieId
	 *            id
	 * @param startIndex
	 * 
	 * @param maxResult
	 * 
	 */
	public SubjectFeed getMovieRelated(String movieId, int startIndex,
			int maxResult) throws IOException, ServiceException {
		SubjectQuery query = new SubjectQuery(new URL(
				Namespaces.movieSubjectURL + "/" + movieId + "/related"));
		query.setStartIndex(startIndex);
		query.setMaxResults(maxResult);

		return query(query, SubjectFeed.class);

	}

	/**
	 * @deprecated apidouban
	 * 
	 * @param musicId
	 *            id
	 * @param startIndex
	 * 
	 * @param maxResult
	 * 
	 */
	public SubjectFeed getMusicRelated(String musicId, int startIndex,
			int maxResult) throws IOException, ServiceException {
		SubjectQuery query = new SubjectQuery(new URL(
				Namespaces.musicSubjectURL + "/" + musicId + "/related"));

		query.setStartIndex(startIndex);
		query.setMaxResults(maxResult);

		return query(query, SubjectFeed.class);

	}

	/**
	 * 
	 * 
	 * @param reviewId
	 *            id
	 * 
	 */
	public ReviewEntry getReview(String reviewId) throws IOException,
			ServiceException {
		String url = Namespaces.reviewURL + "/" + reviewId;
		return getEntry(url, ReviewEntry.class);
	}

	/**
	 * 
	 * 
	 * @param userId
	 *            id
	 */
	public ReviewFeed getUserReviews(String userId) throws IOException,
			ServiceException {
		String url = Namespaces.userURL + "/" + userId + "/reviews";
		DoubanQuery query = new DoubanQuery(new URL(url));

		return getFeed(query, ReviewFeed.class);
	}

	/**
	 * 
	 * 
	 * @param bookId
	 *            id
	 * @param startIndex
	 * 
	 * @param maxResult
	 * 
	 */
	public ReviewFeed getBookReviews(String bookId, int startIndex,
			int maxResult, String orderby) throws IOException, ServiceException {
		ReviewQuery query = new ReviewQuery(new URL(Namespaces.bookSubjectURL
				+ "/" + bookId + "/reviews"));
		query.setStartIndex(startIndex);
		query.setMaxResults(maxResult);
		query.setOrderby(orderby);

		return query(query, ReviewFeed.class);
	}

	/**
	 * 
	 * 
	 * @param movieId
	 *            id
	 * @param startIndex
	 * 
	 * @param maxResult
	 * 
	 */
	public ReviewFeed getMovieReviews(String movieId, int startIndex,
			int maxResult, String orderby) throws IOException, ServiceException {
		ReviewQuery query = new ReviewQuery(new URL(Namespaces.movieSubjectURL
				+ "/" + movieId + "/reviews"));
		query.setStartIndex(startIndex);
		query.setMaxResults(maxResult);
		query.setOrderby(orderby);
		return query(query, ReviewFeed.class);
	}

	/**
	 * 
	 * 
	 * @param musicId
	 *            id
	 * @param startIndex
	 * 
	 * @param maxResult
	 * 
	 */
	public ReviewFeed getMusicReviews(String musicId, int startIndex,
			int maxResult, String orderby) throws IOException, ServiceException {
		ReviewQuery query = new ReviewQuery(new URL(Namespaces.musicSubjectURL
				+ "/" + musicId + "/reviews"));
		query.setStartIndex(startIndex);
		query.setMaxResults(maxResult);
		query.setOrderby(orderby);
		return query(query, ReviewFeed.class);
	}

	/**
	 * 
	 * 
	 * @param userId
	 *            id
	 * @param cat
	 * 
	 * @param tag
	 *            tag
	 * @param status
	 * 
	 * @param startIndex
	 * 
	 * @param maxResult
	 * 
	 */
	public CollectionFeed getUserCollections(String userId, String cat,
			String tag, String status, int startIndex, int maxResult)
			throws IOException, ServiceException {

		CollectionQuery query = new CollectionQuery(new URL(Namespaces.userURL
				+ "/" + userId + "/collection"));
		query.setCat(cat);
		query.setTag(tag);
		query.setStartIndex(startIndex);
		query.setMaxResults(maxResult);
		query.setStatus(status);

		return query(query, CollectionFeed.class);
	}

	/**
	 * 
	 * 
	 * @param cid
	 *            id
	 */
	public CollectionEntry getCollection(String cid) throws IOException,
			ServiceException {

		String url = Namespaces.collectionURL + "/" + cid;
		return getEntry(url, CollectionEntry.class);
	}

	/**
	 * 
	 * 
	 * @param bookId
	 *            Id
	 * @param startIndex
	 * 
	 * @param maxResult
	 * 
	 */
	public TagFeed getBookTags(String bookId, int startIndex, int maxResult)
			throws IOException, ServiceException {
		TagQuery query = new TagQuery(new URL(Namespaces.bookSubjectURL + "/"
				+ bookId + "/tags"));

		query.setStartIndex(startIndex);
		query.setMaxResults(maxResult);

		return query(query, TagFeed.class);
	}

	/**
	 * 
	 * 
	 * @param movieId
	 *            Id
	 * @param startIndex
	 * 
	 * @param maxResult
	 * 
	 */
	public TagFeed getMovieTags(String movieId, int startIndex, int maxResult)
			throws IOException, ServiceException {
		TagQuery query = new TagQuery(new URL(Namespaces.movieSubjectURL + "/"
				+ movieId + "/tags"));

		query.setStartIndex(startIndex);
		query.setMaxResults(maxResult);

		return query(query, TagFeed.class);
	}

	/**
	 * 
	 * 
	 * @param musicId
	 *            Id
	 * @param startIndex
	 * 
	 * @param maxResult
	 * 
	 */
	public TagFeed getMusicTags(String musicId, int startIndex, int maxResult)
			throws IOException, ServiceException {
		TagQuery query = new TagQuery(new URL(Namespaces.musicSubjectURL + "/"
				+ musicId + "/tags"));

		query.setStartIndex(startIndex);
		query.setMaxResults(maxResult);

		return query(query, TagFeed.class);
	}

	/**
	 * 
	 * 
	 * @param userId
	 *            Id
	 * @param cat
	 *            movie|music|book
	 * @param startIndex
	 * 
	 * @param maxResult
	 * 
	 */
	public TagFeed getUserTags(String userId, String cat, int startIndex,
			int maxResult) throws IOException, ServiceException {
		TagQuery query = new TagQuery(new URL(Namespaces.userURL + "/" + userId
				+ "/tags"));
		query.setCat(cat);

		query.setStartIndex(startIndex);
		query.setMaxResults(maxResult);
		return query(query, TagFeed.class);
	}

	/**
	 * 
	 * 
	 * @param subjectEntry
	 * 
	 * @param title
	 * 
	 * @param content
	 *            50
	 * @param rating
	 *            (1-5)
	 */
	public ReviewEntry createReview(SubjectEntry subjectEntry,
			TextConstruct title, TextConstruct content, Rating rating)
			throws IOException, ServiceException {
		String url = Namespaces.reviewCreateURL;
		ReviewEntry re = new ReviewEntry();
		Subject subject = new Subject();

		subject.setId(subjectEntry.getId());
		re.setSubject(subject);
		if (title != null) {
			re.setTitle(title);
		}
		if (content != null) {
			re.setContent(content);
		}
		if (rating != null) {
			re.setRating(rating);
		}

		return insert(new URL(url), re);
	}

	/**
	 * 
	 * 
	 * @param reviewEntry
	 * 
	 * @param title
	 * 
	 * @param content
	 *            50
	 * @param rating
	 *            1-5
	 */
	public ReviewEntry updateReview(ReviewEntry reviewEntry,
			TextConstruct title, TextConstruct content, Rating rating)
			throws IOException, ServiceException {
		ReviewEntry re = new ReviewEntry(reviewEntry);
		if (title != null) {
			re.setTitle(title);
		}
		if (content != null) {
			re.setContent(content);
		}
		if (rating != null) {
			re.setRating(rating);
		}

		return update(new URL(reviewEntry.getId()), re);
	}

	/**
	 * 
	 * 
	 * @param reviewEntry
	 * 
	 */
	public void deleteReview(ReviewEntry reviewEntry) throws IOException,
			ServiceException {
		delete(new URL(reviewEntry.getId()));
	}

	/**
	 * 
	 * 
	 * @param status
	 *            book:wish|reading|read movie:wish|watched
	 *            tv:wish|watching|watched music:wish|listening|listened
	 * @param se
	 * 
	 * @param tags
	 * 
	 * @param rating
	 *            (1-5)
	 */
	public CollectionEntry createCollection(Status status, SubjectEntry se,
			List<Tag> tags, Rating rating) throws MalformedURLException,
			IOException, ServiceException {
		String url = Namespaces.collectionCreateURL;
		CollectionEntry ceNew = new CollectionEntry();
		Subject subject = new Subject();
		subject.setId(se.getId());
		ceNew.setSubjectEntry(subject);

		if (status != null) {
			ceNew.setStatus(status);
		}
		if (tags != null) {
			ceNew.setTags(tags);
		}
		if (rating != null) {
			ceNew.setRating(rating);
		}

		return insert(new URL(url), ceNew);
	}

	/**
	 * 
	 * 
	 * @param ce
	 * 
	 * @param status
	 *            book:wish|reading|read movie:wish|watched
	 *            tv:wish|watching|watched music:wish|listening|listened
	 * @param tags
	 * 
	 * @param rating
	 *            (1-5)
	 */
	public CollectionEntry updateCollection(CollectionEntry ce, Status status,
			List<Tag> tags, Rating rating) throws MalformedURLException,
			IOException, ServiceException {

		CollectionEntry ceNew = new CollectionEntry();
		ceNew.setId(ce.getId());

		Subject subject = new Subject();
		subject.setId(ce.getSubjectEntry().getId());
		ceNew.setSubjectEntry(subject);

		if (status != null) {
			ceNew.setStatus(status);
		} else {
			ceNew.setStatus(ce.getStatus());
		}
		if (tags != null) {
			ceNew.setTags(tags);
		} else {
			ceNew.setTags(ce.getTags());
		}
		if (rating != null) {
			ceNew.setRating(rating);
		} else {
			ceNew.setRating(ce.getRating());
		}
		return update(new URL(ce.getId()), ceNew);
	}

	/**
	 * 
	 * 
	 * @param ce
	 * 
	 */
	public void deleteCollection(CollectionEntry ce)
			throws MalformedURLException, IOException, ServiceException {
		delete(new URL(ce.getId()));
	}

}
