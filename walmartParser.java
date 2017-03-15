package com.cc.goods.hibernate;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//Using javax.json-1.0.4.jar


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.io.IOException;
import java.io.StringReader;
import java.util.Scanner;

public class walmartParser {

	String results;
	String searchURL = "https://www.walmart.com/search/?query=";
	String[] searchStringWords;
	String website;
	String brand=""; // name of brand to return
	String ratingNumber=""; // rating decimal
	String number=""; // price of product
	String title=""; // title of the webpage
	String vendor = "Walmart";
	Date timeStamp = Calendar.getInstance().getTime();
	DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	String timeStampString=df.format(timeStamp);
	double price;
	double rating;
	
	public void walmartParse()
	{
	}
	public void parse(String searchString) throws IOException
	{
		searchStringWords = searchString.split(" ");
		
		for(int i = 0; i<searchStringWords.length-1; i++)
		{
			searchStringWords[i]+="%20";
		}
		
		for(String word : searchStringWords)
		{
			//System.out.println(word); 
			searchURL+=word;
		}
		//System.out.println(searchURL);
		
		Document searchDoc = Jsoup.connect(searchURL).maxBodySize(0).get();
		
		try{
		website = searchDoc.select("a.js-product-image").first().attr("abs:href");
		
		//System.out.println(website);
		
		// Using JSoup, Parses the HTML to retrieve the JSON data
		
		Document doc = Jsoup.connect(website).maxBodySize(0).get();
		//String html = Jsoup.connect("https://www.walmart.com/ip/Refurbished-Fitbit-Charge-HR-Wireless-Activity-Wristband-Plum-Large/107277236").maxBodySize(0).get().html();
		
		Elements element = doc.select("script");	//script id="tb-djs-wml-base" type="application/json"
	    element = doc.getElementsByAttributeValue("type", "application/json");		

	    Element e = doc.getElementById("tb-djs-wml-base");
	    String jsonFromHTML = e.html();
	    
	    String ratings = doc.select("span[class=visuallyhidden]").text();
	    
	    int starStringPosition = 0;
	    int starNumberPosition = 0;
	    String findStarString = "stars";
	    
	    starStringPosition = ratings.toLowerCase().indexOf(findStarString.toLowerCase());
		
	    starNumberPosition = starStringPosition - 4;
	    
	    char currentChar= ' ';
	    char nextChar = ' ';
	    
	  //Retrieves the price, stops before 's' (in stars) which ends the ratings number in the string
	  		for(int i=0; i<10 && nextChar!='s'; i++)
	  		{
	  			 currentChar = ratings.charAt(starNumberPosition + i);
	  			 ratingNumber += currentChar;
	  			 nextChar = ratings.charAt(starNumberPosition + i+1);
	  		}
	  	
	  	rating = Double.parseDouble(ratingNumber);
	    
// Parses the JSON string for price using string manipulation		
		
		int priceStringPosition = 0;
		int priceNumberLocation = 0;
		String findPriceString = "price";
		
		priceStringPosition = jsonFromHTML.toLowerCase().indexOf(findPriceString.toLowerCase());
		
		priceNumberLocation = priceStringPosition+7; // walmart json structure has actual price start 7 spaces after the index of string price
		
		char current=' ';
		char next = ' ';
		
		//Retrieves the price, stops before ',' which ends the price digits in the string
		for(int i=0; i<10 && next!=','; i++)
		{
			 current = jsonFromHTML.charAt(priceNumberLocation + i);
			 number += current;
			 next = jsonFromHTML.charAt(priceNumberLocation + i+1);
		}

	    price = Double.parseDouble(number);
		
// Parses the JSON string for Manufacturer/brand		

		int brandStringPosition = 0;
		int brandActualPosition = 0;
		String findBrandString = "brand";
	
		brandActualPosition = jsonFromHTML.toLowerCase().indexOf(findBrandString.toLowerCase()); // find "brand" string
		brandActualPosition = brandActualPosition+8; // walmart json structure has actual brand start 8 spaces after the index of string manufacturer
		

		char brandCurrent=' ';
		char brandNext = ' ';
		
		//Retrieves the brand, stops before ',' which ends the brand chars in the string
		for(int i=0; i<50 && brandNext!='"'; i++)
		{
			 brandCurrent = jsonFromHTML.charAt(brandActualPosition + i);
			 brand += brandCurrent;
			 brandNext = jsonFromHTML.charAt(brandActualPosition + i+1);
		}		
		
		title = doc.title();
		}
		catch(NullPointerException e)
		{
			System.out.println("No Walmart Results Found");
		}
	}
	public String toString()
	{
		results = "\nWalmart Result\n";
		results += "_______________\n\n";
		results += "URL of search page: \t\t" + searchURL+ "\n";
		results += "URL of selected product page: \t" + website + "\n";
		results += "Page Title: \t\t\t" + title + "\n";
		results += "Vendor: \t\t\t" + vendor + "\n";
		results += "Brand: \t\t\t\t" + brand + "\n";
		results += "Price: \t\t\t\t" + number + "\n";
		results += "Rating: \t\t\t" + ratingNumber +"\n";
		results += "Date: \t\t\t\t" + timeStampString;
		return results;
		
	}
}
