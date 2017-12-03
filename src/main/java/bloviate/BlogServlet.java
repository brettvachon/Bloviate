/*
 * Copyright (c) 2017 daloonik
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package bloviate;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import com.google.api.services.blogger.Blogger;
import com.google.api.services.blogger.Blogger.Posts.Insert;
import com.google.api.services.blogger.BloggerScopes;
import com.google.api.services.blogger.model.Post;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;

import net.jeremybrooks.knicker.KnickerException;
import net.jeremybrooks.knicker.WordApi;
import net.jeremybrooks.knicker.WordsApi;
import net.jeremybrooks.knicker.dto.Example;
import net.jeremybrooks.knicker.dto.Word;

/**
 * Servlet getting an Access and Refresh Token from the Callback Handler Servlet (if needed)
 * and using those credentials to post to Blogger
 */  

@SuppressWarnings("serial")
public class BlogServlet extends HttpServlet 
   {
   private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
   private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();   
   public static GoogleAuthorizationCodeFlow flow;
   public static String MY_FILE = "WEB-INF/StaticFiles/twits";
   
   public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
      {     

      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();   
      Entity OAuthTokenEntity;
      OAuthProperties oauthProperties = new OAuthProperties();
      String OAuthAccessToken, OAuthRefreshToken;
      
      // Checking if we already have tokens 
      try
         { 
         OAuthTokenEntity = datastore.get(KeyFactory.createKey("OAuthTokenEntity","OA"));
         OAuthAccessToken = OAuthTokenEntity.getProperty("OAuthAccessToken").toString();
         OAuthRefreshToken = OAuthTokenEntity.getProperty("OAuthRefreshToken").toString();
         }
    
      // If we don't have tokens 
      catch(EntityNotFoundException e)
         {
         Collection<String> scopes = Arrays.asList(BloggerScopes.BLOGGER);;
         flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
               oauthProperties.getClientId(), oauthProperties.getClientSecret(), scopes)
               .setAccessType("offline")
               .setApprovalPrompt("auto").build();
         String url = flow.newAuthorizationUrl()
                .setRedirectUri(OAuthCodeCallbackHandlerServlet.getOAuthCodeCallbackHandlerUrl(request))
                .build();
         response.sendRedirect(url);
         return;
         }
                 
      response.setContentType("text/plain");

      response.getWriter().append("Posting to blog... \n\n");
      printToBlog(OAuthAccessToken, OAuthRefreshToken, response.getWriter());
      }
         
   public static String getFullRequestUrl(HttpServletRequest req) 
      {
      String scheme = req.getScheme() + "://";
      String serverName = req.getServerName();
      String serverPort = (req.getServerPort() == 80) ? "" : ":" + req.getServerPort();
      String contextPath = req.getContextPath();
      String servletPath = req.getServletPath();
      String pathInfo = (req.getPathInfo() == null) ? "" : req.getPathInfo();
      String queryString = (req.getQueryString() == null) ? "" : "?" + req.getQueryString();
      return scheme + serverName + serverPort + contextPath + servletPath + pathInfo + queryString;
      }
   
   /**
    * Builds a new Google Credential with the Access and Refresh Tokens and
    * posts to Blogger with some content grabbed by Wordnik and an optional
    * file
    *
    * @param OAuthAccessToken the Access Token
    * @param OAuthRefreshToken the Refresh Token
    * @throws IOException IF there is an issue reading a file
    */ 
   
   public static void printToBlog(String OAuthAccessToken, String OAuthRefreshToken, Writer output) throws IOException
      {
      //Get our Google Credential
      OAuthProperties oauthProperties = new OAuthProperties();
      GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(HTTP_TRANSPORT)
                .setJsonFactory(JSON_FACTORY)
                .setClientSecrets(oauthProperties.getClientId(), oauthProperties.getClientSecret())
                .build();
      
      //Set our Access and Refresh Tokens
      credential.setAccessToken(OAuthAccessToken);
      credential.setRefreshToken(OAuthRefreshToken);
      
      //Get our Blog APP Name and ID
      Properties p = new Properties();
      InputStream in = BlogServlet.class.getResourceAsStream("blogger.properties");
      p.load(in);
      String APP_NAME = p.getProperty("APP_NAME");
      String BLOG_ID = p.getProperty("BLOG_ID");
      
      // Initialize the Blogger service and log in
      Blogger blog = new Blogger.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
         .setApplicationName(APP_NAME).setHttpRequestInitializer(credential)
         .build();

      Post content = new Post();
      content.setTitle("Posted by Bloviate");
      
      //Make up some content
      StringBuilder builder = new StringBuilder();
      
      /** Append a line from a file (optional) */
      builder.append(new GetLine(MY_FILE).line()).append(" ");
      
      //Get the content from Wordnik
      builder.append(getWordnikContent(output));
      
      /** Restrict the length of the content (optional) */
      while(builder.length() > 280)
         {
         builder.setLength(0);
         /** Optional: append another line
          builder.append(new GetLine(MY_FILE).line()).append(" ");
         */
         builder.append(getWordnikContent(output));
         }
           
      //Set the content
      content.setContent(builder.toString());
      
      // The request action.
      Insert postsInsertAction = blog.posts().insert(BLOG_ID, content);

      // Restrict the result content to just the data we need.
      postsInsertAction.setFields("content,published,title");

      // This step sends the request to the server.
      Post post = postsInsertAction.execute();
           
      output.append("Posted to blog!");
      output.append("\n");
      output.append("Published: " + post.getPublished());
      output.append("\n");
      output.append("Content: " + post.getContent());
      output.append("\n");
      }
   
   public static String getWordnikContent(Writer output) throws IOException 
      {
      Properties p = new Properties();
      InputStream in = BlogServlet.class.getResourceAsStream("wordnik.properties");
      p.load(in);
      System.setProperty("WORDNIK_API_KEY", p.getProperty("WORDNIK_API_KEY"));
      
      try
         {
         StringBuilder builder = new StringBuilder();
         Word random = WordsApi.randomWord();
         Example ex = WordApi.topExample(random.getWord());   
         builder.append(ex.getText());
         
         /* Append some hashtags to the post (optional)  */      
         builder.append(" #" + WordsApi.randomWord().getWord());
         builder.append(" #" + WordsApi.randomWord().getWord());
         return builder.toString();
         }
      catch(KnickerException e)
         {
         output.append("Problem with Knicker!\n\n");
         output.append(e.toString());
         return "";
         }
      }
   
   }