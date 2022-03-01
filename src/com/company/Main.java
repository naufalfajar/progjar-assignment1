package com.company;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static String url;
    private static String urn;
    private final Collection<Runnable> tasks = new ArrayList<Runnable>();
    public static void main(String[] args) {
        // write your code here
        Paralel tasks = new Paralel();

        while(true){
            Socket socket;
            String listLinks = "";
            try {
                socket = new Socket("monta.if.its.ac.id", 80);
                BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
                BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());

                Scanner read = new Scanner(System.in);
                System.out.print("URI :");
                String urlInput = read.nextLine();
                parseURL(urlInput);
                String temp = "GET "+ urn +" HTTP/1.1\r\nHost: "+ url +"\r\n\r\n";
                bos.write(temp.getBytes(StandardCharsets.UTF_8));
                bos.flush();

                int bufferSize = 100;
                byte[] resp = new byte[bufferSize];
                resp = bis.readAllBytes();
                String bResp = new String(resp);
                boolean isHTML = bResp.contains("Content-Type: text/html");

                URL link = new URL(urlInput);
                if (!isHTML){
                    String fileName = reverseString(urn);
                    fileName = (fileName.substring(0, fileName.indexOf("/")));
                    fileName = reverseString(fileName);
                    File file1 = new File(urlInput);
//                    downloadFile(link, "./"+fileName);

                    System.out.println("Sedang mengunduh file");
                }

                Pattern pat = Pattern.compile("<a[^>]*>([^<]+)<\\/a>");
                Matcher mat = pat.matcher(bResp);
                while (mat.find())
                    listLinks = listLinks.concat((mat.group())+"\n");


    //            String[] first_line = bResp.split(" ");
    //            int c = bis.read(bResp);
    //            while(c!=-1){
    //                resp +=
    //            }
    //            System.out.println(new String(bResp));
    //            System.out.println("Status Code : "+ first_line[1]);

    //            String dest = "D:/a.txt";
    //            Path targetPath = Paths.get(dest);
    //            Files.write(targetPath, resp, StandardOpenOption.CREATE);
    //            Pattern pat = Pattern.compile("<a[^>]*>([^<]+)<\\/a>");
    //
    //            Matcher mat = pat.matcher(bResp);
    //            System.out.println(mat.matches());
    //            while (mat.find())
    //                System.out.println("Match: " + mat.group());

                System.out.println(isHTML);

    //            listLinks = "<ul>"+listLinks+"</ul>";
    //            System.out.println(listLinks);
                socket.close();
    //        }
    //        try {
    //            FileInputStream fis = new FileInputStream("D:\\ITS\\Semester 6\\Progjar\\test.txt");
    //            DataInputStream dis = new DataInputStream(fis);
    //
    //            byte[] c;
    //            c = dis.readAllBytes();
    //            String tmp = new String(c);
    //
    //            dis.close();
    //            fis.close();
    //
    //            System.out.println(tmp);
            } catch (IOException ex){
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null,ex);
            }
        }
    }

    public static void parseURL(String uri) {
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
}

