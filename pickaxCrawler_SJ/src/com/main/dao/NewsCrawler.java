package com.main.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.main.util.JDBCUtil;
import com.main.vo.NewsVO;
import com.mysql.jdbc.PreparedStatement;

public class NewsCrawler {
public String fn_countryCrawl(List<NewsVO> curnewslist) throws UnsupportedEncodingException, IOException, ParseException, SQLException, ClassNotFoundException{
		
		ArrayList<NewsVO> newslist = (ArrayList<NewsVO>) curnewslist;
	
		if(newslist.size() == 0){
			return "fail";
		}else{
			Connection conn = JDBCUtil.connect();
			String sql;
			Statement smtp = conn.createStatement();
			for(int i=0; i < newslist.size(); i++){
				try{
				sql= "INSERT INTO yonhapnews (content_id, dist_date, dist_time, real_text_body, text_body, title,url,writer_name) VALUES(?,?,?,?,?,?,?,?)"; 
				PreparedStatement pstmt = (PreparedStatement) conn.prepareStatement(sql);
				pstmt.setString(1,newslist.get(i).getContent_id());
				pstmt.setString(2,newslist.get(i).getDist_date());
				pstmt.setString(3,newslist.get(i).getDist_time());
				pstmt.setString(4,newslist.get(i).getReal_text_body());
				pstmt.setString(5,newslist.get(i).getText_body());
				pstmt.setString(6,newslist.get(i).getTitle());
				pstmt.setString(7,newslist.get(i).getUrl());
				pstmt.setString(8,newslist.get(i).getWriter_name());
						
				pstmt.execute();
				}
				catch(Exception e){
					continue;
				}
			}			
			
			smtp.close();
			conn.close();
		}
		
		return "success";
}
}
