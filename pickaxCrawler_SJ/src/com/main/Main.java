package com.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.main.dao.NewsCrawler;
import com.main.util.JDBCUtil;
import com.main.vo.NewsVO;

public class Main {

	public static void main(String[] args) throws UnsupportedEncodingException, IOException, ParseException, ClassNotFoundException, SQLException, InterruptedException {

		String sUrl;
		URL url;
		NewsCrawler mycrawler = new NewsCrawler();
		List<String> countrylist = Arrays.asList(
				 "터키", "투르크 메니스탄", "알바니아", "우간다", "우크라이나", "마케도니아", "이집트", "영국", "탄자니아", "벨리즈", "미국", "부르키나 파소", 
				"우루과이", "우즈베키스탄", "베네수엘라", "예멘 아랍 공화국", "잠비아", "솔로몬 제도", "브루나이"
				);
		
		for (String country : countrylist)
		{	
			List<NewsVO> newsList = new ArrayList<NewsVO>();
			String pageSize="";
			if(country.indexOf(" ")>=0)
			country=country.replace(" ", "%20");

			sUrl = "http://srch.yonhapnews.co.kr/NewSearch.aspx?callback=Search.SearchPreCallback&query="+ country+ "&ctype=A&page_size=1&channel=basic_kr";
			String referer = "http://www.yonhapnews.co.kr/home09/7091000000.html?query=" + country+ "&ctype=A";
			try {
				url = new URL(sUrl);
				URLConnection urlConn= url.openConnection();
				urlConn.setRequestProperty("referer", sUrl);
				InputStreamReader isr =new InputStreamReader(urlConn.getInputStream(), "UTF-8");
				BufferedReader br = new BufferedReader(isr);
			
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
						if(line.indexOf("totalCount")>=0)
						{
							pageSize=line.substring(line.indexOf("totalCount")+13,line.lastIndexOf(","));
							break;
					}
					
					}
				}catch (Exception e) {
					continue;
				}
			
				sUrl = "http://srch.yonhapnews.co.kr/NewSearch.aspx?callback=Search.SearchPreCallback&query="+ country+ "&ctype=A&page_size="+pageSize+"&channel=basic_kr";
				referer = "http://www.yonhapnews.co.kr/home09/7091000000.html?query=" + country+ "&ctype=A";
				try {
					url = new URL(sUrl);
					URLConnection urlConn= url.openConnection();
					urlConn.setRequestProperty("referer", sUrl);
					InputStreamReader isr =new InputStreamReader(urlConn.getInputStream(), "UTF-8");
					BufferedReader br = new BufferedReader(isr);
				
					StringBuilder sb = new StringBuilder();
					String line;
				while ((line = br.readLine()) != null) {

					if(line.indexOf("SearchPreCallback")>=0)
						sb.append(line.substring(line.indexOf("SearchPreCallback")+18));
					else if(line.indexOf("});")>=0)
						sb.append(line.substring(line.indexOf("});"),line.indexOf("});")+1));
					else
						sb.append(line);
						
				}

				JSONParser parser = new JSONParser();
				JSONObject object = (JSONObject) parser.parse(sb.toString());
				object =  (JSONObject) object.get("KR_ARTICLE");
				JSONArray articleInfoArray = (JSONArray) object.get("result");
				for(int i=0; i< articleInfoArray.size(); i++ )
				{
					JSONObject articleObject = (JSONObject) articleInfoArray.get(i);
					NewsVO news =new NewsVO();
					news.setDist_date((String) articleObject.get("DIST_DATE"));
					news.setDist_time((String) articleObject.get("DIST_TIME"));
					news.setText_body((String) articleObject.get("WRITER_NAME"));
					news.setTitle((String) articleObject.get("TITLE"));
					news.setWriter_name((String) articleObject.get("WRITER_NAME"));
					news.setContent_id((String) articleObject.get("CONTENTS_ID"));
					news.setUrl("http://www.yonhapnews.co.kr/bulletin/"
					+ news.getDist_date().substring(0,4)+"/"
					+ news.getDist_date().substring(4,6)+"/"
					+ news.getDist_date().substring(6,8)+"/"
					+"0200000000"
					+ news.getContent_id()
					+".HTML?from=search");
					newsList.add(news);
				}
			
			}catch (Exception e) {
				continue;
			}
			int index= 1;
			for(NewsVO news : newsList)
			{
				System.out.println(country + "전체 " + pageSize + "개 중 " + index++ +"개");
					sUrl = news.getUrl();
					try {
						url = new URL(sUrl);
						InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8"); 
						BufferedReader br = new BufferedReader(isr);
						
						StringBuilder sb = new StringBuilder();
						String line;
						boolean con=false;
						boolean scriptskip =false;
						while ((line = br.readLine()) != null) {
							if(line.indexOf("<!-- 기사// -->")>=0){ con =true; continue;}
							if(line.indexOf("<!-- //기사 -->")>=0){ con= false; break;}
							if(con && line.indexOf("<script")>=0){ con=false; scriptskip=true;}
							if(scriptskip && line.indexOf("/script>")>=0){ con=true; scriptskip = false; continue;}
							if(con)
							sb.append(line);
						}
						news.setReal_text_body(sb.toString()); 
					}catch (Exception e) {
						continue;
					}
			}

		}
	

	}
	
}
