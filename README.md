# Bloviate
## A bot running on GAE that posts to Blogger

This bot automatically posts to a blog hosted by Blogger using the Google App Engine and uses the Blogger API to post to Blogger. The posts are generated using Wordnik.

Bloviate uses the Blogger API from Google and the Knicker library to access Wordnik for the creation of a random post.

This bot was written with the intent that the posts will show up on Twitter. You can use a service like IFTTT to synchronize with a Twitter account. 

----------

##How to set up the bot

1. Go to https://www.blogger.com and create a blog. 

2. Take note of the Blog ID - it's the number that appears after https://www.blogger.com/blogger.g?blogID= . Copy this number into the `blogger.properties` file.

3. Next create a Google App Engine application. Go to https://console.cloud.google.com/appengine and create a project.  

4. Now create some API keys so we can log into Google and authenticate with Blogger. Go to https://console.developers.google.com/apis/ . You must be logged into your Google account. Next click on Credentials in the left menu and click Create credentials. Click OAuth Client ID, and then select Web application. Name the application Bloviate. Be sure to input your redirect URI here. For now you can just set it to http://localhost:8080/oauth2callback but later you must use the actual callback URL that the app will use when uploaded to the Google App Engine. 

5. Place the newly created API keys in the `oauth.properties` file. Also remember to add your App Name now to the `blogger.properties` file.

6. Finally, you will need a Wordnik API Key. Create a Wordnik account and then go to http://developer.wordnik.com/ to request an API key. When you receive it place the key in the `wordnik.properties` file.
 
7. Deploy the App to GAE. You can set the frequency of the posting to the blog in the `cron.xml` file.

**Please note** this bot is limited due to the quotas placed on posting by the API (only 50 posts per day allowed). 

----------

##Posting to Twitter

As mentioned before, this App was written to subvert the strict restrictions that Twitter places on posting with its apps. Twitter also requires adding a mobile phone to a Twitter account before creating an application.  

To get this Bot to post to Twitter, you will need to:

1. Follow all the steps above to get the App posting to your blog on Blogger.

2. Go to IFTTT, use the "Share your New Blogger Posts to Twitter" application by dude3966, and make sure only `{{Post Content}}` is selected.

----------

##@ Mentions

If you would like the blog to have @ mentions to random Twitter users, use the functionality that grabs a random line from a file and adds it to the Blog post. 

You will then need a list of Twitter users to randomly Tweet, and without access to the Twitter API, you will have to create a file with a list of Twitter users. 
An example file is in `/war/WEB-INF/StaticFiles/twits`.

You can create your own list of Twitter users to message by signing in to Twitter and going to someone's followers page and then extracting the Twitter followers. Here is how to do this:
 
1. Find a Twitter user that has lots of followers and go to the follower page. For example, https://twitter.com/Barackobama/followers.
 
2. Next you will need to scroll down until the page displays a sufficient number of followers. Hold the page down button for a wee while. Then press Ctrl+A to copy the contents of the page and paste it into a text editor. Save this file as `users`.

3. Now remove the first 20 or so lines until you are left with a file with lines containing something like this:

        Actions de l'utilisateur 
         Suivre
        Daloonik
        @daloonik
        Profile information
    
4. Save the file and now use the following code:

         awk "/^\@/" users >> twits
         
to extract the handle name and create your own twits file. 

If you choose to use this functionality **please note** that your account may be banned by Twitter as it is against their terms and conditions for a bot to @ someone. 

----------

On Twitter, this bot has pretty basic functionality. A nice thing would be able to respond to users and add trends. Without access to the Twitter API it's more difficult to do this.