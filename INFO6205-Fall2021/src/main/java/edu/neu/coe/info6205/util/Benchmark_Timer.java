/*
 * Copyright (c) 2018. Phasmid Software
 */

package edu.neu.coe.info6205.util;

import java.sql.Array;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import edu.neu.coe.info6205.sort.Helper;
import edu.neu.coe.info6205.sort.elementary.InsertionSort;

import static edu.neu.coe.info6205.util.Utilities.formatWhole;

/**
 * This class implements a simple Benchmark utility for measuring the running time of algorithms.
 * It is part of the repository for the INFO6205 class, taught by Prof. Robin Hillyard
 * <p>
 * It requires Java 8 as it uses function types, in particular, UnaryOperator&lt;T&gt; (a function of T => T),
 * Consumer&lt;T&gt; (essentially a function of T => Void) and Supplier&lt;T&gt; (essentially a function of Void => T).
 * <p>
 * In general, the benchmark class handles three phases of a "run:"
 * <ol>
 *     <li>The pre-function which prepares the input to the study function (field fPre) (may be null);</li>
 *     <li>The study function itself (field fRun) -- assumed to be a mutating function since it does not return a result;</li>
 *     <li>The post-function which cleans up and/or checks the results of the study function (field fPost) (may be null).</li>
 * </ol>
 * <p>
 * Note that the clock does not run during invocations of the pre-function and the post-function (if any).
 *
 * @param <T> The generic type T is that of the input to the function f which you will pass in to the constructor.
 */
public class Benchmark_Timer<T> implements Benchmark<T> {

    /**
     * Calculate the appropriate number of warmup runs.
     *
     * @param m the number of runs.
     * @return at least 2 and at most m/10.
     */
    static int getWarmupRuns(int m) {
        return Integer.max(2, Integer.min(10, m / 10));
    }

    /**
     * Run function f m times and return the average time in milliseconds.
     *
     * @param supplier a Supplier of a T
     * @param m        the number of times the function f will be called.
     * @return the average number of milliseconds taken for each run of function f.
     */
    @Override
    public double runFromSupplier(Supplier<T> supplier, int m) {
        logger.info("Begin run: " + description + " with " + formatWhole(m) + " runs");
        // Warmup phase
        final Function<T, T> function = t -> {
            fRun.accept(t);
            return t;
        };
        new Timer().repeat(getWarmupRuns(m), supplier, function, fPre, null);

        // Timed phase
        return new Timer().repeat(m, supplier, function, fPre, fPost);
    }

    /**
     * Constructor for a Benchmark_Timer with option of specifying all three functions.
     *
     * @param description the description of the benchmark.
     * @param fPre        a function of T => T.
     *                    Function fPre is run before each invocation of fRun (but with the clock stopped).
     *                    The result of fPre (if any) is passed to fRun.
     * @param fRun        a Consumer function (i.e. a function of T => Void).
     *                    Function fRun is the function whose timing you want to measure. For example, you might create a function which sorts an array.
     *                    When you create a lambda defining fRun, you must return "null."
     * @param fPost       a Consumer function (i.e. a function of T => Void).
     */
    public Benchmark_Timer(String description, UnaryOperator<T> fPre, Consumer<T> fRun, Consumer<T> fPost) {
        this.description = description;
        this.fPre = fPre;
        this.fRun = fRun;
        this.fPost = fPost;
    }

    /**
     * Constructor for a Benchmark_Timer with option of specifying all three functions.
     *
     * @param description the description of the benchmark.
     * @param fPre        a function of T => T.
     *                    Function fPre is run before each invocation of fRun (but with the clock stopped).
     *                    The result of fPre (if any) is passed to fRun.
     * @param fRun        a Consumer function (i.e. a function of T => Void).
     *                    Function fRun is the function whose timing you want to measure. For example, you might create a function which sorts an array.
     */
    public Benchmark_Timer(String description, UnaryOperator<T> fPre, Consumer<T> fRun) {
        this(description, fPre, fRun, null);
    }

    /**
     * Constructor for a Benchmark_Timer with only fRun and fPost Consumer parameters.
     *
     * @param description the description of the benchmark.
     * @param fRun        a Consumer function (i.e. a function of T => Void).
     *                    Function fRun is the function whose timing you want to measure. For example, you might create a function which sorts an array.
     *                    When you create a lambda defining fRun, you must return "null."
     * @param fPost       a Consumer function (i.e. a function of T => Void).
     */
    public Benchmark_Timer(String description, Consumer<T> fRun, Consumer<T> fPost) {
        this(description, null, fRun, fPost);
    }

