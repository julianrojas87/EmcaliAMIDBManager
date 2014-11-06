/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emcali.ami.control.webservice;

import emcali.ami.control.security.Credentials;
import emcali.ami.persistence.control.TelcoInfoJpaController;
import emcali.ami.persistence.entity.AmyConsumos;
import emcali.ami.persistence.entity.AmyMedidores;
import emcali.ami.persistence.entity.ComercialClientes;
import emcali.ami.persistence.entity.TelcoInfo;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.transaction.UserTransaction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author julian
 */
@WebService(serviceName = "IPTVAMIWebService")
public class IPTVAMIWebService {

    @PersistenceUnit(unitName = "EMCALIAMIDBManagerPU")
    EntityManagerFactory emf;
    @Resource
    UserTransaction utx;

    /**
     * This is a sample web service operation
     *
     * @param args
     * @return
     */
    @WebMethod(operationName = "execute")
    public String execute(@WebParam(name = "args") String args) {
        JSONObject jsonResponse = new JSONObject();
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(args);
            JSONObject jsonRequest = (JSONObject) obj;

            String user = (String) jsonRequest.get("user");
            String password = (String) jsonRequest.get("password");

            Credentials credentials = new Credentials();
            String message = credentials.authenticate(user, password);

            switch (message) {
                case "user unauthorized":
                    jsonResponse.put("type", "error");
                    jsonResponse.put("message", message);
                    break;

                case "wrong password":
                    jsonResponse.put("type", "error");
                    jsonResponse.put("message", message);
                    break;

                case "success":
                    jsonResponse.put("type", "info");
                    jsonResponse.put("message", message);

                    if (((String) jsonRequest.get("command")).equals("getData")) {
                        jsonResponse = this.getData(jsonRequest, jsonResponse);
                    }
                    break;
            }

            return jsonResponse.toJSONString();

        } catch (ParseException ex) {
            jsonResponse.put("type", "error");
            jsonResponse.put("message", "JSON Format error: " + ex.getMessage());

            return jsonResponse.toJSONString();
        }
    }

    private JSONObject getData(JSONObject req, JSONObject resp) {
        Calendar cal = new GregorianCalendar();
        DateFormat dateformat = new SimpleDateFormat("dd-MM-YYYY");
        Date today = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, -11);
        Date tendaysago = cal.getTime();
        
        String telcoid = (String) req.get("suscriptor");
        JSONObject data = new JSONObject();
        JSONArray consumos = new JSONArray();
        
        TelcoInfoJpaController tijc = new TelcoInfoJpaController(utx, emf);
        TelcoInfo ti = tijc.findTelcoInfo(Long.parseLong(telcoid));
        AmyMedidores med = ti.getFkAmyMedidores();
        ComercialClientes cc = ti.getFkComercialClientes();
        ArrayList<AmyConsumos> ac = new ArrayList<>(med.getAmyConsumosCollection());
        
        data.put("id_cliente", cc.getIdClientes());
        data.put("nombre_cliente", cc.getNombreClientes());
        data.put("direccion", cc.getDireccion());
        data.put("serial_medidor", med.getSerial());
        
        for (AmyConsumos amic : ac) {
            if (amic.getFkAmyInterval().getIntervalo().equals("Dia")) {
                Date fechacons = amic.getFechaConsumo();
                if ((fechacons.compareTo(tendaysago) > 0) && fechacons.before(today)) {
                    JSONObject consumo = new JSONObject();
                    consumo.put("consumo", amic.getConsumo());
                    consumo.put("fecha", dateformat.format(amic.getFechaConsumo()));
                    consumos.add(consumo);
                }
            }
        }
        resp.put("data", consumos);
        return resp;
    }
}
