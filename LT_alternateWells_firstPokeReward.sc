% PROGRAM NAME: 	LINEAR TRACK ALTERNATION
% AUTHOR: 			MCZ
% DESCRIPTION:
%						This program delivers reward from each reward well (on a linear track) when the beam is broken, and the animal successfully alternates from one well to the other. Reward delivery ceases after 1sec.

%CONSTANTS

int deliverPeriod= 1000   % how long to deliver the reward

%VARIABLES

int count= 0                % blink count
int lastWell= 0             % last well
int currWell= 0            % current well
int rewardWell= 0       % reward well
int nowRewarding = 0 % keep track of reward being dispensed



% -----------------------
% Function Name: 	Two Port Error Flag
% Description:		Both port 1 and 4 should never be on. This function throws an error
% 					when both ports simultaneously shoot up to HIGH.
% -----------------------



% reward function
function 1
	nowRewarding = 1 % nowRewarding
		portout[rewardWell]=1 % reward
		do in deliverPeriod % NOTE: USING A VARIABLE HERE WORKS
			portout[rewardWell]=0 % reset reward
			nowRewarding=0 % no longer rewarding
		end
end;

function 2
	if lastWell==0 do
		rewardWell=currWell
		trigger(1)
	end
end;

callback portin[1] up
	disp('portin1 up')
	currWell=1 % well currently active
	trigger(2)
	if lastWell == 2 do
		disp('Rewarding Well 1')
		rewardWell=1 % dispense reward from here
		trigger(1)% trigger reward
	end
end;

callback portin[1] down
	disp('portin1 down')
	lastWell=1 % well left, now last well
end;

callback portin[2] up
	disp('portin2 up')
	currWell=2 % well currently active
	trigger(2)
	if lastWell == 1 do
		disp('Rewarding Well 2')
		rewardWell=2 % dispense reward from here
		trigger(1) % trigger reward
	end
end;

callback portin[2] down
	disp('portin2 down')
	lastWell=2 % well left, now last well
end;


		

