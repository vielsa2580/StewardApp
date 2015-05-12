package com.example.stewardimperial.connectivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.stewardimperial.models.MODELResultSet;
import com.example.stewardimperial.utils.UTILConstants;

public class ServerConnect {

	InputStream is=null;

	public MODELResultSet requestServer(String service,int type, List<BasicNameValuePair> params){


		JSONObject jObject = null;

		final MODELResultSet modelJsonResponse = new MODELResultSet();

		try{

			if(type == UTILConstants.HTTP_GET){
				DefaultHttpClient httpClient = new DefaultHttpClient();
				String paramString = URLEncodedUtils.format(params, "utf-8");
				String temp = (UTILConstants.serverurl+service);
				temp += "?"+paramString;	
				HttpGet httpGet = new HttpGet(temp);				

				HttpResponse response = httpClient.execute(httpGet);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();


			}else if(type == UTILConstants.HTTP_POST){
				
				HttpPost httpPost = new HttpPost(UTILConstants.serverurl+service);
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params);  
				httpPost.setEntity(entity);  
				
				HttpParams httpParameters = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
				HttpConnectionParams.setSoTimeout(httpParameters, 10000);

				DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();

			}

			//convert response to string

			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			String result=sb.toString();

			jObject = new JSONObject(result);

			if (jObject != null) {
				modelJsonResponse.setError(""+jObject.get("message"));
				modelJsonResponse.setMessage(""+jObject.get("data"));
				modelJsonResponse.setJsonObject(jObject);	
			}

		}catch(JSONException e){
			modelJsonResponse.setError("Error");
			modelJsonResponse.setMessage(""+e.getMessage());
			e.printStackTrace();
		}catch (ConnectTimeoutException e) {
			modelJsonResponse.setError("Error");
			modelJsonResponse.setMessage("Connection time out");
		}catch(Exception e){
			e.printStackTrace();
			modelJsonResponse.setError("Error");
			modelJsonResponse.setMessage(""+e.getMessage());	

		}

		return modelJsonResponse;

	}
}
