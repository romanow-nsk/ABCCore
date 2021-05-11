package romanow.abc.core.entity.base;

import romanow.abc.core.entity.Entity;
import romanow.abc.core.entity.EntityLink;
import romanow.abc.core.entity.artifacts.Artifact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class HelpFile extends Entity {
    private String title="";
    private String tagList="";          // Массив идентификаторов тегов
    private EntityLink<Artifact> itemFile = new EntityLink<>(Artifact.class);
    public boolean isTagPresent(String tag){
        return createTagMap().get(tag) !=null;
        }
    public int calcTagCount(ArrayList<String> src){
        int count=0;
        HashMap<String,Object> xx = createTagMap();
        for(String zz : src)
            if (xx.get(zz)!=null)
                count++;
        return count;
        }
    public boolean isAllTagsPresent(String src){
        ArrayList<String> tags = createTagArray(src);
        if (tags.size()==0)
            return true;
        HashMap<String,Object> xx = createTagMap();
        for(String zz : tags)
            if (xx.get(zz)==null)
                return false;
        return true;
        }
    public HelpFile(){}
    @Override
    public String getTitle() {
        return title; }
    public void setTitle(String name) {
        this.title = name; }
    public EntityLink<Artifact> getItemFile() {
        return itemFile; }
    public void setItemFile(EntityLink<Artifact> itemFile) {
        this.itemFile = itemFile; }
    public String getTagList() {
        return tagList; }
    public void setTagList(String tagList) {
        this.tagList = tagList; }
    public void addTag(String tag) {
        if (isTagPresent(tag)) return;
        if (tagList.length()==0)
            this.tagList = tag;
        else
            tagList = tagList +","+tag;
        }
    public void removeTag(String tag){
        String zz = "";
        for(String ss : createTagArray()){
            if (ss.equals(tag))
                continue;
            if (zz.length()==0)
                zz = ss;
            else
                zz = zz +","+ss;
            }
        tagList = zz;
        }
    public ArrayList<String> createTagArray(){
        return createTagArray(tagList);
        }
    public HashMap<String,Object> createTagMap(){ return createTagMap(tagList); }

    @Override
    public String toFullString() {
        return title + " ["+tagList+"] "+itemFile.getTitle();
    }

    //-----------------------------------------------------------------------------------
    public static ArrayList<String> createTagArray(String zz){
        ArrayList<String> out = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(zz,",");
        while (tokenizer.hasMoreElements()){
            out.add(tokenizer.nextToken());
            }
        return out;
        }
    public static HashMap<String,Object> createTagMap(String zz){
        Object oo = new Object();
        HashMap<String,Object> out = new HashMap<>();
        StringTokenizer tokenizer = new StringTokenizer(zz,",");
        while (tokenizer.hasMoreElements()){
            out.put(tokenizer.nextToken(),oo);
            }
        return out;
        }
    public static void main(String ss[]){
        HelpFile ff = new HelpFile();
        ff.setTagList("aaa,bbb,ccc");
        ArrayList<String> zz = ff.createTagArray();
        System.out.println(zz.size());
    }
}
