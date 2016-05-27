package com.zing.demo;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ActAES128 extends ActBase {
    Button btnEncrypt, btnDecrypt;
    TextView tvEncrypt, tvResult;
    EditText etInput;

//    String strKey = "N2UWNwIDKg17TwsMDQ8HAQ==";
    String strKey = "7f39a980096521740ca0a5f0a07bfed9";
    String encryptedString, decryptedString;
    byte[] encrypted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_aes);

        etInput = (EditText) findViewById(R.id.etInput);

        tvEncrypt = (TextView) findViewById(R.id.tvEncrypt);
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

        btnEncrypt = (Button) findViewById(R.id.btnEncrypt);
        btnEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(!TextUtils.isEmpty(etInput.getText())) {
                        String data = etInput.getText().toString();

                        byte[] key = Base64.decode(strKey, Base64.DEFAULT);
                        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                        SecretKeySpec sks = new SecretKeySpec(key, "AES");
                        IvParameterSpec ivParameterSpec = new IvParameterSpec(getIvKey(strKey));
                        cipher.init(Cipher.ENCRYPT_MODE, sks, ivParameterSpec);

                        encrypted = cipher.doFinal(data.getBytes("UTF-8"));
                        encryptedString = new String(Base64.encode(encrypted, Base64.DEFAULT));
                        tvEncrypt.setText(encryptedString);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btnDecrypt = (Button) findViewById(R.id.btnDecrypt);
        btnDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(!TextUtils.isEmpty(encryptedString)) {
                        byte[] key = Base64.decode(strKey, Base64.DEFAULT);

                        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                        SecretKeySpec sks = new SecretKeySpec(key, "AES");
                        IvParameterSpec ivParameterSpec = new IvParameterSpec(getIvKey(strKey));
                        cipher.init(Cipher.DECRYPT_MODE, sks, ivParameterSpec);

                        byte[] decrypted = cipher.doFinal(Base64.decode(encryptedString, Base64.DEFAULT));
                        decryptedString = new String(decrypted);
                        tvResult.setText(decryptedString);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private byte[] getIvKey(String sk) throws UnsupportedEncodingException {
        String key = "";
        while (key.length() < 16)
            key += sk;
        return sk.substring(0, 16).getBytes("UTF-8");
    }
}
