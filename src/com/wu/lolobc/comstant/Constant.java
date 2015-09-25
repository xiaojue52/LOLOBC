package com.wu.lolobc.comstant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.wu.lolobc.comstant.LOLStruct.LastRace;
import com.wu.lolobc.comstant.LOLStruct.Mode;

public class Constant {
	public static class ServerMap{
		static public String[] server = {"电信一","电信二","电信三","电信四","电信五","电信六","电信七","电信八","电信九","电信十","电信十一",
				"电信十二","电信十三","电信十四","电信十五","电信十六","电信十七","电信十八","电信十九","网通一",
				"网通二","网通三","网通四","网通五","网通六","教育一"};
		static public String[] name = {"艾欧尼亚 电一","祖安 电二","诺克萨斯 电三","班德尔城 电四","皮尔特沃夫 电五","战争学院 电六","巨神峰 电七","雷瑟守备 电八","裁决之地 电九","黑色玫瑰 电十","暗影岛 电十一","钢铁烈阳 电十二","均衡教派 电十三","水晶之痕 电十四","影流 电十五",
				"守望之海 电十六","征服之海 电十七","卡拉曼达 电十八","皮城警备 电十九",
				"比尔吉沃特 网一","德玛西亚 网二","弗雷尔卓德 网三","无畏先锋 网四","恕瑞玛 网五","扭曲丛林 网六","教育网专区 教育一"};
	}

