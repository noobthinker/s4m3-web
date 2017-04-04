package com.xkorey.myibatis.service;

import java.util.Map;

public interface YoutubeService {

	public Map search(String channelId,String playlistId,String text,String pageToken);
	
	
}
