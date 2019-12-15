package uk.co.devworx.web;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebServlet;

@WebServlet("/")
public class ForkJoinPoolInitListener implements ServletContextListener
{
	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		ForkJoinTask<?> task = ForkJoinPool.commonPool().submit(() -> 
		{
			System.out.println("INITIALISED ! ");
		});
	
		try
		{
			task.get();
		} 
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	

}
