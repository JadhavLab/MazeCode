% PROGRAM NAME: 	LINEAR TRACK ALTERNATION
% AUTHOR: 			MCZ
% DESCRIPTION:
%						This program delivers reward from each reward well (on a linear track) when the beam is broken, and the animal successfully alternates from one well to the other. Reward delivery ceases after 1sec.

%CONSTANTS

int deliverPeriod= 500   % how long to deliver the reward



% ---------------------
% Digital Input Assignments
% ----------------------
int LeftRewardWell = 1
int RightRewardWell  = 2

% ---------------------
% Digital Output Assigments
% ----------------------
int left_led = 1
int right_led = 2
int left_pump =  3
int right_pump = 4

%VARIABLES

int count= 0                % blink count
int lastWell= 0             % last well
int currWell= 0            % current well
int rewardWell= 0       % reward well
int nowRewarding = 0 % keep track of reward being dispensed
int reward_counter = 0


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% FUNCTIONS

% -----------------------
% Function Name: 	Reward
% Description:		This function administers reward to a marked well.
% -----------------------

function 1
	nowRewarding = 1 % nowRewarding
		portout[currWell]=1 % reward
		disp(currWell)
		do in deliverPeriod % NOTE: USING A VARIABLE HERE WORKS
			portout[currWell]=0 % reset reward
			nowRewarding=0 % no longer rewarding
		reward_counter = reward_counter + 1
		disp(reward_counter)
		end
end;


% -----------------------
% Function Name: 	Reward first poke
% Description:		This function adminsters reward to the first poke.
% -----------------------
function 2
	if lastWell==0 do
		rewardWell=currWell
		trigger(1)
		if rewardWell == 3 do
			portout[3] = 0
			portout[4] = 1
		else do
			portout[3] = 1
			portout[4] = 0
		end
		do in deliverPeriod
			portout[3] = 0
			portout[4] = 0
		end			
	end
end;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% CALLBACKS -- EVENT-DRIVEN TRIGGERS

callback portin[1] up
	disp('Left Well Triggered')
	currWell=3 % well currently active
	trigger(2)
	if lastWell == 2 do
		disp('Poke 1 rewarded')
		rewardWell=3 % dispense reward from here
		trigger(1)% trigger reward
		%portout[1] = 0 %turn off left LED
		%portout[2] = 1 %turn on right LED
	else do 
		portout[3] = 0
	end
	
end;

callback portin[1] down
	disp('Left Well Down')
	lastWell=1 % well left, now last well
end;

callback portin[2] up
	disp('Right Well Triggered')
	currWell=4 % well currently active
	trigger(2)
	if lastWell == 1 do
		disp('Poke 2 rewarded')
		rewardWell=4 % dispense reward from here
		trigger(1) % trigger reward
		%portout[1] = 1
		%portout[2] = 0
	else do 
		portout[4] = 0
	end
end;

callback portin[2] down
	disp('Right Well Down')
	lastWell=2 % well left, now last well
end;


		

