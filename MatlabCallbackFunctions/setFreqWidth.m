function setFreqWidth(freq,width)

    global masterFreq
    global masterWidth
    
    masterFreq = round((1/freq)*1000);
    masterWidth = round(width);
    
    sendScQtControlMessage(['stimFreq = ', num2str(masterFreq), ';']);
    sendScQtControlMessage(['stimWidth = ', num2str( masterWidth ), ';']);

