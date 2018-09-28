package com.example.jkllh.diary_final;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
/*
깃이 병신인지 내가 병신인지 둘다 병신인지
fgergfdbcvbvcbf
 */
    DatePicker datePicker;  //  datePicker - 날짜를 선택하는 달력
    TextView sel;
    TextView tText;
    TextView check;
    Button popup;
    String fileName;   //  fileName - 돌고 도는 선택된 날짜의 파일 이름
    String fileName0;
    boolean[] checkList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        datePicker = (DatePicker) findViewById(R.id.datePicker);
        sel=(TextView)findViewById(R.id.sel);
        tText = (TextView)findViewById(R.id.tText);
        popup=(Button)findViewById(R.id.popup);
        check=(TextView)findViewById(R.id.check);
        checkList=new boolean[6];
        Calendar c = Calendar.getInstance();
        int cYear = c.get(Calendar.YEAR);
        int cMonth = c.get(Calendar.MONTH);
        int cDay = c.get(Calendar.DAY_OF_MONTH);
        sel.setText(getData());
        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 이미 선택한 날짜에 일기가 있는지 없는지 체크해야할 시간이다
                checkedDay(year,monthOfYear,dayOfMonth);
            }

        });

    }
    private void checkedDay(int year, int monthOfYear, int dayOfMonth) {

        // 받은 날짜로 날짜 보여주는
        sel.setText(year + " - " + monthOfYear + " - " + dayOfMonth);
        check.setText(Arrays.toString(checkList));

        // 파일 이름을 만들어준다. 파일 이름은 "20170318.txt" 이런식으로 나옴
        fileName = year + "" + monthOfYear + "" + dayOfMonth + ".txt";
        fileName0 = year + "" + monthOfYear + "" + dayOfMonth + "check.txt";

        // 읽어봐서 읽어지면 일기 가져오고
        // 없으면 catch 그냥 살아? 아주 위험한 생각같다..
        FileInputStream fis = null;
        FileInputStream fis0 = null;
        try {
            fis = openFileInput(fileName);
            fis0 = openFileInput(fileName0);

            byte[] fileData = new byte[fis.available()];
            byte[] fileData0 = new byte[fis0.available()];
            fis.read(fileData);
            fis0.read(fileData0);
            fis.close();
            fis0.close();

            String str = new String(fileData, "EUC-KR");
            String str0 = new String(fileData0, "EUC-KR");
            // 읽어서 토스트 메시지로 보여줌
            Toast.makeText(getApplicationContext(), "Diary Exist", Toast.LENGTH_SHORT).show();
            tText.setText(str);
            check.setText(str0);
            popup.setText("Edit");
        } catch (Exception e) { // UnsupportedEncodingException , FileNotFoundException , IOException
            // 없어서 오류가 나면 일기가 없는 것 -> 일기를 쓰게 한다.
            Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_SHORT).show();
            tText.setText("");
            check.setText("");
            popup.setText("New");
            e.printStackTrace();
        }

    }
    public String getData() {
        return new String(String.valueOf(datePicker.getYear()) +" - "+ String.valueOf(datePicker.getMonth()) +" - "+ String.valueOf(datePicker.getDayOfMonth()));
    }
    //버튼
    public void mOnPopupClick(View v){
        //데이터 담아서 팝업(액티비티) 호출
        Intent intent = new Intent(this, PopupActivity.class);
        intent.putExtra("date",this.getData());
        intent.putExtra("data",this.tText.getText().toString());
        intent.putExtra("check",check.getText().toString());
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                //데이터 받기
                String result = data.getStringExtra("result");
                boolean[] c=data.getBooleanArrayExtra("check");
                tText.setText(result);
                checkList=c;
                check.setText(Arrays.toString(c));
                saveDiary(fileName,fileName0);
            }
        }
    }
    private void saveDiary(String readDay,String readDay0) {

        FileOutputStream fos = null;
        FileOutputStream fos0 = null;

        try {
            fos = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS); //MODE_WORLD_WRITEABLE
            fos0 = openFileOutput(readDay0, MODE_NO_LOCALIZED_COLLATORS);
            String content = tText.getText().toString();
            String content0 = check.getText().toString();

            // String.getBytes() = 스트링을 배열형으로 변환?
            fos.write(content.getBytes());
            fos0.write(content0.getBytes());
            //fos.flush();
            fos.close();
            fos0.close();

            // getApplicationContext() = 현재 클래스.this ?
            Toast.makeText(getApplicationContext(), "Diary Saved", Toast.LENGTH_SHORT).show();

        } catch (Exception e) { // Exception - 에러 종류 제일 상위 // FileNotFoundException , IOException
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }
}