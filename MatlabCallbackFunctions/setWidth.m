function setWidth(width)

    global masterWidth;
    global masterFreq;

    masterWidth = round(width);
    
%     if masterWidth > masterFreq
%         return
%     end
    
    sendScQtControlMessage(['stimWidth = ', num2str( masterWidth ), ';']);
    
    disp('New width set!');
    
end