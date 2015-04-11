%	FileName: 		proto.sc
%	Description:		Prototype of W-maze Code
%	Authors:		Mark Z, Ryan Y
%   Date Mod:		2/27/2015
%   ToDos: 			Add feedback portins. Shift port numbers in order to cluster together beembreak and feedback. Hunt for bugs.

%%%%%%%%%%%%%%%%%%%%%%%%
%
%	 PORT DESCRIPTIONS
% 
%	 Inputs
% 		portin[3] = Left arm's IR receiver
%		portin[4] = Right arm's IR receiver
%		portin[5] = Center arm's IR receiver
%
%	Outputs
%		portout[1] = Left arm's pump trigger
%		portout[2] = Right arm's pump trigger
%		portout[3] = Center arm's pump trigger
%
%%%%%%%%%%%%%%%%%%%%%%%%%

%CONSTANTS

int deliverPeriod = 1000   % blinking delay

%VARIABLES

% vars for tracking behavior in maze
int lastSideWell= 0           % 1 if left, 2 if right
int lastWell=0		 	% 1 if left, 2 if right, 3 if center
int currWell= 0            	% current well

% vars for tracking where and how much reward
int rewardWell= 0       	% reward well
int nowRewarding = 0 	% keep track of reward being dispensed

int count= 0                	% blink count




%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% FUNCTIONS

% -----------------------
% Function Name: 	Two Port Error Flag
% Description:		Both port 1 and 4 should never be on. This function throws an error
% 					when both ports simultaneously shoot up to HIGH.
% -----------------------

function 1
	if ( (portin[3] == 1 && portin[4]==1) || (portin[4]==1 && portin[5]==1) || (portin[3]==1 && portin[5]==1) ) do
		disp('Error! Both ports on')
		portout[1]=0
		portout[2]=0
		portout[3]=0
		lastWell=0
	end
end;

% -----------------------
% Function Name: 	Reward function
% Description:		Both port 1 and 4 should never be on. This function throws an error
% 					when both ports simultaneously shoot up to HIGH.
% -----------------------

function 2

	trigger(1) 								% check for error
	nowRewarding = nowRewarding+1		 % want to keep nowRewarding at 1 only

	if nowRewarding == 1 do 				% if reward not triggered twice
		portout[rewardWell]=1 				% reward
		do in 1000
			portout[rewardWell]=0 			% reset reward
			nowRewarding=0 				% no longer rewarding
		end
	else do
		nowRewarding=0 					% no longer rewarding/ reset two rewards at once
	end

end;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% CALLBACKS -- EVENT-DRIVEN TRIGGERS

% TriggerDescription: 	Well 1 is active!
%

callback portin[3] up
	disp('Portin3 up - Left well on') 		% Print state of port to terminal
	trigger(1) 							% Run Error Check

	currWell=1							 % Left/1 well active

	if lastWell == 3 do					% Check if previous well = center
		if lastSideWell == 2	do			% Check if side last visited = right
			disp('Rewarding Well Left')
			rewardWell=1 				% dispense reward from here
			trigger(2)					% trigger reward
		end
	end
end;



% TriggerDescription: 	Well 1 is inactive!
%
callback portin[3] down
	nowRewarding=0 					%  Halt rewarding if animal leaves well

		if rewardWell != 0 do
			portout[rewardWell] = 0 	% Reset reward well- if not first trial
		end

	disp('Portin3 down - Left well off') 	% Print state of port to terminal

	lastWell = 1 						% Well left, now last well
	lastSideWell  = 1
end;



% TriggerDescription: 	Well 2/Right is active!
%
callback portin[4] up
	disp('portin4 up')					% Print state of port to terminal
	trigger(1) 							% Run Error Check

	currWell = 2 						% Set currently active well

	% Should we reward?
	if lastWell == 3 do					% Did animal last visit center arm?				
		if lastSideWell == 1	do			% Was previous side arm left?
			disp('Rewarding Well Right')
			rewardWell=2 				% Dispense reward from here
			trigger(2) 					% Trigger reward
		end
	end

end;   



% TriggerDescription: Well 2/Right is inactive!
%
callback portin[4] down

	nowRewarding = 0 					%  Halt rewarding if animal leaves well
		if rewardWell != 0 do
			portout[rewardWell] = 0 	% Reset reward well- if not first trial
		end
	disp('Portin4 down - Right well off')
	lastWell=2 							% Well left, now last well
	lastSideWell = 2
end;



% TriggerDescription: 	Well 3/Center well is active!
%
callback portin[5] up
	disp('Portin5 up - Center well on') 	% Print state of port 5
	trigger(1) 							% Run Error Check

	currWell = 3

	% Should we reward?
	if lastWell == 1 || lastWell == 2 do 	% Did the animal previously visit left/right arm?
		disp('Rewarding Well Center')
		rewardWell = 3
		trigger(2)
	end

end;


% TriggerDescription: 	Well 3/Center well is inactive!
%
callback portin[5] down

	% Shutting the reward down
	nowRewarding = 0
		if rewardWell != 0 do 
			portout[rewardWell] = 0
		end
	
	disp('Portin5 down - Center well off'')		% Print state of port 5

	lastWell=3								% Well center is now the last well	

end;


