package com.eroad.widget.calendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.eroad.widget.calendar.CollapseCalendarView.OnDateSelect;
import com.eroad.widget.calendar.CollapseCalendarView.OnTitleClickListener;
import com.eroad.widget.calendar.manager.CalendarManager;
import com.eroad.widget.calendar.manager.CalendarManager.OnMonthChangeListener;
import com.example.calendarnew.R;
/**
 * Main
 * @author MaJian
 *
 */
public class MainActivity extends Activity {

	private CollapseCalendarView calendarView;
	private CalendarManager mManager;
	private JSONObject json;
	private SimpleDateFormat sdf;
	private boolean show = false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sdf = new SimpleDateFormat("yyyy-MM-dd");
		calendarView = (CollapseCalendarView) findViewById(R.id.calendar);
		mManager = new CalendarManager(LocalDate.now(),
				CalendarManager.State.MONTH, LocalDate.now().withYear(100),
				LocalDate.now().plusYears(100));
		//月份切换监听器
		mManager.setMonthChangeListener(new OnMonthChangeListener() {

			@Override
			public void monthChange(String month, LocalDate mSelected) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, month, Toast.LENGTH_SHORT).show();
			}

		});
		/**
		 * 日期选中监听器
		 */
		calendarView.setDateSelectListener(new OnDateSelect() {

			@Override
			public void onDateSelected(LocalDate date) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, date.toString(), Toast.LENGTH_SHORT).show();
			}
		});
		calendarView.setTitleClickListener(new OnTitleClickListener() {

			@Override
			public void onTitleClick() {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "点击标题", Toast.LENGTH_SHORT).show();
			}
		});
		//回到今天
		findViewById(R.id.btn_today).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				calendarView.changeDate(LocalDate.now().toString());
			}
		});
		//周月切换
		findViewById(R.id.btn_changemode).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mManager.toggleView();
				calendarView.populateLayout();
			}
		});
		//显示或者隐藏农历
		findViewById(R.id.btn_hide).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				calendarView.showChinaDay(show);
				show = !show;
			}
		});
		Calendar cal = Calendar.getInstance();
//		cal.set(Calendar.MONTH, 9);
//		cal.set(Calendar.DAY_OF_MONTH, 0);
		json = new JSONObject();
		try {
			for (int i = 0; i < 30; i++) {
				JSONObject jsonObject2 = new JSONObject();
				if (i <= 6) {
					jsonObject2.put("type", "休");
				} else if ( i > 6 && i< 11) {
					jsonObject2.put("type", "班");
				}
				if (i%3 == 0) {
					jsonObject2.put("list", new JSONArray());
				}

				json.put(sdf.format(cal.getTime()), jsonObject2);

				cal.add(Calendar.DAY_OF_MONTH, 1);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		//设置数据显示
		calendarView.setArrayData(json);
		//初始化日历管理器
		calendarView.init(mManager);
	}
}
