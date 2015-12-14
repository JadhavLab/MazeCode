function setFreq(freq)

    global masterFreq;
%     global masterWidth;

    masterFreq = round((1/freq)*1000);

%     if masterWidth > masterFreq
%         return;
%     end
    
    sendScQtControlMessage(['stimFreq = ', num2str(masterFreq), ';']);
    
end
