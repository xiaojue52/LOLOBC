package com.wu.lolobc;

import java.util.HashMap;
import java.util.Map;

import com.wu.lolobc.comstant.CallBack;
import com.wu.lolobc.comstant.Constant;
import com.wu.lolobc.comstant.Constant.ServerMap;
import com.wu.lolobc.comstant.WorkRunnable;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {
	private TextView result;
	private Spinner serverName;
	private EditText playerName;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.result = (TextView)this.findViewById(R.id.result);
		this.serverName = (Spinner)this.findViewById(R.id.serverName);
		this.playerName = (EditText)this.findViewById(R.id.playerName);
		String[] items = ServerMap.name;
		ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,items);
		this.serverName.setAdapter(adapter);
		this.serverName.setSelection(0);
		
	}
	Handler handler =  new Handler(){
		@Override
		public void handleMessage(Message msg){
			Bundle data = msg.getData();
			String content = data.getString("result");
			if(content==null)
				result.setText("服务器异常");
			else if(content.contains("暂无数据"))
				result.setText("暂无数据");
			else{
				result.setText(Constant.parseContent(content));
			}
			pd.dismiss();
		}
		
	};
	private CallBack callBack = new CallBack(){

		@Override
		public void downloadResult(String content) {
			// TODO Auto-generated method stub
			Message msg = new Message();
			Bundle data = new Bundle();
			data.putString("result", content);
			msg.setData(data);
			handler.sendMessage(msg);
		}
		
	};
	public void click(View v){
		switch(v.getId())
		{
		case R.id.search:
			this.result.setText("");
			String url = "http://lolbox.duowan.com/playerDetail.php";
			Map<String,String> pars = new HashMap<String,String>();
			String serverName = ServerMap.server[this.serverName.getSelectedItemPosition()];
			String playerName = this.playerName.getText().toString();
			pars.put("serverName", serverName);
			pars.put("playerName", playerName);
			
			System.out.print(serverName+","+playerName);
			pd = ProgressDialog.show(this, "正在查询", "请稍后。。。。。。");
			new Thread(new WorkRunnable(url,pars,callBack)).start();
			break;
		}
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {

			// 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
			View v = getCurrentFocus();

			if (isShouldHideInput(v, ev)) {
				hideSoftInput(v.getWindowToken());
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
	 * 
	 * @param v
	 * @param event
	 * @return
	 */
	private boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] l = { 0, 0 };
			v.getLocationInWindow(l);
			int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
					+ v.getWidth();
			if (event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom) {
				// 点击EditText的事件，忽略它。
				return false;
			} else {
				return true;
			}
		}
		// 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
		return false;
	}

	/**
	 * 多种隐藏软件盘方法的其中一种
	 * 
	 * @param token
	 */
	private void hideSoftInput(IBinder token) {
		if (token != null) {
			InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			im.hideSoftInputFromWindow(token,
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
}
