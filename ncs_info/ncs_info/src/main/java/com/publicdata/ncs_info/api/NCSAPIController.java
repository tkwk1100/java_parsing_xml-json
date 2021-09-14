package com.publicdata.ncs_info.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.publicdata.ncs_info.mapper.NcsInfoMapper;
import com.publicdata.ncs_info.vo.NcsInfoVO;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;


@RestController
public class NCSAPIController {
    @Autowired 
    NcsInfoMapper mapper;

    @GetMapping("/ncs/get")
    public Map<String, Object> ncsGetAPI() throws IOException {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        StringBuilder urlBuilder = new StringBuilder("https://infuser.odcloud.kr/oas/docs?namespace=15037743/v1"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "6o9k%2FijVJS6Syp4mxKkkLoK4Ax%2F5LpR6Rl0CcUgX6BB%2FzD1%2BL7FGFGaF7wocaB0J6A5B%2Bu3qY1%2FZY%2BQsDaseSQ%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*제공형태(json/xml)*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호 - 기본값 : 1*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지당 데이터 수 - 기본값 : 10*/


        URL url = new URL(urlBuilder.toString());


        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        String result = sb.toString();
        
        try{
        JSONParser parser = new JSONParser();

        JSONObject obj = (JSONObject)parser.parse(result);

        JSONObject rootObj = (JSONObject)obj.get("root");

        // JSONObject infoObj = (JSONObject)rootObj.get("info");

        // System.err.println("전체카운트 : "+infoObj.get("totalCount"));
        // System.err.println("페이지 번호"+infoObj.get("pagrNo"));
        // System.err.println("페이지 당 데이터 수"+infoObj.get("numOfRows"));

            JSONArray items = (JSONArray)rootObj.get("items");
            for(int i = 0; i < items.size(); i++){
                JSONObject item = (JSONObject)items.get(i);
                NcsInfoVO vo = new NcsInfoVO();
                vo.setNs_code( (String)item.get( "ncsClCd") );
                vo.setNs_l_class( (String)item.get( "ncsLclasCdnm") );
                vo.setNs_m_class( (String)item.get( "ncsMclasCdnm") );
                vo.setNs_s_class( (String)item.get( "ncsSclasCdnm") );
                vo.setNs_d_class( (String)item.get( "ncsSubdCdnm") );
                vo.setNs_name( (String)item.get( "compeUnitName") );
                vo.setNs_def( (String)item.get( "compeUnitDef") );
                vo.setNs_level( Integer.parseInt((String)item.get( "compeUnitLevel")) );
                
                mapper.insertNcsInfo(vo);

                // System.out.println("NCS코드 : "+item.get("ncsClCd"));
                // System.out.println("NCS 대분류 : "+item.get("ncsLclasCdnm"));
                // System.out.println("NCS 중분류 : "+item.get("ncsMclasCdnm"));
                // System.out.println("NCS 소분류 : "+item.get("ncsSclasCdnm"));
                // System.out.println("NCS 세분류 : "+item.get("ncsSubdCdnm"));
                // System.out.println("NCS 능력단위 명 : "+item.get("compeUnitName"));
                // System.out.println("NCS 능력단위 정의 : "+item.get("compeUnitDef"));
                // System.out.println("NCS 능력단위  레벨 : "+item.get("compeUnitLevel"));
                // System.out.println("=============================================================");
            }
        }
        catch(ParseException pe){
            pe.printStackTrace();
            resultMap.put("status", true);
            resultMap.put("message","데이터 파싱 실패");
            resultMap.put("reason", pe.getMessage());
        }
        resultMap.put("status", true);
        resultMap.put("message","데이터 파싱 완료");

        return resultMap;   
    }

    @GetMapping("/ncs/get/xml")
    public String getNcsXML() throws IOException {

        StringBuilder urlBuilder = new StringBuilder("https://infuser.odcloud.kr/oas/docs?namespace=15037743/v1"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "6o9k%2FijVJS6Syp4mxKkkLoK4Ax%2F5LpR6Rl0CcUgX6BB%2FzD1%2BL7FGFGaF7wocaB0J6A5B%2Bu3qY1%2FZY%2BQsDaseSQ%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("type","UTF-8") + "=" + URLEncoder.encode("xml", "UTF-8")); /*제공형태(json/xml)*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호 - 기본값 : 1*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*페이지당 데이터 수 - 기본값 : 10*/

        System.out.println(urlBuilder.toString());
        
        /// XML parsing
        try {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(urlBuilder.toString());
        // root tag
        doc.getDocumentElement().normalize();
        System.out.println(doc.getDocumentElement().getNodeName());

        NodeList nList = doc.getElementsByTagName("ncsInfo");
        // System.out.println(nList.getLength());
        for(int i=0; i <nList.getLength(); i++){
            Node node = nList.item(i);
            Element elem = (Element)node; // ncsinfo rag 1덩어리
            
            //ncsInfo 아래쪽에 위치한 ncsClCd 타입의 태그를 모두 가져오고,
            //ncsClCd 타입의 태그 중 첫번째 태그를 가져와서, - .item(0)
            //ncsClCd 타입의 태그 내부의 모든 내용을 가져온다. - .getChildNodes()
            // System.out.println("능력단위 분류 번호 : "+getValue(elem, "ncsClCd"));
            // System.out.println("능력단위 분류 명 : "+getValue(elem, "compeUnitName"));
            // System.out.println("능력단위 분류 수준 : "+getValue(elem, "compeUnitLevel"));
            // System.out.println("능력단위 분류 대분류 : "+getValue(elem, "ncsLclasCdnm"));
            // System.out.println("능력단위 분류 중분류 : "+getValue(elem, "ncsMclasCdnm"));
            // System.out.println("능력단위 분류 소분류 : "+getValue(elem, "ncsSclasCdnm"));
            // System.out.println("능력단위 분류 세분류 : "+getValue(elem, "ncsSubdCdnm"));
            // System.out.println("능력단위 분류 정의 : "+getValue(elem, "compeUnitDef"));
            // System.out.println("==================================================================");
            NcsInfoVO vo = new NcsInfoVO();
            vo.setNs_code( getValue(elem, "ncsClCd") );
            vo.setNs_l_class( getValue(elem, "ncsLclasCdnm") );
            vo.setNs_m_class( getValue(elem, "ncsMclasCdnm") );
            vo.setNs_s_class( getValue(elem, "ncsSclasCdnm") );
            vo.setNs_d_class( getValue(elem, "ncsSubdCdnm") );
            vo.setNs_name( getValue(elem, "compeUnitName") );
            vo.setNs_def( getValue(elem, "compeUnitDef") );
            vo.setNs_level( Integer.parseInt(getValue(elem, "compeUnitLevel")) );

            mapper.insertNcsInfo(vo);
        }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static String getValue(Element elem, String tagName){
        // if(elem.getElementsByTagName(tagName).item(0) == null)return "";
        NodeList elem_nodelist = elem.getElementsByTagName(tagName).item(0).getChildNodes();
        Node elem_node = elem_nodelist.item(0);
        return elem_node.getNodeValue();
    }
}
