package com.cc.goods.hibernate;

public class ConsumerGood {
	public String name;
	private double price;
	private String brand;
	private String store;
	private double rating;
	private String date;
	
	public ConsumerGood(String n, double p, String b, String s, double r, String d){
		this.name = n;
		this.price = p;
		this.brand = b;
		this.store = s;
		this.rating = r;
		this.date = d;
	}
	
	public ConsumerGood(){
		
	}

	public void setName(String productName){
		name = productName;
	}
	
	public String getName(){
		return name;
	}
	
	public void setPrice(double productPrice){
		this.price = productPrice;
	}
	
	public double getPrice(){
		return price;
	}
	
	public void setBrand(String brand){
		this.brand = brand;
	}
	
	public String getBrand(){
		return brand;
	}
	
	public void setStore(String productStore){
		this.store = productStore;
	}
	
	public String getStore(){
		return store;
	}
	
	public void setRating(double productRating){
		this.rating = productRating;
	}
	
	public double getRating(){
		return rating;
	}
	
	public void setDate(String d){
		this.date = d;
	}
	
	public String getDate(){
		return date;
	}
	

}