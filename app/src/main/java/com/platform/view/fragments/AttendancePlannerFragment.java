package com.platform.view.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.provider.ContactsContract;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.platform.R;
import com.platform.dao.UserAttendanceDao;
import com.platform.dao.UserCheckOutDao;
import com.platform.database.DatabaseManager;
import com.platform.models.attendance.AttendaceCheckOut;
import com.platform.models.attendance.AttendaceData;
import com.platform.models.attendance.Attendance;
import com.platform.models.attendance.Datum;
import com.platform.models.attendance.HolidayList;
import com.platform.models.attendance.MonthlyAttendance;
import com.platform.presenter.MonthlyAttendanceFragmentPresenter;
import com.platform.presenter.SubmitAttendanceFragmentPresenter;
import com.platform.presenter.SubmitAttendanceFromInnerPlanner;
import com.platform.services.AttendanceService;
import com.platform.utility.Constants;
import com.platform.utility.EventDecorator;
import com.platform.utility.GPSTracker;
import com.platform.utility.PreferenceHelper;
import com.platform.utility.Util;
import com.platform.view.activities.GeneralActionsActivity;
import com.platform.view.adapters.AttendanceAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class AttendancePlannerFragment extends Fragment implements View.OnClickListener,
        OnDateSelectedListener {

    private View plannerView;
    private boolean isDashboard;
    private boolean isMonth = true;
    private int tabClicked = -1;

    private TextView tvClickPending;
    private TextView tvClickApproved;
    private TextView tvClickRejected;
    private TextView tvAttendanceDetails;
    private TextView tvCheckInTime;
    private TextView tvCheckOutTime;
    private TextView tv_lab_total_hours;
    private Button btCheckIn, btCheckout;

    private RecyclerView rvAttendanceList;
    private RelativeLayout attendanceCardLayout;
    private RelativeLayout lyCalender;
    private LinearLayout lyCheckInOutDashboard;
    private LinearLayout lvAttendanceStatus;
    private MaterialCalendarView calendarView;



    private String status,dateTimeStamp;
    public static final String APPROVED="approve";
    public static final String PENDING="pending";
    public static final String REJECTED="rejected";

    ArrayList<String>pendingList;
    ArrayList<String>approveList;
    ArrayList<String>rejectList;


    //private List<AttendanceStatus>attendanceStatusList;
    String TAG=AttendancePlannerFragment.class.getSimpleName();
    MonthlyAttendanceFragmentPresenter monthlyAttendanceFragmentPresenter;
    private int year,month,cmonth;
    private String strLat, strLong;
    private GPSTracker gpsTracker;
    private String strAdd = "";
    private Long millis=null;
    private UserAttendanceDao userAttendanceDao;
    private UserCheckOutDao  userCheckOutDao;
    private CharSequence time;
    Long attendanceDate=null;
    String date=null;
    Date userAttendaceDate=null;
    Date currentDate=null;
    private String previousTime;
    private String attendaceType;

    public String todayAsString;
    List<AttendaceData>attendaceDataList;
    private String CHECK_IN="checkin";
    private String CHECK_OUT="checkout";
    private List<AttendaceData> getUserType;
    private List<AttendaceCheckOut>getUserCheckOutType;
    private PreferenceHelper preferenceHelper;

    SubmitAttendanceFromInnerPlanner submitAttendanceFragmentPresenter;
    private String attendaceId;
    private AlarmManager alarmManager;

    public AttendancePlannerFragment() {
        // Required empty public constructor
    }

    public void onCreate(){

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        plannerView = inflater.inflate(R.layout.fragment_attendance_planner, container, false);
        return plannerView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        createJson();


        // get attendance id

        preferenceHelper=new PreferenceHelper(getActivity());
        userAttendanceDao=DatabaseManager.getDBInstance(getActivity()).getAttendaceSchema();
        userCheckOutDao=DatabaseManager.getDBInstance(getActivity()).getCheckOutAttendaceSchema();

        alarmManager= (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        Calendar calendar=Util.setHours(16,06);

        if(calendar.getTimeInMillis()<System.currentTimeMillis()){

        }else {
            Intent callAlarmReceiver=new Intent(getActivity(), AttendanceService.class);
            callAlarmReceiver.putExtra("requestJson","");
            callAlarmReceiver.putExtra("messenger",new Messenger(handler));
            PendingIntent pendingIntent=PendingIntent.getService(getActivity(),0,callAlarmReceiver,PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
        }


        getUserType=userAttendanceDao.getUserAttendanceType(CHECK_IN,Util.getTodaysDate());
        getUserCheckOutType=userCheckOutDao.getCheckOutData(CHECK_OUT,Util.getTodaysDate());

        if(getUserType!=null&&getUserType.size()>0&&!getUserType.isEmpty()){

            btCheckIn.setBackground(getResources().getDrawable(R.drawable.bg_grey_box_with_border));
            btCheckIn.setTextColor(getResources().getColor(R.color.attendance_text_color));
            String checkInTime=getUserType.get(0).getTime();
            tvCheckInTime.setText(checkInTime);
            tvCheckInTime.setVisibility(View.VISIBLE);
        }
        else {
            // gray check out button
            btCheckout.setBackground(getResources().getDrawable(R.drawable.bg_grey_box_with_border));
            btCheckout.setTextColor(getResources().getColor(R.color.attendance_text_color));
            btCheckout.setEnabled(false);

        }

        if(getUserCheckOutType!=null&&getUserCheckOutType.size()>0&&!getUserCheckOutType.isEmpty()){

            btCheckout.setBackground(getResources().getDrawable(R.drawable.bg_grey_box_with_border));
            btCheckout.setTextColor(getResources().getColor(R.color.attendance_text_color));
            String checkOutTime=getUserCheckOutType.get(0).getTime();
            tvCheckOutTime.setText(checkOutTime);
            tvCheckOutTime.setVisibility(View.VISIBLE);
        }

    }

    private void initView() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            isDashboard = bundle.getBoolean(Constants.Planner.KEY_IS_DASHBOARD);
        }


        tv_lab_total_hours=plannerView.findViewById(R.id.tv_lab_total_hours);
        tv_lab_total_hours.setVisibility(View.INVISIBLE);

        lyCalender = plannerView.findViewById(R.id.ly_calender);
        tvCheckInTime = plannerView.findViewById(R.id.tv_check_in_time);
        tvCheckOutTime = plannerView.findViewById(R.id.tv_check_out_time);
        // lyWorkingHours = plannerView.findViewById(R.id.ly_working_hours);
        lyCheckInOutDashboard = plannerView.findViewById(R.id.ly_check_in_out_dashboard);
        lvAttendanceStatus = plannerView.findViewById(R.id.lv_attendance_status);
        rvAttendanceList = plannerView.findViewById(R.id.rv_attendance_list);
        tvClickPending = plannerView.findViewById(R.id.tv_tb_pending);
        tvClickPending.setOnClickListener(this);
        tvClickApproved = plannerView.findViewById(R.id.tv_tb_approved);
        tvClickApproved.setOnClickListener(this);
        tvClickRejected = plannerView.findViewById(R.id.tv_tb_rejected);
        tvClickRejected.setOnClickListener(this);
        btCheckIn = plannerView.findViewById(R.id.bt_check_in);
        btCheckIn.setOnClickListener(this);
        btCheckout = plannerView.findViewById(R.id.bt_checkout);
        btCheckout.setOnClickListener(this);
        tvAttendanceDetails = plannerView.findViewById(R.id.tv_attendance_details);
        tvAttendanceDetails.setOnClickListener(this);

        ImageView ivCalendarMode = plannerView.findViewById(R.id.iv_calendar_mode);
        calendarView = plannerView.findViewById(R.id.calendarView);
        ivCalendarMode.setOnClickListener(this);

        attendanceCardLayout = plannerView.findViewById(R.id.rv_card_attendance);

        setUIData();

    }

    private void setUIData() {
        if (isDashboard) {
            lvAttendanceStatus.setVisibility(View.GONE);
            rvAttendanceList.setVisibility(View.GONE);
            lyCalender.setVisibility(View.GONE);
            // lyWorkingHours.setVisibility(View.VISIBLE);
            lyCheckInOutDashboard.setVisibility(View.VISIBLE);
        } else {
            rvAttendanceList.setVisibility(View.VISIBLE);
            lvAttendanceStatus.setVisibility(View.VISIBLE);

            lyCalender.setVisibility(View.VISIBLE);
            //lyWorkingHours.setVisibility(View.VISIBLE);
            tvAttendanceDetails.setVisibility(View.GONE);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(70, 50, 70, 50);
            attendanceCardLayout.setLayoutParams(lp);


            CalendarDay calendarDay=calendarView.getCurrentDate();
            year=calendarDay.getYear();
            month=calendarDay.getMonth();
            cmonth=month+1;


            MonthlyAttendanceFragmentPresenter monthlyAttendanceFragmentPresenter=new MonthlyAttendanceFragmentPresenter(this);
            monthlyAttendanceFragmentPresenter.getMonthlyAttendance(year,cmonth);

            //attendanceListData();
        }

        isMonth = !isMonth;
        setCalendar();
    }

    public void getAttendanceInfo(MonthlyAttendance data) {
     Log.i("AttendanceData","222"+data);
     List<Datum>  dataList=data.getData();
     List<Attendance> attendanceList;
     String id,status,subModule;
     String holidayName,holidayDate;
     List<HolidayList> holidayList=null;
     SimpleDateFormat formatter=new SimpleDateFormat("yyyy/MM/dd");

       ArrayList<CalendarDay> holidays = new ArrayList<>();
       /* try {
            holidays.add(CalendarDay.from(formatter.parse("2019/06/19")));
            holidays.add(CalendarDay.from(formatter.parse("2019/06/21")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
*/

        pendingList=new ArrayList<>();
        approveList=new ArrayList<>();
        rejectList=new ArrayList<>();





     for(int i=0;i<dataList.size();i++){

         subModule=dataList.get(i).getSubModule();
         if(subModule.equalsIgnoreCase("attendance")){
             attendanceList=dataList.get(i).getAttendance();
             for(int j=0;j<attendanceList.size();j++){
                 id=attendanceList.get(j).getId();
                 status=attendanceList.get(j).getStatus();

                 if(status.equalsIgnoreCase(PENDING))
                 {

                     String number= attendanceList.get(j).getCreatedOn();
                     pendingList.add(number);
                 }
                 if(status.equalsIgnoreCase(APPROVED))
                 {

                     approveList.add(attendanceList.get(j).getCreatedOn());
                 }
                 if(status.equalsIgnoreCase(REJECTED))
                 {
                     rejectList.add(attendanceList.get(j).getCreatedOn());
                 }

             }
         }

         if(subModule.equalsIgnoreCase("holidayList")){
             holidayList=dataList.get(i).getHolidayList();
             for(int k=0;k<holidayList.size();k++){
                 id=holidayList.get(k).getId();
                 holidayName=holidayList.get(k).getName();
                 holidayDate=holidayList.get(k).getDate().getDate().getNumberLong();
                 // convert this date and add into list

                 Long TimeStamp=Long.parseLong(holidayDate);
                 Date d1 = new Date(TimeStamp);
                 java.text.DateFormat df = java.text.DateFormat.getDateTimeInstance(java.text.DateFormat.FULL, java.text.DateFormat.FULL);
                 df.setTimeZone(TimeZone.getTimeZone("UTC"));
                 //String FinalDate=df.format(d1);
                 holidays.add(CalendarDay.from(d1));
                 calendarView.addDecorator(new EventDecorator(getActivity(),
                         holidays, getResources().getDrawable(R.drawable.circle_background)));




             }

         }

     }


    }

    public void attendanceListData(String type) {

        if(type.equalsIgnoreCase(PENDING))
        {
            AttendanceAdapter adapter = new AttendanceAdapter(getActivity(),pendingList,type);
            rvAttendanceList.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvAttendanceList.setAdapter(adapter);
        }
        if(type.equalsIgnoreCase(APPROVED))
        {
            AttendanceAdapter adapter = new AttendanceAdapter(getActivity(),approveList,type);
            rvAttendanceList.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvAttendanceList.setAdapter(adapter);
        }
        if(type.equalsIgnoreCase(REJECTED))
        {
            AttendanceAdapter adapter = new AttendanceAdapter(getActivity(),rejectList,type);
            rvAttendanceList.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvAttendanceList.setAdapter(adapter);
        }

    }

    @Override
    public void onClick(View v) {
        Date d = new Date();
        Timestamp ts=new Timestamp(d.getTime());
        String pattern = "MM/dd/yyyy HH:mm:ss";
        SimpleDateFormat df = new SimpleDateFormat(pattern);
// Get the today date using Calendar object.
        Date today = Calendar.getInstance().getTime();
// Using DateFormat format method we can create a string
// representation of a date with the defined format.
        todayAsString = df.format(today);
// Print it!
        Long epoch=Util.getDateInLong(todayAsString);
        Log.i("Epoch","111"+epoch);
        time = DateFormat.format(Constants.TIME_FORMAT, d.getTime());
        Log.i("Check time","111"+time);

        switch (v.getId()) {
            case R.id.bt_check_in:

                tvCheckInTime.setVisibility(View.VISIBLE);
                gpsTracker = new GPSTracker(getActivity());

                String attendace_id=preferenceHelper.getString(Constants.KEY_ATTENDANCDE);

                if(attendace_id!=null&&attendace_id.length()>0){
                  Toast.makeText(getActivity(),"User already check in",Toast.LENGTH_LONG).show();
                }
                else {
                    if (gpsTracker.isGPSEnabled(getActivity(), this)) {
                        if (!gpsTracker.canGetLocation()) {
                            Toast.makeText(getActivity(),"Unable to get lat and log",Toast.LENGTH_LONG).show();
                        }
                        getLocation();
                        getCompleteAddressString(Double.parseDouble(strLat),Double.parseDouble(strLong));
                        millis=getLongFromDate();
                        if(!Util.isConnected(getActivity())){

                            // offline storage
                            //userAttendanceDao=DatabaseManager.getDBInstance(getActivity()).getAttendaceSchema();
                            // offline save
                            getUserType=userAttendanceDao.getUserAttendanceType(CHECK_IN, Util.getTodaysDate());
                            if(getUserType!=null&&getUserType.size()>0&&!getUserType.isEmpty())
                            {
                                Toast.makeText(getActivity(),"Already check in",Toast.LENGTH_LONG).show();
                            }
                            else {
                                AttendaceData attendaceData=new AttendaceData();
                                attendaceData.setUid("000");
                                Double lat=Double.parseDouble(strLat);
                                Double log=Double.parseDouble(strLong);
                                attendaceData.setLatitude(lat);
                                attendaceData.setLongitude(log);
                                attendaceData.setAddress(strAdd);
                                attendaceData.setAttendaceDate(millis);
                                attendaceData.setAttendanceType(CHECK_IN);
                                attendaceData.setTime(String.valueOf(time));
                                attendaceData.setSync(false);
                                attendaceData.setAttendanceFormattedDate(Util.getTodaysDate());
                                userAttendanceDao.insert(attendaceData);


                                tvCheckInTime.setText(time);
                                btCheckIn.setBackground(Objects.requireNonNull(getActivity())
                                        .getResources().getDrawable(R.drawable.bg_grey_box_with_border));
                                btCheckIn.setTextColor(getActivity().getResources().getColor(R.color.attendance_text_color));

                                enableCheckOut();

                                Log.i("OfflineStorage","111"+attendaceData);
                            }


                        }
                        else {
                            Util.showSimpleProgressDialog(getActivity(),"Attendance","Loading...",false);
                            submitAttendanceFragmentPresenter=new SubmitAttendanceFromInnerPlanner(this);
                            submitAttendanceFragmentPresenter.markAttendace(CHECK_IN.toLowerCase(),millis,time.toString(),"",strLat,strLong,strAdd);
                        }


                    }else {
                        gpsTracker.showSettingsAlert();
                    }
                }


                // show time
                break;

            case R.id.bt_checkout:

                markCheckOut();

                break;

            case R.id.tv_attendance_details:
                Intent intent = new Intent(getActivity(),GeneralActionsActivity.class);
                intent.putExtra(Constants.Planner.KEY_IS_DASHBOARD, false);
                intent.putExtra("title", getString(R.string.attendance));
                intent.putExtra("switch_fragments", "AttendancePlannerFragment");
                startActivity(intent);
                break;

            case R.id.tv_tb_pending:
                if (tabClicked != 1) {
                    tabClicked = 1;
                    tvClickPending.setTextColor(getResources().getColor(R.color.black_green));
                    tvClickApproved.setTextColor(getResources().getColor(R.color.blur_tab));
                    tvClickRejected.setTextColor(getResources().getColor(R.color.blur_tab));
                }
                attendanceListData(PENDING);


                break;

            case R.id.tv_tb_approved:
                if (tabClicked != 2) {
                    tabClicked = 2;
                    tvClickPending.setTextColor(getResources().getColor(R.color.blur_tab));
                    tvClickApproved.setTextColor(getResources().getColor(R.color.black_green));
                    tvClickRejected.setTextColor(getResources().getColor(R.color.blur_tab));

                }
                attendanceListData(APPROVED);
                break;

            case R.id.tv_tb_rejected:
                if (tabClicked != 3) {
                    tabClicked = 3;
                    tvClickPending.setTextColor(getResources().getColor(R.color.blur_tab));
                    tvClickApproved.setTextColor(getResources().getColor(R.color.blur_tab));
                    tvClickRejected.setTextColor(getResources().getColor(R.color.black_green));
                }
                attendanceListData(REJECTED);
                break;

            case R.id.iv_calendar_mode:
                isMonth = !isMonth;
                setCalendar();
                break;
        }
    }

    private void markCheckOut()  {

        boolean isAlreadyCheckOut=false;
        tvCheckOutTime.setVisibility(View.VISIBLE);

     /*   btCheckout.setBackground(Objects.requireNonNull(getActivity())
                .getResources().getDrawable(R.drawable.bg_grey_box_with_border));
        btCheckout.setTextColor(getActivity().getResources().getColor(R.color.attendance_text_color));*/

        gpsTracker = new GPSTracker(getActivity());
        if (gpsTracker.isGPSEnabled(getActivity(), this)) {
            if (!gpsTracker.canGetLocation()) {
                gpsTracker.showSettingsAlert();
            }
            getLocation();
            getCompleteAddressString(Double.parseDouble(strLat),Double.parseDouble(strLong));
            millis=getLongFromDate();
            if(!Util.isConnected(getActivity())){

                getUserCheckOutType=userCheckOutDao.getCheckOutData(CHECK_OUT,Util.getTodaysDate());

                if(getUserCheckOutType!=null&&getUserCheckOutType.size()>0&&!getUserCheckOutType.isEmpty()){
                    Toast.makeText(getActivity(),"User already check out",Toast.LENGTH_LONG).show();
                }
                else {
                    AttendaceCheckOut attendaceData=new AttendaceCheckOut();
                    attendaceData.setUid("000");
                    Double lat=Double.parseDouble(strLat);
                    Double log=Double.parseDouble(strLong);
                    attendaceData.setLatitude(lat);
                    attendaceData.setLongitude(log);
                    attendaceData.setAddress(strAdd);
                    attendaceData.setAttendaceDate(millis);
                    attendaceData.setAttendanceType(CHECK_OUT);
                    attendaceData.setTime(String.valueOf(time));
                    attendaceData.setSync(false);
                    attendaceData.setAttendanceFormattedDate(Util.getTodaysDate());

                    try {
                        attendaceData.setTotalHrs(getTotalHours());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    userCheckOutDao.insert(attendaceData);

                    btCheckout.setBackground(Objects.requireNonNull(getActivity())
                            .getResources().getDrawable(R.drawable.bg_grey_box_with_border));
                    btCheckout.setTextColor(getActivity().getResources().getColor(R.color.attendance_text_color));

                    tvCheckOutTime.setText(time);

                }

            }else {
                attendaceId = userAttendanceDao.getUserId(Util.getTodaysDate());
                Util.showSimpleProgressDialog(getActivity(),"Attendance","Loading...",false);
                submitAttendanceFragmentPresenter=new SubmitAttendanceFromInnerPlanner(this);
                submitAttendanceFragmentPresenter.markOutAttendance(attendaceId,CHECK_OUT.toLowerCase(),millis,strLat,strLong);
            }


        }else {
            gpsTracker.showSettingsAlert();
        }

    }

    private void setCalendar() {
        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        Calendar instance = Calendar.getInstance();
        calendarView.setSelectedDate(instance.getTime());

        Calendar instance1 = Calendar.getInstance();
        instance1.set(instance.get(Calendar.YEAR), Calendar.JANUARY, 1);

        if (isMonth) {
            calendarView.state().edit()
                    .setMinimumDate(instance1.getTime())
                    .setCalendarDisplayMode(CalendarMode.MONTHS)
                    .commit();
        } else {
            calendarView.state().edit()
                    .setMinimumDate(instance1.getTime())
                    .setCalendarDisplayMode(CalendarMode.WEEKS)
                    .commit();
        }

        calendarView.setSelectedDate(instance.getTime());
        calendarView.setCurrentDate(instance.getTime());
        highlightDates();

    }

    private void highlightDates() {
        // set the date list to highlight
        ArrayList<CalendarDay> dateList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        //String reg_date = formatter.format(cal.getTime());

        cal.add(Calendar.DATE, 2);
        try {
            dateList.add(CalendarDay.from(formatter.parse(formatter.format(cal.getTime()))));
        } catch (ParseException e) {
            Log.e("TAG", e.getMessage());
        }
        cal.add(Calendar.DATE, 3);
        try {
            dateList.add(CalendarDay.from(formatter.parse(formatter.format(cal.getTime()))));
        } catch (ParseException e) {
            Log.e("TAG", e.getMessage());
        }
        //noinspection deprecation
        calendarView.addDecorator(new EventDecorator(getActivity(),
                dateList, getResources().getDrawable(R.drawable.circle_background)));
        // list for holiday

       /* ArrayList<CalendarDay> holidayList = new ArrayList<>();
        try {
            holidayList.add(CalendarDay.from(formatter.parse("2019/06/19")));
            holidayList.add(CalendarDay.from(formatter.parse("2019/06/21")));
        } catch (ParseException e) {
            e.printStackTrace();
        }*/



        /*calendarView.addDecorator(new EventDecorator(getActivity(),
                holidayList, getResources().getDrawable(R.drawable.circle_background)));
*/



    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView,
                               @NonNull CalendarDay calendarDay, boolean b) {

        Toast.makeText(getActivity(), "date:" + calendarDay, Toast.LENGTH_SHORT).show();
    }

    public void showError(String error){

        //Toast.makeText(getActivity(),"User Already Checked In",Toast.LENGTH_LONG).show();

    }

    public void createJson()
    {
        HashMap<String,String>checkinMap=new HashMap<String,String>();
        checkinMap.put("checin","9:30 ");
        checkinMap.put("checkout","6:30");
        JSONObject obj=new JSONObject(checkinMap);
        Log.i("CheckIn","111"+obj);

    }

    public void getLocation(){
        Location location = gpsTracker.getLocation();

        if (location != null) {
            strLat = String.valueOf(location.getLatitude());
            strLong = String.valueOf(location.getLongitude());
        } else {
            strLat = gpsTracker.getLatitude();
            strLong = gpsTracker.getLongitude();
        }
        Log.i("latLong","222"+strLat+":-"+strLong);


    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My address", strReturnedAddress.toString());
            } else {
                Log.w("My address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My address", "Canont get Address!");
        }
        Log.i("Address","222"+strAdd);
        return strAdd;
    }

    public Long getLongFromDate(){
        Long millis=null;
        String pattern = "yyyy/MM/dd HH:mm:ss";

// Create an instance of SimpleDateFormat used for formatting
// the string representation of date according to the chosen pattern
        SimpleDateFormat df = new SimpleDateFormat(pattern);

// Get the today date using Calendar object.
        Date today = Calendar.getInstance().getTime();


// Using DateFormat format method we can create a string
// representation of a date with the defined format.
        String todayAsString = df.format(today);

// Print it!
        System.out.println("Today is: " + todayAsString);

        try {
            millis = new SimpleDateFormat("yyyy/MM/dd").parse(todayAsString).getTime();
            Log.i("Epoch","111"+millis);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return millis;


    }
    public void checkInResponse(String response){

        Util.removeSimpleProgressDialog();
        getUserType=userAttendanceDao.getUserAttendanceType(CHECK_IN, Util.getTodaysDate());
        if(getUserType!=null&&getUserType.size()>0&&!getUserType.isEmpty()){
            Toast.makeText(getActivity(),"User Already check in",Toast.LENGTH_LONG).show();
        }
        else {
            String attendanceId;
            int status;
            try{

                JSONObject jsonObject=new JSONObject(response);
                status= jsonObject.getInt("status");
                if(status==300){
                    Toast.makeText(getActivity(), "Today is holiday you cant login", Toast.LENGTH_SHORT).show();
                }else {
                    JSONObject jsonData=jsonObject.getJSONObject("data");
                    attendanceId=jsonData.getString("attendanceId");

                    AttendaceData attendaceData=new AttendaceData();
                    attendaceData.setUid(attendanceId);
                    Double lat=Double.parseDouble(strLat);
                    Double log=Double.parseDouble(strLong);
                    attendaceData.setLatitude(lat);
                    attendaceData.setLongitude(log);
                    attendaceData.setAddress(strAdd);
                    attendaceData.setAttendaceDate(millis);
                    attendaceData.setAttendanceType(CHECK_IN);
                    attendaceData.setTime(String.valueOf(time));
                    attendaceData.setSync(true);
                    attendaceData.setAttendanceFormattedDate(Util.getTodaysDate());
                    tvCheckInTime.setText(time);
                    userAttendanceDao.insert(attendaceData);
                    Log.i("Online","111"+attendaceData);

                    btCheckIn.setBackground(Objects.requireNonNull(getActivity())
                            .getResources().getDrawable(R.drawable.bg_grey_box_with_border));
                    btCheckIn.setTextColor(getActivity().getResources().getColor(R.color.attendance_text_color));

                    enableCheckOut();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public void checkOutResponse(String response){
        Log.i("checkOut","111"+response);
        Util.removeSimpleProgressDialog();

        getUserCheckOutType=userCheckOutDao.getCheckOutData(CHECK_OUT,Util.getTodaysDate());

        if(getUserCheckOutType!=null&&getUserCheckOutType.size()>0&&!getUserCheckOutType.isEmpty()){
            Toast.makeText(getActivity(),"User Already Check Out",Toast.LENGTH_LONG).show();
        }else {
            AttendaceCheckOut attendaceData=new AttendaceCheckOut();
            attendaceData.setUid(attendaceId);
            Double lat=Double.parseDouble(strLat);
            Double log=Double.parseDouble(strLong);
            attendaceData.setLatitude(lat);
            attendaceData.setLongitude(log);
            attendaceData.setAddress(strAdd);
            attendaceData.setAttendaceDate(millis);
            attendaceData.setAttendanceType(CHECK_OUT);
            attendaceData.setTime(String.valueOf(time));
            attendaceData.setSync(true);
            attendaceData.setAttendanceFormattedDate(Util.getTodaysDate());
            tvCheckOutTime.setText(time);
            try {
                attendaceData.setTotalHrs(getTotalHours());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            userCheckOutDao.insert(attendaceData);

            btCheckout.setBackground(Objects.requireNonNull(getActivity())
                    .getResources().getDrawable(R.drawable.bg_grey_box_with_border));
            btCheckout.setTextColor(getActivity().getResources().getColor(R.color.attendance_text_color));

            Log.i("Online","111"+attendaceData);
        }

    }


    public void checkInError(String response){
        Toast.makeText(getActivity(),"User Already CheckIn",Toast.LENGTH_LONG).show();
        Util.removeSimpleProgressDialog();

    }
    public void checkOutError(String response){
        Toast.makeText(getActivity(),response,Toast.LENGTH_LONG).show();
        Util.removeSimpleProgressDialog();
    }
    public void enableCheckOut()
    {
        btCheckout.setBackground(getResources().getDrawable(R.drawable.bg_button_switch));
        btCheckout.setTextColor(getResources().getColor(R.color.white));
        btCheckout.setEnabled(true);
    }
    Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle= msg.getData();
            String success=bundle.getString("success");
            Log.i("Planner","111"+success);

            // clear all table values



        }
    };

    public String getTotalHours() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date startDate = null;
        try {
            startDate = simpleDateFormat.parse((String)time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date endDate = simpleDateFormat.parse((String)time);

        long difference = endDate.getTime() - startDate.getTime();
        if(difference<0)
        {
            Date dateMax = simpleDateFormat.parse("24:00");
            Date dateMin = simpleDateFormat.parse("00:00");
            difference=(dateMax.getTime() -startDate.getTime() )+(endDate.getTime()-dateMin.getTime());
        }
        int days = (int) (difference / (1000*60*60*24));
        int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
        int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
        Log.i("log_tag","Hours: "+hours+", Mins: "+min);
        String totalHrs= String.valueOf((hours)+(min));
        return totalHrs;
    }

    }



