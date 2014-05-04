package br.liveo.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Packages implements Parcelable {

	@SerializedName("id")	
	private int id;
	
	@SerializedName("name")	
	private String name;

	@SerializedName("people")	
	private String people;
			
	@SerializedName("image")	
	private String image;
		
	@SerializedName("value")	
	private Double value;
	
	@SerializedName("description")	
	private String description;	

	@SerializedName("lat")	
	private Double lat;
	
	@SerializedName("lng")	
	private Double lng;
	
	
	public Packages() {
		// TODO Auto-generated constructor stub
	}
		
	public Packages(Parcel source) {
		// TODO Auto-generated constructor stub
		
	    String[] data = new String[8];
	    source.readStringArray(data);

	    this.setId(Integer.valueOf(data[0]));	    
        this.setName(data[1]); 
        this.setPeople(data[2]);        
        this.setImage(data[3]);        
		this.setValue(Double.valueOf((data[4])));
		this.setDescription(data[5]);			
		this.setLat(Double.valueOf((data[6])));
		this.setLng(Double.valueOf((data[7])));		
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeStringArray(new String[] {String.valueOf(getId()),
											this.getName(),
											this.getPeople(),											
											this.getImage(),											
											String.valueOf(this.getValue()),
											this.getDescription(),
											String.valueOf(this.getLat()),		
											String.valueOf(this.getLng())});	
	}
	
	public static final Parcelable.Creator<Packages> CREATOR = new Parcelable.Creator<Packages>() {
		
		public Packages[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Packages[size];
		}
		
		public Packages createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new Packages(source);
		}
	};	
	
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPeople() {
		return people;
	}

	public void setPeople(String people) {
		this.people = people;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

}
