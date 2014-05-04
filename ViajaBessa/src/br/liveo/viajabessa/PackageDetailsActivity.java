package br.liveo.viajabessa;

import java.text.NumberFormat;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import br.liveo.utils.Constant;
import br.liveo.utils.Utils;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

public class PackageDetailsActivity extends ActionBarActivity {
		
	private TextView txtTitle;
	private GoogleMap googleMap;	
	private TextView txtDescription;	
	private ImageView imgPackage;
	
	private String name; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		viewDoneDiscard();		
		setContentView(R.layout.packages_details);
		
		txtTitle = (TextView) findViewById(R.id.txtTitle);	
		txtDescription = (TextView) findViewById(R.id.txtDescription);		
		imgPackage = (ImageView) findViewById(R.id.imgPackage);
				
		getInforPackages();		
		
	}
		
	private void getInforPackages(){
		
		Bundle extras = getIntent().getExtras();
		
		if (extras != null){
			
			NumberFormat numberFormat = NumberFormat.getCurrencyInstance();			
			String buy = numberFormat.format(extras.getDouble(Constant.VALUE));						
			
			name = extras.getString(Constant.NAME) + " - " + buy;
			txtTitle.setText(name);
			
			txtDescription.setText(extras.getString(Constant.DESCRIPTION));			
			Picasso.with(this).load(extras.getString(Constant.IMAGE)).placeholder(R.drawable.default_img).into(imgPackage);
			
			Double lat = extras.getDouble(Constant.LAT);
			Double lng = extras.getDouble(Constant.LNG);			
			
			LatLng latLng = new LatLng(lat, lng);			
			executarGoogleMaps(latLng);			
		}
	}
	
	private void executarGoogleMaps(LatLng latLng){	
		
		FragmentManager fragmentManager = getSupportFragmentManager();
		SupportMapFragment supportMapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.fragmentMapa);
		
		googleMap = supportMapFragment.getMap();
		googleMap.setMyLocationEnabled(true);
		googleMap.getUiSettings().setMyLocationButtonEnabled(true);		
		
		googleMap.setInfoWindowAdapter(infoWindow);
		MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        googleMap.addMarker(markerOptions);		
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);								
	}
	
	private GoogleMap.InfoWindowAdapter infoWindow = new GoogleMap.InfoWindowAdapter() {

		  
		@Override
		public View getInfoWindow(Marker arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public View getInfoContents(Marker marker) {
			// TODO Auto-generated method stub
			View view = getLayoutInflater().inflate(R.layout.details_marker, null);

			ImageView image = (ImageView) view.findViewById(R.id.imageViewIcone);
			image.setImageResource(R.drawable.ic_launcher);

			TextView txtTitulo = (TextView) view.findViewById(R.id.txtTituloApelido);
			txtTitulo.setText(name);
			return view;
		}
	};	
	
	private void viewDoneDiscard(){
			
			LayoutInflater inflater = (LayoutInflater) getSupportActionBar().getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
			
			View customActionBarView = null;
			customActionBarView = inflater.inflate(R.layout.actionbar_custom_view_done_discard, null);			        
	        customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(
	                new View.OnClickListener() {
	                    @Override
	                    public void onClick(View v) {
	                    	Utils.toastLong(PackageDetailsActivity.this, R.string.thanks_for_buying);
	                    	finish();
	                    }
	                });
	                        	
	        
	        customActionBarView.findViewById(R.id.actionbar_discard).setOnClickListener(
	                new View.OnClickListener() {
	                    @Override
	                    public void onClick(View v) {
	                        finish(); 
	                    }
	                });        	        	
	                
			getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,ActionBar.DISPLAY_SHOW_CUSTOM | 
					                                ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
			
			getSupportActionBar().setCustomView(customActionBarView,
					new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
							ViewGroup.LayoutParams.MATCH_PARENT));   	
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		finish();
		return super.onOptionsItemSelected(item);
	}
}
