package com.roboclub.robobuggy.messages;

import java.util.ArrayList;
import java.util.Date;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.Uptime;

import com.roboclub.robobuggy.nodes.CpuUsageNode;
import com.roboclub.robobuggy.ros.Message;

public class CpuInfoMessage extends BaseMessage implements Message{
	
	//basic info 
	public Date timestamp;
	public String CpuVender; 
	public String CpuModel;
	public int CpuMhz;
	public int CpuTotalCores;
	public int CpuPhysicalNumber;
	public long cacheSize;
	public long pid;
	public long numThreads;
	public int threadPriority;
	public double uptime;
	public String user;
	public String group;
	public int egid;
	public int euid;
	public int gid;
	public int uid;
	
	//memory Stuff
	public long usedMemory_total;
	public long freeMemory_total;
	
	//info per core
    public ArrayList<CpuCoreData> coreCpuData;

  
    //total info 
    public CpuCoreData totalCpuData;
	
    
    public CpuInfoMessage(){
    	timestamp = new Date();
    }
	
	@Override
	public String toLogString() {
		String coreData_string = "";//is variable length so must be built up as such
		for(int i = 0;i<coreCpuData.size();i++){
			coreData_string +=coreCpuData.get(i).toString();
		}
		
		return	timestamp.toString() + "\t" + 
				user + "\t" + 
				group + "\t" +
				egid + "\t" +
				euid + "\t" + 
				gid + "\t" + 
				uid + "\t" + 
				CpuVender + "\t" +
				CpuModel + "\t"  +
				CpuMhz + "\t" +
				CpuTotalCores + "\t" + 
				CpuPhysicalNumber + "\t" + 
				cacheSize + "\t" + 
				uptime + "\t" +
				pid + "\t" + 
				usedMemory_total + "\t" + 
				freeMemory_total + "\t" +
				numThreads + "\t" + 
				threadPriority + "\t" +
				totalCpuData.toString()+ "\t" + 
				coreData_string+"\t"
 				+"\n";
	}

	@Override
	public Message fromLogString(String str) {
		// TODO 
		return null;
	}

}

