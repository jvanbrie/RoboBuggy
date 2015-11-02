package com.roboclub.robobuggy.nodes;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.roboclub.robobuggy.messages.CpuCoreData;
import com.roboclub.robobuggy.messages.CpuInfoMessage;
import com.roboclub.robobuggy.ros.Node;
import com.roboclub.robobuggy.ros.Publisher;
import com.roboclub.robobuggy.ros.SensorChannel;

import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.shell.ShellCommandExecException;
import org.hyperic.sigar.shell.ShellCommandUsageException;



/**
 * This node publishes info about the computer that it is running on at a periodic time 
 * @author Trevor Decker
 *
 */
public class CpuUsageNode implements Node {
	private static final int RUN_PERIOD = 1000;
	
	private Sigar s;
	
	public Publisher msgPub;
	public CpuUsageNode(SensorChannel sensor){
		super();
		msgPub = new Publisher(sensor.getMsgPath());
		
		TimerTask timerTask = new CpuDataPublisherTask();
		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(timerTask, 0, RUN_PERIOD);
	}
	
	/**
	 * This method pulls the computes current cpu usage by alice, cpu usage in total , memory usage, battery level, available storage space
	 * and publishes all of this information for logging and other nodes to consume 
	 * @throws ShellCommandExecException 
	 * @throws ShellCommandUsageException 
	 * @throws SigarException 
	 */
	private void publishCPUData() throws ShellCommandUsageException, ShellCommandExecException, SigarException{
	if(s == null){
		s = new Sigar();
	}
		//TODO add battery level 
		  org.hyperic.sigar.CpuInfo[] infos =
	                s.getCpuInfoList();
		  
		  		CpuInfoMessage m = new CpuInfoMessage();	
	            CpuPerc[] cpus = s.getCpuPercList();
	            org.hyperic.sigar.CpuInfo info = infos[0];
		  	    
		  	    //adds process info
		  	    m.pid = s.getPid();
	            m.CpuVender =   info.getVendor();
	            m.CpuModel = info.getModel();
	            m.CpuMhz = info.getMhz();
	            m.CpuTotalCores = info.getTotalCores();
		  		m.uptime = s.getUptime().getUptime();

	            
		  		//adds memory data 
		  		m.freeMemory_total = s.getMem().getActualFree();
		  	    m.usedMemory_total = s.getMem().getActualUsed();
		  	    m.threadPriority = s.getProcState(m.pid).getPriority();
		  	    m.numThreads = s.getProcState(m.pid).getThreads();
		  	    m.cacheSize = info.getCacheSize();
		  	    
		  	    //totals
		  	    m.totalCpuData = cpuOut(s.getCpuPerc());
		  	    m.coreCpuData = new ArrayList<CpuCoreData>();
		  	    
		  	    //core specific data
		  	    for (int i=0; i<cpus.length; i++) {
	                m.coreCpuData.add(cpuOut(cpus[i]));
		  	    }
		 
		  		s.getProcCredName(m.pid).getUser();
		  		s.getProcCredName(m.pid).getGroup();
		  		s.getProcCred(m.pid).getEgid();
		  		s.getProcCred(m.pid).getEuid();
		  		s.getProcCred(m.pid).getGid();
		  		s.getProcCred(m.pid).getUid();
		  	    /*
		  		org.hyperic.sigar.FileSystem[] files = s.getFileSystemList();
		  		//System.out.println(s.getDirStat("/"));
		  		//TODO get disk avilablity info 
		  		for(org.hyperic.sigar.FileSystem f : files){
		  			System.out.println("dirName...."+f.getDirName());
		  			System.out.println("devName...."+f.getDevName());
		  			//System.out.println("diskUsage..."+s.getDiskUsage(f.getDirName()));
		  			System.out.println("dirUsage..."+s.getDirUsage(f.getDirName()));
		  			System.out.println("dirstats..."+s.getDirStat(f.getDirName()));
		  		    System.out.println("diskUsage..."+s.getDiskUsage(f.getDevName()));
		  			System.out.println("dirUsage..."+s.getDirUsage(f.getDevName()));
		  			System.out.println("dirstats..."+s.getDirStat(f.getDevName()));
		  		}

		  		//TODO figure out how many and what devices are attached 
		  		*/
		  	    
		  	  msgPub.publish(m);
	}
    
    public  CpuCoreData cpuOut(CpuPerc cpu) {
    	CpuCoreData result = new CpuCoreData(); 
    	result.userTime = cpu.getUser();
    	result.sysTime = cpu.getSys();
    	result.idleTime = cpu.getIdle();
    	result.waitTime = cpu.getWait();
    	result.niceTime = cpu.getWait();
    	result.idleTime = cpu.getIrq();
    	result.combinedTime = cpu.getCombined();
        return result;
    }
	
	@Override
	public boolean shutdown() {
        		return false;
	}

	
	public class CpuDataPublisherTask extends TimerTask{

		@Override
		public void run() {
			try {
				publishCPUData();
			} catch (ShellCommandUsageException | ShellCommandExecException
					| SigarException e) {
				e.printStackTrace();
			}
			
		}
		
		
	}

}


