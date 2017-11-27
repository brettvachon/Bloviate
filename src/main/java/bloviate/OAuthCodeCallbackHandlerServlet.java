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

import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet handling the OAuth callback from the authentication service. We are
 * retrieving the OAuth code, then exchanging it for a refresh and an access
 * token and saving it.
 */
@SuppressWarnings("serial")

public class OAuthCodeCallbackHandlerServlet extends HttpServlet
   {

   /** The name of the OAuth error URL parameter */
   public static final String ERROR_URL_PARAM_NAME = "error";
   /** The name of the OAuth code URL parameter */
   public static final String CODE_URL_PARAM_NAME = "code";
   /** The URL suffix of the OAuth Callback handler servlet */
   public static final String URL_MAPPING = "/oauth2callback";
   /** The URL suffix of the Blogger servlet */
   public static final String REDIRECT_URI = "/cron/BlogServlet";
    
   public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
      {
      // Check for no errors in the OAuth process
      String[] error = request.getParameterValues(ERROR_URL_PARAM_NAME);
      if (error != null && error.length > 0) 
        {
        response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
        return;
        }

       // Check for the presence of the response code
       String[] code = request.getParameterValues(CODE_URL_PARAM_NAME);
       if (code == null || code.length == 0) 
          {
          response.sendError(HttpServletResponse.SC_BAD_REQUEST);
          return;
          }
             
       //Use code to generate a Redirect URI
       GoogleTokenResponse tokenResponse = BlogServlet.flow.newTokenRequest(code[0]).setRedirectUri(getOAuthCodeCallbackHandlerUrl(request)).execute();
          
       //Store the token
       DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();    
       Entity OAuthTokenEntity = new Entity("OAuthTokenEntity","OA");
       OAuthTokenEntity.setProperty("OAuthAccessToken", tokenResponse.getAccessToken());
       OAuthTokenEntity.setProperty("OAuthRefreshToken",tokenResponse.getRefreshToken());
       datastore.put(OAuthTokenEntity);
       
       response.sendRedirect(REDIRECT_URI);
      }

    public static String getOAuthCodeCallbackHandlerUrl(HttpServletRequest request) 
       {
       StringBuilder oauthURL = new StringBuilder();
       oauthURL.append(request.getScheme() + "://");
       oauthURL.append(request.getServerName());
       oauthURL.append(request.getServerPort() == 80 ? "" : ":" + request.getServerPort());
       oauthURL.append(request.getContextPath());
       oauthURL.append(URL_MAPPING);
       oauthURL.append(request.getPathInfo() == null ? "" : request.getPathInfo());
         
       return oauthURL.toString(); 
       }
    
   }
