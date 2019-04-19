/**
  * Copyright 2018 bejson.com 
  */
package com.zslin.web.bean;
import java.util.List;

/**
 * Auto-generated: 2018-10-23 11:45:20
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class JsonRootBean {

    private Emotion emotion;
    private Intent intent;
    private List<Results> results;
    public void setEmotion(Emotion emotion) {
         this.emotion = emotion;
     }
     public Emotion getEmotion() {
         return emotion;
     }

    public void setIntent(Intent intent) {
         this.intent = intent;
     }
     public Intent getIntent() {
         return intent;
     }

    public void setResults(List<Results> results) {
         this.results = results;
     }
     public List<Results> getResults() {
         return results;
     }

}