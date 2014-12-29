function [ output_args ] = ReadImage( input_args )
%TODO remove the 1 seg which is an outline instead of being split and
%asigned properly

%have retrain button so that the system will only retrain when
%CategoryML.mat is non exsistent or the button is pressed 

%TODO speed up, a lot 

%TODO a lot of training 

run('lib2/vlfeat-0.9.19/toolbox/vl_setup')
%TODO train ML stuff 
options = {'Road','white Lane Line','Yellow Lane Line','SideWalk','other','monument','cathedral','otherbuilding','I Do not know','haey bail','sky','tree'};
colors = [128,128,128; %gray
          255,255,255; %white
          255,255,  0; %yellow
          255,  0,  0; %red
          100,  0,  0; %gray Red
            0,100,  0; %gray green
            0,  0,100; %gray green
          100,100,  0; %gray yelow
            0,  0,  0; %black
            0,  0,  0; %black
            0,  0,255; %blue
            0,255,  0; %green
            ];
         
filterBank = createFilterBank();
T = 100;%number of pixels to sample per segment 
k = 18; %k must be greater then the size of the data set we are training with 
if(~exist('CategoryML.mat'))
    %so we do not need to retrain every time 
    [wordMaps,labels,dictionary] = trainSuperPixelCategorySelector(filterBank,options,T,k);
    save('CategoryML.mat','wordMaps','labels','dictionary');
end
load('CategoryML.mat');
testI = imread('training_Log/20141227306.3963/I.jpg');
seg = segmentIMG(testI);
description = createDescriptor(testI,filterBank);
numSegs = max(max(seg));
outI = testI;
for s = 1:numSegs
   mask = seg == s;
   ValidPixels = find(mask);
   %an alterntive to this would be to remove or merge sections of an image
   %that are too small 
   while(size(ValidPixels,1) < T)
     %concat validPixels on to itself to allow for repeates so we have at
     %aleast T pixels to choose from
     ValidPixels = cat(1,ValidPixels,ValidPixels);
   end
   
   % Random permutation of the descrip
   randind = randperm(size(ValidPixels,1));
    
   %Getting the first T responses and adding them to the selected set;
   thisDescription = description(randind(1:T),:);

   %Getting the filter responses
    DMatrix = pdist2(thisDescription,dictionary);

   %Chosing the closest one
    [C,wordVector] = min(DMatrix,[],2)   
    minDist = 100000; %TODO rewrite
    minL = -1; 
    for j = 1:size(wordMaps,1)
       d = sum((wordVector-wordMaps{j}).^2);
       if minDist > d
           minDist = d;
           minL = j
       end
    end
    l = labels(minL);
    for x = 1:size(mask,1)
        for y = 1:size(mask,2)
            if(mask(x,y) == 1)
               outI(x,y,1) = colors(l,1);
               outI(x,y,2) = colors(l,2);
               outI(x,y,3) = colors(l,3);
            end
        end
    end
end
   %todo find histogram which is closest to the description

    subplot(1,2,1)
    imshow(outI)
    subplot(1,2,2)
    imshow(testI)
    pause()



imshow(testI)
trainSignDetector();



start = 4000;%3730;
numFrames_to_get = 30;

movObj = VideoReader('run1-cam0.avi');
step = 1;

% Read in all video frames.
vidFrames = read(movObj,[start,start+numFrames_to_get]);

% Get the number of frames.
numFrames = get(movObj, 'NumberOfFrames');

% creates a file for all of the logs
mkdir('training_Log')

% Create a MATLAB movie struct from the video frames.
for k = 1:numFrames_to_get %: numFrames
    I = vidFrames(:,:,:,k);
    if mod(k,step) == 0
        outI = I;  %outI is the image which is saved for every frame 
        outI = surfDetector(I,outI);
        outI = signDetector(I,outI);
        outI = SuperPixelCategorySelector(I,outI);
        %TODO Visual Odom 
    else
        I_post = I;
    end
    mov(k).cdata = outI;
    mov(k).colormap = [];
end
 
% Create a figure
%hf = figure; 
       
% Resize figure based on the video's width and height
%set(hf, 'position', [150 150 movObj.Width movObj.Height])

% Playback movie once at the video's frame rate
%movie(hf, mov, 1, movObj.FrameRate);
%movie2avi(mov, 'out.avi')
end

