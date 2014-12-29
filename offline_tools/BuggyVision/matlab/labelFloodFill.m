%assumes m starts at 2 1 is for boundires
function I = labelFloodFill(I,m,x,y)
Frontier = [x,y];
count = 0;
while(size(Frontier,1) > 0)
    count = count+1;
    %  if( m >400 && mod(count,100) == 0)
    %  imshow(I)
    %   pause()
    %  end
    this_x = Frontier(1,1);
    this_y = Frontier(1,2);
    Frontier = Frontier(2:size(Frontier,1),:);
    if(I(this_x,this_y) == -1)
        new = [
            % this_x+1,this_y+1;
            this_x+1,this_y;
            % this_x+1,this_y-1;
            this_x  ,this_y+1;
            this_x  ,this_y-1;
            %  this_x-1,this_y+1;
            this_x-1,this_y;
            %  this_x-1,this_y-1
            ];
        Frontier = cat(1,Frontier,new);
        I(this_x,this_y) = m;
    end
end


end