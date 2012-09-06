package com.dianping.lion.env;

public enum Env {
	DEV{
		@Override
		public String zkAddress() {
			return "192.168.7.41:2181";
		}
	},
	ALPHA{
		@Override
		public String zkAddress() {
			return "192.168.7.41:2182";
		}
	},
	BETA{
		@Override
		public String zkAddress() {
			return "10.1.77.10:2181";
		}
		
	},
	PRERELEASE{
		@Override
		public String zkAddress() {
			return "10.1.3.163:2181";
		}
		
	},
	PRODUCT{
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
