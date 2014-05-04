package br.liveo.adapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import br.liveo.interfaces.IButtonsList;
import br.liveo.model.Packages;
import br.liveo.viajabessa.R;

import com.squareup.picasso.Picasso;

public class PackagesAdapter extends BaseAdapter {

	protected static final String TAG = "PackagesAdapter";	
	
	private int lastPosition = 1;
	private final Context context;
	private IButtonsList iButtonsList;
	private List<Packages> packages = null;
	private ArrayList<Packages> packagesArraylist;

	public PackagesAdapter(Context context, List<Packages> packages, IButtonsList iButtonsList) {		
		this.context = context;		
		this.packages = packages;
		this.iButtonsList = iButtonsList;
		this.packagesArraylist = new ArrayList<Packages>();
		this.packagesArraylist.addAll(packages);
	}

	public class ViewHolder {
		TextView txtTitle;
		TextView txtValue;		
		TextView txtPeople;
		ImageView imgPackage;
		
		RelativeLayout layout_Favorite;
		RelativeLayout layout_Shared;		
	}

	@Override
	public int getCount() {
		return packages.size();
	}

	@Override
	public Packages getItem(int position) {
		return packages.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		
		final ViewHolder holder;
		
		if ( convertView == null ) {
			
			holder = new ViewHolder();
			
			LayoutInflater inflater = ( LayoutInflater ) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);			
			convertView = inflater.inflate(R.layout.fragment_pab_packages_column, null);		
	
			holder.txtTitle = ( TextView ) convertView.findViewById(R.id.txtTitle);			
			holder.txtValue = ( TextView ) convertView.findViewById(R.id.txtValue);			
			holder.txtPeople = ( TextView ) convertView.findViewById(R.id.txtPeople);
			holder.imgPackage  = ( ImageView ) convertView.findViewById(R.id.imgPackage);				

			holder.layout_Favorite = ( RelativeLayout ) convertView.findViewById(R.id.layout_Favorite);
			holder.layout_Shared  = ( RelativeLayout ) convertView.findViewById(R.id.layout_Shared);				
			
			convertView.setTag(holder);			
		} else {
			
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.txtTitle.setText(packages.get(position).getName());		
		holder.txtPeople.setText(packages.get(position).getPeople());
		
		NumberFormat numberFormat = NumberFormat.getCurrencyInstance();		
		holder.txtValue.setText(context.getResources().getString(R.string.price) + " " + numberFormat.format(packages.get(position).getValue()));		
		
		Picasso.with(context).load(packages.get(position).getImage()).placeholder(R.drawable.default_img).into(holder.imgPackage);		
		
		holder.layout_Favorite.setTag(position);
		holder.layout_Favorite.setOnClickListener(onClickFavorite);		
		
		holder.layout_Shared.setTag(position);
		holder.layout_Shared.setOnClickListener(onClickShared);		
		
		if (position > lastPosition) {
			Animation animation = AnimationUtils.loadAnimation(context, R.anim.animation_list);
			convertView.startAnimation(animation);
		}
		
		lastPosition = position;				
		return convertView;
	}

	
	private OnClickListener onClickFavorite = new OnClickListener() {
		
		@Override
		public void onClick(final View v) {
			// TODO Auto-generated method stub
			ImageView iconFavorite = (ImageView) v.findViewById(R.id.iconFavorite);			
			final Animation animation = AnimationUtils.loadAnimation(iconFavorite.getContext(), R.anim.animation_vibrate);
			iconFavorite.startAnimation(animation);
		    Handler handle = new Handler();
		    handle.postDelayed(new Runnable() {

		        @Override
		        public void run() {
		            // TODO Auto-generated method stub		        	
					try{
						iButtonsList.executeFavorite(v, ((Integer) v.getTag()));
					}catch(Exception e){
						Log.e(TAG, "onClickCompartilhar: " + e.getMessage());
					}
					
		            animation.cancel();
		        }
		    }, 500);								
		
		}
	};

	private OnClickListener onClickShared = new OnClickListener() {
		
		@Override
		public void onClick(final View v) {
			// TODO Auto-generated method stub
			
			ImageView iconShared = (ImageView) v.findViewById(R.id.iconShared);			
			final Animation animation = AnimationUtils.loadAnimation(iconShared.getContext(), R.anim.animation_vibrate);
			iconShared.startAnimation(animation);
		    Handler handle = new Handler();
		    handle.postDelayed(new Runnable() {

		        @Override
		        public void run() {
		            // TODO Auto-generated method stub		        	
					try{
						iButtonsList.executeShared(v, ((Integer) v.getTag()));
					}catch(Exception e){
						Log.e(TAG, "onClickCompartilhar: " + e.getMessage());
					}
					
		            animation.cancel();
		        }
		    }, 500);											
		}
				
	};
	
	public void filterPackages(String charText) {
		
		charText = charText.toLowerCase(Locale.getDefault());
		
		packages.clear();
		
		if (charText.length() == 0) {
			packages.addAll(packagesArraylist);
		} 
		else 
		{
			for (Packages pack : packagesArraylist){
				if (pack.getName().toLowerCase(Locale.getDefault()).contains(charText)){					
					packages.add(pack);
				}
			}
		}		
		
		notifyDataSetChanged();
	}
}
