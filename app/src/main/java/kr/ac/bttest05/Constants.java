package kr.ac.bttest05;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Constants {
    /*
 UUID && MAC
  */
    public final static String MAC_ADDR = "84:0D:8E:07:5A:CA";

    //Map<Mac, DeviceName>
    public final static Map<String, String> DeviceList = new HashMap<>();
    // Tag name for Log message
    public final static String TAG = "Central";

    public final static int REQUEST_ENABLE_BT = 1;
    // used to request fine location permission
    public final static int REQUEST_FINE_LOCATION = 2;
    // scan period in milliseconds
    public final static int SCAN_PERIOD = 5000;

    public static String SERVICE_STRING = "6E400001-B5A3-F393-E0A9-E50E24DCCA9E";
    public static UUID UUID_TDCS_SERVICE = UUID.fromString(SERVICE_STRING);
    public static String CHARACTERISTIC_COMMAND_STRING = "0000FFE1-0000-1000-8000-00805F9B34FB";
    public static UUID UUID_CTRL_COMMAND = UUID.fromString(CHARACTERISTIC_COMMAND_STRING);
    public static String CHARACTERISTIC_RESPONSE_STRING = "0000AAB2-F845-40FA-995D-658A43FEEA4C";
    public static UUID UUID_CTRL_RESPONSE = UUID.fromString(CHARACTERISTIC_RESPONSE_STRING);
    public static String CHARACTERRISTIC_RX_STRING = "6E400002-B5A3-F393-E0A9-E50E24DCCA9E";
    public static UUID UUID_RX = UUID.fromString(CHARACTERRISTIC_RX_STRING);
    public static String CHARACTERRISTIC_TX_STRING = "6E400003-B5A3-F393-E0A9-E50E24DCCA9E";
    public static UUID UUID_TX = UUID.fromString(CHARACTERRISTIC_TX_STRING);


    public static UUID SERVICE_UUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e");
    public static UUID RX_CHAR_UUID = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e");
    public static UUID TX_CHAR_UUID = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e");
}
