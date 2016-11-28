# BloggerBot
## A bot that posts to blogger.com
This bot posts to a blog hosted by Blogger with the intent that the posts will show up on Twitter. The blog uses Twitterfeed to synchronize with a Twitter account. This bot is intentionally made to Tweet random users, so **please note** that your account will eventually be banned by Twitter as it is against their terms and conditions for a bot to @ someone. 

Twitter now requires adding a mobile phone to a Twitter profile before creating an application. This bot is intended to subvert this restriction for those who don't care to give their phone numbers to a Big Data company.

To get this Bot to post to Twitter, you will need to:

1. Create a blog on Blogger with your Google account.

2. ~~Create a Twitterfeed account~~ As of 31 October 2016, Twitterfeed is offline. Go to IFTTT, use the "Share your New Blogger Posts to Twitter" application by dude3966, and make sure only `{{Post Content}}` is selected.

3. In Blogger, go to Settings -> E-mail and activate publishing by e-mail. You can also set your secret e-mail address here.

4. Finally, you will need a Wordnik account. Create one and then go to http://developer.wordnik.com/ to request an API key.

1. Create a blog on Blogger with your Google account

- Create a Twitterfeed account, insert your Blogger URL and next synchronize your Twitter account. You can tell Twitterfeed to synchronize up to 5 postings at a time.

- In Blogger, go to Settings -> E-mail and activate publishing by e-mail. You can also set your secret e-mail address here.

- Finally, you will need a Wordnik account. Create one and then go to http://developer.wordnik.com/ to request an API key.

You will then need a list of Twitter users to randomly Tweet, and without access to the Twitter API, you will have to create a file with a list of Twitter users. An example file is in /war/WEB-INF/StaticFiles/twits.
You can create your own list of Twitter users to message by signing in to Twitter and going to someone's followers page and then extracting the Twitter followers. Here is how to do this:
 
1. Find a Twitter user that has lots of followers and go to the follower page. For example, https://twitter.com/Barackobama/followers.
 
- Next you will need to scroll down until the page displays a sufficient number of followers. Hold the page down button for a wee while. Then press Ctrl+A to copy the contents of the page and paste it into a file called users.

- Now remove the first 20 or so lines until you are left with a file with lines containing something like this:

        Actions de l'utilisateur 
         Suivre
        Daloonik
        @daloonik
        Profile information
    
- The Twitter Handle is among those lines. Use the following code:

         awk "/^\@/" users >> twits
         
to extract the handle name and create your own twits file.   

This bot has pretty basic functionality. A nice thing would be able to respond to users and add hashtags. Without access to the Twitter API it's more difficult to do this.

**Please note** this bot is further limited due to the quotas placed on e-mailing external accounts (only 10 per day allowed). A workaround would be to e-mail an administrator account, and in the administrator account set up a filter which forwards to your secret blogger posting address.
=======
This bot has pretty basic functionality. A nice thing would be able to respond to users and add hashtags. Without access to the Twitter API it's more difficult to do this.

