package com.github.Nols1000.SAD.config;

import java.util.List;

public class SignDataPack {
	
	private List<SignData> signs;
	
	public SignDataPack(List<SignData> arg0){
		
		signs = arg0;
	}
	
	public List<SignData> getSignsList(){
		
		return signs;
	}
	
}
