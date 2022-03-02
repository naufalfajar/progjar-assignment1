package com.company;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.awt.desktop.SystemSleepEvent;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static String url;
    private static String urn;
    private static Integer protocol;
    private final Collection<Runnable> tasks = new ArrayList<Runnable>();
    public static void main(String[] args) {
        Paralel tasks = new Paralel();
        Scanner read = new Scanner(System.in);
        List<String> list = new ArrayList<String>();

        while(true) {
            try {
                System.out.println("Type URI or \"quit\" to exit");
                String urlInput = read.nextLine();

                if (urlInput.equalsIgnoreCase("quit"))
                    break;

                if(urlInput.equalsIgnoreCase("download")){
                    try {
                        tasks.go();
                    } catch (Exception e){
                        System.err.println(e.getMessage());
                    }
                }

                String listLinks = "";
                parseURL(urlInput);
                String temp = "";
                SocketFactory socketFactory = SSLSocketFactory.getDefault();
                Socket socket;

                if(protocol==1){
                    socket = socketFactory.createSocket(url, 443);
                    temp = ("GET " + urn + " HTTP/1.1\r\nHost: " + url + "\r\n\r\n");
                }else{
                    socket = new Socket(url, 80);
                    temp = "GET " + urn + " HTTP/1.1\r\nHost: " + url + "\r\n\r\n";
                }

                BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
                BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());

                bos.write(temp.getBytes(StandardCharsets.UTF_8));
                bos.flush();

                int bufferSize = 100;
                byte[] resp = new byte[bufferSize];
                resp = bis.readAllBytes();
                String bResp = new String(resp);

                //checkRedirection
                String stat = bResp.substring(bResp.indexOf("HTTP/1.1")+9, bResp.indexOf("HTTP/1.1")+10);
                System.out.println("STATUS CODE = "+ stat);
                String loc1[] = bResp.split("\n");
                String loc2 = "";
                for(int i=0; i<loc1.length; i++){
                    if(loc1[i].startsWith("Location")){
                        loc2=loc1[i];
                        loc2=loc2.substring(10);
                        break;
                    }
                }
                System.out.println("LOCATION = "+ loc2);

                while(stat.equalsIgnoreCase("3"))
                {
                    System.out.println("REDIRECTION TO "+loc2);
                    parseURL(loc2);
                    System.out.println("protocol = "+protocol);
                    System.out.println("TEMP B = " + temp);
                    System.out.println("URI = " + url + "+" + urn);
                    if(protocol==1){
                        System.out.println("MAsuk");
                        socket = socketFactory.createSocket(url, 443);
                        temp = ("GET " + urn + " HTTP/1.1\r\nHost: " + url + "\r\n\r\n");

                    }else{
                        System.out.println("MAsuk http");
                        socket = new Socket(url, 80);
                        temp = "GET " + urn + " HTTP/1.1\r\nHost: " + url + "\r\n\r\n";
                    }

                    bis = new BufferedInputStream(socket.getInputStream());
                    bos = new BufferedOutputStream(socket.getOutputStream());

                    bos.write(temp.getBytes(StandardCharsets.UTF_8));
                    bos.flush();

//                    int bufferSize = 100;
//                    byte[] resp = new byte[bufferSize];
                    resp = bis.readAllBytes();
                    bResp = new String(resp);

                    stat = bResp.substring(bResp.indexOf("HTTP/1.1")+9, bResp.indexOf("HTTP/1.1")+10);
                    System.out.println("STATUS CODE = "+ stat);
                    String loc3[] = bResp.split("\n");
                    loc2 = "";
                    for(int i=0; i<loc3.length; i++){
                        if(loc3[i].startsWith("Location")){
                            loc2=loc3[i];
                            loc2=loc2.substring(10);
                            break;
                        }
                    }
                }

//                getHeader(urlInput);
                boolean isHTML = bResp.contains("Content-Type: text/html");
                System.out.println(bResp);
                URL link = new URL(urlInput);
                if (!isHTML) {
                    String fileName = reverseString(urn);
                    fileName = (fileName.substring(0, fileName.indexOf("/")));
                    fileName = reverseString(fileName);

                    File file = new File(urlInput);
                    System.out.println("absl"+file);
                    System.out.println("flname"+file.getName());
                    tasks.add(new DownloadFileTask(urlInput, "./" + "tes"));
                    System.out.println("File di tangguhkan");
//                    downloadFile(link, "./" + fileName);
//                    System.out.println("Sedang mengunduh file");
                    continue;
                }

                Pattern pat = Pattern.compile("<a[^>]*>([^<]+)<\\/a>");
                Matcher mat = pat.matcher(bResp);

                while (mat.find())
                    listLinks = listLinks.concat((mat.group()) + "\n");

                System.out.println(isHTML);
                System.out.println(listLinks);
                socket.close();

            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void parseURL(String uri) {
        if(uri.startsWith("http://") || uri.startsWith("https://"))
            uri = uri.substring(uri.indexOf("/") + 2);

        if(uri.startsWith("https://")){
            protocol = 1;
        }else{
            protocol = 0;
        }
        if (uri.contains("/")) {
            url = uri.substring(0, uri.indexOf("/"));
            urn = uri.substring(url.length());
        }
        else {
           url = uri;
        }
    }
    public static void downloadFile(URL url, String fileName) throws IOException {
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
    public static String reverseString(String str){
        char ch[]=str.toCharArray();
        String rev="";
        for(int i=ch.length-1;i>=0;i--){
            rev+=ch[i];
        }
        return rev;
    }
    public static void getHeader(String url_download) {
        try {
            URL obj = new URL(url_download);
            URLConnection conn = obj.openConnection();
            Map<String, List<String>> map = conn.getHeaderFields();

            System.out.println("Printing Response Header...\n");

            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                System.out.println("Key : " + entry.getKey()
                        + " ,Value : " + entry.getValue());
            }

            System.out.println("\nGet Response Header By Key ...\n");
            String server = conn.getHeaderField("Server");

            if (server == null) {
                System.out.println("Key 'Server' is not found!");
            } else {
                System.out.println("Server - " + server);
            }

            System.out.println("\n Done");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

