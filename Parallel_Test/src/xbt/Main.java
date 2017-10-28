package xbt;

import harness.Executor;

public class Main {
	public static void main(String[] args){
		try{ 
		 Executor TestRun	= new Executor();
		 TestRun.ExecuteSuite();
		}catch(Exception err){
			System.out.println(err.getMessage());
		}
	  }
}
