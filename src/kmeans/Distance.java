package kmeans;


/**
 *	计算各个武将对象到中心武将的距离 	<br>
 *<p>
 *	Description:	<br>
 *<p>
 *	Company:cstor	<br>
 *	
 *	@author zhuxy
 *	2016年7月19日 下午2:05:05
 */
public class Distance implements Comparable<Object>{
	
	int dest;// 目的  
	int source;// 源  
	double dist;// 欧式距离  

	
	public int getDest() {
		return dest;
	}

	public void setDest(int dest) {
		this.dest = dest;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public double getDist() {
		return dist;
	}

	public void setDist(double dist) {
		this.dist = dist;
	}

	/** 
	 * 计算源和目的的距离 
	 * @param dest 目的武将 
	 * @param source 源武将 
	 * @param dist 两者间的距离 
	 */
	public Distance(int dest, int source, double dist) {
		this.dest = dest;
		this.source = source;
		this.dist = dist;
	}
/**
 * 空的构造方法
 */
	public Distance() {
	}
	
	
	/**
	 *  实现距离的之间的比较，适用于treeset函数
	 *  @param o Object类型，用于比较的参数
	 */
	@Override
	public int compareTo(Object o) {

		Distance distance=null;
		if (o instanceof Distance) {
			distance = (Distance) o;
		}
		
		
		return this.dist>distance.dist?1:-1;
		
	}
	
	/*
	 * 输出：
	 * -1
		2
	 */
//	public static void main(String[] args) {
//		Distance distance1=new Distance(-1,2,2.5);
//		Distance distance2=new Distance(-1,4,4.5);
//		Distance distance3=new Distance(-1,6,6.5);
//		System.out.println(distance1.compareTo(distance2));
//		
//		TreeSet<Distance> treeSet=new TreeSet<>();
//		treeSet.add(distance1);
//		treeSet.add(distance2);
//		treeSet.add(distance3);
//		System.out.println(treeSet.first().source);
//	}
}