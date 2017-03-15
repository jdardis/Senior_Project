package com.cc.goods.hibernate;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
//import org.hibernate.service.internal.*;
//import org.hibernate.service.*;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

//import com.cc.example.hibernate.Employee;

public class MainClass {
	private static ServiceRegistry serviceRegistry;
	private static SessionFactory sessionFactory;
	
	public static void main(String [] args){
		try{
		// CREATES configuration object 
		Configuration cfg = new Configuration().configure();
		
		// Uses service Registry to create session factory
		serviceRegistry = new ServiceRegistryBuilder().applySettings(
				cfg.getProperties()).buildServiceRegistry();
		sessionFactory = cfg.buildSessionFactory(serviceRegistry);
		
		walmartParser wP = new walmartParser();
		overstockParser oP = new overstockParser();
		
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Please enter product search string: ");
		String searchString = scan.nextLine();
		
		wP.parse(searchString);
		ConsumerGood walmartGood = new ConsumerGood(wP.title, wP.price, wP.brand, wP.vendor, wP.rating, wP.timeStampString);
		//System.out.println(wP.toString());
		
		oP.parse(searchString);
		ConsumerGood overstockGood = new ConsumerGood(oP.title, oP.price, oP.brand, oP.vendor, oP.rating, oP.timeStampString);

		//System.out.println(oP.toString());
		
		//Create ConsumerGood objects to be added,deleted,etc.
		//ConsumerGood g2 = new ConsumerGood("hammer", 12.00, "Dell", "Wal-Mart", 4.2,"08/28/1994");
		
		// handler is where the transaction will take place within
		//Handler is created to access the methods
		MainClass handler= new MainClass();
		
		// uses the add product method for Walmart or update if it is already in db
//		try{
//			handler.addproduct(walmartGood);
//		}
//		catch(Exception e){
//			handler.update_product(walmartGood.getName(), walmartGood);
//		}
		
		//Add overStock or update if it is already in db
		//handler.addproduct(overstockGood);

		//handler.update_product(overstockGood.getName(), overstockGood);
		
		handler.addproduct(walmartGood);
		handler.addproduct(overstockGood);
		//handler.getProduct(wP.title);
		//handler.getProduct(oP.title);
		//handler.getProduct("asd");
		
		
		}	
		catch (Exception e){
			System.out.println(e.getMessage());
		}
		finally{
			// Actual constant insertion will happen at this step
		}
	}

public void addproduct(ConsumerGood product){
	//new session and transaction needed for every method
	Session session = null;
	Transaction trans = null;
	
	try{
		session =sessionFactory.openSession();
		trans=session.getTransaction();
		// transaction begins and commits everything that happens before commit command
		trans.begin();
		session.save(product);
		trans.commit();
		
		// Flushing the session forces Hibernate to synchronize the in-memory 
		// state of the Session with the database
		session.flush();
	}
	catch(HibernateException he){
		if(trans!=null)trans.rollback();
	    System.out.println("Not able to open session");
	    he.printStackTrace();
	}
	catch(Exception e){
		e.printStackTrace();
	}
	finally{
		if(session!=null)
			session.close();
	}
}

public void update_product(String name, ConsumerGood g1){
	Session session = null;
	Transaction trans = null;
	try{
		session = sessionFactory.openSession();
		trans = session.beginTransaction();
		ConsumerGood n1 = (ConsumerGood)session.get(ConsumerGood.class, name);
		
		n1.setPrice(g1.getPrice());
		n1.setStore(g1.getStore());
		n1.setRating(g1.getRating());
		n1.setBrand(g1.getBrand());
		n1.setDate(g1.getDate());
		
		session.update(n1);
		trans.commit();
	}catch(HibernateException he){
		if(trans!=null)trans.rollback();
		he.printStackTrace();
	}finally{
		if(session!=null)
			session.close();
	}
}

public void getProduct(String name)
{
	  Session session=null;
	  Transaction trans=null;
	  try
	  {
		    session=sessionFactory.openSession();
		    trans=session.beginTransaction();
		    ConsumerGood g1=(ConsumerGood)session.get(ConsumerGood.class, name);
		    System.out.println("\nproduct:"+g1.getName());
		    System.out.println("price:"+g1.getPrice());
		    trans.commit();
	  }catch(HibernateException he){
		    if(trans!=null)trans.rollback();
		    he.printStackTrace();
	  }finally{
		    if(session!=null)
		        session.close();
		}
}


}