package kmeans;

import javax.xml.parsers.*;

import java.io.*;
import java.util.ArrayList;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 * 	用于读取xml中的681个武将 
 *	<p>Description: 利用DOM解析技术，解析XML文档信息
 *	<p>Company: cstor
 *	
 *	@author zhuxy
 *	2016年7月19日 下午2:31:41
 */
public class DomParser {

	private ArrayList<General> generals = new ArrayList<General>();

	/**
	 * 通过DOM解析XML文件获取所有的武将对象信息
	 * @return XML文件中的所有武将的对象，返回的以General为类型的ArrayList泛型数组
	 */
	public ArrayList<General> prepare() {
		// get dom解析器工厂  
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		// get dom 解析器  
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		// 解析文档  
		Document doc = null;
		try {
			doc = builder.parse(new File("./tmp/general.xml"));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 取得根节点  
		Element generalList = doc.getDocumentElement();
		// 得到所有row节点  
		NodeList nodeList = generalList.getElementsByTagName("Row");
		// 便利所有row节点  
		for (int i = 1; i < nodeList.getLength(); i++) {

			// System.out.println("------------the " + i + " element--------------");

			Node row = nodeList.item(i);
			// 取得所有Data数据  
			NodeList attList = row.getChildNodes();
			// 取得数据中的各个部分, 并加入ArrayList中  
			generals.add(new General(Tool.xingji(attList.item(1).getTextContent()), attList.item(3).getTextContent(),
					Integer.parseInt(attList.item(5).getTextContent()), Integer.parseInt(attList.item(7).getTextContent()),
					Integer.parseInt(attList.item(9).getTextContent()), Integer.parseInt(attList.item(11).getTextContent()),
					Tool.change(attList.item(13).getTextContent()), Tool.change(attList.item(15).getTextContent()),
					Tool.change(attList.item(17).getTextContent()), Tool.change(attList.item(19).getTextContent()),
					Tool.change(attList.item(21).getTextContent()), Integer.parseInt(attList.item(23).getTextContent()),
					Integer.parseInt(attList.item(25).getTextContent()), Integer.parseInt(attList.item(27).getTextContent()),
					Integer.parseInt(attList.item(29).getTextContent()), Integer.parseInt(attList.item(31).getTextContent())));

			// the next pharse is only to output the item in xml file, in order to check the right of Dom parse
//			System.out.println(" 星级:" + Tool.xingji(attList.item(1).getTextContent()) + " 姓名:" + attList.item(3).getTextContent() + " 统率:"
//					+ attList.item(5).getTextContent() + " 武力:" + attList.item(7).getTextContent() + " 智力:" + attList.item(9).getTextContent() + " 政治:"
//					+ attList.item(11).getTextContent() + "枪兵:" + Tool.change(attList.item(13).getTextContent()) + " 戟兵:"
//					+ Tool.change(attList.item(15).getTextContent()) + " 弩兵:" + Tool.change(attList.item(17).getTextContent()) + " 骑兵:"
//					+ Tool.change(attList.item(19).getTextContent()) + " 兵器:" + Tool.change(attList.item(21).getTextContent()) + " 统武:"
//					+ attList.item(23).getTextContent() + " 统智:" + attList.item(25).getTextContent() + " 统武智:" + attList.item(27).getTextContent() + " 统武智政:"
//					+ attList.item(29).getTextContent() + " 50级工资:" + attList.item(31).getTextContent() + " ");
//			
		}
		return generals;

	}
}
