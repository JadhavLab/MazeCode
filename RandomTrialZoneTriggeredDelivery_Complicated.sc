% PROGRAM NAME: 	TRIGGER TASTE DELIVERY WITH WELL
% AUTHOR: 			LH
% DESCRIPTION: Manually trigger a sweet or salty taste (chosen randomly) to be delivered.
% 
% 
%CONSTANTS
int valve1time = 30
int valve2time = 30
int taste = 0
int countSweet = 0
int countSalty = 0
int numTrials = 0
int trial = 0
int countTastes = 0
int minWait = 5000
int noTrigger = 0
int T1Flg = 0
int T2Flg = 0
int TimerFlg = 0
int countZ1 = 0
int countZ2 = 0
int countNone = 0
int waitTime = 0


function 3 % Check flags for when in Zone 1
noTrigger = 0
if TimerFlg == 1 do
    noTrigger = 1
else if T1Flg==1 do
    noTrigger = 1    
end
if T2Flg == 1 do
    T2Flg  = 0
    noTrigger = 0
end
end;


function 4 % Check Flags for when in zone 2
noTrigger = 0
if TimerFlg == 1 do
    noTrigger = 1    
else if T2Flg == 1 do
    noTrigger = 1
end
if T1Flg == 1 do
    T1Flg = 0
    noTrigger = 0
end
end;


% Functions %

function 1 % Zone 1 trigger functions
trial = random(1) % choose whether there will be a trial or not
disp('Zone 1 Triggered')
trigger(3)
if noTrigger == 1 do
    disp('Recently Tiggered, No Trial')
    T2Flg = 0
    trigger(3)
else if trial == 0 do 
    numTrials = numTrials + 1
    countNone = countNone +1
    disp(numTrials)
    disp('No taste trial')
else do % if there is a trial, choose which taste will be delivered
    waitTime = random(500) % Random time to trigger taste after crossing border
    do in waitTime
        countZ2 = countZ2 + 1
        taste = random(1)+1 % choose whether sweet (1) or salty (2) will be delivered 
        if taste == 1 do % if 1 is chosen, do sweet trial
            disp(taste)
            disp('Sweet trial')
            countSweet = countSweet + 1
            countTastes = countTastes + 1
            numTrials = numTrials + 1
            disp(numTrials)
            portout[2] = 1 
            do in valve1time
                portout[2]=0
            end
        else do % if 2 is chosen, do salt trial
            disp(taste)
            disp('Salty trial')
            countSalty = countSalty + 1
            countTastes = countTastes + 1
            numTrials = numTrials + 1
            disp(numTrials)
            portout[3] = 1
    	    do in valve2time
    	        portout[3] = 0
    	    end
        end
        TimerFlg = 1
        T1Flg = 1
        do in minWait
            TimerFlg = 0
        end
        trigger(3)
    end
end
end;

function 2 % Zone 2 trigger function
trial = random(1) % choose whether there will be a trial or not
disp('Zone 2 Triggered')
trigger(4)
if noTrigger == 1 do
    disp('Recently Tiggered, No Trial')
    T1Flg = 0
    trigger(4)
else if trial == 0 do 
    numTrials = numTrials + 1
    countNone = countNone +1
    disp(numTrials)
    disp('No taste trial')
else do % if there is a trial, choose which taste will be delivered
    waitTime = random(500) % Random time to trigger taste after crossing border
    do in waitTime
        countZ2 = countZ2 + 1
        taste = random(1)+1 % choose whether sweet (1) or salty (2) will be delivered 
        if taste == 1 do % if 1 is chosen, do sweet trial
            disp(taste)
            disp('Sweet trial')
            countSweet = countSweet + 1
            countTastes = countTastes + 1
            numTrials = numTrials + 1
            disp(numTrials)
            portout[2] = 1 
            do in valve1time
                portout[2]=0
            end
        else do % if 2 is chosen, do salt trial
            disp(taste)
            disp('Salty trial')
            countSalty = countSalty + 1
            countTastes = countTastes + 1
            numTrials = numTrials + 1
            disp(numTrials)
            portout[3] = 1
    	    do in valve2time
    	        portout[3] = 0
    	    end
        end
        TimerFlg = 1
        T2Flg = 1
        do in minWait
            TimerFlg = 0
        end
        trigger(4)
    end
end
end;

function 5 % Display function
disp('Salt Trials')
disp(countSalty)
disp('Sweet Trials')
disp(countSweet)
disp('Zone 1 Trials')
disp(countZ1)
disp('Zone 2 Trials')
disp(countZ2)
disp('No Trials')
disp(countNone)
end;

callback portin[1] up
end;
