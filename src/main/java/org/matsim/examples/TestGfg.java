package org.matsim.examples;

public class TestGfg {
	
	public static void main(String[] args) throws InterruptedException 
    { 
        TestGfg t1 = new TestGfg(); 
        TestGfg t2 = new TestGfg(); 
          
        // Nullifying the reference variable 
        t1 = null; 
          
        // requesting JVM for running Garbage Collector 
        System.gc(); 
          
        // Nullifying the reference variable 
        t2 = null; 
          
        // requesting JVM for running Garbage Collector 
        
        System.gc();
//        Thread.sleep(1000); 
//        Runtime.getRuntime().gc(); 
      
    } 
      
    @Override
    // finalize method is called on object once  
    // before garbage collecting it 
    protected void finalize() throws Throwable 
    { 
        System.out.println("Garbage collector called"); 
        System.out.println("Object garbage collected : " + this); 
    } 
} 


