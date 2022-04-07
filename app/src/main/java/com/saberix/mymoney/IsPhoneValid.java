package com.saberix.mymoney;

public class IsPhoneValid {
    public static boolean check(String phone){
        boolean flag=true;
        if(phone.length()!=10)
            flag=false;
        try{
            Long.parseLong(phone);
        }
        catch (NumberFormatException e)
        {
            flag=false;
        }
        return flag;
    }
}
