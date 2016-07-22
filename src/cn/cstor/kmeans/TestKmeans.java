package cn.cstor.kmeans;

/**
 * 	k-means测试类<br>
 *<p>
 *	Description:仅用于测试k-means算法的main方法 <br>
 *<p>
 *	Company: cstor	<br>
 *
 *	@author zhuxy
 *	2016年7月20日 下午4:40:23
 */
public class TestKmeans {

	public static void main(String[] args) {
		Kmeans kmeans = new Kmeans();
		kmeans.outputtoFileTxt(kmeans.getResult());
	}

}