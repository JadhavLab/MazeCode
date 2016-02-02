% PROGRAM NAME: 	TRIGGER PASSIVE TASTES
% AUTHOR: 			JDS/RY/MCZ/LH
% DESCRIPTION:
%This program triggers each solenoid valve to open when one of the four corresponding reward well beams is broken, delivering tastes passively via IOC.

%CONSTANTS

int valveOnTime= 100000 % how long laser is on in ms

%VARIABLES

int count = 0                % blink count
int valvePort = 1       % trigger valve
int valveOn = 0 % valve state
int countT1= 0
int countT2= 0
int countT3= 0
int countT4= 0 

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% CALLBACKS -- EVENT-DRIVEN TRIGGERS

callback portin[1] up
	disp('dispensing T1')
	portout[4] = 1
	do in valveOnTime
		portout[4] = 0
	end
	countT1= countT1+1
	disp(countT1)
end;

callback portin[2] up
	disp('dispensing T2')
	portout[5] = 1
	do in valveOnTime
		portout[5] = 0
	end
	countT2= countT2+1
	disp(countT2)
end;

callback portin[3] up
	disp('dispensing T3')
	portout[6] = 1
	do in valveOnTime
		portout[6] = 0
	end
	countT3= countT3+1
	disp(countT3)
end;	

callback portin[4] up
	disp('dispensing T4')
	portout[7] = 1
	do in valveOnTime
		portout[7] = 0
	end
	countT4= countT4+1
	disp(countT4)
end;
	