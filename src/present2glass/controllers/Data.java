package present2glass.controllers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import present2glass.Main;
import present2glass.components.Item;
import present2glass.components.Slide;

import java.util.ArrayList;

/* This Class is designed to reduce the size of the final JSON object by creating properties that are needed to be saved back */
public class Data {
    public ArrayList<String> notes = new ArrayList<String>();
    public ArrayList<Long> amounts = new ArrayList<Long>();
    public ArrayList<Boolean> stream = new ArrayList<Boolean>();
    public ArrayList<Boolean> hidden = new ArrayList<Boolean>();
    public ArrayList<Long> timings = new ArrayList<Long>();
    public String title;
    public String ip;
    /* Codes for presenter software
    * 0 = PowerPoint
    * 1 = KeyNote
    * 2 = OpenOffice
    * 3 = LibreOffice
    */
    public int software = 0;
    public int totalNotes = 0;
    public int totalSlides = 0;

    public Data(){
        title = Main.title.getText();
        ip = Main.nav.getIP();
        software = Main.nav.getSoftware();
        totalSlides = Main.editor.slides.size();
        for(int i = 0; i < totalSlides; i++){
            Slide slide = Main.editor.slides.get(i);
            amounts.add(Long.parseLong("0"));
            hidden.add(slide.isHidden());
            totalNotes += slide.items.size();
            long total = 0;
            for(int j = 0; j < slide.items.size(); j++, total++) {
                Item item = slide.items.get(j);
                notes.add(item.getText());
                stream.add(item.isStream());
                timings.add(item.getDisplayTime());
            }

            amounts.set(i, total);
        }
    }

    public Data(String title, String ip, int software, ArrayList<String> notes, ArrayList<Long> amounts, ArrayList<Boolean> hidden, ArrayList<Boolean> stream, ArrayList<Long> timings){
        this.totalNotes = notes.size();
        this.totalSlides = amounts.size();
        this.amounts = amounts;
        this.title = title;
        this.notes = notes;
        this.hidden = hidden;
        this.stream = stream;
        this.timings = timings;
        this.ip = ip;
        this.software = software;
    }

    @SuppressWarnings("unchecked")
    public static String encode(Data data){
        JSONObject json = new JSONObject();
        JSONArray notes = new JSONArray();
        JSONArray stream = new JSONArray();
        JSONArray hidden = new JSONArray();
        JSONArray timings = new JSONArray();
        JSONArray amounts = new JSONArray();
        notes.addAll(data.notes);
        stream.addAll(data.stream);
        timings.addAll(data.timings);
        amounts.addAll(data.amounts);
        hidden.addAll(data.hidden);
        json.put("title", data.title);
        json.put("notes", notes);
        json.put("stream", stream);
        json.put("hidden", hidden);
        json.put("timings", timings);
        json.put("amounts", amounts);
        json.put("ip", data.ip);
        json.put("software", data.software);

        return json.toJSONString();
    }

    public static JSONObject parse(String string){
        JSONParser parser = new JSONParser();
        try {
            return (JSONObject) parser.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    @SuppressWarnings("unchecked")
    public static Data decode(JSONObject jsonObject){
        String title = (String) jsonObject.get("title");
        String ip = (String) jsonObject.get("ip");
        Long longSoftware = (Long) jsonObject.get("software");
        int software = Integer.parseInt(longSoftware.toString());
        ArrayList<String> notes = (ArrayList<String>) jsonObject.get("notes");
        ArrayList<Boolean> stream = (ArrayList<Boolean>) jsonObject.get("stream");
        ArrayList<Boolean> hidden = (ArrayList<Boolean>) jsonObject.get("hidden");
        ArrayList<Long> timings = (ArrayList<Long>) jsonObject.get("timings");
        ArrayList<Long> amounts = (ArrayList<Long>) jsonObject.get("amounts");
        return new Data(title, ip, software, notes, amounts, hidden, stream, timings);

    }

}
