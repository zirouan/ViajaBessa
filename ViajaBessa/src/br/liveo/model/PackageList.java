package br.liveo.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class PackageList {

	@SerializedName("pacotes")
	public List<Packages> pacotes = new ArrayList<Packages>();		
	
}
