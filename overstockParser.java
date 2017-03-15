package com.cc.goods.hibernate;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class overstockParser {
		
	String searchURL = "https://www.overstock.com/search?keywords=";
	String[] searchStringWords;
	String results;
	String website;
	String brand=""; // name of brand to return
	String ratingStr=""; // rating decimal
	String price1=""; // price of product
	String title=""; // title of the webpage
	String vendor = "OverStock.com";
	Date timeStamp = Calendar.getInstance().getTime();
	DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
	String timeStampString=df.format(timeStamp);
	double price;
	double rating;
	
	public void overstockParse()
	{
	}
	public void parse(String searchString) throws IOException
	{	
	
	searchStringWords = searchString.split(" ");
	
	for(int i = 0; i<searchStringWords.length-1; i++)
	{
		searchStringWords[i]+="+";
	}
	
	for(String word : searchStringWords)
	{
		//System.out.println(word); 
		searchURL+=word;
	}
	//System.out.println(searchURL);
	
	Document searchDoc = Jsoup.connect(searchURL).maxBodySize(0).get();
	
	//Element e = searchDoc.getElementsByClass("img-middle").first();	
	Element link = searchDoc.select("div.img-middle a").first();
	try
	{
	website = link.absUrl("href");

	// Using JSoup, Parses the HTML to retrieve the JSON data
	
	Document doc = Jsoup.connect(website).maxBodySize(0).get();
	
		
		Elements ePrice = doc.getElementsByClass("monetary-price-value");
		
	try
	{	
		Elements eRating = doc.getElementsByClass("overall-rating");
		Element eBrand = doc.getElementById("brand-name");
		price1 = ePrice.html();
		price = Double.parseDouble(price1.substring(1));
		String ratingString = eRating.html();
		//brand = eBrand.html();
		title = doc.title().substring(0, 99);
		
		// Parses the rating string for the rating
		
		try
		{
		ratingStr = "";
		char ratingChar = ' ';
		for(int i = ratingString.length()-3; i<ratingString.length(); i++)
		{
			ratingChar=ratingString.charAt(i);
			ratingStr+=ratingChar;
		}
		rating = Double.parseDouble(ratingStr);
		}
		catch(StringIndexOutOfBoundsException e)
		{
			ratingStr = "0.0";
		}
	}
	catch(NullPointerException e)
	{
		ratingStr = "0.0";
	}
	}
	catch(NullPointerException e)
	{
		System.out.println("No Overstock Results Found");
	}
	
	//String price2 = "84.99";
    //double price = Double.parseDouble(price2);
	}
	public String toString()
	{
		results = "\nOverStock.com Result\n";
		results += "_____________________\n\n";
		results += "URL of search page: \t\t" + searchURL+ "\n";
		results += "URL of selected product page: \t" + website + "\n";
		results += "Page Title: \t\t\t" + title + "\n";
		results += "Vendor: \t\t\t" + vendor + "\n";
		results += "Brand: \t\t\t\t" + brand + "\n";
		results += "Price: \t\t\t\t" + price1 + "\n";
		results += "Rating: \t\t\t" + rating + "\n";
		results += "Date: \t\t\t\t" + timeStampString;
		return results;
		
	}
}
