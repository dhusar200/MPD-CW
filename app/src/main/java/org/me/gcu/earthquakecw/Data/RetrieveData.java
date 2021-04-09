package org.me.gcu.earthquakecw.Data;
import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

/**
 * Class which is used to retrieve the data from the xml using ASYNC class. It would be possible
 * to implement onPreExecute, onPostExecute, onProgressUpdate, however, I am trying to make it
 * so the used does not know when the data is being updated. So no progress bar or anything.
 * Of course, this is only possible as the data is low size, with bigger data-set, this would have
 * to be done differently, at least with the first load.
 */

public class RetrieveData extends AsyncTask<Object, Void, Object> {

    private final LinkedList<EarthQuake> alist = new LinkedList<>();
    @Override
    protected Object doInBackground(Object[] objects) {
        // Initializing instance variables

        try {
            URL url = new URL("https://quakes.bgs.ac.uk/feeds/MhSeismology.xml");

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();

            // We will get the XML from an input stream
            xpp.setInput(getInputStream(url), "UTF_8");

            /* We will parse the XML content looking for the "<title>" tag which appears inside the "<item>" tag.
             * However, we should take in consideration that the rss feed name also is enclosed in a "<title>" tag.
             * As we know, every feed begins with these lines: "<channel><title>Feed_Name</title>...."
             * so we should skip the "<title>" tag which is a child of "<channel>" tag,
             * and take in consideration only "<title>" tag which is a child of "<item>"
             *
             * In order to achieve this, we will make use of a boolean variable.
             */
            boolean insideItem = false;
            EarthQuake eQuake = null;

            // Returns the type of current event: START_TAG, END_TAG, etc..
            int eventType = xpp.getEventType();
            String temp = "";
            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                // Found a start tag
                if(eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equalsIgnoreCase("item")) {
                        Log.e("MyTag", "Item Start Tag found");
                        eQuake = new EarthQuake();
                        insideItem = true;
                    }
                    if (insideItem) {
                        switch (xpp.getName().toLowerCase()){
                            case "title":
                                temp = xpp.nextText();
                                // Do something with text
                                Log.e("MyTag", "Title is " + temp);
                                eQuake.setTitle(temp);
                                break;
                            case "description":
                                // Now just get the associated text
                                temp = xpp.nextText();
                                // Do something with text
                                String[] split = temp.split(";");
                                eQuake.setLocation(split[1].split(":")[1].substring(1));
                                eQuake.setDepth(Integer.parseInt(split[3].split(":")[1].replaceAll("\\s+","").split("km")[0]));
                                eQuake.setMagnitude(Float.parseFloat(split[4].split(":")[1].replaceAll("\\s+","")));
                                Log.e("MyTag", "Description is " + temp);
                                //TODO
                                //more with description, separate into location depth magnitude
                                break;
                            case "link":
                                // Now just get the associated text
                                temp = xpp.nextText();
                                // Do something with text
                                Log.e("MyTag", "Link is " + temp);
                                eQuake.setLink(temp);
                                break;
                            case "pubdate":
                                // Now just get the associated text
                                temp = xpp.nextText();
                                // Do something with text
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss");
                                LocalDateTime date = LocalDateTime.parse(temp, formatter);
                                eQuake.setDate(date);
                                Log.e("MyTag", "Publication Date is " + date);
                                break;
                            case "category":
                                // Now just get the associated text
                                temp = xpp.nextText();
                                // Do something with text
                                Log.e("MyTag", "Category is " + temp);
                                eQuake.setCategory(temp);
                                break;
                            case "geo:lat":
                                // Now just get the associated text
                                temp = xpp.nextText();
                                // Do something with text
                                Log.e("MyTag", "Latitude is " + temp);
                                eQuake.setGlat(Float.parseFloat(temp));
                                break;
                            case "geo:long":
                                // Now just get the associated text
                                temp = xpp.nextText();
                                // Do something with text
                                Log.e("MyTag", "Longitude is " + temp);
                                eQuake.setGlong(Float.parseFloat(temp));
                                break;
                        }
                    }
                }
                else if(eventType == XmlPullParser.END_TAG)
                {
                    if (xpp.getName().equalsIgnoreCase("item"))
                    {
                        alist.add(eQuake);
                        insideItem = false;
                    }
                    else if (xpp.getName().equalsIgnoreCase("channel"))
                    {
                        int size;
                        size = alist.size();
                        Log.e("MyTag","EarthQuake list size is " + size);
                    }
                }

                eventType = xpp.next(); //move to next element
            }

        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        return alist;
    }


    private InputStream getInputStream(URL url) throws IOException {
        return url.openConnection().getInputStream();
    }

    public LinkedList<EarthQuake> getAlist()
    {
        Log.e("MyTag", alist.size() + "");
        return alist;
    }
}