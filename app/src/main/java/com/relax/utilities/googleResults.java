package com.relax.utilities;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

public class googleResults {

    public static void main(String[] args) {
    }

    public static Map<String, String> result(String search) {
        Map<String, String> map = new HashMap<>();
        String href, linkText, url;
        try {
            search = search.replace(' ', '+');
            search = search +"+-youtube";
            url = "https://www.google.com.ly/search?q=" + search + "&num=5";
            Log.d("URL", "= "+url);
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("a > h3");

            for (Element link : links) {

                assert link.parent() != null;
                Elements parent = link.parent().getAllElements();

                href = parent.attr("href");
                if (href.startsWith("/url?q=")) {
                    href = href.replace("/url?q=", "");
                }

                String[] splittedString = href.split("&sa=");
                if (splittedString.length > 1) {
                    href = splittedString[0];
                }

                linkText = trim(link.text());
                map.put(linkText, href);
            }
        } catch (Exception e) {
            map.put(e.toString(), "Exception");
            return map;
        }
        return map;
    }

    private static String trim(String s) {
        int width = 40;
        if (s.length() > width)
            return s.substring(0, width - 1) + ".";
        else
            return s;
    }

}

