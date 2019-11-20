package kr.ac.bttest05;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static kr.ac.bttest05.Constants.*;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class DeviceSetting extends Activity {
    private EditText Text1, Text2, Text3;
    private Button btnSave;
    private SharedPreferences pref;
    private SharedPreferences.Editor edit;
    private TextToSpeech tts;


    /*
       파일 입출력 구현 예정.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        Text1 = findViewById(R.id.setting_Text1);
        Text2 = findViewById(R.id.setting_Text2);
        Text3 = findViewById(R.id.setting_Text3);
        btnSave = findViewById(R.id.setting_btnSave);
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                tts.setLanguage(Locale.KOREAN);
            }
        });
        tts.speak("설정 화면입니다.", TextToSpeech.QUEUE_FLUSH, null);


        pref = getApplicationContext().getSharedPreferences("DevList", MODE_PRIVATE);
        edit = pref.edit();

        //장비 부를 이름이 들어갈 예정. array[0], [1], [2] 순으로 장비 1, 2, 3
        String[] deviceList = new String[3];
        deviceList[0] = pref.getString(MAC_ADDR1, "");
        deviceList[1] = pref.getString(MAC_ADDR2, "");
        deviceList[2] = pref.getString(MAC_ADDR3, "");

        Text1.setText(pref.getString(MAC_ADDR1, ""));
        Text2.setText(pref.getString(MAC_ADDR2, ""));
        Text3.setText(pref.getString(MAC_ADDR3, ""));

        /*
        파일을 저장하고 저장된 파일에서 device MAC과, 부를 이름을 가져와서 <String, String>
        setText
         */


        //버튼 클릭했을 때 장비이름 쓰기 수행
        btnSave.setOnTouchListener(new View.OnTouchListener() {
            GestureDetector gd = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTapEvent(MotionEvent e) {
                    saveName();
                    tts.speak("저장되었습니다.", TextToSpeech.QUEUE_FLUSH, null);
                    finish();
                    return true;
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    tts.speak("저장 버튼", TextToSpeech.QUEUE_FLUSH, null);
                    return true;
                }

            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gd.onTouchEvent(event);
                return true;
            }
        });




        Text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.speak(Text1.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });


        Text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.speak(Text2.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        Text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.speak(Text3.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
            }
        });

    }


    void saveName() {
        //Text1, Text2, Text3의 name을 가져와서 파일 쓰기 구현.
        edit.putString(MAC_ADDR1, Text1.getText().toString());
        edit.putString(MAC_ADDR2, Text2.getText().toString());
        edit.putString(MAC_ADDR3, Text3.getText().toString());
        edit.commit();
        Toast.makeText(this, "저장 완료", Toast.LENGTH_SHORT);
    }
}
