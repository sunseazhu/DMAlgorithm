package cn.cstor.kmeans;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


/**
 * 	k-means算法的核心类<br>
 * 
 *<p>
 *	Description:该类主要包含如下功能：<br>
 * 	(1)包括类中心随机选择；<br>
 *	(2)通过欧式距离计算聚簇；<br>
 *	(3)计算聚类后子类的类中心；<br>
 *	(4)迭代，直至先后两次聚类的子类不再改变	<br>
 *
 *<p>
 *	Company:cstor	<br>
 *	
 *	@author Administrator
 *	2016年7月19日 下午1:31:41
 */
public class Kmeans {
	public ArrayList<General> allGenerals = null;//用于存放xml中的所有对象
	public int totalNumber = 0;// 得到所有的武将数目  
	public int K = 0;// 假设K=10  

	/**
	 * 负责给allGenerals、totalNumber、K赋值。
	 * <p>此处allGenerals为读取的xml数据；<br>
	 * totalNumber为数据对象总个数；<br>
	 * K为聚类的子类个数<br>
	 */
	public Kmeans() {
		allGenerals = new DomParser().prepare();//返回XMl中所有的数据对象
		totalNumber = allGenerals.size(); //数据对象的总个数
		K = 10; //聚簇的个数
	}

	/**
	 * 随机产生k个不懂的整数值，位于0~totalNumber之间，放置在center集合中，Integer的泛型Set集合
	 * @return 聚类中心的对象
	 */
	public Set<Integer> firstRandom() {
		Set<Integer> center = new HashSet<Integer>();// 聚类中心的点的id,采用set保证不会有重复id  
		Random ran = new Random();
		int roll = ran.nextInt(totalNumber);
		while (center.size() < K) {
			roll = ran.nextInt(totalNumber);
			center.add(roll);
		}
		return center;
	}

	/**
	 * 根据聚类中心初始化聚类信息，质心赋值（整型ID），然后将该质心对应的子类添加到大类中 ，大类为子类构成的动态数组 
	 * @param center 聚类中子类的中心
	 * @return 子类的集合。此时的cluster中，他的质心ID已经赋值，并且已经将质心添加到该类中
	 */
	public ArrayList<Cluster> init(Set<Integer> center) {
		ArrayList<Cluster> cluster = new ArrayList<Cluster>();// 聚类 的数组  
		Iterator<Integer> it = center.iterator();
		while (it.hasNext()) {
			Cluster c = new Cluster();// 代表一个聚类  
			c.setCenter(it.next()); //设置聚类中的子类的质心为center
			cluster.add(c);//将一个子类添加到cluster数组中，cluster数组表示整个聚类，中间的每个元素表示一个小类
			
		}
		return cluster;// 
	}

