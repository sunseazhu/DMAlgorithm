package cn.cstor.logicalregression;

import java.util.Arrays;

public class LogRegression {

	public static void main(String[] args) {
		
		LogRegression lr = new LogRegression();
		Instances instances = new Instances();
		lr.train(instances, 0.01f, 200, (short)1);
	}
	
	public void train(Instances instances, float step, int maxIt, short algorithm) {
		
		float[][] datas = instances.datas;
		float[] labels = instances.labels;
		int size = datas.length;
		int dim = datas[0].length;
		float[] w = new float[dim];
		
		for(int i = 0; i < dim; i++) {
			w[i] = 1;
		}
		
		switch(algorithm){
		//批量梯度下降
		case 1: 
			for(int i = 0; i < maxIt; i++) {
				//求输出
				float[] out = new float[size];
				for(int s = 0; s < size; s++) {
					float lire = innerProduct(w, datas[s]);
					out[s] = sigmoid(lire);
				}
				for(int d = 0; d < dim; d++) {
					float sum = 0;
					for(int s = 0; s < size; s++) {
						sum  += (labels[s] - out[s]) * datas[s][d];
					}
					w[d] = w[d] + step * sum;
				}
				System.out.println(Arrays.toString(w));
			}
			break;
		//随机梯度下降
		case 2: 
			for(int i = 0; i < maxIt; i++) {
				for(int s = 0; s < size; s++) {
					float lire = innerProduct(w, datas[s]);
					float out = sigmoid(lire);
					float error = labels[s] - out;
					for(int d = 0; d < dim; d++) {
						w[d] += step * error * datas[s][d];
					}
				}
				System.out.println(Arrays.toString(w));
			}
			break;
		}
	}
	
	private float innerProduct(float[] w, float[] x) {
		
		float sum = 0;
		for(int i = 0; i < w.length; i++) {
			sum += w[i] * x[i];
		}
		
		return sum;
	}
	
	private float sigmoid(float src) {
		return (float) (1.0 / (1 + Math.exp(-src)));
	}
}
