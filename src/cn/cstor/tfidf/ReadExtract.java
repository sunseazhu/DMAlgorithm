package cn.cstor.tfidf;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * 这是 TF-IDF算法
 *<p>
 *	Description:主要是实现从文档中自动抽取关键词的功能	<br>
 *<p>
 *	Company:cstor	<br>
 *<p>
 *	Reference:http://www.cnblogs.com/ywl925/archive/2013/08/26/3275878.html	<br>
 *	
 *	@author zhuxy
 *	2016年7月19日 上午10:26:01
 */
public class ReadExtract {

	private static ArrayList<String> FileList = new ArrayList<String>(); // the list of file

	/**
	 * get list of file for the directory, including sub-directory of it
	 * @param filepath	文件的输入路径
	 * @return the all files absolute path under the dictionary of filepath
	 * @throws FileNotFoundException 没找到文件异常
	 * @throws IOException IO异常
	 */
	public static List<String> readDirs(String filepath) throws FileNotFoundException, IOException {
		try {
			File file = new File(filepath);
			if (!file.isDirectory()) {
				System.out.println("输入的[]");
				System.out.println("filepath:" + file.getAbsolutePath());
			} else {
				String[] flist = file.list();
				for (int i = 0; i < flist.length; i++) {
					File newfile = new File(filepath + "\\" + flist[i]);
					if (!newfile.isDirectory()) {
						FileList.add(newfile.getAbsolutePath());
					} else if (newfile.isDirectory()) //if file is a directory, call ReadDirs
					{
						readDirs(filepath + "\\" + flist[i]);
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return FileList;
	}

	/**
	 * for each absolute path of file, the method read the file content, and then return the file content int the style of string
	 * @param file 输入文件路径
	 * @return the file content of file absolute path
	 * @throws FileNotFoundException 没找到文件异常
	 * @throws IOException IO异常
	 */
	public static String readFile(String file) throws FileNotFoundException, IOException {
		StringBuffer strSb = new StringBuffer(); //String is constant, StringBuffer can be changed.
		InputStreamReader inStrR = new InputStreamReader(new FileInputStream(file), "gbk"); //byte streams to character streams
		BufferedReader br = new BufferedReader(inStrR);
		String line = br.readLine();
		while (line != null) {
			strSb.append(line).append("\r\n");
			line = br.readLine();
		}

		return strSb.toString();
	}

	/**
	* word segmentation: to split the file content to each word 
	* @param file 文件
	* @return the words set in one file
	* @throws IOException IO异常
	*/
	public static ArrayList<String> cutWords(String file) throws IOException {

		ArrayList<String> words = new ArrayList<String>();
		String text = ReadExtract.readFile(file);
		IKAnalyzer analyzer = new IKAnalyzer();
		words = analyzer.split(text);

		return words;
	}

	/**
	 * term frequency in a file, times for each word
	 * @param cutwords 分词数组
	 * @return  key-word; value-times
	 */
	public static HashMap<String, Integer> normalTF(ArrayList<String> cutwords) {
		HashMap<String, Integer> resTF = new HashMap<String, Integer>();

		for (String word : cutwords) {
			if (resTF.get(word) == null) {
				resTF.put(word, 1);
				System.out.println(word);
			} else {
				resTF.put(word, resTF.get(word) + 1);
				System.out.println(word.toString());
			}
		}
		return resTF;
	}

	/**
	 * Relative Word Frequency: term frequency in a file, frequency of each word
	 * @param cutwords 读取文件内容分词的动态数组
	 * @return this is relative word frequency-the real TF
	 */
	public static HashMap<String, Float> tf(ArrayList<String> cutwords) {
		HashMap<String, Float> resTF = new HashMap<String, Float>();

		int wordLen = cutwords.size();// obtain the size of the array of cutwords that is the number of word the file contain
		HashMap<String, Integer> intTF = ReadExtract.normalTF(cutwords);

		Iterator iter = intTF.entrySet().iterator(); //iterator for that get from TF
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			resTF.put(entry.getKey().toString(), Float.parseFloat(entry.getValue().toString()) / wordLen);
			System.out.println(entry.getKey().toString() + " = " + Float.parseFloat(entry.getValue().toString()) / wordLen);
		}
		return resTF;
	}

	/**
	* tf times for file
	* @param dirc 目录
	* @return key-file; value-the pair of key(word) and value(normTF);
	* @throws IOException IO异常
	*/

	public static HashMap<String, HashMap<String, Integer>> normalTFAllFiles(String dirc) throws IOException {
		HashMap<String, HashMap<String, Integer>> allNormalTF = new HashMap<String, HashMap<String, Integer>>();

		List<String> filelist = ReadExtract.readDirs(dirc);
		for (String file : filelist) {
			HashMap<String, Integer> dict = new HashMap<String, Integer>();
			ArrayList<String> cutwords = ReadExtract.cutWords(file); //get cut word for one file

			dict = ReadExtract.normalTF(cutwords);
			allNormalTF.put(file, dict);
		}
		return allNormalTF;
	}

	/**
	 * tf for all file
	 * @param dirc 目录
	 * @return allTF TF计算结果
	 * @throws IOException IO异常
	 */ 
	public static HashMap<String, HashMap<String, Float>> tfAllFiles(String dirc) throws IOException {
		HashMap<String, HashMap<String, Float>> allTF = new HashMap<String, HashMap<String, Float>>();
		List<String> filelist = ReadExtract.readDirs(dirc);

		for (String file : filelist) {
			HashMap<String, Float> dict = new HashMap<String, Float>();
			ArrayList<String> cutwords = ReadExtract.cutWords(file); //get cut words for one file

			dict = ReadExtract.tf(cutwords);
			allTF.put(file, dict);
		}
		return allTF;
	}

	/**
	 * the idf calculate
	 * @param all_tf HashMap类型，所有的TF度量值
	 * @return 返回IDF的计算结果
	 */
	public static HashMap<String, Float> idf(HashMap<String, HashMap<String, Float>> all_tf) {
		HashMap<String, Float> resIdf = new HashMap<String, Float>();
		HashMap<String, Integer> dict = new HashMap<String, Integer>();
		int docNum = FileList.size();

		for (int i = 0; i < docNum; i++) {
			HashMap<String, Float> temp = all_tf.get(FileList.get(i));
			Iterator<Entry<String, Float>> iter = temp.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, Float> entry = iter.next();
				String word = entry.getKey().toString();
				if (dict.get(word) == null) {
					dict.put(word, 1);
				} else {
					dict.put(word, dict.get(word) + 1);
				}
			}
		}
		System.out.println("IDF for every word is:");
		Iterator<Entry<String, Integer>> iter_dict = dict.entrySet().iterator();
		while (iter_dict.hasNext()) {
			Entry<String, Integer> entry = iter_dict.next();
			float value = (float) Math.log(docNum / Float.parseFloat(entry.getValue().toString()));
			resIdf.put(entry.getKey().toString(), value);
			System.out.println(entry.getKey().toString() + " = " + value);
		}
		return resIdf;
	}

	/**
	 * the tf-idf calculate
	 * @param all_tf 所有的TF计算值
	 * @param idfs 所有的IDF的计算值
	 */
	public static void tf_idf(HashMap<String, HashMap<String, Float>> all_tf, HashMap<String, Float> idfs) {

		HashMap<String, HashMap<String, Float>> resTfIdf = new HashMap<String, HashMap<String, Float>>();

		int docNum = FileList.size();
		for (int i = 0; i < docNum; i++) {
			String filepath = FileList.get(i);
			HashMap<String, Float> tfidf = new HashMap<String, Float>();
			HashMap<String, Float> temp = all_tf.get(filepath);
			Iterator<Entry<String, Float>> iter = temp.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, Float> entry = iter.next();
				String word = entry.getKey().toString();
				Float value = (float) Float.parseFloat(entry.getValue().toString()) * idfs.get(word);
				tfidf.put(word, value);
			}
			resTfIdf.put(filepath, tfidf);
		}
		System.out.println("TF-IDF for Every file is :");
		DisTfIdf(resTfIdf);
	}

	/**
	 * only output the info to control, and other do nothing
	 * @param tfidf 输入为TF_IDF计算结果；用于本方法（输出）的输入
	 */
	public static void DisTfIdf(HashMap<String, HashMap<String, Float>> tfidf) {
		Iterator<Entry<String, HashMap<String, Float>>> iter1 = tfidf.entrySet().iterator();
		while (iter1.hasNext()) {
			Entry<String, HashMap<String, Float>> entrys = iter1.next();
			System.out.println("FileName: " + entrys.getKey().toString());
			System.out.print("{");
			HashMap<String, Float> temp = entrys.getValue();
			Iterator<Entry<String, Float>> iter2 = temp.entrySet().iterator();
			while (iter2.hasNext()) {
				Entry<String, Float> entry = iter2.next();
				System.out.print(entry.getKey().toString() + " = " + entry.getValue().toString() + ", ");
			}
			System.out.println("}");
		}

	}

	public static void main(String[] args) throws IOException {
		String file = "D:/testfiles";

		HashMap<String, HashMap<String, Float>> all_tf = tfAllFiles(file);
		System.out.println();
		HashMap<String, Float> idfs = idf(all_tf);
		System.out.println();
		tf_idf(all_tf, idfs);
	}
}