package com.wu.lolobc.comstant;

import java.util.ArrayList;
import java.util.List;

public class LOLStruct {

	public int level = 0;
	public int getGood = 0;
	public int getBad = 0;
	public int power = 0;
	public int hidScore = 0;
	public Mode mode_cp = new Mode();
	public Mode mode_per = new Mode();
	public Mode mode_mt = new Mode();
	public List<LastRace> list = new ArrayList<LastRace>();
	public LOLStruct(){
		mode_cp.title = "人机对战";
		mode_per.title = "经典模式";
		mode_mt.title = "大乱斗";
	}
	static class Mode{
		public String title;
		public int allCount;
		public int goodCount;
		public int badCount;
		public String winPrc;		
	}
	static class LastRace{
		public String hero;
		public String raceMode;
		public String result;
	}
}
