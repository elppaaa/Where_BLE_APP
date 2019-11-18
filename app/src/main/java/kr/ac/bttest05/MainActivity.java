package kr.ac.bttest05;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.widget.Toast;

import static kr.ac.bttest05.Constants.*;

public class MainActivity extends AppCompatActivity {
    //// GUI variables
    // text view for status
    private TextView tv_status_;
    // text view for read
    private TextView tv_read_;
    // button for start scan
    private Button btn_scan_;
    // button for stop connection
    private Button btn_stop_;
    // button for send data
    private Button btn_send_;
    // button for show paired devices
    private Button btn_show_;

    private EditText console;

    private TextView logview;

    private Button btnCall1, btnCall2, btnCall3;






    // used to identify adding bluetooth names

    // ble adapter
    private BluetoothAdapter ble_adapter_;
    // flag for scanning
    private boolean is_scanning_ = false;
    // flag for connection
    private boolean connected_ = false;
    // scan results
    private Map<String, BluetoothDevice> scan_results_;
    // scan callback
    private ScanCallback scan_cb_;
    // ble scanner
    private BluetoothLeScanner ble_scanner_;
    // scan handler
    //private Handler scan_handler_;

    //private BluetoothGatt ble_gatt_;
    //Map<MAC, BluetoothGatt
    static Map<String, BluetoothGatt> connectedDeviceList = new HashMap<>();

    //Device On/Off toggle
    static String toggle_A = "A";

    Menu menu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_actions,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Handle presses on the action bar
        switch (item.getItemId()) {
            case R.id.action_setting:
                startActivity(new Intent(MainActivity.this, DeviceSetting.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //// get instances of gui objects
        // status textview
        tv_status_ = findViewById(R.id.tv_status);
        // read textview
        tv_read_ = findViewById(R.id.tv_read);
        // scan button
        btn_scan_ = findViewById(R.id.btn_scan);
        // stop button
        btn_stop_ = findViewById(R.id.btn_stop);
        // send button
        btn_send_ = findViewById(R.id.btn_send);
        // show button
        btn_show_ = findViewById(R.id.btn_show);

        logview = findViewById(R.id.logview);
        logview.setMovementMethod(ScrollingMovementMethod.getInstance());
        console = findViewById(R.id.console);

        btnCall1 = findViewById(R.id.btnCall1);


        /* BLE Manager */
        BluetoothManager ble_manager;
        ble_manager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        //set ble adapter
        ble_adapter_ = ble_manager.getAdapter();

        DeviceList.put(MAC_ADDR1, "봉숙");

        new Thread(() -> {
            startScan();
        }).start();

        new Thread(() -> {
            while (true) {

                if (connectedDeviceList.size() < 3) {
                    if(connectedDeviceList.size() == 0){
                    }
                    startScan();
                } else {
                    stopScan();
                }
                for (BluetoothGatt _gatt : connectedDeviceList.values()) {
                    logview.append("\n\n혹시 여기 넘어오나요.... ㄷ ㄷ \n\n");
                    if (ble_manager.getConnectionState(_gatt.getDevice(), BluetoothProfile.GATT) == BluetoothProfile.STATE_DISCONNECTED) {
                        logview.append("remove : " + _gatt.getDevice().getAddress() + "\n");
                        connectedDeviceList.remove(_gatt.getDevice().getAddress());

                    }
                }

                SystemClock.sleep(60000);
            }
        }).start();


        btn_scan_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startScan();
                logview.append("Scan버튼 누름 작동 X\n");
                return;

            }
        });

        btnCall1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData(connectedDeviceList.get(MAC_ADDR1), toggle_A);

