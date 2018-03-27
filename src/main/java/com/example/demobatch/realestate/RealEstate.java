package com.example.demobatch.realestate;

import java.io.Serializable;

import org.springframework.batch.item.ItemCountAware;

public class RealEstate implements Serializable, ItemCountAware {
	private String street;
	private String city;
	private String zip;
	private String state;
	private String beds;
	private String baths;
	private String sqFt;
	private String type;
	private String saleDate;
	private int price;
	private double latitude;
	private double longitude;
	private int count;

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getBeds() {
		return beds;
	}

	public void setBeds(String beds) {
		this.beds = beds;
	}

	public String getBaths() {
		return baths;
	}

	public void setBaths(String baths) {
		this.baths = baths;
	}

	public String getSqFt() {
		return sqFt;
	}

	public void setSqFt(String sqFt) {
		this.sqFt = sqFt;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(String saleDate) {
		this.saleDate = saleDate;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "RealEstate{" + "street='" + street + '\'' + ", city='" + city + '\''
				+ ", zip='" + zip + '\'' + ", state='" + state + '\'' + ", beds='" + beds
				+ '\'' + ", baths='" + baths + '\'' + ", sqFt='" + sqFt + '\''
				+ ", type='" + type + '\'' + ", saleDate='" + saleDate + '\'' + ", price="
				+ price + ", latitude=" + latitude + ", longitude=" + longitude + '}';
	}

	@Override
	public void setItemCount(int count) {
		this.count = count;
	}

	public int getCount() {
		return count;
	}
}
