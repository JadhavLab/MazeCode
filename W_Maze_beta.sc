%	FileName: 		proto.sc
%	Description:		Prototype of W-maze Code
%	Authors:		Mark Z, Ryan Y
%   Date Mod:		4/13/2015
%   ToDos: 			Add feedback portins. Shift port numbers in order to cluster together beembreak and feedback. Hunt for bugs.

%%%%%%%%%%%%%%%%%%%%%%%%
%
%	 PORT DESCRIPTIONS
% 
%	 Inputs
% 		portin[1] = Left arm's IR receiver
%		portin[2] = Center arm's IR receiver
%		portin[3] = Right arm's IR receiver
%
%	Outputs
%		portout[1] = Left arm's pump trigger
%		portout[2] = Center arm's pump trigger
%		portout[3] = Right arm's pump trigger
%
%%%%%%%%%%%%%%%%%%%%%%%%%

%CONSTANTS

int deliverPeriod = 250   % blinking delay

%VARIABLES

% vars for tracking behavior in maze
int lastSideWell= 0           % 1 if left, 3 if right ... this variable tracks the previously activated side well.
int lastWell=0		 	% 1 if left, 3 if right, 2 if center ... this variable trackes the previously activated well
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

callback portin[1] up
	disp('Portin1 up - Left well on') 		% Print state of port to terminal

	% Set current well
	currWell=1							 % Left/1 well active

	% Should we reward?
	trigger(2) 							% Reward if first poke
	
	if lastWell == 2 do					% Check if previous well = center
		if lastSideWell == 3	do			% Check if side last visited = right
			disp('Rewarding Well Left')
			rewardWell=1 				% dispense reward from here
			trigger(1)					% trigger reward
		end
	end
end;



% TriggerDescription: 	Left well is inactive!
%
callback portin[1] down

		if rewardWell != 0 do
			portout[rewardWell] = 0 	% Reset reward well- if not first trial
		end

	disp('Portin1 down - Left well off') 	% Print state of port to terminal

	lastWell = 1 						% Well left, now last well
	lastSideWell  = 1
end;



% TriggerDescription: 	Center well is active!
%
callback portin[2] up
	disp('Portin2 up - Center well on') 	% Print state of port 2

	% Set current well
	currWell = 2

	% Should we reward?
	trigger(2) 							% Reward if first poke
	
	if lastWell == 1 || lastWell == 3 do 	% Did the animal previously visit left/right arm?
		disp('Rewarding Well Center')
		rewardWell = 2
		trigger(1)
	end

end;


% TriggerDescription: 	Center well is inactive!
%
callback portin[2] down

	% Shutting the reward down
		if rewardWell != 0 do 
			portout[rewardWell] = 0
		end
	
	disp('Portin2 down - Center well off'')		% Print state of port 2

	lastWell=2								% Well center is now the last well	

end;

% TriggerDescription: 	Right well is active!
%
callback portin[3] up
	disp('portin3 up')					% Print state of port to terminal
	trigger(1) 							% Run Error Check
	
	% Set current well
	currWell = 3 						% Set currently active well

	% Should we reward?
	trigger(2) 							% Reward if first poke
	
	if lastWell == 2 do					% Did animal last visit center arm?				
		if lastSideWell == 1	do			% Was previous side arm left?
			disp('Rewarding Well Right')
			rewardWell=3 				% Dispense reward from here
			trigger(1) 					% Trigger reward
		end
	end

end;   



% TriggerDescription: Right well is inactive!
%
callback portin[3] down

		if rewardWell != 0 do
			portout[rewardWell] = 0 	% Reset reward well- if not first trial
		end
	disp('Portin3 down - Right well off')
	lastWell=3 							% Well left, now last well
	lastSideWell = 3
end;