                if(toggle_A == "A") {
                    toggle_A = "B";
                } else {
                    toggle_A = "A";
                }
            }
        });



    }


    /*
    send data
     */
    private void sendData(BluetoothGatt bts, String _str) {
        //check connection
        if (!connected_) {
            Toast.makeText(getApplicationContext(), "Failed to send data -> no connection", Toast.LENGTH_SHORT).show();
            return;
        }



        /*

        BluetoothGattCharacteristic cmd_characteristic = BluetoothUtils.findCommandCharacteristic(ble_gatt_);
        if (cmd_characteristic == null) {
            logview.append("cmd_charateristic is null");
            //disconnectGattServer();
            return;
        }


        startStimulation(cmd_characteristic, 1);

         */
/*
        for(BluetoothGattCharacteristic bts : ble_gatt_.getService(SERVICE_UUID).getCharacteristics()) {
            logview.append(bts.getUuid().toString() + "\n");
        }

 */

        BluetoothGattCharacteristic btt = bts.getService(SERVICE_UUID).getCharacteristic(RX_CHAR_UUID);
        startStimulation(bts, btt, _str);


    }

    /*
    Start stimulation
    @param cmd_characteristic command characteristic instance
    @param program_id stimulation program id
     */
    private void startStimulation(BluetoothGatt ble_gatt, BluetoothGattCharacteristic _cmd_characteristic, final String _program_id) {


        // set values to the characteristic
        //_cmd_characteristic.setValue( cmd_bytes );
        _cmd_characteristic.setValue(_program_id);

        //_cmd_characteristic.setValue(new byte[]{41});
        // write the characteristic
        boolean success = ble_gatt.writeCharacteristic(_cmd_characteristic);
        // check the result
        if (success) {
            logview.append("success");
        } else {
            logview.append("failed");
        }
    }


    /*
    Start BLE Scan
     */
    private void startScan() {
        tv_status_.setText("Scanning...");
        //check ble adapter and ble enabled
        if (ble_adapter_ == null || !ble_adapter_.isEnabled()) {
            requestEnableBLE(); //BLE 실행 메소드
            tv_status_.setText("Scanning Failed: ble not enabled");
            return;
        }

        //check if location permission
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocatioonPermission();
            tv_status_.setText("Scanning Failed: no fine location permission");
            return;
        }


        /*setup scan filtesrs
        filter 수정하려면 UUID부분 수정해야함.
        filters 변수 List에 추가하는 구조로 자면 될듯./
         */
        List<ScanFilter> filters = new ArrayList<>();

        /*
        Serivce UUID 기반으로 필터링이 가능하면 아래 것 사용
        ScanFilter scan_filter = new ScanFilter.Builder().setServiceUuid(ParcelUuid.fromString(SERVICE_STRING)).build();
        오류가 많아서 device 목록에서 필터링해도됨
         */
        //ScanFilter scan_filter = new ScanFilter.Builder().setDeviceAddress(MAC_ADDR).build();
        //filters.add(scan_filter);

        /*
        scan setting low_power
         */
        ScanSettings settings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_POWER).build();

        /*
        scanresult
         */
        scan_results_ = new HashMap<>();
        scan_cb_ = new BLEScanCallback(scan_results_);


        /*
        일단은 scan filter 없앰
        */
        ble_scanner_ = ble_adapter_.getBluetoothLeScanner();
        ble_scanner_.startScan(null, settings, scan_cb_);
        is_scanning_ = true;

        //scan_handler_ = new Handler();
        //scan_handler_.postDelayed(this::stopScan, SCAN_PERIOD);
    } //startScan

    /*
    Stop Scanning
     */
    private void stopScan() {
        if (is_scanning_ && ble_adapter_ != null && ble_adapter_.isEnabled() && ble_scanner_ != null) {
            ble_scanner_.stopScan(scan_cb_);
            //scanComplete();
        }
        ble_scanner_.stopScan(scan_cb_);
        //reset flags
        scan_cb_ = null;
        is_scanning_ = false;
        //scan_handler_ = null;
        //update the status
        tv_status_.setText("Scanning Stop");
    } //stopScan()

    /*
    Handle scan result after scan stopped
     */
    private void scanComplete() {
        //check if nothing found
        if (scan_results_.isEmpty()) {
            tv_status_.setText("scan result is empty");
            return;
        }
        //lop over the scan results and connect to them
        for (String device_addr : scan_results_.keySet()) {
            //get device instance using its MAC address
            BluetoothDevice device = scan_results_.get(device_addr);
            connectDevice(device);
/*
            if (MAC_ADDR.equals(device_addr)) {
                connectDevice(device);
            }

 */


        }
    }

    /*
    connect to the ble device
     */
    private void connectDevice(BluetoothDevice _device) {
        //update the status
        //tv_status_.setText("Connecting to " + _device.getAddress());
        logview.append(_device.getAddress() + "  connect시도중 \n");
        GattClientCallback gatt_client_cb = new GattClientCallback();

        /*
        GattClientCallback에서ㅓ BluetoothProfile이 SUCCESS로 넘어올  때, connectedDeviceList.put
        안될 시에,   여기 메소드(connectDevice)에서  connectGatt의 리턴을 connectedDeviceList.put할 예정
         */
        _device.connectGatt(this, true, gatt_client_cb);
    }


    /*
    Gatt Client callback class
     */
    private class GattClientCallback extends BluetoothGattCallback {
        @Override
        public void onConnectionStateChange(BluetoothGatt _gatt, int _status, int _new_state) {
            super.onConnectionStateChange(_gatt, _status, _new_state);
            if (_status == BluetoothGatt.GATT_FAILURE) {
                Toast.makeText(getApplicationContext(), "GATT_FAILURE", Toast.LENGTH_SHORT).show();
                //disconnectGattServer();
                return;
            } else if (_status != BluetoothGatt.GATT_SUCCESS) {
                Toast.makeText(getApplicationContext(), "GATT_FAILURE", Toast.LENGTH_SHORT).show();
                //disconnectGattServer();
                return;
            }
            if (_new_state == BluetoothProfile.STATE_CONNECTED) {
                logview.append(_gatt.getDevice().getAddress() + "   연결됨 \n");
                connectedDeviceList.put(_gatt.getDevice().getAddress(), _gatt);
                connected_ = true;
                Log.d(TAG, "Connected to the GATT server");
                _gatt.discoverServices();
            } else if (_new_state == BluetoothProfile.STATE_DISCONNECTED) {

                //disconnectGattServer();
            }
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
            Toast.makeText(getApplicationContext(), "onReliableWirtecomplie", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt _gatt, int _status) {
            super.onServicesDiscovered(_gatt, _status);
            // check if the discovery failed
            if (_status != BluetoothGatt.GATT_SUCCESS) {
                Log.e(TAG, "Device service discovery failed, status: " + _status);
                return;
            }
            // find discovered characteristics
            List<BluetoothGattCharacteristic> matching_characteristics = BluetoothUtils.findBLECharacteristics(_gatt);
            if (matching_characteristics.isEmpty()) {
                Log.e(TAG, "Unable to find characteristics");
                return;
            }
            // log for successful discovery
            Log.d(TAG, "Services discovery is successful");
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt _gatt, BluetoothGattCharacteristic _characteristic) {
            super.onCharacteristicChanged(_gatt, _characteristic);

            Log.d(TAG, "characteristic changed: " + _characteristic.getUuid().toString());
            readCharacteristic(_characteristic);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt _gatt, BluetoothGattCharacteristic _characteristic, int _status) {
            super.onCharacteristicWrite(_gatt, _characteristic, _status);
            if (_status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "Characteristic written successfully");
                Toast.makeText(getApplicationContext(), "write success", Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "Characteristic write unsuccessful, status: " + _status);
                // disconnectGattServer();
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "Characteristic read successfully");
                readCharacteristic(characteristic);
            } else {
                Log.e(TAG, "Characteristic read unsuccessful, status: " + status);
                // Trying to read from the Time Characteristic? It doesnt have the property or permissions
                // set to allow this. Normally this would be an error and you would want to:
                // disconnectGattServer();
            }
        }

        /*
        Log the value of the characteristic
        @param characteristic
         */
        private void readCharacteristic(BluetoothGattCharacteristic _characteristic) {
            byte[] msg = _characteristic.getValue();
            Log.d(TAG, "read: " + msg.toString());
        }
    }

    /*
    public void disconnectGattServer() {
        Log.d(TAG, "Closing Gatt connection");
        // reset the connection flag
        connected_ = false;
        // disconnect and close the gatt
        if (ble_gatt_ != null) {
            Toast.makeText(getApplicationContext(), "ble -> null", Toast.LENGTH_SHORT);
            ble_gatt_.disconnect();
            ble_gatt_.close();
        }
    }
     */


    private class BLEScanCallback extends ScanCallback {
        private Map<String, BluetoothDevice> cb_scan_results_;

        /*
        constructor
         */
        BLEScanCallback(Map<String, BluetoothDevice> _scan_results) {
            cb_scan_results_ = _scan_results;
        }

        @Override
        public void onScanResult(int _callback_type, ScanResult _result) {
            if (DeviceList.containsKey(_result.getDevice().getAddress())) {
                addScanResult(_result);
            }

        }

        @Override
        public void onBatchScanResults(List<ScanResult> _results) {
            for (ScanResult result : _results) {
                if (DeviceList.containsKey(result.getDevice().getAddress())) {
                    addScanResult(result);
                }
            }
        }

        @Override
        public void onScanFailed(int _error) {
            Toast.makeText(getApplicationContext(), "BLE Scan Failed ", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "BLE scan failed with code " + _error);
        }

        /*
        Add scan result
         */
        private void addScanResult(ScanResult _result) {
            logview.append("Scan Callback 시작\n");
            // get scanned device
            BluetoothDevice device = _result.getDevice();
            // get scanned device MAC address
            String device_address = device.getAddress();

            /*

            HashMap<MAC, callname>으로 이루어진 데이터셋에 해당 MAC이 있는지 확인.
            scan 결과에서 getAddress HasHMap.containKey와 비교
            no 라면 등록. connectDeviceList를 유지하고,
            connectDevice에서는
            coonectedDeviceList HashMap<MAC, BluetoothGatt>에 추가하고 connect로 가버렷.
             */

            // 기등록 기기이고, 연결된 디바이스Gatt에에 장비가 없을 경우 connect로 보냄.
            if (!connectedDeviceList.containsKey(device_address)) {
                connectDevice(device);
            }
        }


    }

    //BLECallback Class

    /*
    Request BLE Enable
     */
    private void requestEnableBLE() {
        Intent ble_enable_intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(ble_enable_intent, REQUEST_ENABLE_BT);
    }

    /*
    Fine location permission
     */
    private void requestLocatioonPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
    }


    @Override
    protected void onResume() {
        super.onResume();

        //finish app if the BLE is not supported
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            finish();
        }
    } //onResume() -> BLE not support -> finish()


}
