package br.liveo.fragments;

import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import br.liveo.adapter.PackagesAdapter;
import br.liveo.interfaces.IButtonsList;
import br.liveo.model.Packages;
import br.liveo.service.HttpService;
import br.liveo.utils.Constant;
import br.liveo.utils.Menus;
import br.liveo.utils.Utils;
import br.liveo.viajabessa.PackageDetailsActivity;
import br.liveo.viajabessa.R;

public class TabPackagesFragment extends Fragment implements IButtonsList{    

	private ListView listPackages;
	private boolean SearchClick;
	private boolean checkFavorite;		
	private ImageView iconFavorite;
	private ArrayList<Packages> packages;
	private PackagesAdapter packagesAdapter;
	private RelativeLayout layout_no_Records;	
	
	public static TabPackagesFragment newInstance() {
        TabPackagesFragment fragment = new TabPackagesFragment();
        return fragment;
    }	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {	
		
		View rootView = inflater.inflate(R.layout.default_list, container, false);	
		
		listPackages = (ListView) rootView.findViewById(R.id.listPackages);	
		listPackages.setOnItemClickListener(onItemClick);
		
		layout_no_Records = (RelativeLayout) rootView.findViewById(R.id.layout_no_Records);
		rootView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT ));		
		return rootView;
	}
			
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		
		boolean redeOK = Utils.isNetworkAvailabel(getActivity());
		
		if (redeOK){	
			layout_no_Records.setVisibility(RelativeLayout.GONE);
			if (savedInstanceState != null){			
				packages = savedInstanceState.getParcelableArrayList(Constant.OBJ_PACKAGES);						
				resultado();
			}else{
				checkFavorite = false;				
				new Executar().execute();				
			}
		}else{
			Utils.toastShort(getActivity(), R.string.connection_unavailable);
			layout_no_Records.setVisibility(RelativeLayout.VISIBLE);			
		}		
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putBoolean(Constant.CHECK_FAVORITE, checkFavorite);
		outState.putParcelableArrayList(Constant.OBJ_PACKAGES, packages);
	}
	
	private class Executar extends AsyncTask<Void, Void, ArrayList<Packages>>{
		
		@Override
		protected ArrayList<Packages> doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return HttpService.readPackagesJson("https://viajabessaliveo.apiary-mock.com/pacotes");
		}
		
		@Override
		protected void onPostExecute(ArrayList<Packages> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);	
					
			if (result != null && result.size() > 0){
				packages = result;
				resultado();
			}
		}
	}	
	
	private void resultado(){
		if (packages != null && packages.size() > 0){
			packagesAdapter = new PackagesAdapter(getActivity(), packages, this);
			listPackages.setAdapter(packagesAdapter);
		}
	}
	
	private OnItemClickListener onItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub

			Intent intent = new Intent(getActivity(), PackageDetailsActivity.class);
			intent.putExtra(Constant.NAME, packages.get(position).getName());
			intent.putExtra(Constant.PEOPLE, packages.get(position).getPeople());
			intent.putExtra(Constant.VALUE, packages.get(position).getValue());
			intent.putExtra(Constant.DESCRIPTION, packages.get(position).getDescription());
			intent.putExtra(Constant.IMAGE, packages.get(position).getImage());
			intent.putExtra(Constant.LAT, packages.get(position).getLat());
			intent.putExtra(Constant.LNG, packages.get(position).getLng());									
			startActivity(intent);
		}
	};
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);		
		inflater.inflate(R.menu.menu, menu);
					    	
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(Menus.SEARCH));
	    searchView.setQueryHint(this.getString(R.string.search));
	    
	    ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text))
        .setHintTextColor(getResources().getColor(R.color.white));	    
	    searchView.setOnQueryTextListener(onQuery);
		
		menu.findItem(Menus.ADD).setVisible(false);
		menu.findItem(Menus.UPDATE).setVisible(false);		
		menu.findItem(Menus.SEARCH).setVisible(true);	
		SearchClick = false;
	}	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch (item.getItemId()) {
		case Menus.SEARCH:
			SearchClick = true;
			break;

		default:
			break;
		}
		
		return true;		
	}
	
	private OnQueryTextListener onQuery = new OnQueryTextListener() {
		
		@Override
		public boolean onQueryTextSubmit(String arg0) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean onQueryTextChange(String arg0) {
			// TODO Auto-generated method stub
			
			try{
				if (SearchClick){
					packagesAdapter.filterPackages(arg0);
				}
			}catch(Exception e){
				
			}
			
			return false;
		}
	};

	@Override
	public void executeShared(View view, int position) {
		// TODO Auto-generated method stub
		
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append(getString(R.string.tab_one) + ": ");		
		stringBuilder.append(packages.get(position).getName() + ", ");
		stringBuilder.append(getString(R.string.description) + ": ");		
		stringBuilder.append(packages.get(position).getDescription() + ", ");		
		stringBuilder.append(packages.get(position).getPeople() + ", ");
		
		String price = getString(R.string.price);
		stringBuilder.append(price + " " + packages.get(position).getValue());		
		
		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString());
		sendIntent.putExtra(Intent.EXTRA_SUBJECT, "ViajaBessa");
		sendIntent.setType("text/plain");
		startActivity(sendIntent);		
	}

	@Override
	public void executeFavorite(View view, int position) {
		// TODO Auto-generated method stub		
		iconFavorite = (ImageView) view.findViewById(R.id.iconFavorite);
				
		if (!checkFavorite){
			iconFavorite.setImageResource(R.drawable.ic_action_favorite_on);
			checkFavorite = !checkFavorite;
		}else{
			iconFavorite.setImageResource(R.drawable.ic_action_favorite);
			checkFavorite = !checkFavorite;			
		}
	}
}


