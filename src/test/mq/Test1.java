package test.cs;

import java.io.UnsupportedEncodingException;

public class Test1 {

	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
//		int i = 0;
//		int a=1;
//		a=i/2;
		
//		for(int i=0;i<10000;i++){
//			if(i%2==1){
//				if(i%3==0){
//					if(i%4==1){
//						if(i%5==4){
//							if(i%6==3){
//								if(i%7==5){
//									if(i%8==1){
//										if(i%9==0){
//											System.out.println(i);
//										}
//									}
//								}
//							}
//						}
//					}
//				}
//			}
//		}
		String ss="中";
		int len=ss.length();
		int leee=ss.getBytes("UTF-8").length;
		System.out.println("len="+len);
		System.out.println("leee="+leee);
	}
	

}
