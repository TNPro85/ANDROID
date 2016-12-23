package com.zing.demo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ActNetworkStrength extends ActBase {
    private TextView tvResult, tvLatency;
    private Button btnTestLatency;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_newwork_strength);

        tvResult = (TextView) findViewById(R.id.tvResult);
        tvLatency = (TextView) findViewById(R.id.tvLatency);
        btnTestLatency = (Button) findViewById(R.id.btnTestLatency);
        btnTestLatency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLatency();
            }
        });

//        Generally --------------------------------------------------------------------------------
//        db >= -50 db = 100% quality
//        db <= -100 db = 0% quality
//
//        For RSSI signal between -50db and -100db,
//        quality ~= 2* (db + 100)
//        RSSI ~= (percentage / 2) - 100
//
//        For example:
//        High quality:     90% ~= -55db
//        Medium quality:   50% ~= -75db
//        Low quality:      30% ~= -85db
//        Unusable quality: 8% ~= -96db

//        ------------------------------------------------------------------------------------------
//        Signal    TL;DR	 	Desc                                                Required for
//        -30 dBm	Amazing	    Max achievable signal strength. The client          N/A
//                              can only be a few feet from the AP to achieve
//                              this. Not typical or desirable in the real world.
//        -67 dBm	Very Good	Minimum signal strength for applications that       VoIP/VoWiFi,
//                              require very reliable, timely delivery of data      streaming video
//                              packets.
//        -70 dBm	Okay	    Minimum signal strength for reliable packet         Email, web
//                              delivery.
//        -80 dBm	Not Good	Minimum signal strength for basic connectivity.     N/A
//                              Packet delivery may be unreliable.
//        -90 dBm	Unusable	Approaching or drowning in the noise floor.         N/A
//                              Any functionality is highly unlikely.

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                            int linkSpeed = wifiManager.getConnectionInfo().getRssi();
                            String result = "WIFI: " + linkSpeed + " " + getWifiStrength(linkSpeed);

                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1
                                    && ContextCompat.checkSelfPermission(ActNetworkStrength.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                                List<CellInfo> listGsm = telephonyManager.getAllCellInfo();
                                if(listGsm.size() > 0) {
                                    if(listGsm.get(0) instanceof CellInfoGsm) {
                                        CellInfoGsm cellInfo = (CellInfoGsm) listGsm.get(0);
                                        CellSignalStrengthGsm cellSignalStrengthGsm = cellInfo.getCellSignalStrength();
                                        int speed = cellSignalStrengthGsm.getDbm();
                                        int gsmLevel = cellSignalStrengthGsm.getAsuLevel();
                                        result += "\nGSM: " + speed + " " + getGsmStrength(gsmLevel);
                                    }
                                    else if(listGsm.get(0) instanceof CellInfoCdma) {
                                        CellInfoCdma cellInfoCdma = (CellInfoCdma) listGsm.get(0);
                                        CellSignalStrengthCdma cellSignalStrengthCdma = cellInfoCdma.getCellSignalStrength();
                                        int speed = cellSignalStrengthCdma.getDbm();
                                        int gsmLevel = cellSignalStrengthCdma.getAsuLevel();
                                        result += "\nCDMA: " + speed + " " + getGsmStrength(gsmLevel);
                                    }
                                    else if(listGsm.get(0) instanceof CellInfoLte) {
                                        CellInfoLte cellInfoLte = (CellInfoLte) listGsm.get(0);
                                        CellSignalStrengthLte cellSignalStrengthLte = cellInfoLte.getCellSignalStrength();
                                        int speed = cellSignalStrengthLte.getDbm();
                                        int gsmLevel = cellSignalStrengthLte.getAsuLevel();
                                        result += "\nLTE: " + speed + " " + getGsmStrength(gsmLevel);
                                    }
                                    else if(listGsm.get(0) instanceof CellInfoWcdma
                                            && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                                        CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) listGsm.get(0);
                                        CellSignalStrengthWcdma cellSignalStrengthWcdma = cellInfoWcdma.getCellSignalStrength();
                                        int speed = cellSignalStrengthWcdma.getDbm();
                                        int gsmLevel = cellSignalStrengthWcdma.getAsuLevel();
                                        result += "\nWCDMA: " + speed + " " + getGsmStrength(gsmLevel);
                                    }
                                }

                            }

                            tvResult.setText(result);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }, 300, 1500);

        checkLatency();
    }

    private String getWifiStrength(int speed) {
        int level = WifiManager.calculateSignalLevel(speed, 5);

        if(level == 4)
            return "Excellent";
        else if (level == 3)
            return "Good";
        else if (level == 2)
            return "Medium";
        else if (level == 1)
            return "Low";
        else
            return "Unusable";
    }

    private String getGsmStrength(int asuLevel) {

        // ASU ranges from 0 to 31 - TS 27.007 Sec 8.5
        // asu = 0 (-113dB or less) is very weak
        // signal, its better to show 0 bars to the user in such cases.
        // asu = 99 is a special case, where the signal strength is unknown.
        if (asuLevel <= 2 || asuLevel == 99) return "SIGNAL_STRENGTH_NONE_OR_UNKNOWN";
        else if (asuLevel >= 12) return "SIGNAL_STRENGTH_GREAT";
        else if (asuLevel >= 8)  return "SIGNAL_STRENGTH_GOOD";
        else if (asuLevel >= 5)  return "SIGNAL_STRENGTH_MODERATE";
        else return "SIGNAL_STRENGTH_POOR";
    }

    private boolean isTestingLatency;
    private void checkLatency() {
        if(isTestingLatency) return;

        isTestingLatency = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(!isFinishing()) {
                        final Ping p = ping(new URL("https://www.google.com.vn/"), ActNetworkStrength.this);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String latency = "cnt: " + p.cnt
                                        +"\ndns:" + p.dns
                                        +"\nhost:" + p.host
                                        +"\nip:" + p.ip
                                        +"\nnet:" +p.net;

                                if(tvLatency != null)
                                    tvLatency.setText(latency);

                                isTestingLatency = false;
                            }
                        });
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    isTestingLatency = false;
                }
            }
        }).start();

