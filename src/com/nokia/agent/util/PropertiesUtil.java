package com.nokia.agent.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;


public class PropertiesUtil {
    /**
     * 读取单个属性
     *
     * @param filePath
     * @param key
     * @return
     */
    public static String getProperty(String filePath, String key) {
        Properties props = new Properties();
        InputStream is = null;

        try {
            is = new BufferedInputStream(new FileInputStream(filePath));
            props.load(is);

            String value = props.getProperty(key);

            return value;
        } catch (FileNotFoundException e) {
            e.printStackTrace();

            return null;
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e2) {
            }
        }
    }

    /**
     * 读取全部属性
     *
     * @param filePath
     */
    public static HashMap<String, String> getProperties(String filePath) {
        Properties props = new Properties();
        InputStream is = null;
        HashMap<String, String> properties = new HashMap<String, String>();

        try {
            is = new BufferedInputStream(new FileInputStream(filePath));
            props.load(is);

            Enumeration<?> enum1 = props.propertyNames();

            while (enum1.hasMoreElements()) {
                String key = (String) enum1.nextElement();
                String value = props.getProperty(key);
                properties.put(key, value);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e2) {
            }
        }

        return properties;
    }

    /**
     * 写入单个属性
     *
     * @param filePath
     * @param paraKey
     * @param paraValue
     */
    public static void writeProperty(String filePath, String paraKey,
        String paraValue, String comments) {
        Properties props = new Properties();
        File propertiesFile = null;
        OutputStream os = null;

        try {
            propertiesFile = new File(filePath);

            if (!propertiesFile.exists()) {
                propertiesFile.createNewFile();
            }

            os = new FileOutputStream(filePath);
            props.setProperty(paraKey, paraValue);
            props.store(os, comments);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * 写入多个属性
     * @param filePath
     * @param properties
     */
    public static void writeProperties(String filePath,
        HashMap<String, String> properties, String comments) {
        Properties props = new Properties();
        File propertiesFile = null;
        OutputStream os = null;

        try {
            propertiesFile = new File(filePath);

            if (!propertiesFile.exists()) {
                propertiesFile.createNewFile();
            }

            os = new FileOutputStream(propertiesFile);

            Iterator<String> it = properties.keySet().iterator();

            while (it.hasNext()) {
                String key = it.next();
                props.setProperty(key, properties.get(key));
            }

            props.store(os, comments);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
            }
        }
    }
    
    /**
     * 更新已有属性
     * @param filePath
     * @param updateprops
     */
    public static void upateProperties(String filePath, HashMap<String, String> updateprops, String comments) {
    	 Properties props = new Properties();
    	 InputStream is = null;
         OutputStream os = null;
         
         try {
        	 ////先读取原来的属性
        	 is = new BufferedInputStream(new FileInputStream(filePath));
        	 props.load(is);
        	 
        	 //将原来的属性替换为新属性，没有被替换的保持不变
             Iterator<String> it = updateprops.keySet().iterator();
        	 while (it.hasNext()) {
        		 String key = it.next();
        		 props.setProperty(key, updateprops.get(key));
        	 }

             os = new FileOutputStream(filePath);
             props.store(os, comments);
             
         } catch (IOException e) {
             e.printStackTrace();
         } finally {
             try {
                 if (os != null) {
                     os.close();
                 }
                 if (is != null) {
                	 is.close();
                 }
             } catch (IOException e) {
             }
         }
    }

    public static void main(String[] args) {
         HashMap<String, String> properties = new HashMap<String, String>();
         properties.put("port", "2345");
         PropertiesUtil.writeProperties("conf.properties", properties, "agent config");
//         PropertiesUtil.upateProperties("conf.properties", properties, "agent config");
    	
    	
    }
}
