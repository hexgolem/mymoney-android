package com.saberix.mymoney;

import android.content.Context;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MyMoneyTools {

    /**
     * A tool to get a specific data about a user from its "phone.bin" file.
     *
     * @param pos      the position of the data i.e. 1 for name, 2 for email etc.
     * @param filename the name of the user's associated "phone.bin" file
     * @param ctx      the Context instance where this function has to be used
     * @return value of data if found, else returns null
     */
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

    /**
     * A tool to get all data about a user from its "phone.bin" file.
     *
     * @param filename the name of the user's associated "phone.bin" file
     * @param ctx      the Context instance where this function has to be used
     * @param arr      the String array(length=5) in which data will be return
     * @return true if data retrieved successfully, else return false
     */
    public static boolean getDataFromFile(String filename, Context ctx, String arr[]) {
        try (
                FileInputStream fin = ctx.openFileInput(filename);
                DataInputStream din = new DataInputStream(fin)) {
            for (int i = 0; i < 5; i++) {
                arr[i] = din.readUTF();
            }
        } catch (IOException e) {
            Toast.makeText(ctx, e.toString(), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * A tool to set a specific data about a user in its "phone.bin" file
     *
     * @param pos      the position of the data i.e. 1 for name, 2 for email etc.
     * @param filename the name of the user's associated "phone.bin" file
     * @param Data     the data to be set
     * @param ctx      the Context instance where this function has to be used
     * @return true if data set successfully, else false
     */
    public static boolean setDataInFileAt(int pos, String filename, String Data, Context ctx) {
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
            return false;
        }
        return true;
    }

    /**
     * A tool to set all new data of a user in its "phone.bin" file.
     *
     * @param filename the name of the user's associated "phone.bin" file
     * @param ctx      the Context instance where this function has to be used
     * @param arr      the String array(length=5) in which new data is stored
     * @return true if data is set successfully, else false
     */
    public static boolean setDataInFile(String filename, Context ctx, String arr[]) {
        try {
            // to clear file data
            ctx.openFileOutput(filename, Context.MODE_PRIVATE).close();

            FileOutputStream fout = ctx.openFileOutput(filename, Context.MODE_APPEND);
            DataOutputStream dout = new DataOutputStream(fout);

            for (int j = 1; j <= 5; j++) {
                dout.writeUTF(arr[j - 1]);
            }

            dout.close();
            fout.close();
        } catch (IOException e) {
            Toast.makeText(ctx, e.toString(), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * A tool to register a new phone number in "pnos.bin" file
     *
     * @param ctx   the Context instance where this function has to be used
     * @param phone the phone which has to be registered
     * @return true if phone numbered registered successfully, else false
     */
    public static boolean register_phone(Context ctx, String phone) {
        try (FileOutputStream fout = ctx.openFileOutput("pnos.bin", Context.MODE_APPEND);
             DataOutputStream dout = new DataOutputStream(fout)) {

            dout.writeUTF(phone);

        } catch (IOException e) {
            Toast.makeText(ctx, e.toString(), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * A tool to deregister a phone number from "pnos.bin" file
     *
     * @param ctx   the Context instance where this function has to be used
     * @param phone the phone which has to be deregistered
     * @return true if phone numbered deregistered successfully, else false
     */
    public static boolean deregister_phone(Context ctx, String phone) {

        boolean EOF = false;

        // create a new file name "temp.bin"
        try (FileInputStream fin = ctx.openFileInput("pnos.bin");
             DataInputStream din = new DataInputStream(fin);
             FileOutputStream fout = ctx.openFileOutput("temp.bin", Context.MODE_APPEND);
             DataOutputStream dout = new DataOutputStream(fout)) {

            // copy all data of "pnos.bin" except the number to be deregistered
            String st;
            while (!EOF) {
                try {
                    if (!phone.equals(st = din.readUTF()))
                        dout.writeUTF(st);
                } catch (EOFException e) {
                    EOF = true;
                }
            }
            // delete the file "pnos.bin" then rename "temp.bin" to "pnos.bin"
            File dir = ctx.getFilesDir();
            File file = new File(dir, "pnos.bin");
            file.delete();
            File file1 = new File(dir, "temp.bin");
            file1.renameTo(file);
        } catch (IOException e) {
            Toast.makeText(ctx, e.toString(), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * function to check whether a given phone number is valid or not
     *
     * @param phone the phone number to be checked
     * @return true if phone number is valid, else false
     */
    public static boolean isPhoneValid(String phone) {
        boolean flag = true;
        if (phone.length() != 10)
            flag = false;
        try {
            Long.parseLong(phone);
        } catch (NumberFormatException e) {
            flag = false;
        }
        return flag;
    }

    /**
     * function to check whether a given phone number is already registered
     * in the "pnos.bin" file
     *
     * @param ctx   the Context instance where this function has to be used
     * @param phone the phone number to be checked
     * @return      true if phone number registered or if some error occur, else false
     */
    public static boolean isPhoneRegistered(Context ctx, String phone) {
        boolean EOF = false;

        try (FileInputStream fin = ctx.openFileInput("pnos.bin");
             DataInputStream din = new DataInputStream(fin);) {
            while (!EOF) {
                try {
                    if (phone.equals(din.readUTF())) {
                        {
                            Toast.makeText(ctx, "Phone number already registered", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    }
                } catch (EOFException e) {
                    EOF = true;
                }
            }
        } catch (FileNotFoundException e) {
            //if no file means first user
        } catch (IOException e) {
            Toast.makeText(ctx, e.toString(), Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}
