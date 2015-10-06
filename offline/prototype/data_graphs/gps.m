function [ output_args ] = untitled2( input_args )
%UNTITLED2 Summary of this function goes here
%   Detailed explanation goes here
fileID = fopen('sensors.txt','r');
line = fgetl(fileID)
encoder= [];
steering = [];
gps = [];
imu = [];
while line ~= -1
    C = strsplit(line,',');
    if(strcmp(C(1),'sensors/steering'))
        try
       time = parseTime(C(2));
       angle = str2num(C{3});
       steering = cat(1, steering, [time,angle]);
        catch 
        end
    elseif(strcmp(C(1),'sensors/encoder'))
        try
        time = parseTime(C(2));
        A = str2num(C{3})
        B = str2num(C{4})
        D = str2num(C{5})
        encoder = cat(1, encoder, [time,A,B]);
        catch
        end
    elseif(strcmp(C(1),'sensors/gps'))
        try
        time = parseTime(C(2));
        time_2 = C(3);
        lat = str2num(C{4});
        dir1 = str2num(C{5});
        lon = str2num(C{6});
        dir2 = str2num(C{7});
        gps = cat(1, gps, [time,lat,lon]);
        size(gps,1)
        catch 
        end
    elseif(strcmp(C(1),'sensors/imu'))
       try
       time = parseTime(C(2));
        A = str2num(C{2});
        B = str2num(C{3});
        D = str2num(C{4});
           imu = cat(1, imu, [time,A,B,D]);
       catch
       end
    else
       C(1)
    end
    line = fgetl(fileID);

end
fclose(fileID)
format long
gps(:,2:3)
save('data.mat','gps','encoder','imu','steering')

end

%note will have a bug at midnight 
function result = parseTime(input)
s = strsplit(input{1},' ');
date = s(1);
time = strsplit(s{2},':')

result = 3600*str2num(time{1})+60*str2num(time{2})+str2num(time{3});

end

