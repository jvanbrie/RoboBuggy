function [ output_args ] = processData( input_args )
%UNTITLED Summary of this function goes here
%   Detailed explanation goes here
data = open('data.mat');
plot(medfilt1(data.steering(:,2),3));
plot(medfilt1(data.encoder(:,2),3));
plot(medfilt1(data.encoder(:,3),3));
figure()
A = 100*(data.gps(:,2) - data.gps(1,2)) + data.gps(1,2);
B = data.gps(:,3);
plot(-B,A)
plot_google_map('maptype', 'roadmap','refresh',1);



end

