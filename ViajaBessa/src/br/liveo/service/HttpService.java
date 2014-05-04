package br.liveo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import br.liveo.model.PackageList;
import br.liveo.model.Packages;

import com.google.gson.Gson;

import android.util.Log;

public class HttpService {

	public static ArrayList<Packages> readPackagesJson(String URL) {
		
		List<Packages> packages = null;		
		StringBuilder stringBuilder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(URL);

		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();

			if (statusCode == 200) {				
				
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));

				String line;
				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line);
				}

				Gson serializer = new Gson();
				PackageList list = (PackageList) serializer.fromJson(stringBuilder.toString(), PackageList.class);
				packages = list.pacotes;
				
			} else {
				Log.e("Json", "Erro durante o download do arquivo");
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ((ArrayList<Packages>) packages);
	}
}
