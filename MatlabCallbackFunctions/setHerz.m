function setHerz(freq)

    freq = round((1/freq)*1000);
    
    sendScQtControlMessage('variableFreq = ' num2st(freq));
    
end