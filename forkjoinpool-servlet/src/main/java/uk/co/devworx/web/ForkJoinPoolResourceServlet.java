package uk.co.devworx.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

@WebServlet("/")
public class ForkJoinPoolResourceServlet extends HttpServlet
{
	public static final String CLASSPATH_RESOURCE = "devworx-resource/document.txt";
	
	@Override
	protected void doGet(	HttpServletRequest req, 
	                     	HttpServletResponse resp) throws ServletException, IOException
	{
		final PrintWriter writer = resp.getWriter();
		writer.write("<html>");
		
		writer.write("<h2>(1) - " + CLASSPATH_RESOURCE + " is : " + 
					 getContent( Thread.currentThread().getContextClassLoader().getResourceAsStream(CLASSPATH_RESOURCE)) +  //Get the Content Normally
					 "</h2>");
		
		writer.write("<h2>(1) - ClassLoader: " + 
				 		Thread.currentThread().getContextClassLoader() + // List my class loader
				 	  "</h2>");
	 
		ForkJoinTask<?> task = ForkJoinPool.commonPool().submit(() -> 
		{
			writer.write("<h2>(2) - " + CLASSPATH_RESOURCE + " is : " + 
				 		  getContent( Thread.currentThread().getContextClassLoader().getResourceAsStream(CLASSPATH_RESOURCE)) +  //Get the Content From Within Stream. 
				 		 "</h2>");
			
			writer.write("<h2>(2) - ClassLoader: " + 
			 			Thread.currentThread().getContextClassLoader() +  // List my class loader
						"</h2>");
		});
		
		try
		{
			task.get();
		} 
		catch (InterruptedException | ExecutionException e)
		{
			throw new RuntimeException(e);
		}
		
		writer.write("</html>");
	}
	
	private String getContent(InputStream ins)
	{
		try
		{
			if(ins == null) return "NULL";
			else return IOUtils.toString(ins, "UTF-8");
		} 
		catch (IOException e)
		{
			throw new RuntimeException("Unexpected error ! - " + e, e);
		}
	}

}
