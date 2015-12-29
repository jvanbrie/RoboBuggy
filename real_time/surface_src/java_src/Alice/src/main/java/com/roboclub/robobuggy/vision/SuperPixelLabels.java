package com.roboclub.robobuggy.vision;

	public enum SuperPixelLabels{
		ROAD,
		GRASS,
		SIDE_WALK,
		HAY_BALE,
		YELLOW_ROAD_MARKING,
		WHITE_ROAD_MARKING,
		GREEN_BIKE_LANE_ROAD_MARKING,
		OTHER;
		
		//null will be returned if the input string does not match any of the inputs 
		public static SuperPixelLabels fromString(String input){
			SuperPixelLabels[] strs = SuperPixelLabels.values();
			for(int i = 0;i<strs.length;i++){
				if(input.startsWith(strs[i].toString())){
					return strs[i];
				}
			}
			//If this point is reached then the input did not mach any of the currently labels 
			return null;
			
		}
		
	}
	

	
