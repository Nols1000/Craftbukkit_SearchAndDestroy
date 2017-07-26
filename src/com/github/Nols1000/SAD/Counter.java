package com.github.Nols1000.SAD;

import java.util.Timer;
import java.util.TimerTask;

import com.github.Nols1000.SAD.events.OnCounterCompleteEvent;
import com.github.Nols1000.SAD.events.OnCounterTickEvent;

public class Counter extends Thread {
	
	private OnCounterCompleteEvent cEvent;
	private OnCounterTickEvent     tEvent;
	
	protected int n  = 0;
	protected int dt = 100;
	private boolean running = true;
	
	private Timer t = new Timer();
	
	public Counter(int n, int dt, OnCounterCompleteEvent cEvent, OnCounterTickEvent tEvent){
		
		this.n  = n;
		this.dt = dt;
		
		this.cEvent = cEvent;
		this.tEvent = tEvent;
	}
	
	public void run(){
		
		t.schedule(new TimerTask(){

			@Override
			public void run() {
				
				if(running)
					tick();
				else{
					
					t.cancel();
					t.purge();
				}
			}
			
		}, 0, dt);
	}
	
	protected void tick(){
		
		tEvent.tick(n);
		
		if(n <= 0){
			
			cEvent.onCounterComplete(this);
			interrupt();
		}
		
		n--;
	}
	
	public int getTicksLeft(){
		
		return n;
	}
	
	@Override
	public void interrupt(){
		
		super.interrupt();
		
		System.out.println(this.toString()+" interrupted");
		
		running = false;
	}	
}
