package com.roboclub.robobuggy.messages;

import java.util.ArrayList;
import java.util.Hashtable;

import javafx.util.Pair;

import com.roboclub.robobuggy.ros.Message;

//represents a series of pixels that are grouped together representing the same region of an image 
public class SuperPixelMessage extends BaseMessage implements Message{
	int[][] rgbs;
	int[][] ids;
	int width;
	int height;
	int numGroups;
	
	//the coordinates of each group
	Hashtable<Integer,ArrayList<Pair<Integer, Integer>>> groupAddresses;
	
	public SuperPixelMessage(int[][] ids, int[][] rgbs,int width,int height,int numGroups) {
		this.ids = ids;
		this.rgbs = rgbs;
		this.width = width;
		this.height = height;
		this.numGroups = numGroups;
		
		groupAddresses = new Hashtable<Integer,ArrayList<Pair<Integer, Integer>>>();
		
		//populates the group addresses 
		for(int i = 0;i< height;i++){
			for(int j = 0;j<width;j++){
				ArrayList<Pair<Integer,Integer>> thisGroup = groupAddresses.get(ids[i][j]);
				if(thisGroup == null){
					thisGroup = new ArrayList<Pair<Integer,Integer>>();
				}
				thisGroup.add(new Pair<Integer, Integer>(i, j));
				groupAddresses.put(ids[i][j], thisGroup);
			}
		}
		
	}
	public int getNumGroups(){
		return numGroups;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int[][] getRgbs(){
		return rgbs;
	}
	
	public int[][] getGroups(){
		return ids;
	}
	
	public ArrayList<Pair<Integer,Integer>> getGroup(int groupId){
		return groupAddresses.get(groupId);
	}
	
	@Override
	public String toLogString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message fromLogString(String str) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
