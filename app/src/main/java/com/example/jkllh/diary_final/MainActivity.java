package com.example.jkllh.diary_final;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    //선언 구간
    DatePicker datePicker;  //  datePicker - 날짜를 선택하는 달력
    TextView sel;           //  선택한 날짜
    TextView tText;         //  메모
    TextView check;         //  체크리스트 부울 배열. 안보임
    Button popup;           //  팝업버튼 온클릭 액티비티
    String fileName;        //  fileName - 선택된 날짜의 파일 이름. 메모 저장.
    String fileName0;       //  fileName - 선택된 날짜의 파일 이름. 체크리스트 저장.
    boolean[] checkList;    //  체크리스트 저장하는 배열.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //정의 구간
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        sel=(TextView)findViewById(R.id.sel);
        tText = (TextView)findViewById(R.id.tText);
        popup=(Button)findViewById(R.id.popup);
        check=(TextView)findViewById(R.id.check);
        checkList=new boolean[6];
        //파일 이름들은 아래 메소드들에서 정의.
        //캘린더 클래스(자바 유틸), 오늘날짜 정의
        Calendar c = Calendar.getInstance();
        int cYear = c.get(Calendar.YEAR);
        int cMonth = c.get(Calendar.MONTH);
        int cDay = c.get(Calendar.DAY_OF_MONTH);
        //오늘날짜 기본값으로 저장. 겟데이타 메소드 아래에 정의.
        sel.setText(getData());
        //달력에 오늘날짜 기본값으로 저장.
        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            //새로운 날짜 선택 시
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 이미 선택한 날짜에 일기가 있는지 체크.
                checkedDay(year,monthOfYear,dayOfMonth);
            }

        });

    }
    //날짜를 선택할 시 데이터가 있는지를 확인.
    private void checkedDay(int year, int monthOfYear, int dayOfMonth) {

        Log.d("PATH",getApplicationContext().toString());

        // 선택한 날짜로 텍스트 세팅.
        sel.setText(year + " - " + monthOfYear + " - " + dayOfMonth);
        // 선택한 날짜의 체크리스트 세팅. 안보임
        check.setText(Arrays.toString(checkList));

        // 파일 이름을 만들어준다. 파일 이름은 "20170318.txt" 이런식으로 나옴. 체크리스트는 체크 +
        fileName = year + "" + monthOfYear + "" + dayOfMonth + ".txt";
        fileName0 = year + "" + monthOfYear + "" + dayOfMonth + "check.txt";

        // 읽어봐서(try) 읽어지면 일기 가져오고 없으면 catch
        FileInputStream fis = null;
        FileInputStream fis0 = null;
        try {
            //없으면 오류발생
            fis = openFileInput(fileName);
            fis0 = openFileInput(fileName0);
            //뭔지 모르는데 뜻풀이로 대충 유추
            byte[] fileData = new byte[fis.available()];
            byte[] fileData0 = new byte[fis0.available()];
            fis.read(fileData);
            fis0.read(fileData0);
            fis.close();
            fis0.close();
            //필요할 때 사용하기 위한 파일이름들 새로운 스트링 변수로 저장? 아니면 걍 유니코드 변환인듯
            String str = new String(fileData, "EUC-KR");
            String str0 = new String(fileData0, "EUC-KR");
            // 오류가 발생하지 않을(일기가 존재하지 않을) 경우 토스트 메시지로 일기 존재 알림
            Toast.makeText(getApplicationContext(), "Diary Exist", Toast.LENGTH_SHORT).show();
            // 메모 내용 저장한 스트링으로 설정
            tText.setText(str);
            // 체크리스트 저장한 스트링으로 설정
            check.setText(str0);
            // 팝업버튼 수정으로 텍스트 설정
            popup.setText("Edit");
        } catch (Exception e) { // UnsupportedEncodingException , FileNotFoundException , IOException   각각 언제날지 생각
            // 파일이나 기타 정보가 없어서 오류가 나면 일기가 없는 것 -> 일기를 쓰게 한다.
            // 토스트 메세지로 일기 존재하지 않음 알림
            Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_SHORT).show();
            // 나머지 텍스트뷰들 공백으로 세팅
            tText.setText("");
            check.setText("");
            // 버튼 내용 새 파일로 텍스트 설정
            popup.setText("New");
            //뭔지모름
            e.printStackTrace();
        }

    }
    //날짜 보기좋은 형식의 스트링으로 반환하는 메소드
    public String getData() {
        //데이트피커에서 가져온 값들 스트링으로 변환하고 - 붙여서 반환
        return new String(String.valueOf(datePicker.getYear()) +" - "+ String.valueOf(datePicker.getMonth()) +" - "+ String.valueOf(datePicker.getDayOfMonth()));
    }
    //팝업 버튼 눌렸을때 메소드. 온클릭이 아닌 직접 이름지은 id값으로 진행함
    public void mOnPopupClick(View v){
        //데이터 담아서 팝업(액티비티) 호출 데이터 전송에 사용되는 Intent 중요
        //인텐트 정의
        Intent intent = new Intent(this, PopupActivity.class);
        //전송할 값들 인텐트에 삽입
        intent.putExtra("date",this.getData());
        intent.putExtra("data",this.tText.getText().toString());
        intent.putExtra("check",check.getText().toString());
        //아마 실행메소드
        startActivityForResult(intent, 1);
    }
    //팝업 종료버튼 눌렸을 시 데이터 전송받아서 설정.
    @Override
    //파라메터 뭐일지 확인
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //있으면
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                //데이터 받기
                String result = data.getStringExtra("result");
                boolean[] c=data.getBooleanArrayExtra("check");
                //받은 값들로 필요한 변수들 재정의.
                tText.setText(result);
                checkList=c;
                check.setText(Arrays.toString(c));
                //일기 저장하는 메소드.
                saveDiary(fileName,fileName0);
            }
        }
    }
    //제일 빢세지만 제일 중요
    private void saveDiary(String readDay,String readDay0) {
        //일기내용, 체크리스트 저장할 객체 각각 정의
        FileOutputStream fos = null;
        FileOutputStream fos0 = null;

        try {
            fos = openFileOutput(readDay, MODE_NO_LOCALIZED_COLLATORS); //MODE_WORLD_WRITEABLE 뭔지모름
            fos0 = openFileOutput(readDay0, MODE_NO_LOCALIZED_COLLATORS); //MODE_WORLD_WRITEABLE 걍 복붙
            String content = tText.getText().toString();    //일기가 있든 없든 저장된 일기 내용 호출(없으면 "")
            String content0 = check.getText().toString();   //체크리스트 내용 호출(없으면 ffffff)

            // String.getBytes() = 스트링을 배열형으로 변환? getBytes메소드 확인
            fos.write(content.getBytes());
            fos0.write(content0.getBytes());
            //fos.flush();
            fos.close();
            fos0.close();
            // 파일아웃풋스트림 종료
            // getApplicationContext() = 현재 클래스.this ?
            // 저장이 되면=오류가 없으면 일기 저장 출력
            Toast.makeText(getApplicationContext(), "Diary Saved", Toast.LENGTH_SHORT).show();
        
        } catch (Exception e) { // Exception - 에러 종류 제일 상위 // FileNotFoundException , IOException
            //뭐지
            e.printStackTrace();
            //에러발생 출력
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }
}
