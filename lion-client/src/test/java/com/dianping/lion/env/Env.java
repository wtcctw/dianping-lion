package com.dianping.lion.env;

public enum Env {
	dev{
		@Override
		public String zkAddress() {
			return "dev.lion.dp:2181";
		}
	},
	alpha{
		@Override
		public String zkAddress() {
			return "alpha.lion.dp:2182";
		}
	},
	qa{
		@Override
		public String zkAddress() {
			return "qa.lion.dp:2181";
		}
		
	},
	prelease{
		@Override
		public String zkAddress() {
			return "10.2.8.143:2181";
		}
		
	},
	product{
		@Override
		public String zkAddress() {
			return "10.1.2.32:2181,10.1.2.37:2181,10.1.2.62:2181,10.1.2.67:2181,10.1.2.58:2181";
		}
		
	};
	public abstract String zkAddress();
	public static Env analyzeFrom(int order){
		for(Env e : Env.values()){
			if(e.ordinal()==order){
				return e;
			}
		}
		throw new IndexOutOfBoundsException();
	}
}