	/** 
	 * 计算各个武将到各个聚类中心的距离，聚类
	 *  
	 * @param cluster 
	 *            聚类数组,用来聚类的，根据最近原则把武将聚类 
	 * @param center 
	 *            中心点id,用于计算各个武将到中心点的距离 return cluster 聚类后的所有聚类组成的数组 
	 * @return 重新聚类后的子类
	 */
	public ArrayList<Cluster> juLei(Set<Integer> center, ArrayList<Cluster> cluster) {
		ArrayList<Distance> distances = new ArrayList<Distance>();// 存放距离信息,表示每个点到各个中心点的距离组成的数组  
		General source = null;
		General dest = null;
		int id = 0;// 目的节点id  
		int id2 = 0;// 源节点id  
		Object[] p = center.toArray();// p 为聚类中心点id数组  
		boolean flag = false;//控制和质心计算距离的点是不是被选择为质心
		// 分别计算各个点到各个中心点的距离，并将距离最小的加入到各个聚类中，进行聚类  
		for (int i = 0; i < totalNumber; i++) {//非质心点
			// 每个点计算完,并聚类到距离最小的聚类中就清空距离数组  
			distances.clear();
			// 计算到j个类中心点的距离,便利各个中心点  
			for (int j = 0; j < center.size(); j++) {
				// 如果该点不在中心点内 则计算距离  
				if (!(center.contains(i))) {
					flag = true;
					// 计算距离  
					source = allGenerals.get(i);// 某个点 ，此处为外层FOR循环，用于控制每个非质心的点 
					dest = allGenerals.get((Integer) p[j]);// 各个 中心点  
					// 计算距离并存入数组  
					distances.add(new Distance((Integer) p[j], i, Tool.juli(source, dest)));//存入距离对象，如果对于给定非质心点，每个质心都与他计算完成后则可以进行比较
				} else {
					flag = false;
				}
			}//此时非质心的点已经与所有质心的距离计算完毕，保存在distances对象数组中
			
			// 说明计算完某个武将到类中心的距离,开始比较  
			if (flag == true) {
				// 排序比较一个点到各个中心的距离的大小,找到距离最小的武将的 目的id,和源id,  
				// 目的id即类中心点id，这个就归到这个中心点所在聚类中  
				double min = distances.get(0).getDist();// 默认第一个distance距离是最小的  
				// 从1开始遍历distance数组  
				int minid = 0;
				for (int k = 1; k < distances.size(); k++) {
					if (min > distances.get(k).getDist()) {
						min = distances.get(k).getDist();
						id = distances.get(k).getDest();// 目的，即类中心点  
						id2 = distances.get(k).getSource();// 某个武将  
						minid = k;
					} else {//如果最初的0位置就是最小的，那么此时源id和目的id就是0对应的
						id = distances.get(minid).getDest();
						id2 = distances.get(minid).getSource();
					}
				}//此时的的id、id2分别对应的距离最小的质心和源点（非质心，对应for循环中的j）
				
				// 遍历cluster聚类数组，找到类中心点id与最小距离目的武将id相同的聚类，然后将源id添加到该聚类中  
				for (int n = 0; n < cluster.size(); n++) {
					// 如果和中心点的id相同 则setError  
					if (cluster.get(n).getCenter() == id) {
						cluster.get(n).addGeneral(allGenerals.get(id2));// 将与该聚类中心距离最小的武将加入该聚类  
						break;
					}
				}
			}
		}
		return cluster;
	}

	/**
	 * 产生新的聚类中心点id，中心点id为子类序号。  
	 * @return 整型set集合
	 */
	public Set<Integer> updateCenter() {
		Set<Integer> center = new HashSet<Integer>();
		for (int i = 0; i < K; i++) {
			center.add(i);
		}
		return center;
	}

	/**
	 * 更细聚类的子类质心，质心id为子类的序号，并将平均值添加到子类中。
	 * @param cluster 所有的子类，ArrayList类型，泛型中的类型为Cluster
	 * @return 返回所有更新过质心的子类
	 */
	public ArrayList<Cluster> updateCluster(ArrayList<Cluster> cluster) {
		ArrayList<Cluster> result = new ArrayList<Cluster>();
		// 重新产生的新的聚类中心组成的数组  
		// k个聚类进行更新聚类中心  
		for (int j = 0; j < K; j++) {
			ArrayList<General> ps = cluster.get(j).getOfCluster();// 该聚类的所有 武将  
																	// 组成的数组  
			ps.add(allGenerals.get(cluster.get(j).getCenter()));// 同时将该类中心对应的武将加入该武将数组  
			int size = ps.size();// 该聚类的长度大小  
			// 计算和，然后在计算平均值  
			int sumrender = 0, sumtongshai = 0, sumwuli = 0, sumzhili = 0, sumjibin = 0, sumnubin = 0, sumqibin = 0, sumpolic = 0, sumqiangbin = 0,
					sumbinqi = 0, sumtongwu = 0, sumtongzhi = 0, sumtongwuzhi = 0, sumtongwuzhizheng = 0, sumsalary = 0;
			for (int k1 = 0; k1 < size; k1++) {
				sumrender += ps.get(k1).getRender();
				sumtongshai += ps.get(k1).getRender();
				sumwuli += ps.get(k1).getWuli();
				sumzhili += ps.get(k1).getZhili();
				sumjibin += ps.get(k1).getJibin();
				sumnubin += ps.get(k1).getNubin();
				sumqibin += ps.get(k1).getQibin();
				sumpolic += ps.get(k1).getPolic();
				sumqiangbin += ps.get(k1).getQiangbin();
				sumbinqi += ps.get(k1).getBinqi();
				sumtongwu += ps.get(k1).getTongwu();
				sumtongzhi += ps.get(k1).getTongzhi();
				sumtongwuzhi += ps.get(k1).getTongwuzhi();
				sumtongwuzhizheng += ps.get(k1).getTongwuzhizheng();
				sumsalary += ps.get(k1).getSalary();
			}
			// 产生新的聚类，然后加入到聚类数组中  
			Cluster newCluster = new Cluster();
			General general=new General(sumrender / size, sumtongshai / size, sumwuli / size, sumzhili / size, sumjibin / size, sumnubin / size,
					sumqibin / size, sumpolic / size, sumqiangbin / size, sumbinqi / size, sumtongwu / size, sumtongzhi / size, sumtongwuzhi / size,
					sumtongwuzhizheng / size, sumsalary / size);
			
			int centerID=getSubClusterCenter(ps,general);
			newCluster.setCenter(centerID);//设置新的子类中心
			// 将子类的新的中心的对象添加到新的子类中  
			newCluster.addGeneral(allGenerals.get(centerID));
			result.add(newCluster);
		}
		return result;//此时result表示一个大类ArrayList类型，其中的泛型为Cluster表示一个小类，该小类的中心
	}
	
