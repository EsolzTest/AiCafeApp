package com.esolz.aicafeapp.Helper;

import com.esolz.aicafeapp.Datatype.LoginDataType;
import com.esolz.aicafeapp.Datatype.SettingsDataType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ltp on 09/07/15.
 */
public class AppData {

    public static String HOST = "http://www.esolz.co.in/lab9/aiCafe/iosapp/";

    public static LoginDataType loginDataType;

    public static List<Integer> stikersHolder;

    public static String appRegId = "";

    public static boolean isChatAlive = false;

    public static ArrayList<SettingsDataType> settingsDataTypeArrayList;
}
