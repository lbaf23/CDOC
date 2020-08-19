package com.example.myproject.service;



import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.tomcat.util.codec.binary.Base64;
import org.w3c.dom.Document;

import sun.misc.BASE64Decoder;

/**
 * 一些文件的操作，根目录为src
 *
 */
public class FileOperate {
	public final static String imageTempPath = "DocSrc/temp/image";
	
	/**
	 * .doc 文件转换为html
	 * 使用poi的 hwpf 包
	 * @param docPath doc的完整路径
	 * @param htmlPath html的完整路径
	 */
	public static void Word2003ToHtml(String docPath,String htmlPath) throws IOException, TransformerException, ParserConfigurationException {
        final String imagepath = imageTempPath;
        final String file = docPath;
        InputStream input = new FileInputStream(new File(file));
        HWPFDocument wordDocument = new HWPFDocument(input);
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
        wordToHtmlConverter.setPicturesManager(new PicturesManager() {
            public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches, float heightInches) {
                File imgPath = new File(imagepath);
                if(!imgPath.exists()) {
                    imgPath.mkdirs();
                }
                File file = new File(imagepath + suggestedName);
                try {
                    OutputStream os = new FileOutputStream(file);
                    os.write(content);
                    os.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return imagepath + suggestedName;
            }
        });
        
        wordToHtmlConverter.processDocument(wordDocument);
        Document htmlDocument = wordToHtmlConverter.getDocument();
        
        File htmlFile = new File(htmlPath);
        OutputStream outStream = new FileOutputStream(htmlFile);
        
        //也可以使用字符数组流获取解析的内容
//        ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
//        OutputStream outStream = new BufferedOutputStream(baos);

        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(outStream);

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer serializer = factory.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        
        serializer.transform(domSource, streamResult);

        //也可以使用字符数组流获取解析的内容
//        String content = baos.toString();
//        System.out.println(content);
//        baos.close();
        outStream.close();
    }
	/**
	 * 将html文件中图片的src换成base64码并保存
	 * @param fileDir 文件目录
	 * @param fileName 文件名
	 * @param outPath 输出的html完整地址
	 * @return
	 */
	public static boolean replaceAllPicSrcToBase64(String fileDir,String fileName,String outPath) {
		try {
			String line = "";
			File f = new File(fileDir+"/"+fileName);
			
			File out = new File(outPath);
			if(!out.exists())
				out.createNewFile();

			FileWriter fw = new FileWriter(out.getPath(),true);
			
			BufferedReader reader = new BufferedReader(new FileReader(f));
			while ((line = reader.readLine()) != null) {
				int index = line.indexOf("<img src=");
				if(index >= 0) {
					String head = line.substring(0,index+10);
					String sc = line.substring(index+10);
					int end = sc.indexOf("\"");
					String tail = sc.substring(end);
					String imgPath = sc.substring(0,end);
					String imgContent = FileOperate.getBase64File(imgPath);
					line = head+imgContent+tail;
				}
				fw.write(line+"\n");
			}
			fw.close();
			reader.close();
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	
	
	
	

	/**
	 * 获取文件内容，返回String类型，文件不存在则抛出异常
	 * @param filePath 文件路径
	 * @return 文件内容
	 * @throws Exception 文件不存在抛出异常
	 */
	public static String getFileContent(String filePath,boolean withLines) throws Exception {
		StringBuilder content = new StringBuilder();
		String line = "";
		int num = 0;
		File f = new File(filePath);
		BufferedReader reader = new BufferedReader(new FileReader(f));
		while ((line = reader.readLine()) != null) {
			content.append(line);
			content.append("\n");
			num ++;
		}
		reader.close();
		if(withLines)
			return num+":"+content.toString();
		else
			return content.toString();
	}

	/**
	 * 返回指定路径图片内容的base64编码，不包含图片名称和格式信息
	 * @param filePath 图片路径
	 * @return 编码
	 * @throws Exception 文件不存在则抛出异常
	 */
	public static String getBase64FileContent(String filePath) throws Exception{
		InputStream in = null;
		byte[] data = null;
		in = new FileInputStream(filePath);
		data = new byte[in.available()];
		in.read(data);
		in.close();
		return new String(Base64.encodeBase64(data));
	}
	
	/**
	 * 获取指定路径文件内容的完整base64编码，包含前缀和格式
	 * @param filePath
	 * @return
	 */
	public static String getBase64File(String filePath) throws Exception{
		String c = getBase64FileContent(filePath);
		return "data:image/"+getFileForm(filePath)+";base64,"+c;
	}

	/**
	 * 写入指定文件内容，若文件不存在则创建,写入失败返回false，成功返回true，写入失败返回false
	 * @param path 文件路径
	 * @param content 写入内容
	 * @return true或false
	 */
	public static boolean writeFile(String path, String content) {
		try {
			File out = new File(path);
			if(!out.exists())
				out.createNewFile();

			FileWriter fw = new FileWriter(out.getPath(),true);
			fw.write(content);
			fw.close();
	        return true;
		} catch(Exception e) {
			// e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将获取的base64编码文件解码为图片并保存
	 * @param path 保存的指定路径
	 * @param base64 图片的base64编码，不包括图片的名称和格式部分
	 * @return 保存成功返回true，失败返回false
	 */
	public static boolean writeBase64File(String path, String base64) {
		if (base64 == null)
			return false;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			File f = new File(path);
			if(f.exists()) {
				f.delete();
			}
			byte[] b = decoder.decodeBuffer(base64);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					b[i] += 256;
				}
			}
			OutputStream out = new FileOutputStream(path);
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 获取文件格式，不包含.  若无后缀则返回""
	 * @param file 文件
	 * @return 文件后缀
	 */
	public static String getFileForm(File file) {
		try {
			String t = file.getName();
			t = t.substring(t.indexOf(".") + 1);
			if(t.equals(""))
				return t;
			return t;
		}catch(Exception e) {
			return "";
		}
	}
	public static String getFileForm(String path) {
		try {
			path = path.substring(path.indexOf(".") + 1);
			if(path.equals(""))
				return path;
			return path;
		}catch(Exception e) {
			return "";
		}
	}
	/**
	 * 获取文件名
	 * @param path
	 * @return
	 */
	public static String getFileName(String path) {
		try {
			File f = new File(path);
			String name = f.getName();
			if(name.contains("."))
				return name.substring(name.indexOf("."));
			return name;
		}catch(Exception e) {
			return "";
		}
	}
	
	/**
	 * 将文件内容转换为byte[]格式，不存在则返回null
	 * @param file 文件
	 * @return byte[]
	 */
	public static byte[] fileToByte(File file) {
		byte[] data = null;
		try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            int len;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }

            data = baos.toByteArray();

            fis.close();
            baos.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return data;
	}
	
	
	public static boolean renameFile(String path,String newPath) {
		File f = new File(path);
		File n = new File(newPath);
		return f.renameTo(n);
	}

}
