% PROGRAM NAME: 	LASER TRIGGER
% AUTHOR: 			JDS/RY
% DESCRIPTION:
%This program triggers a laser when the reward well beam is broken.

%CONSTANTS

int laserOnTime= 500   % how long laser is on

%VARIABLES

int count = 0                % blink count
int laserPort = 1       % trigger laser
int laserOn = 0 % laser state


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% CALLBACKS -- EVENT-DRIVEN TRIGGERS

callback portin[1] up
	disp('laserPort up')
	portout[4] = 1
	do in laserOnTime
		portout[4] = 0
	end
end;


