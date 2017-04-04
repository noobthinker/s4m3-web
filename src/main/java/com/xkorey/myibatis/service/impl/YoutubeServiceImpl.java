package com.xkorey.myibatis.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xkorey.myibatis.service.YoutubeService;

@Service
public class YoutubeServiceImpl implements YoutubeService {

	@Value("${key}")
	String key;
	
	@Value("${part}")
	String part;
	
	@Value("${url}")
	String url;
	
	@Value("${maxResults}")
	int size;
	
	@Value("${playlist}")
	String playlist;
	
	@Value("${imges.dir}")
	String imagePath;
	
	ObjectMapper om = new ObjectMapper();
	
	
	public Map search(String channelId,String playlistId,String text,String pageToken) {
		try {
			String requestURL = makeQ(channelId,playlistId,text,pageToken);
			System.out.println(requestURL);
			CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(requestURL);
            String result = httpclient.execute(httpGet,responseHandler);
            Map tube = om.readValue(result, new TypeReference<HashMap<String,Object>>(){});
            Map re = new HashMap();
            re.put("nextPageToken",tube.get("nextPageToken"));
            if (null != tube.get("prevPageToken")) {
            	re.put("prevPageToken", tube.get("prevPageToken"));
            }
            re.put("totalResults", tube.get("totalResults"));
            List<Map> videos = (List<Map>) tube.get("videos");
            List<Map> vs = new ArrayList();
            for(Map i:videos){
            	Map temp = new HashMap();
            	String id = i.get("id").toString();
            	temp.put("id",id);
            	Map snippet = (Map) i.get("snippet");
            	temp.put("type",snippet.get("type").toString());
            	temp.put("description", snippet.get("description"));
            	temp.put("title", snippet.get("title"));
            	temp.put("publishedAt",snippet.get("publishedAt"));
            	Map thumbnails = (Map) snippet.get("thumbnails");
            	if(null==thumbnails){
            		continue;
            	}
            	vs.add(temp);
            	 Map sh = (Map) thumbnails.get("high");
            	 downlaodImage(sh.get("url").toString(),id,"-h");
            }
            re.put("videos",vs);
            return re;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

        public String handleResponse(
                final HttpResponse response) throws ClientProtocolException, IOException {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        }

    };
    
	
	String makeQ(String channelId,String playlistId,String text,String pageToken) throws UnsupportedEncodingException {
        String s =  url;
        s +="part="+ URLEncoder.encode(part,"UTF-8");
        if(null!=channelId && channelId.equals("")){
            s +="&channelId="+channelId;
//            s += "&relevanceLanguage=zh-Hans";
        }else if(null!=playlistId && !playlistId.equals("")){
            s = playlist;
            s +="part="+ URLEncoder.encode(part,"UTF-8");
            s +="&playlistId="+playlistId;
        }else{
            s += "&q="+URLEncoder.encode(text,"UTF-8");
            if(null!=pageToken && !pageToken.equals("")){
               s += "&pageToken="+pageToken;
            }
//            s += "&relevanceLanguage=zh-Hans";
        }
        s +="&maxResults="+size;
        s += "&key="+key;
        return s;
    }
	
	
	int downlaodImage(String url,String id,String prefix){
        try {
            File img = new File(imagePath+id+prefix+".jpg");
            if(img.exists())return 0;
            if(url.indexOf("/")==0){
                url = "https:"+url;
            }
            BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
            BufferedOutputStream out = new BufferedOutputStream(
                    new FileOutputStream(img));
            byte[] buf = new byte[2048];
            int length = in.read(buf);
            while (length != -1) {
                out.write(buf, 0, length);
                length = in.read(buf);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
        return 0;
    }

}
