package com.saberix.mymoney;

import android.content.Context;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MyMoneyTools {
    public static String getDataFromFileAt(int pos, String filename, Context ctx) {
        String data = null;
        try (
                FileInputStream fin = ctx.openFileInput(filename);
                DataInputStream din = new DataInputStream(fin)) {
            for (int i = 0; i < pos; i++) {
                data = din.readUTF();
            }
        } catch (IOException e) {
            Toast.makeText(ctx, e.toString(), Toast.LENGTH_SHORT).show();
        }
        return data;
    }

    public static void setDataInFileAt(int pos, String filename, String Data, Context ctx) {
        try (FileInputStream fin = ctx.openFileInput(filename);
             DataInputStream din = new DataInputStream(fin)) {

            String arr[] = new String[5];
            for (int i = 0; i < 5; i++) {
                arr[i] = din.readUTF();
            }

            ctx.openFileOutput(filename, Context.MODE_PRIVATE).close();
            FileOutputStream fout = ctx.openFileOutput(filename, Context.MODE_APPEND);
            DataOutputStream dout = new DataOutputStream(fout);

            for (int j = 1; j <= 5; j++) {
                if (j == pos) {
                    dout.writeUTF(Data);
                } else {
                    dout.writeUTF(arr[j - 1]);
                }
            }
            dout.close();
            fout.close();
        } catch (IOException e) {
            Toast.makeText(ctx, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
