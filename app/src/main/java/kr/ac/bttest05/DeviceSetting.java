package kr.ac.bttest05;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static kr.ac.bttest05.Constants.*;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;



public class DeviceSetting extends Activity {
    private EditText Text1, Text2, Text3;
    private Button btnSave;
    private SharedPreferences pref;
    private SharedPreferences.Editor edit;


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
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveName();
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
