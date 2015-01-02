function outI =  superPixelCategorySelectorManul(I,outI,T,filterBank,wordMaps,labels,dictionary,colors)
%I is the input image to process on 
%OutI is the image which we want to superimpose data on 


seg = segmentIMG(I);
numSegs = max(max(seg));

outI(:,:,:)  = 0;

'segmentation done'
I_hsv = rgb2hsv(I);
this_I = zeros(size(I,1),size(I,2),3);
subplot(1,2,1)
imshow(I)
for s = 1:numSegs
    mask = seg == s;
    this_I(:,:,1) = I_hsv(:,:,1) + mask - 1; %mask.*I_hsv(:,:,1);
    this_I(:,:,2) = I_hsv(:,:,2) + mask - 1; %mask.*I_hsv(:,:,2);
    this_I(:,:,3) = I_hsv(:,:,3) + mask -1 ; %mask.*I_hsv(:,:,3);
 
    %SKY
    sky1 = this_I(:,:,1) > .5;
    sky2 = this_I(:,:,1) <= 1;
    sky3 = this_I(:,:,2) >= 0;
    sky4 = this_I(:,:,2) <= 1;
    sky5 = this_I(:,:,3) >= .5;
    sky6 = this_I(:,:,3) <= 1;
    sky = sky1.*sky2.*sky3.*sky4.*sky5.*sky6;
    skySum = sum(sum(sky));
    
    %ROAD
    road1 = this_I(:,:,1) >= 0;
    road2 = this_I(:,:,1) <= 1;
    road3 = this_I(:,:,2) >= 0;
    road4 = this_I(:,:,2) <= 1;
    road5 = this_I(:,:,3) >= 0;
    road6 = this_I(:,:,3) <= .5; 
    road = road1.*road2.*road3.*road4.*road5.*road6;
    roadSum = sum(sum(road));
    
    
    %White Line
    wline1 = this_I(:,:,1) >= 0;
    wline2 = this_I(:,:,1) <= 1;
    wline3 = this_I(:,:,2) >= 0;
    wline4 = this_I(:,:,2) <= .7;
    wline5 = this_I(:,:,3) >= .5;
    wline6 = this_I(:,:,3) <= 1; 
    wline = wline1.*wline2.*wline3.*wline4.*wline5.*wline6;
    wlineSum = sum(sum(wline));
    
    
    

    
    %choses what type the super pixel is 
    if(skySum > wlineSum && skySum > roadSum)
       'sky'
       
    pixels = find(mask);
    for k =1:size(pixels,1)
       x = floor(pixels(k)/size(outI,1)) + 1;
       y = mod(pixels(k),size(outI,1)) + 1  ; %don't think is correct 
       outI(y,x,1) = colors(11,1);
       outI(y,x,2) = colors(11,2);
       outI(y,x,3) = colors(11,3);
    end
    
    elseif(roadSum > wlineSum)
        'road'    
   pixels = find(mask);
    for k =1:size(pixels,1)
       x = floor(pixels(k)/size(outI,1)) + 1;
       y = mod(pixels(k),size(outI,1)) + 1  ; %don't think is correct 
       outI(y,x,1) = colors(1,1);
       outI(y,x,2) = colors(1,2);
       outI(y,x,3) = colors(1,3);
    end
    else
        'wline'
   pixels = find(mask);
        for k =1:size(pixels,1)
       x = floor(pixels(k)/size(outI,1)) + 1;
       y = mod(pixels(k),size(outI,1)) + 1  ; %don't think is correct 
       outI(y,x,1) = colors(2,1);
       outI(y,x,2) = colors(2,2);
       outI(y,x,3) = colors(2   ,3);
    end
    end
        
    
    
end
subplot(1,2,2)
imshow(outI)


pause

end