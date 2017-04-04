package com.xkorey.myibatis.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xkorey.myibatis.service.YoutubeService;

@RestController
public class Index {

	@Autowired
	YoutubeService service;
	
	@RequestMapping(value="/mvc")  
	public String hello(){
		return "hello spring.";
	}
	
	@RequestMapping(value="/q")
	public Map search(String channelId,String playlistId,String text,String pageToken){
		return service.search(channelId, playlistId, text, pageToken);
	}
}
