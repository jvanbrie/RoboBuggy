package com.roboclub.robobuggy.messages;


//container class for some cpu data per core
public class CpuCoreData{
	public CpuCoreData(){
		
	}
    public double userTime;
    public double sysTime;
    public double idleTime;
    public double waitTime;
    public double niceTime;
    public double combinedTime;
    public int irqTime;	
    
    @Override
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString(){
		return userTime+"\t"+sysTime+"\t"+idleTime+"\t"+waitTime+"\t"+niceTime+"\t"+combinedTime+"\t"+irqTime;
    }
}