//        try {
//            long BeforeTime = System.currentTimeMillis();
//            URL u = new URL("http://www.google.com.vn");
//            HttpURLConnection httpUrlConnection = (HttpURLConnection) u.openConnection();
//            httpUrlConnection.setUseCaches(true);
//            httpUrlConnection.setConnectTimeout(30000);
//            httpUrlConnection.setReadTimeout(30000);
//            try {
//                httpUrlConnection.setRequestProperty("device_version", String.valueOf(Build.VERSION.SDK_INT));
//                httpUrlConnection.setRequestProperty("device_model", String.valueOf(Build.MODEL));
//                httpUrlConnection.setRequestProperty("device_brand", String.valueOf(Build.BRAND));
//                httpUrlConnection.setRequestProperty("device_manufacturer", String.valueOf(Build.MANUFACTURER));
//                httpUrlConnection.setRequestProperty("device_name", String.valueOf(Build.DEVICE));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            long AfterTime = System.currentTimeMillis();
//            final Long TimeDifference = AfterTime - BeforeTime;
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(ActNetworkStrength.this, TimeDifference + "", Toast.LENGTH_LONG).show();
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(timer != null) {
            timer.purge();
            timer.cancel();
            timer = null;
        }
    }

    public static class Ping {
        public String net = "NO_CONNECTION";
        public String host;
        public String ip;
        public int dns = Integer.MAX_VALUE;
        public int cnt = Integer.MAX_VALUE;
    }

    public Ping ping(URL url, Context ctx) {
        Ping r = new Ping();
        if (isNetworkConnected(ctx)) {
            r.net = getNetworkType(ctx);
            try {
                String hostAddress;
                long start = System.currentTimeMillis();
                hostAddress = InetAddress.getByName(url.getHost()).getHostAddress();
                long dnsResolved = System.currentTimeMillis();
                Socket socket = new Socket(hostAddress, 80);
                socket.close();
                long probeFinish = System.currentTimeMillis();
                r.dns = (int) (dnsResolved - start);
                r.cnt = (int) (probeFinish - dnsResolved);
                r.host = url.getHost();
                r.ip = hostAddress;
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return r;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Nullable
    public static String getNetworkType(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            return activeNetwork.getTypeName();
        }
        return null;
    }
}
