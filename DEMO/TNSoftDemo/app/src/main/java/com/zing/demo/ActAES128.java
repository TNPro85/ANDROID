package com.zing.demo;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import java.net.URLEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ActAES128 extends ActBase {
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_aes);
        tvResult = (TextView) findViewById(R.id.tvResult);
        tvResult.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("aes128", tvResult.getText());
                clipboard.setPrimaryClip(clip);
            }
        });

        try {
            String strKey = "N2UWNwIDKg17TwsMDQ8HAQ==";
            byte[] key = Base64.decode(strKey, Base64.DEFAULT);
            byte[] iv = Base64.decode(strKey, Base64.DEFAULT);
            byte[] skey = new byte[]{55, 101, 22, 55, 2, 3, 42, 13, 123, 79, 11, 12, 13, 15, 7, 1};

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec sks = new SecretKeySpec(key, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, sks, ivParameterSpec);

            JSONObject data = new JSONObject();
            data.put("e", "test e");
            data.put("c", "test c");
            data.put("a", "test a");
            data.put("b", "test k");
            data.put("timestamp", 1200000000000L);

            byte[] encrypted = cipher.doFinal(data.toString().getBytes());
            String encryptedText = new String(Base64.encode(encrypted, Base64.DEFAULT));
            String result = data.toString() + "\n\n" + URLEncoder.encode(encryptedText, "UTF-8");
            tvResult.setText(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
