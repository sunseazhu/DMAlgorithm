package cn.cstor.kmeans;

/**
 *工具类:	<br>
 *	(1)用于转换武将的星级为数字等操作;<br>
 *	(2)计算两个武将对象之间的欧式距离<br>
 *<p>
 *	Description:此类在读取武将信息的时候将武将对象中的等级信息转换成对应的数字；<br>
 *					输出武将对象时，将对应的星级对应的数据转换成星级<br>
 *<p>Company:cstor	<br>
 *	
 *	@author zhuxy
 *	2016年7月19日 下午2:14:17
 */

public class Tool {
	//将各种武器的精通程度转为数字  
	public static int change(String str) {
		int result = str.equals("精") ? 4 : (str.equals("神") ? 3 : (str.equals("通") ? 2 : 1));
		return result;
	}

	//将星级转为数字  
	public static int xingji(String str) {

		if (str.equals("★★★★★ ")) {
			return 5;
		}
		int result = str.equals("★★★★★") ? 5 : (str.equals("★★★★") ? 4 : (str.equals("★★★") ? 3 : (str.equals("★★") ? 2 : 1)));
		return result;
	}

	//反转 将各种武器的数字转为精通程度  
	public static String dchange(int str) {
		String result = str == 4 ? "精" : (str == 3 ? "神" : (str == 2 ? "通" : "疏"));
		return result;
	}

	//反转 将数字转为星级  
	public static String dxingji(int str) {
		String result = str == 5 ? "★★★★★" : (str == 4 ? "★★★★" : (str == 3 ? "★★★" : (str == 2 ? "★★" : "★")));
		return result;
	}

	//计算欧式距离 传入两个将军对象。。  
	public static double juli(General g1, General g2) {
		double result = (Double) Math.sqrt(StrictMath.pow(g1.getRender() - g2.getRender(), 2) + StrictMath.pow(g1.getTongshai() - g2.getTongshai(), 2)
				+ StrictMath.pow(g1.getWuli() - g2.getWuli(), 2) + StrictMath.pow(g1.getZhili() - g2.getZhili(), 2)
				+ StrictMath.pow(g1.getPolic() - g2.getPolic(), 2) + StrictMath.pow(g1.getQiangbin() - g2.getQiangbin(), 2)
				+ StrictMath.pow(g1.getQibin() - g2.getQibin(), 2) + StrictMath.pow(g1.getJibin() - g2.getJibin(), 2)
				+ StrictMath.pow(g1.getNubin() - g2.getNubin(), 2) + StrictMath.pow(g1.getBinqi() - g2.getBinqi(), 2)
				+ StrictMath.pow(g1.getTongwu() - g2.getTongwu(), 2) + StrictMath.pow(g1.getTongzhi() - g2.getTongzhi(), 2)
				+ StrictMath.pow(g1.getTongwuzhizheng() - g2.getTongwuzhizheng(), 2) + StrictMath.pow(g1.getTongwuzhi() - g2.getTongwuzhi(), 2)
				+ StrictMath.pow(g1.getSalary() - g2.getSalary(), 2));
		return result;
	}
}