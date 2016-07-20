    package kmeans;  
      
    import java.util.ArrayList;  
      
    /**
     * 	实体类：定义了聚类的子类实体（1个类中心武将id，n个子类成员） <br>
     *<p>
     *	Description:java聚类对象包含一个中心武将和该聚类中所有武将<br>
     *<p>
     *	Company:cstor</p>
     *	
     *	@author zhuxy
     *	2016年7月19日 下午3:35:06
     */
    public class Cluster {  
        private int center;//聚类中心武将的id  
        private ArrayList<General> ofCluster = new ArrayList<General>();// 属于这个聚类的武将的集合  
      
        public int getCenter() {  
            return center;  
        }  
      
        public void setCenter(int center) {  
            this.center = center;  
        }  
      
        public ArrayList<General> getOfCluster() {  
            return ofCluster;  
        }  
      
        public void setOfCluster(ArrayList<General> ofCluster) {  
            this.ofCluster = ofCluster;  
        }  
      
        public void addGeneral(General general) {  
            if (!(this.ofCluster.contains(general)))  
                this.ofCluster.add(general);  
        }  
    }  
