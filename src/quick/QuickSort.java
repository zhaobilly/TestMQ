package quick;

public class QuickSort {
	
	public static void main(String[] args) {
		int [] arr={4,6,9,2,0,1}; 
		int[] v=quicksort(arr,0,5);
//		int[] v=sort(arr,0,5);
		for(int i=0;i<v.length;i++){
			System.out.println(v[i]);
		}
	}
	public static int[] sort(int arr[],int low,int high) { 
		int l=low;
		int h=high;
		int povit=arr[low];   
		while(l<h) { 
			while(l<h&&arr[h]>=povit) 
			   h--; 
		if(l<h){
			int temp=arr[h]; 
			arr[h]=arr[l]; 
			arr[l]=temp;
			l++; 
		}   
		while(l<h&&arr[l]<=povit) 
			l++; 
		if(l<h){ 
			int temp=arr[h]; 
			arr[h]=arr[l]; 
			arr[l]=temp; 
			h--; 
		} 
	} 
			if(l>low)sort(arr,low,l-1);
			if(h<high)sort(arr,l+1,high);
			return arr; 
	} 
	
	
	public static int[] quicksort(int v[], int left, int right){
        if(left < right){
                int key = v[left];
                int low = left;
                int high = right;
                while(low < high){
                        while(low < high && v[high] > key){
                                high--;
                        }
                        v[low] = v[high];
                        while(low < high && v[low] < key){
                                low++;
                        }
                        v[high] = v[low];
                }
                v[low] = key;
                quicksort(v,left,low-1);
                quicksort(v,low+1,right);
        }
      return v;  
	}

}
