package com.zing.demo;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.TextView;
import android.widget.Toast;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ActJNI extends ActBase {

    static {
        try {
            System.loadLibrary("tnprocore");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_jni);

        String sk = null;
        String iv = "";
        try {
            sk = getKey();
            iv = getIvKey();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(!TextUtils.isEmpty(sk) || !TextUtils.isEmpty(iv)) {
            String result = "SKey: " + sk + "\nIv: " + iv + "\n\n";

            try {
                byte[] key = Base64.decode(sk, Base64.DEFAULT);

                String data = "tuannh2";
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                SecretKeySpec sks = new SecretKeySpec(key, "AES");
                IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
                cipher.init(Cipher.ENCRYPT_MODE, sks, ivParameterSpec);

                byte[] encrypted = cipher.doFinal(data.getBytes("UTF-8"));
                String encryptedString = new String(Base64.encode(encrypted, Base64.DEFAULT));
                result += "Encrypted: " + encryptedString + "\n";

                cipher.init(Cipher.DECRYPT_MODE, sks, ivParameterSpec);
                byte[] decrypted = cipher.doFinal(Base64.decode(encryptedString, Base64.DEFAULT));
                String decryptedString = new String(decrypted);
                result += "Decrypted: " + decryptedString + "\n";
            } catch (Exception e) {
                e.printStackTrace();
            }

            ((TextView) findViewById(R.id.tvResult)).setText(result);
        }
        else
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
    }

    private native String getKey();
    private native String getIvKey();
}
