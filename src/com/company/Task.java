package com.company;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task {
    public String getResp(String uri) {
        String bResp = null;
        try {
            Parse getParse = new Parse();
            getParse.parseURI(uri);

            String url = getParse.url;
            Integer protocol = getParse.protocol;
            String urn = getParse.urn;
            String temp;
            Socket socket;

            if (protocol == 1) {
                SocketFactory socketFactory = SSLSocketFactory.getDefault();
                socket = socketFactory.createSocket(url, 443);
            } else {
                socket = new Socket(url, 80);
            }

            temp = ("GET " + urn + " HTTP/1.1\r\nHost: " + url + "\r\n\r\n");
            BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
            BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());

            bos.write(temp.getBytes(StandardCharsets.UTF_8));
            bos.flush();

            int bufferSize = 100;
            byte[] resp = new byte[bufferSize];
            resp = bis.readAllBytes();
            bResp = new String(resp);

            socket.close();
            //-----------------------------------
//            System.out.println(url + "\n" + urn + "\n" + protocol);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bResp;
    }

    public void downloadFile(URL url, String fileName) throws IOException {
        try (InputStream in = url.openStream();
             BufferedInputStream bis = new BufferedInputStream(in);
             FileOutputStream fos = new FileOutputStream(fileName)) {

            byte[] data = new byte[1024];
            int count;
            while ((count = bis.read(data, 0, 1024)) != -1) {
                fos.write(data, 0, count);
            }
        }
    }

    public String reverseString(String str){
        char[] ch =str.toCharArray();
        StringBuilder rev= new StringBuilder();
        for(int i=ch.length-1;i>=0;i--){
            rev.append(ch[i]);
        }
        return rev.toString();
    }

    public void no1(String no1input) {
        String all = getResp(no1input);
        String header = all.substring(0,all.indexOf("\r\n\r\n")+4);
        String text = all.substring(header.length());
        System.out.println(text);

    }
    public void no2(String no2input){
        String listLinks = "";
        String all = getResp(no2input);
        String header = all.substring(0,all.indexOf("\r\n\r\n")+4);
        String text = all.substring(header.length());

        Pattern pat = Pattern.compile("<a[^>]*>([^<]+)<\\/a>");
        Matcher mat = pat.matcher(text);

        while (mat.find())
            listLinks = listLinks.concat((mat.group()) + "\n");

        System.out.println(listLinks);
    }
    public void no3(String no3input) {
        try {
            String all = getResp(no3input);
            String header = all.substring(0, all.indexOf("\r\n\r\n") + 4);

            Parse uri = new Parse();
            uri.parseURI(no3input);

            URL link = new URL(no3input);

            boolean isHTML = header.contains("Content-Type: text/html");

            if (!isHTML) {
                String fileName = reverseString(uri.urn);
                fileName = (fileName.substring(0, fileName.indexOf("/")));
                fileName = reverseString(fileName);
                downloadFile(link, "./" + fileName);
            } else
                System.out.println("Link ini mempunyai Content-Type: text/html");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void no4(){
        System.out.println("Not yet implemented!");
    }
    public void no5(String no5input){
        String all = getResp(no5input);
        String header = all.substring(0, all.indexOf("\r\n\r\n") + 4);
//        String text = all.substring(header.length());

        String http = header.substring(0,header.indexOf(" "));
        String stat = header.substring(http.length()+1,http.length()+4);

        if(stat.charAt(0) == '3'){
            String headtoloc = header.substring(0,header.indexOf("Location: ")+10);//10 is length of "Location: "
            String loctoEnd = header.substring(headtoloc.length());//location until end
            String loc = loctoEnd.substring(0,loctoEnd.indexOf("\r\n"));//newloc

            System.out.println("REDIRECTING TO "+loc);
            no5(loc);
        }
        else{
            System.out.println("There is no redirection again\nCurrent Location : "+no5input);
        }
    }
    public void no6(String no6input){
        String all = getResp(no6input);
        String header = all.substring(0, all.indexOf("\r\n\r\n") + 4);

        String http = header.substring(0,header.indexOf(" "));
        String statmsg = header.substring(http.length()+1,header.indexOf("\r\n"));

        System.out.println("Header status code : " + statmsg);
    }
    public void no7(String no7input){
        System.out.println("Not yet implemented!");
    }
}
