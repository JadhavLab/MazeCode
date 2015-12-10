function setExpDuration(dur)

    dur = round(dur);
    
%     if masterWidth > masterFreq
%         return
%     end
    
    sendScQtControlMessage(['expDuration = ', num2str( dur ), ';']);
    
    disp('New duration set!');
end