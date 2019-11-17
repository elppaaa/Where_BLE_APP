package kr.ac.bttest05;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;


public class DeviceSetting extends Activity {
    private EditText Text1, Text2, Text3;
    private Button btnSave;


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

        //장비 부를 이름이 들어갈 예정. array[0], [1], [2] 순으로 장비 1, 2, 3
        String[] deviceList = new String[3];


        /*
        파일을 저장하고 저장된 파일에서 device MAC과, 부를 이름을 가져와서 <String, String>
        setText
         */
        readName();


        //버튼 클릭했을 때 장비이름 쓰기 수행
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveName(deviceList);
            }
        });

    }

    private String[] readName() {
        //장비이름을 가져오기.
        String[] _deviceList = new String[3];
        return _deviceList;
    }

    private void saveName(String[] _List) {
        //Text1, Text2, Text3의 name을 가져와서 파일 쓰기 구현.
    }
}
