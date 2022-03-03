package com.company;

public class Parse {
    Integer protocol;
    String url;
    String urn;

    public void parseURI(String uri) {
        if (uri.startsWith("https://"))
            protocol = 1;
        else
            protocol = 0;

        if(uri.startsWith("http://") || uri.startsWith("https://"))
            uri = uri.substring(uri.indexOf("/") + 2);

        if (uri.contains("/")) {
            url = uri.substring(0, uri.indexOf("/"));
            urn = uri.substring(url.length());
        }
        else {
            url = uri;
        }
    }
}
