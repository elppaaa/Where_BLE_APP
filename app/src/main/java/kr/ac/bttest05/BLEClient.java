package kr.ac.bttest05;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.content.Context;
import android.os.Handler;

import java.util.Map;

public class BLEClient {

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
    private Handler scan_handler_;

    private BluetoothGatt ble_gatt_;


}
