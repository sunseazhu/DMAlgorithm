package cn.cstor.svm;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

public class TestLibSVM {

    /**
     * 第一行null是svm.svm_check_parameter(problem, param)的输出，表示参数设置无误；<br>
     * 最后一行的-1.0表示对c点的预测lable是-1.0。<br>
     * 
     * 要注意的几点是：
　	 *	1. 主要用了svm.svm_train()做训练，用svm.svm_predict()做预测，其中用到了svm_problem、svm_parameter、svm_model、svm_node几种“结构体”对象。
　　	 *	2. svm_node表示的是{向量的分量序号，向量的分量值}，很多稀疏矩阵均用此方法存储数据，可以节约空间；
	 *	svm_node[]则表示一个向量，一个向量的最后一个分量的svm_node.index用-1表示；svm_node[][]则表示一组向量，也就是训练集。
     * 
     * @param args
     */
    public static void main(String[] args) {
        //定义训练集点a{10.0, 10.0} 和 点b{-10.0, -10.0}，对应lable为{1.0, -1.0}
        svm_node pa0 = new svm_node();
        pa0.index = 0;
        pa0.value = 10.0;
        
        svm_node pa1 = new svm_node();
        pa1.index = -1;
        pa1.value = 10.0;
        
        svm_node pb0 = new svm_node();
        pb0.index = 0;
        pb0.value = -10.0;
        
        svm_node pb1 = new svm_node();
        pb1.index = 0;
        pb1.value = -10.0;
        
        svm_node[] pa = {pa0, pa1}; //点a
        svm_node[] pb = {pb0, pb1}; //点b
        svm_node[][] datas = {pa, pb}; //训练集的向量表
        double[] lables = {1.0, -1.0}; //a,b 对应的lable
        
        
        //定义svm_problem对象
        svm_problem problem = new svm_problem();
        problem.l = 2; //向量个数
        problem.x = datas; //训练集向量表
        problem.y = lables; //对应的lable数组
        
        
        //定义svm_parameter对象
        svm_parameter param = new svm_parameter();
        param.svm_type = svm_parameter.C_SVC;
        param.kernel_type = svm_parameter.RBF;//svm_parameter.LINEAR;
        param.cache_size = 100;
        param.eps = 0.00001;
        param.C = 1;
        
        
        //训练SVM分类模型
        System.out.println("输出null表示参数没问题，Erro表示参数有问题："+'\t'+svm.svm_check_parameter(problem, param)); //如果参数没有问题，则svm.svm_check_parameter()函数返回null,否则返回error描述。
        svm_model model = svm.svm_train(problem, param); //svm.svm_train()训练出SVM分类模型
        /*
         *上述语句会输出部分训练数据信息，含义如下：
         *optimization finished, #iter = 480 迭代次数
         *nu = 0.909091 SVC,one-class-SVM,SVR参数
         *obj = -108.333321 二次规划的最小值
         *rho = -0.166667 决策函数常数项
         *nSV = 220 支持向量数
         *nBSV = 100 边界上支持向量数
         *Total nSV = 220 支持向量总数
         *Accuracy = 100% (220/220) (classification) 分类精度 
         */
        
        //定义测试数据点c
        svm_node pc0 = new svm_node();
        pc0.index = 0;
        pc0.value = -0.1;
        svm_node pc1 = new svm_node();
        pc1.index = -1;
        pc1.value = 0.0;
        svm_node[] pc = {pc0, pc1};
        
        System.out.println("=========   model 参数   =========");
        showModelParameter(model);
        
        
        
        System.out.println("=========   预测结果     =========");
        System.out.println(svm.svm_predict(model, pc));
    }

    //展示model常用参数
	private static void showModelParameter(svm_model model) {
		System.out.println("model.Param.s"+'\t'+model.param.svm_type);
        System.out.println("model.Param.t"+'\t'+model.param.kernel_type);
        System.out.println("model.Param.d"+'\t'+model.param.degree);
        System.out.println("model.Param.g"+'\t'+model.param.gamma);
        System.out.println("model.Param.r"+'\t'+model.param.coef0);
        System.out.println("model.Lable类别标签"+'\t'+model.label.length);
        System.out.println("model.nr_class不同标签的总个数（几分类）"+'\t'+model.nr_class);
        System.out.println("model.total #SV总共的支持向量个数"+'\t'+model.l);
        System.out.println("model.nSV每个类别对应的支持向量的个数"+'\t'+model.nSV.length);
        System.out.println("model.ProbA概率估计"+'\t'+model.probA);
        System.out.println("model.ProbB概率估计"+'\t'+model.probB);
        System.out.println("model.sv_coef支持向量在决策函数中的系数"+'\t'+model.sv_coef.length);
        System.out.println("model.SVs稀疏矩阵"+'\t'+model.sv_coef.length);
	}
}