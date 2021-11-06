package kr.ac.jbnu.software.checkmeServer.controller;

import kr.ac.jbnu.software.checkmeServer.model.JSONDB;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity<?> getuserAuthresponseEntity(HttpServletRequest request, @RequestBody Map<String, String> requestMap) {
        ResponseEntity<?> responseEntity = null;

        JSONDB jsondb = JSONDB.getInstance();

        if (requestMap != null) {
            responseEntity = new ResponseEntity<>("OK", HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("NOT_FOUND", HttpStatus.NOT_FOUND);
        }
        return responseEntity;
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> postUserLoginResponseEntity(HttpServletRequest request, @RequestBody Map<String, String> requestMap) {
        ResponseEntity<?> responseEntity = null;

        JSONDB jsondb = JSONDB.getInstance();

        if (!requestMap.isEmpty()) {
            if (jsondb.getDbHashMap().containsKey(requestMap.get("id"))
                    && jsondb.getDbHashMap().get(requestMap.get("id")).get("pw").equals(requestMap.get("pw")))
                responseEntity = new ResponseEntity<>("OK", HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("NO", HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @RequestMapping(value = "/user/signup", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> postUserSignUpResponseEntity(HttpServletRequest request, @RequestBody Map<String, String> requestMap) {
        ResponseEntity<?> responseEntity = null;

        JSONDB jsondb = JSONDB.getInstance();
        HashMap<String, Object> inputHashMap = new HashMap<String, Object>();

        if (requestMap.get("id") != null && requestMap.get("pw") != null && requestMap.get("email") != null) {
            inputHashMap.put("id", requestMap.get("id"));
            inputHashMap.put("pw", requestMap.get("pw"));
            inputHashMap.put("email", requestMap.get("email"));
            jsondb.getDbHashMap().put(requestMap.get("id"), inputHashMap);

            responseEntity = new ResponseEntity<>("OK", HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity<>("NO", HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @RequestMapping(value = "/user/AuthToken", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> postUserAuthTokenResponseEntity(HttpServletRequest request, @RequestBody Map<String, String> requestMap) {
        ResponseEntity<?> responseEntity = null;

        JSONDB jsondb = JSONDB.getInstance();
        HashMap<String, String> returnHashMap = new HashMap<String, String>();

        if (!requestMap.isEmpty()) {
            if (jsondb.getDbHashMap().containsKey(requestMap.get("id")) && requestMap.get("AuthToken") != null
                    && requestMap.get("timestamp") != null && requestMap.get("injection") != null) {

                returnHashMap.put("status", "ok");
                returnHashMap.put("place", "전북대학교 5호관");
                responseEntity = new ResponseEntity<>(returnHashMap, HttpStatus.OK);
            } else {
                responseEntity = new ResponseEntity<>("BAD_REQUEST", HttpStatus.BAD_REQUEST);
            }
        } else {
            responseEntity = new ResponseEntity<>("BAD_REQUEST", HttpStatus.BAD_REQUEST);
        }
        return responseEntity;
    }

    @RequestMapping(value = "/rpi/deleteAuth", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<?> delAuthResponseEntity(HttpServletRequest request) {
        ResponseEntity<?> responseEntity = null;

        JSONDB jsondb = JSONDB.getInstance();
        jsondb.resetDB();
        responseEntity = new ResponseEntity<>("OK", HttpStatus.OK);

        return responseEntity;
    }

}
