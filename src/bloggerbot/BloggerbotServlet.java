/*
<<<<<<< HEAD
 * Bloggerbot is licenced under the The MIT License (MIT)
=======
 * BloggerBot is licenced under the The MIT License (MIT)
>>>>>>> c0949e2fab9d2c90dc71d620c872239593cd2b52
 * 
 * Copyright (c) 2014�2016 Daloonik daloonik@gmail.com
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package bloggerbot;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.*;

<<<<<<< HEAD
import com.google.appengine.api.utils.SystemProperty;

=======
>>>>>>> c0949e2fab9d2c90dc71d620c872239593cd2b52
import net.jeremybrooks.knicker.KnickerException;
import net.jeremybrooks.knicker.WordApi;
import net.jeremybrooks.knicker.WordsApi;
import net.jeremybrooks.knicker.dto.Example;
import net.jeremybrooks.knicker.dto.Word;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@SuppressWarnings("serial")
public class BloggerbotServlet extends HttpServlet
   {
<<<<<<< HEAD
   public static String blogemail = "your_secret_blog_email";
   
=======
>>>>>>> c0949e2fab9d2c90dc71d620c872239593cd2b52
   public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException
      {
      StringBuffer buffer = new StringBuffer(); 
      Properties props = new Properties();
      Session session = Session.getDefaultInstance(props, null);
<<<<<<< HEAD
      GetLine twit = new GetLine("WEB-INF/StaticFiles/twits"); 
=======
      GetLine twit = new GetLine("WEB-INF/StaticFiles/twits");
>>>>>>> c0949e2fab9d2c90dc71d620c872239593cd2b52
      
      System.setProperty("WORDNIK_API_KEY", "your_wordnik_api_key");
      
      try
<<<<<<< HEAD
         { 
=======
         {   
>>>>>>> c0949e2fab9d2c90dc71d620c872239593cd2b52
         do 
            {
            buffer.setLength(0);
            Word random = WordsApi.randomWord();
            Example ex = WordApi.topExample(random.getWord());
<<<<<<< HEAD
            buffer.append(ex.getText());            
            } while(buffer.length() > 140); 
        
=======
            buffer.append(ex.getText());
            } while(buffer.length() > 140);
         
>>>>>>> c0949e2fab9d2c90dc71d620c872239593cd2b52
         if(buffer.length() < 120)
            {
            buffer.append(" #");
            buffer.append(WordsApi.randomWord().getWord());
            }
         
         if(buffer.length() < 125)
            {
            buffer.insert(0, twit.line() + " ");
            }
<<<<<<< HEAD
      
         Message msg = new MimeMessage(session);
         msg.setFrom(new InternetAddress(SystemProperty.applicationId.get() + "@appspot.gserviceaccount.com"));
         msg.addRecipient(Message.RecipientType.TO,
                          new InternetAddress(blogemail, "Bloggerbot"));

         msg.setContent(buffer.toString(),"text/html; charset=utf-8");
         
         Transport.send(msg);
         
=======
         Message msg = new MimeMessage(session);
         msg.setFrom(new InternetAddress("noreply@anybody.com", "Bloggerbot"));
         msg.addRecipient(Message.RecipientType.TO,
                          new InternetAddress("your_secret_address", "Blog"));
         msg.setContent(buffer.toString(),"text/html; charset=utf-8");
         Transport.send(msg);

>>>>>>> c0949e2fab9d2c90dc71d620c872239593cd2b52
         resp.setContentType("text/plain");
         resp.getWriter().println("Posted: ");
         resp.getWriter().println(buffer.toString());
         }
      catch(KnickerException e)
         {
<<<<<<< HEAD
    	  resp.getWriter().println("Problem with Knicker!");
          resp.setContentType("text/plain");
          resp.getWriter().println(e.toString());
         }
      catch (AddressException e)
         {
    	 resp.getWriter().println("Problem involving wrongly formatted address!");
=======
         e.printStackTrace();
         System.exit(1);
         }
      catch (AddressException e)
         {
>>>>>>> c0949e2fab9d2c90dc71d620c872239593cd2b52
         resp.setContentType("text/plain");
         resp.getWriter().println(e.toString());
         }
      catch (MessagingException e)
         {
<<<<<<< HEAD
     	 resp.getWriter().println("Problem with Email!");
=======
>>>>>>> c0949e2fab9d2c90dc71d620c872239593cd2b52
         resp.setContentType("text/plain");
         resp.getWriter().println(e.toString());
         }    
      catch(FileNotFoundException e)
         {
<<<<<<< HEAD
     	 resp.getWriter().println("File not found!");
         resp.setContentType("text/plain");
         resp.getWriter().println(e.toString());       
         }
      catch(Exception e)
      	{
      	 resp.getWriter().println("Problem!");
      	 resp.setContentType("text/plain");
      	 resp.getWriter().println(e.toString()); 
      	}
=======
         resp.setContentType("text/plain");
         resp.getWriter().println(e.toString());       
         }
>>>>>>> c0949e2fab9d2c90dc71d620c872239593cd2b52
      }
   }