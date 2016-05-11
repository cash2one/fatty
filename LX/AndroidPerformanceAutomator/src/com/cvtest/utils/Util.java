package com.cvtest.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Util {
    private File resultFile;
    private String time;

    // get current time
    public String getCurrentTime(int str) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        SimpleDateFormat dateFormat = null;
        if (str == 1) {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else if (str == 2) {
            dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        }
        time = dateFormat.format(calendar.getTime());
        return time;
    }

    // write data into the file
    public void writeData(String[] scores, String fileName, String[] tile) {
        try {
            String fn = "/mnt/sdcard/Autoperf/Performance_" + fileName + "_Result.csv";
            FileWriter out = null;
            if (out == null) {
                resultFile = new File(fn);
                boolean fileExists = resultFile.exists();
                if (!fileExists) {
                    resultFile.getParentFile().mkdirs();
                    resultFile.createNewFile();
                }
                out = new FileWriter(resultFile, true);
                if (!fileExists) {
                    String header = createTileLine(tile);
                    out.write(header);
                    out.write("\n");
                }
            }
            String extras = createContentLine(scores);
            out.write(extras);
            out.write("\n");
            out.flush();
            out.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // create the tile
    private String createTileLine(String[] BenchmarkExtraKeys) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < BenchmarkExtraKeys.length; i++) {
            if (i != BenchmarkExtraKeys.length - 1) {
                result.append(BenchmarkExtraKeys[i]).append(",");
            } else {
                result.append(BenchmarkExtraKeys[i]);
            }
        }
        return result.toString();
    }

    // create the content
    private String createContentLine(final String[] BenchmarkDetail) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < BenchmarkDetail.length; i++) {
            if (i != BenchmarkDetail.length - 1) {
                result.append(BenchmarkDetail[i]).append(",");
            } else {
                result.append(BenchmarkDetail[i]);
            }
        }
        return result.toString();
    }

    // copy file
    public void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    // get Vellamo data
    public String[] getVellamoData(String fileString, String regex, int length) {
        String allString = null;
        BufferedReader reader = null;
        String[] vellamoData = new String[length];
        Pattern pattern;
        Matcher match;

        File file = new File(fileString);
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                allString = allString + tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        pattern = Pattern.compile(regex);
        match = pattern.matcher(allString);
        int i = 1;
        int j = 0;
        while (match.find()) {
            if (i != 2) {
                vellamoData[j] = match.group(1);
                j++;
            }
            i++;
        }
        return vellamoData;
    }

    // get 3DMark data
    public String[] get3DMarkData(String fileString) {

        String[] threeDMarkData = new String[7];
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(fileString));
            NodeList vList = doc.getElementsByTagName("value");
            NodeList prList = doc.getElementsByTagName("primary_result");

            int countThreeDMarkData = 0;

            for (int j = 0; j < vList.getLength(); j++) {
                if (vList.item(j).getAttributes().getLength() == 1) {
                    threeDMarkData[countThreeDMarkData] = vList.item(j)
                            .getTextContent();
                    countThreeDMarkData++;
                }
            }
            String tmpString;
            tmpString = threeDMarkData[countThreeDMarkData - 1];
            threeDMarkData[countThreeDMarkData - 1] = threeDMarkData[countThreeDMarkData - 3];
            threeDMarkData[countThreeDMarkData - 3] = tmpString;

            for (int i = 1; i < prList.getLength(); i++) {
                threeDMarkData[countThreeDMarkData] = prList.item(i).getTextContent();
                countThreeDMarkData++;
            }
            threeDMarkData[countThreeDMarkData] = prList.item(0).getTextContent();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (SAXException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return threeDMarkData;
    }

    public String calculateTotal(String[] args) {
        int s = 0;
        int t = 0;
        for (String score : args) {
            try {
                s = Integer.parseInt(score);
            } catch (Exception e) {
                s = 0;
            }
            t = t + s;
        }
        return String.valueOf(t);
    }
}
