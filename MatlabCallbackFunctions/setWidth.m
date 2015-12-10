function setWidth(width)

    width = round(width/1000);
    sendScQtControlMessage('variableWidth = ' num2st(width));