	/**
	 * 用于根据给定的子类和给定的子类中所有对象属性的均值来计算子类的中心id
	 * 
	 * @param ps 子类对象结合数组
	 * @param avgGeneral 子类的对象各属性的均值构成的对象
	 * @return 与虚拟的子类中心对象距离最近的子类对象的id
	 */
	private int getSubClusterCenter(ArrayList<General> ps,General avgGeneral) {
		// TODO Auto-generated method stub
		TreeSet<Distance> distances=new TreeSet<>();
		for(int i=0;i<ps.size();i++){
			int id2 =allGenerals.indexOf(ps.get(i));
			distances.add(new Distance(-1, id2, Tool.juli(ps.get(i), avgGeneral)));
		}
		
		return distances.first().getSource();
	}

	/** 
	 * 计算各个武将到各个更新后的聚类中心的距离，重新聚类 
	 * @param updateCluster 包含子类的个数，每个子类的中心点id（子类编号）、子类的中心点对应的对象属性（虚拟的对象），原子类对象的平均值 
	 * @return 更新后的聚类
	 */
	public ArrayList<Cluster> updateJuLei(ArrayList<Cluster> updateCluster) {
		ArrayList<Distance> distances = new ArrayList<Distance>();// 存放距离信息,表示每个点到各个中心点的距离组成的数组  
		General source = null;
		General dest = null;
		int id = 0;// 目的节点id  
		int id2 = 0;// 源节点id  
		boolean flag = false;
		// 分别计算各个点到各个中心点的距离，并将距离最小的加入到各个聚类中，进行聚类  
		for (int i = 0; i < totalNumber; i++) {
			// 每个点计算完,并聚类到距离最小的聚类中就清空距离数组  
			distances.clear();
			// 计算到j个类中心点的距离,便利各个中心点  
			for (int j = 0; j < updateCluster.size(); j++) {
				flag = true;
				// 计算距离  
				source = allGenerals.get(i);// 某个点  
				dest = updateCluster.get(j).getOfCluster().get(0);// 各个 中心点  
				// 计算距离并存入数组  
				distances.add(new Distance(updateCluster.get(j).getCenter(), i, Tool.juli(source, dest)));
			}
			// 说明计算完某个武将到类中心的距离,开始比较  
			if (flag == true) {
				// 排序比较一个点到各个中心的距离的大小,找到距离最小的武将的 目的id,和源id,  
				// 目的id即类中心点id，这个就归到这个中心点所在聚类中  
				double min = distances.get(0).getDist();// 默认第一个distance距离是最小的  
				// 从1开始遍历distance数组  
				int mid = 0;
				for (int k = 1; k < distances.size(); k++) {
					if (min > distances.get(k).getDist()) {
						min = distances.get(k).getDist();
						id = distances.get(k).getDest();// 目的，即类中心点  
						id2 = distances.get(k).getSource();// 某个武将  
						mid = k;
					} else {
						id = distances.get(mid).getDest();//此处对于同一个j，此值不改变
						id2 = distances.get(mid).getSource();
					}
				}
				// 遍历cluster聚类数组，找到类中心点id与最小距离目的武将id相同的聚类  
				for (int n = 0; n < updateCluster.size(); n++) {
					// 如果和中心点的id相同 则setError  
					if (updateCluster.get(n).getCenter() == id) {
						updateCluster.get(n).addGeneral(allGenerals.get(id2));// 将与该聚类中心距离最小的武将加入该聚类  
					}
				}
			}
		}
		return updateCluster;
	}

