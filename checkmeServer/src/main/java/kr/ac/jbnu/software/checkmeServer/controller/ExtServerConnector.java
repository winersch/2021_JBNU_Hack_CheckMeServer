package kr.ac.jbnu.software.checkmeServer.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ExtServerConnector {
    private static ExtServerConnector extServerConnector = null;

    private static final String EXT_SERVER_URL = "http://10.10.10.104:4000/pid/Auth/";

    public static ExtServerConnector getInstance() {
        if (extServerConnector == null)  {
            extServerConnector = new ExtServerConnector();
        }

        return extServerConnector;
    }

    private ExtServerConnector() {

    }

    public String checkExtServerVaccineExchange(String AuthToken) {
        try {
            URLConnection urlConnection = new URL(EXT_SERVER_URL + AuthToken).openConnection();

            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36");
            urlConnection.connect();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            StringBuilder stringBuilder = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            if (stringBuilder.toString().contains("2차접종")) {
                return "2차접종";
            } else if (stringBuilder.toString().contains("1차접종")) {
                return "1차접종";
            } else {
                return "미접종";
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "미접종";
    }
}
