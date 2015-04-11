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
int lastSideWell= 0           % 1 if left, 2 if right ... this variable tracks the previously activated side well.
int lastWell=0		 	% 1 if left, 2 if right, 3 if center ... this variable trackes the previously activated well
int currWell= 0            	% current well 	... this variable keeps track of when a well was made active.

% vars for tracking where and how much reward
int rewardWell= 0       	% reward well
int nowRewarding = 0 	% variable that keeps tabs on the reward being dispensed .. when reward is being dispensed and the system is in the midst of executing a reward function, this number hops up to a 1, and then relaxes to 0 when reward is finished.

int count= 0                	% blink count




%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% FUNCTIONS

% -----------------------
% Function Name: 	Reward
% Description:		This function administers reward to a marked well.
% -----------------------

function 1
	nowRewarding = 1 							% nowRewarding
		portout[rewardWell]=1 					% reward
		do in deliverPeriod 						% do after waiting deliverPeriod milliseconds
			portout[rewardWell]=0 				% reset reward
			nowRewarding=0 					% no longer rewarding
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
	end
end;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% CALLBACKS -- EVENT-DRIVEN TRIGGERS

% TriggerDescription: 	Left Well is active!
%

callback portin[3] up
	disp('Portin3 up - Left well on') 		% Print state of port to terminal

	% Set current well
	currWell=1							 % Left/1 well active

	% Should we reward?
	trigger(2) 							% Reward if first poke
	
	if lastWell == 3 do					% Check if previous well = center
		if lastSideWell == 2	do			% Check if side last visited = right
			disp('Rewarding Well Left')
			rewardWell=1 				% dispense reward from here
			trigger(1)					% trigger reward
		end
	end
end;



% TriggerDescription: 	Left well is inactive!
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



% TriggerDescription: 	Right wellt is active!
%
callback portin[4] up
	disp('portin4 up')					% Print state of port to terminal
	trigger(1) 							% Run Error Check
	
	% Set current well
	currWell = 2 						% Set currently active well

	% Should we reward?
	trigger(2) 							% Reward if first poke
	
	if lastWell == 3 do					% Did animal last visit center arm?				
		if lastSideWell == 1	do			% Was previous side arm left?
			disp('Rewarding Well Right')
			rewardWell=2 				% Dispense reward from here
			trigger(1) 					% Trigger reward
		end
	end

end;   



% TriggerDescription: Right well is inactive!
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



% TriggerDescription: 	Center well is active!
%
callback portin[5] up
	disp('Portin5 up - Center well on') 	% Print state of port 5

	% Set current well
	currWell = 3

	% Should we reward?
	trigger(2) 							% Reward if first poke
	
	if lastWell == 1 || lastWell == 2 do 	% Did the animal previously visit left/right arm?
		disp('Rewarding Well Center')
		rewardWell = 3
		trigger(1)
	end

end;


% TriggerDescription: 	Center well is inactive!
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