	/**
	 * 不断循环聚类直到各个聚类没有重新分配  
	 * @return 返回聚类的最终结果result，为以Cluster为泛型对象的动态数组
	 */
	public ArrayList<Cluster> getResult() {
		ArrayList<Cluster> result = new ArrayList<Cluster>();
		ArrayList<Cluster> temp = new ArrayList<Cluster>();
		boolean flag = false;
		// 得到随机中心点然后进行聚类  
		Set<Integer> center = firstRandom();//产生随机数，每个随机数分别为k个子类的中心武将的id
		result = juLei(center, init(center));//根据中心聚类，完成第一次聚类
		//outputtoFileTxt(result);
		do {
			// 重新聚类  
			ArrayList<Cluster> updateCluster = updateCluster(result);//新的聚类中心 ，中心为子类的序号，小类的中心点的对象各属性的值为上次该小类的平均值 
			temp = updateJuLei(updateCluster);
		
			flag = isEquals(temp, result);
			result = temp;
		} while (!flag);
		return result;
	}

	/**
	 * 用于判断前后两次的聚类结果是不是一样的 	<br>	 * 
	 * 判断标准：每个子类前后两次的类中心不变
	 * 
	 * @param temp 临时聚类结果
	 * @param result 上一次聚类结果
	 * @return temp与result相等时，返回true，表示聚类完成
	 */
	public boolean isEquals(ArrayList<Cluster> temp, ArrayList<Cluster> result) {
		boolean flag = false;
		// 1、第一两者的子类个数肯定需要相等
		if (temp.size() != result.size()) {
			return flag;
		}
		//2、比较每个子类中的其他
		for (Cluster tem : temp) {
			for (Cluster res : result) {
				//2.1 比较两者的类中心是否相等，只要对于temp中的每个类中心，result都有类中心与他对应，就代表两者相等
				if (tem.getCenter() == res.getCenter()) {
					flag = true;
				}
			}
			// 如果找了一轮没找到 则说明两个聚类  
			if (flag == false) {
				return false;
			} else {// 如果找了一轮找到了，那么接着找  
				flag = false;
			}
		}
		//如果代码能进行到这边，说明是true  
		flag = true;
		return flag;
	}

	/**
	 * 重写print方法，让其输入到文本文件中,并且打印到控制台
	 * @param cs 聚类的最终结果cs，子类的对象构成的动态数组
	 */
	public void outputtoFileTxt(ArrayList<Cluster> cs) {
		StringBuilder sBuilder = new StringBuilder();

		sBuilder.append("***************************************");
		for (int i = 0; i < cs.size(); i++) {
			Cluster c = cs.get(i);
			sBuilder.append("-------------------第" + i + "个中心-------------------"+"\n");
			sBuilder.append("center: " + allGenerals.get(c.getCenter())+"\n");
			ArrayList<General> p = c.getOfCluster();

			sBuilder.append("此类中包含对象的个数为：" + p.size()+"\n");

			for (int j = 0; j < p.size(); j++) {
				sBuilder.append("general:" + p.get(j) + "\n");
			}
		}

		System.out.println(sBuilder.toString());
		
		try {
			byte buffer[] = sBuilder.toString().getBytes();
			FileOutputStream wf = new FileOutputStream("./tmp/write.txt");

			wf.write(buffer);
			wf.close(); // 当流写操作结束时，调用close方法关闭流。  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 输出所有的聚类  仅将聚类结果打印到控制台
	 * @param cs 聚类的最终结果cs，子类的对象构成的动态数组
	 */
	public void print(ArrayList<Cluster> cs) {
		System.out.println("***************************************");
		for (int i = 0; i < cs.size(); i++) {
			Cluster c = cs.get(i);
			System.out.println("-------------------第" + i + "个中心-------------------");
			System.out.println("center: " + allGenerals.get(c.getCenter()));
			ArrayList<General> p = c.getOfCluster();

			System.out.println("此类中包含对象的个数为：" + p.size());

			for (int j = 0; j < p.size(); j++) {
				System.out.println("general:" + p.get(j) + "\n");
			}
		}
	}
}