    /**
     * Constructor for a Benchmark_Timer where only the (timed) run function is specified.
     *
     * @param description the description of the benchmark.
     * @param f           a Consumer function (i.e. a function of T => Void).
     *                    Function f is the function whose timing you want to measure. For example, you might create a function which sorts an array.
     */
    public Benchmark_Timer(String description, Consumer<T> f) {
        this(description, null, f, null);
    }

    private final String description;
    private final UnaryOperator<T> fPre;
    private final Consumer<T> fRun;
    private final Consumer<T> fPost;

    final static LazyLogger logger = new LazyLogger(Benchmark_Timer.class);
    
public static void main(String[] args) {
    	
    	System.out.println("B-E-G-I-N");
    	    	
    	int n = 20;
    	int doubling_n = 10;
    	int numbersUpperBound = 1000;
    	int running_times = 30;
    	String description="InsertionSort Time";
    	Random rand=new Random();
    	UnaryOperator<Integer[]> fPre = array -> {return array.clone();};
    	Consumer<Integer[]> fRun = array->new InsertionSort<Integer>().sort(array,0,array.length);    	
    	Consumer<Integer[]> fPost = array -> {};
    	InsertionSort<Integer> insert=new InsertionSort<>();
    	Benchmark_Timer<Integer[]> benchmark_t=new Benchmark_Timer<>(description,fPre,fRun,fPost);
    	
    	Integer[] n_=new Integer[doubling_n+1];
    	Double[] rd=new Double[doubling_n+1];
    	Double[] po=new Double[doubling_n+1];
    	Double[] o=new Double[doubling_n+1];
    	Double[] ro=new Double[doubling_n+1];

    	
    	// n will double 10 times
    	for(int i=1; i<=doubling_n; i++) {
    		n = n*2;
    		n_[i]=n;
    		System.out.println("Round "+i);
    		
    		//Random
    		
    		System.out.println("Random: n = "+n);
    		Integer[] random = new Integer[n];
    		for(int j=0; j<random.length; j++) {
    			random[j] = rand.nextInt(numbersUpperBound);
    		}
    		System.out.println("Meantime: "+benchmark_t.run(random, running_times));
    		rd[i]=benchmark_t.run(random, running_times);
    		
    		//Partially-ordered
    		
    		System.out.println("Partially-ordered: n = "+n);
    		insert.sort(random,random.length/2,random.length);
    		Integer[] part_ordered = new Integer[n];
    		for(int j=0; j<random.length; j++) {
    			part_ordered[j] = random[j];
    		}
    		System.out.println("Meantime: "+benchmark_t.run(part_ordered, running_times));
    		po[i]=benchmark_t.run(part_ordered, running_times);
    		
    		//Ordered
    		
    		System.out.println("Ordered: n = "+n);
    		insert.sort(random,0,random.length);
    		Integer[] ordered = new Integer[n];
    		for(int j=0; j<random.length; j++) {
    			ordered[j] = random[j];
    		}
    		System.out.println("Meantime: "+benchmark_t.run(ordered, running_times));
    		o[i]=benchmark_t.run(ordered, running_times);
    		
    		//Reverse-ordered
    		
    		System.out.println("Reverse-ordered n = "+n);
    		Integer[] re_ordered = new Integer[n];
    		for(int j=0,m=random.length-1; j<random.length && m>-1; j++,m--) {
    			 re_ordered[m] = ordered[j];
    		}
    		System.out.println("Mean lap time: "+benchmark_t.run(re_ordered, running_times));
    		//ro[i]=benchmark_t.run(re_ordered, running_times);
    	}
//    	for(int k=0;k<n_.length;k++){
//        	System.out.println(n_[k]);
//    	}
//    	for(int k=0;k<rd.length;k++){
//        	System.out.println(rd[k]);
//    	}
//    	for(int k=0;k<po.length;k++){
//        	System.out.println(po[k]);
//    	}
//    	for(int k=0;k<o.length;k++){
//        	System.out.println(o[k]);
//    	}
//    	for(int k=0;k<ro.length;k++){
//        	System.out.println(ro[k]);
//    	}
		System.out.println("E-N-D");
	}
}