	public static String getData(String url, Map<String, String> pars) {
		// 创建请求对象
		HttpPost post;
		// 创建客户端对象
		HttpClient client;
		// 创建发送请求的对象
		HttpResponse response;
		// 创建接收返回数据的对象
		HttpEntity entity;
		// 创建流对象
		InputStream is;
		UrlEncodedFormEntity urlEntity;
		{
			
			HttpParams params = new BasicHttpParams();
			/* 连接超时 */
			HttpConnectionParams.setConnectionTimeout(params, 2000);
			/* 请求超时 */
			HttpConnectionParams.setSoTimeout(params, 4000);
			post = new HttpPost(url);
			client = new DefaultHttpClient(params);


			// 参数设置
			List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
			if (pars != null) {
				Iterator<Map.Entry<String, String>> iter = pars.entrySet()
						.iterator();
				while (iter.hasNext()) {
					Map.Entry<String, String> mEntry = (Map.Entry<String, String>) iter
							.next();
					pairs.add(new BasicNameValuePair(mEntry.getKey(), mEntry
							.getValue()));
				}
			}
			try {
				// 用UrlEncodedFormEntity来封装List对象
				urlEntity = new UrlEncodedFormEntity(pairs, "utf-8");
				// 设置使用的Entity
				post.setEntity(urlEntity);

				try {
					// 客户端开始向指定的网址发送请求
					response = client.execute(post);
					// 获得请求的Entity
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						entity = response.getEntity();
						is = entity.getContent();
						// 下面是读取数据的过程
						BufferedReader br = new BufferedReader(
								new InputStreamReader(is));
						String line = null;
						StringBuffer sb = new StringBuffer();
						while ((line = br.readLine()) != null) {
							sb.append(line);
						}
						//System.out.println(sb.toString());
						// Toast.makeText(context, sb.toString(),
						// Toast.LENGTH_SHORT).show();
						return sb.toString();
					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	public enum Type{
		GET_GOOD,GET_BAD,GET_POWER,GET_SCORE,GET_MODE,GET_RECENT_RACE;		
	}
	public static String parseContent(String arg){
		String content = "";
		LOLStruct lolStruct = new LOLStruct();
		parseData(lolStruct,Type.GET_GOOD,0,arg,"<div title=\"此玩家在游戏中被队友给的好评数，只有使用lol盒子的玩家才可以进行评价\">");
		parseData(lolStruct,Type.GET_BAD,0,arg,"<div title=\"此玩家在游戏中被多少人拉黑，只有使用lol盒子的玩家才可以进行拉黑操作\">");
		parseData(lolStruct,Type.GET_POWER,2,arg,"title='点击查看战斗力详细计算方法'");
		parseData(lolStruct,Type.GET_MODE,1,arg,lolStruct.mode_per.title);
		parseData(lolStruct,Type.GET_MODE,1,arg,lolStruct.mode_cp.title);
		parseData(lolStruct,Type.GET_MODE,1,arg,lolStruct.mode_mt.title);
		parseData(lolStruct,Type.GET_RECENT_RACE,3,arg,"<h3>最近比赛</h3>");
		content +="被赞"+lolStruct.getGood+"次";
		content +="\n"+"被拉黑"+lolStruct.getBad+"次";
		content +="\n"+"战力"+lolStruct.power;
		if(lolStruct.mode_cp.allCount>0)
			content +="\n"+lolStruct.mode_cp.title+"："+lolStruct.mode_cp.allCount+"场，"+lolStruct.mode_cp.winPrc+"胜率，"+lolStruct.mode_cp.goodCount+"场，"+lolStruct.mode_cp.badCount+"场";
		if(lolStruct.mode_per.allCount>0)
			content +="\n"+lolStruct.mode_per.title+"："+lolStruct.mode_per.allCount+"场，"+lolStruct.mode_per.winPrc+"胜率，"+lolStruct.mode_per.goodCount+"场，"+lolStruct.mode_per.badCount+"场";
		if(lolStruct.mode_mt.allCount>0)
			content +="\n"+lolStruct.mode_mt.title+"："+lolStruct.mode_mt.allCount+"场，"+lolStruct.mode_mt.winPrc+"胜率，"+lolStruct.mode_mt.goodCount+"场，"+lolStruct.mode_mt.badCount+"场";
		content +="\n"+"最近游戏：";
		for(int i=0;i<lolStruct.list.size();i++)
		{
			content +="\n"+ lolStruct.list.get(i).hero+","+lolStruct.list.get(i).raceMode+","+lolStruct.list.get(i).result;
		}
		return content;
	}
	public static int getDigit(String str)
	{
		int digit=0;
		String temp="";
		if(str!=null&&str.length()>0)
		{
			for(int i=0;i<str.length();i++)
				if(str.charAt(i)>=48&&str.charAt(i)<=57)
					temp+=str.charAt(i);
		}
		digit = Integer.valueOf(temp);
		return digit;
	}
	public static void parseData(LOLStruct lolStruct,Type type,int code,String arg,String arg2){
		String[] list;
		switch(code)
		{
		case 0:			
			if(arg.contains(arg2)){
				list = arg.split(arg2,2);
				if(list.length==2){
					list = list[1].split("</div>",2);
					if(list.length==2){
						if(type==Type.GET_GOOD)
							lolStruct.getGood = getDigit(list[0]);
						else
							lolStruct.getBad = getDigit(list[0]);
					}
				}
				
			}
			break;
		case 1:
			if(arg.contains("<div class=\"mod-tabs-content\">")){
				list = arg.split("<div class=\"mod-tabs-content\">",2);
				Mode mode;
				if(arg2==lolStruct.mode_cp.title)
					mode = lolStruct.mode_cp;
				else if(arg2==lolStruct.mode_per.title)
					mode = lolStruct.mode_per;
				else
					mode = lolStruct.mode_mt;
				if(list.length==2){
					list = list[1].split("<td>"+arg2+"</td>",2);
					if(list.length==2){
						list[1] = list[1].replaceAll(" ", "");
						list = list[1].split("<td>",2);
						if(list.length==2){
							list = list[1].split("</td>",2);
							if(list.length==2){
								mode.allCount = getDigit(list[0]);
								list = list[1].split("<td>",2);
								if(list.length==2){
									list = list[1].split("</td>",2);
									if(list.length==2){
										mode.winPrc = list[0];
										list = list[1].split("<td>",2);
										if(list.length==2){
											list = list[1].split("</td>",2);
											if(list.length==2){
												mode.goodCount = getDigit(list[0]);
												list = list[1].split("<td>",2);
												if(list.length==2){
													list = list[1].split("</td>",2);
													if(list.length==2){
														mode.badCount=getDigit(list[0]);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
				
			}
			break;
		case 2:
			if(arg.contains(arg2)){
				list = arg.split(arg2,2);
				if(list.length==2){
					list = list[1].split("title='更新时间",2);
					if(list.length==2){
						list = list[1].split(">",2);
						if(list.length==2){
							list = list[1].split("</span>",2);
							if(list.length==2)
								lolStruct.power = getDigit(list[0]);
						}
					}
				}
				
			}
			break;
		case 3:
			if(arg.contains(arg2)){
				list = arg.split(arg2,2);
				if(list.length==2){
					list[1] = list[1].replace(" ", "");
					list = list[1].split("</table>",2);
					if(list.length==2){
						while((list=list[0].split("title=\"",2)).length==2){
							list = list[1].split("\"",2);
							if(list.length==2){
								LastRace race = new LastRace();
								race.hero = list[0];
								if((list = list[1].split("<td>",2)).length==2)
									if((list = list[1].split("</td>",2)).length==2){
										race.raceMode=list[0];
										if((list = list[1].split("\">",2)).length==2)
											if((list = list[1].split("</",2)).length==2){
												race.result=list[0];
												list[0] = list[1];
												lolStruct.list.add(race);
											}
									}
								
									
							}
						}
					}
				}
				
			}
			break;
		}
	}
}
