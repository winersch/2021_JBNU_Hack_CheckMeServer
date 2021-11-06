package kr.ac.jbnu.software.checkmeServer.controller;

import kr.ac.jbnu.software.checkmeServer.model.JSONDB;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MainController {
    @RequestMapping(value = "/db/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getAllresponseEntity(HttpServletRequest request) {
        ResponseEntity<?> responseEntity = null;

        JSONDB jsondb = JSONDB.getInstance();

        if (!jsondb.getDbHashMap().isEmpty()) {
            responseEntity = new ResponseEntity<>(jsondb.getDbHashMap(), HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    @RequestMapping(value = "/rpi/isHaveAuth", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getuserAuthresponseEntity(HttpServletRequest request) {
        ResponseEntity<?> responseEntity = null;

        JSONDB jsondb = JSONDB.getInstance();

        if (!jsondb.getAuthDBHashMap().isEmpty()) {
            responseEntity = new ResponseEntity<>(jsondb.getAuthDBHashMap(), HttpStatus.OK);

        } else {
            responseEntity = new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
        }

        return responseEntity;
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> postUserLoginResponseEntity(HttpServletRequest request, @RequestBody Map<String, String> requestMap) {
        ResponseEntity<?> responseEntity = null;

        JSONDB jsondb = JSONDB.getInstance();

        if (!requestMap.isEmpty()) {
            if (requestMap.get("id") != null && !requestMap.get("id").equals("")
                    && requestMap.get("pw") != null && !requestMap.get("pw").equals("")) {

                if (jsondb.getDbHashMap().containsKey(requestMap.get("id"))) {
                    if (jsondb.getDbHashMap().get(requestMap.get("id")).get("pw").equals(requestMap.get("pw"))) {
                        responseEntity = new ResponseEntity<>("OK", HttpStatus.OK);

                    } else {
                        responseEntity = new ResponseEntity<>("NO", HttpStatus.BAD_REQUEST);
                    }

                } else {
                    responseEntity = new ResponseEntity<>("NO", HttpStatus.BAD_REQUEST);
                }
            }

        } else {
            responseEntity = new ResponseEntity<>("NO", HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

    @RequestMapping(value = "/user/signup", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> postUserSignUpResponseEntity(HttpServletRequest request, @RequestBody Map<String, String> requestMap) {
        ResponseEntity<?> responseEntity = null;

        JSONDB jsondb = JSONDB.getInstance();

        if (!jsondb.getDbHashMap().containsKey(requestMap.get("id"))) {
            jsondb.getDbHashMap().put(requestMap.get("id"), new HashMap<String, Object>() {{
                put("id", requestMap.get("id"));
                put("pw", requestMap.get("pw"));
                put("email", requestMap.get("email"));
            }});

            responseEntity = new ResponseEntity<>("OK", HttpStatus.OK);

        } else {
            responseEntity = new ResponseEntity<>("NO", HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

    @RequestMapping(value = "/user/AuthToken", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> postUserAuthTokenResponseEntity(HttpServletRequest request, @RequestBody Map<String, String> requestMap) {
        ResponseEntity<?> responseEntity = null;

        JSONDB jsondb = JSONDB.getInstance();

        if (!jsondb.getDbHashMap().isEmpty()) {
            if (requestMap.containsKey("id") && requestMap.containsKey("AuthToken") && requestMap.containsKey("timestamp") && requestMap.containsKey("injection")) {
                String[] decodeHexString;
                HashMap<String, String> uploadHashMap = new HashMap<String, String>();
                HashMap<String, String> authHashMap = new HashMap<String, String>();

                ArrayList<Map<String, String>> userVisitArrayList;

                try {
                    if (jsondb.getDbHashMap().containsKey(requestMap.get("id"))) {
                        decodeHexString = new String(Hex.decodeHex(requestMap.get("AuthToken").toCharArray()), "UTF-8").split("#");

                        Map<String, String> userRequestMap = requestMap;

                        if (decodeHexString[1].equals("10115")) { // test place
                            uploadHashMap.put("place", "전북대학교 5호관");
                            userRequestMap.put("place", "전북대학교 5호관");

                        } else {
                            uploadHashMap.put("place", "전북대학교 어딘가");
                            userRequestMap.put("place", "전북대학교 어딘가");
                        }
                        uploadHashMap.put("status", "ok");

                        authHashMap.put("AuthToken", requestMap.get("AuthToken"));
                        authHashMap.put("injection", requestMap.get("injection"));

                        if (jsondb.getDbHashMap().get(requestMap.get("id")).containsKey("visitList")) {
                            userVisitArrayList = (ArrayList<Map<String, String>>) jsondb.getDbHashMap().get(requestMap.get("id")).get("visitList");
                        } else {
                            userVisitArrayList = new ArrayList<Map<String, String>>();
                        }

                        userVisitArrayList.add(userRequestMap);
                        jsondb.getDbHashMap().get(requestMap.get("id")).put("visitList", userVisitArrayList);

                        jsondb.getAuthDBHashMap().add(authHashMap);

                        responseEntity = new ResponseEntity<>(uploadHashMap, HttpStatus.OK);

                    } else {
                        responseEntity = new ResponseEntity<>("BAD_REQUEST", HttpStatus.BAD_REQUEST);
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (DecoderException e) {
                    e.printStackTrace();
                }

            } else {
                responseEntity = new ResponseEntity<>("BAD_REQUEST", HttpStatus.BAD_REQUEST);
            }

        } else {
            responseEntity = new ResponseEntity<>("BAD_REQUEST", HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @RequestMapping(value = "/rpi/deleteAuth", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> delAuthResponseEntity(HttpServletRequest request) {
        ResponseEntity<?> responseEntity = null;

        JSONDB jsondb = JSONDB.getInstance();

        if (!jsondb.getAuthDBHashMap().isEmpty()) {
            jsondb.getAuthDBHashMap().clear();
        }

        responseEntity = new ResponseEntity<>("OK", HttpStatus.OK);

        return responseEntity;
    }

}
