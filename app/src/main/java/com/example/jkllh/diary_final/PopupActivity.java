package com.example.jkllh.diary_final;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;

public class PopupActivity extends Activity {

    TextView title;
    TextView ch;
    TextView ch0;
    EditText txtText;
    CheckBox cB1;
    CheckBox cB2;
    CheckBox cB3;
    CheckBox cB4;
    CheckBox cB5;
    CheckBox cB6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_activity);

        //UI 객체생성
        title=(TextView)findViewById(R.id.title);
        ch=(TextView)findViewById(R.id.check);
        ch0=(TextView)findViewById(R.id.check0);
        txtText = (EditText) findViewById(R.id.txtText);
        cB1=(CheckBox)findViewById(R.id.act);
        cB2=(CheckBox)findViewById(R.id.std);
        cB3=(CheckBox)findViewById(R.id.ex1);
        cB4=(CheckBox)findViewById(R.id.ex2);
        cB5=(CheckBox)findViewById(R.id.ex3);
        cB6=(CheckBox)findViewById(R.id.ex4);

        //데이터 가져오기
        Intent intent = getIntent();
        String date = intent.getStringExtra("date");
        title.setText(date);
        String data = intent.getStringExtra("data");
        txtText.setText(data);
        String checkList = intent.getStringExtra("check");

        String[] part = checkList.split(", ");
        /*ch.setText(part[0]);
        ch0.setText(part[1]);*/
        boolean[] array=new boolean[part.length];
        for (int i=0;i<part.length;i++) {
            array[i] = Boolean.parseBoolean(part[i]);
        }
        array[0]=part[0].contains("true");
        if(part.length>1) {
            array[5]=part[5].contains("true");
            //체크리스트 업로드
            cB1.setChecked(array[0]);
            cB2.setChecked(array[1]);
            cB3.setChecked(array[2]);
            cB4.setChecked(array[3]);
            cB5.setChecked(array[4]);
            cB6.setChecked(array[5]);
        }

        /*
        SharedPreferences pref=getSharedPreferences("pref",Activity.MODE_PRIVATE);

        //Preferences Load
        final Boolean c1=pref.getBoolean("c1",false);
        final Boolean c2=pref.getBoolean("c2",false);
        final Boolean c3=pref.getBoolean("c3",false);
        final Boolean c4=pref.getBoolean("c4",false);
        final Boolean c5=pref.getBoolean("c5",false);
        final Boolean c6=pref.getBoolean("c6",false);

        //Pref Apply
        cB1.setChecked(c1);
        cB2.setChecked(c2);
        cB3.setChecked(c3);
        cB4.setChecked(c4);
        cB5.setChecked(c5);
        cB6.setChecked(c6);
        */

    }
    /*public void commit() {
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        cB1=(CheckBox)findViewById(R.id.act);
        cB2=(CheckBox)findViewById(R.id.std);
        cB3=(CheckBox)findViewById(R.id.ex1);
        cB4=(CheckBox)findViewById(R.id.ex2);
        cB5=(CheckBox)findViewById(R.id.ex3);
        cB6=(CheckBox)findViewById(R.id.ex4);
        editor.putBoolean("c1",cB1.isChecked());
        editor.putBoolean("c2",cB2.isChecked());
        editor.putBoolean("c3",cB3.isChecked());
        editor.putBoolean("c4",cB4.isChecked());
        editor.putBoolean("c5",cB5.isChecked());
        editor.putBoolean("c6",cB6.isChecked());
        editor.commit();
    }*/
    //체크리스트 확인
    public boolean[] check() {

        boolean c[]=new boolean[6];
        c[0]=cB1.isChecked();
        c[1]=cB2.isChecked();
        c[2]=cB3.isChecked();
        c[3]=cB4.isChecked();
        c[4]=cB5.isChecked();
        c[5]=cB6.isChecked();
        return c;

    }
    //확인 버튼 클릭
    public void mOnClose(View v){
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", txtText.getText().toString());
        setResult(RESULT_OK, intent);
        intent.putExtra("check",check());
        //Pref Send
        //commit();
        //액티비티(팝업) 닫기
        finish();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE) return false;
        return true;
    }
    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}