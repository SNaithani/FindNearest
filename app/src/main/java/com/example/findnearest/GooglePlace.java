package com.example.findnearest;

public class GooglePlace {
	private String name;
	private String category;
	private String rating;
	private String open;
	//Anku : added lat and lng
	private double lat;
	private double lng;
	
	public GooglePlace() {
		this.name = "";
		this.rating = "";
		this.open = "";
		this.setCategory("");
		//Anku : added lat and lng
		this.lat = 0.0;
		this.lng = 0.0;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}
	
	public String getRating() {
		return rating;
	}
	
	public void setOpenNow(String open) {
		this.open = open;
	}

	public String getOpenNow() {
		return open;
	}
	//Anku : Included setLat for latitude of the place
	public void setLat(double lat) {
		this.lat = lat;
	}
	//Anku : Included getLat for latitude of the place
	public double getLat() {
		return lat;
	}
	//Anku : Included setLng for longitude of the place
	public void setLng(double lng) {
		this.lng = lng;
	}
	//Anku : Included getLng for longitude of the place
	public double getLng() {
		return lng;
	}
	
}
