package kr.ac.jbnu.software.checkmeServer.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JSONDB {
    private static JSONDB jsondb = null;

    private HashMap<String, HashMap<String, Object>> dbHashMap;
/*
    {
        "id~~~" :{ "pw" : "String",
                    "email" : "String",
                    "auth"
        timestamp
                injection
        status
                place
        authtoken
                injection
    }
    }

    DB


    id
    pw
    email
    authtoken
    timestamp
    injection
    status
    place
    authtoken
    injection
    */

    private ArrayList<HashMap<String, String>> authDBHashMap;

    /*ArrayList
    AuthToken , injection
    */
    public static JSONDB getInstance() {
        if (jsondb == null) {
            jsondb = new JSONDB();
        }

        return jsondb;
    }

    private JSONDB() {
        dbHashMap = new HashMap<String, HashMap<String, Object>>();
        authDBHashMap = new ArrayList<HashMap<String, String>>();
    }

    public HashMap<String, HashMap<String, Object>> getDbHashMap() {
        return dbHashMap;
    }

    public void setDbHashMap(HashMap<String, HashMap<String, Object>> dbHashMap) {
        this.dbHashMap = dbHashMap;
    }

    public ArrayList<HashMap<String, String>> getAuthDBHashMap() {
        return authDBHashMap;
    }

    public void setAuthDBHashMap(ArrayList<HashMap<String, String>> authDBHashMap) {
        this.authDBHashMap = authDBHashMap;
    }

    public void resetDB(){
        this.jsondb = null;
    }
}
