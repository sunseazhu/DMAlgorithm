
/**
 *	该包主要是TF-IDF方法的操作
 *	<p>
 *TF-IDF（term frequency–inverse document frequency）是一种用于资讯检索与资讯探勘的常用加权技术。
 *<p>
 *主要思想是：<br>
 *	如果某个词或短语在一篇文章中出现的频率TF高，并且在其他文章中很少出现，则认为此词或者短语具有很好的类别区分能力，适合用来分类。<br>
 *
 *<p>
 *TF-IDF实际上是：TF * IDF:<br>
 *		TF词频(Term Frequency)<br>
 *		IDF反文档频率(Inverse Document Frequency)。TF表示词条在文档d中出现的频率。IDF的主要思想是：如果包含词条t的文档越少，也就是n越小，IDF越大，则说明词条t具有很好的类别区分能力。<br>
 *		如果某一类文档C中包含词条t的文档数为m，而其它类包含t的文档总数为k，显然所有包含t的文档数n=m + k，当m大的时候，n也大，按照IDF公式得到的IDF的值会小，就说明该词条t类别区分能力不强。<br>
 *		但是实际上，如果一个词条在一个类的文档中频繁出现，则说明该词条能够很好代表这个类的文本的特征，这样的词条应该给它们赋予较高的权重，并选来作为该类文本的特征词以区别与其它类文档。这就是IDF的不足之处.<br>
 *
 *	<p>Company: cstor
 *	
 *	@author zhuxy
 *	2016年7月19日 上午11:08:18
 */
package tf_idf;