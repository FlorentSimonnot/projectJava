public class Test{

	public class Inner{
		private int b = 5;
		private int c = 7; 
		private int d = 7;
	}

	public void print(){
		Inner in = new Inner();
		in.b = in.d; 
		System.out.println(in.b + in.c);
	}

	public void main(String[] args){
		new Test().print();
	}

}
