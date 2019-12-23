record TestRecord(int lo, int hi, String a) {
  public TestRecord {
    if (lo > hi)  /* referring here to the implicit constructor parameters */
      throw new IllegalArgumentException(String.format("(%d,%d)", lo, hi));
  }

  public String getA(){
	int b = 3;
	return lo + " * " + a;
}

	public static void main(String[] args){
		var r = new TestRecord(3, 6, "a"); 
		var r2 = new TestRecord(3, 12, "b");
		System.out.println(r.equals(r2));
		System.out.println(r.equals(r));
		System.out.println(r);
		System.out.println(r.getA());
		System.out.println(r.hashCode());
	}
}
