

int deliverPeriod = 500  % reward duration- adjust this based on pump

int laserTime = 4000 % duration that the laser is on

%VARIABLES

% vars for tracking behavior in maze
int lastSideWell= 0           % 1 if left, 3 if right ... this variable tracks the previously activated side well.
int lastWell=0		 	% 1 if left, 3 if right, 2 if center ... this variable trackes the previously activated well
int currWell= 0            	% current well 	... this variable keeps track of when a well was made active.

% vars for tracking where and how much reward
int rewardWell= 0       	% reward well
int nowRewarding = 0 	% variable that keeps tabs on the reward being dispensed .. when reward is being dispensed and the system is in the midst of executing a reward function, this number hops up to a 1, and then relaxes to 0 when reward is finished.
int nowLaser = 0 % variable that keeps tabs on when the laser is on


int count= 0                	% blink count

int rewardPump1 = 5
int rewardPump2 = 6
int rewardPump3 = 7


updates off 16


function 1
	nowRewarding = 1 							% nowRewarding
		portout[rewardWell]=1 					% reward
		do in deliverPeriod 						% do after waiting deliverPeriod milliseconds
			portout[rewardWell]=0 				% reset reward
			nowRewarding=0 				% no longer rewarding
		end				     

	nowLaser = 1
		portout[4]=1 
		do in laserTime %laser trigger in milliseconds
			portout[4] = 0 %laser off
			nowLaser = 0
		end
end;



function 2
	if lastWell==0 do
		rewardWell=currWell
		trigger(1)
	end
end;

function 3
	if lastSideWell == 0 && (currWell==1 || currWell == 3) do
		rewardWell=currWell
		trigger(1)
	end

end;



callback portin[1] up
	disp('Portin1 up - Left well on') 		% Print state of port to terminal

	% Set current well
	currWell=1							 % Left/1 well active

	% Should we reward?
	trigger(3)							% Reward if first sidewell
	
	if lastWell == 2 do					% Check if previous well = center
		if lastSideWell == 3	do			% Check if side last visited = right
			disp('Poke 1 rewarded - left ')
			disp('Laser on - Left')
			rewardWell=rewardPump1 				% dispense reward from here
			trigger(1)					% trigger reward
		end
	else do
		disp('Poke 1 not rewarded - left')
	end
end


callback portin[1] down
	disp('Portin1 down - Left well off') 	% Print state of port to terminal
	lastWell = 1 						% Well left, now last well
	lastSideWell  = 1
end



callback portin[2] up
	disp('Portin2 up - Center well on') 	% Print state of port 2

	% Set current well
	currWell = 2

	% Should we reward?
	trigger(2) 							% Reward if first poke
	
	if lastWell == 1 || lastWell == 3 do 	% Did the animal previously visit left/right arm?
		disp('Poke 2 rewarded - center')
		disp('Laser on - Center')
		rewardWell = rewardPump2
		trigger(1)
	else do
		disp('Poke 2 not rewarded - center')
	end

end

callback portin[2] down
	disp('Portin2 down - Center well off')		% Print state of port 2
	lastWell=2								% Well center is now the last wel
end

callback portin[8] up
	disp('Portin3 up - Right well on')					% Print state of port to terminal
	
	% Set current well
	currWell = 3 						% Set currently active well

	% Should we reward?
	trigger(3)							% Reward if first sidewell
	
	if lastWell == 2 do					% Did animal last visit center arm?				
		if lastSideWell == 1	do			% Was previous side arm left?
			disp('Poke 3 rewarded - right')
			disp('Laser on - Right')
			rewardWell=rewardPump3 				% Dispense reward from here
			trigger(1) 					% Trigger reward
		else do
			disp('Poke 3 not rewarded - right')
		end
	end

end


callback portin[8] down
	disp('Portin3 down - Right well off')
	lastWell=3 							% Well right, now last well
	lastSideWell = 3
end;





