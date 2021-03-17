

int deliverPeriod = 500   % blinking delay
int lastSideWell= 0           % 1 if left, 3 if right ... this variable tracks the previously activated side well.
int lastWell=0		 	% 1 if left, 3 if right, 2 if center ... this variable trackes the previously activated well
int currWell= 0            	% current well 	... this variable keeps track of when a well was made active.

% vars for tracking where and how much reward
int rewardWell= 0       	% reward well
int nowRewarding = 0 	
int count= 0                	% blink count

function 1
	nowRewarding = 1 							% nowRewarding
		portout[rewardWell]=1 					% reward
		do in deliverPeriod 						% do after waiting deliverPeriod milliseconds
			portout[rewardWell]=0 				% reset reward
			nowRewarding=0 					% no longer rewarding
		end
end;

function 2
	if lastWell==0 do
		rewardWell=currWell
		trigger(1)
	end
end;



callback portin[1] up
	disp('Portin1 up - Left well on') 		% Print state of port to terminal
	currWell=1							 % Left/1 well active
	trigger(2) 							% Reward if first poke
	
	if lastWell == 2 do					% Check if previous well = center
		if lastSideWell == 3	do			% Check if side last visited = right
			disp('Rewarding Well Left')
			rewardWell=1 				% dispense reward from here
			trigger(1)					% trigger reward
		end
	end
end;


callback portin[1] down
		if rewardWell != 0 do
			portout[rewardWell] = 0 	% Reset reward well- if not first trial
		end

	disp('Portin1 down - Left well off') 	% Print state of port to terminal

	lastWell = 1 						% Well left, now last well
	lastSideWell  = 1
end;

callback portin[2] up
	disp('Portin2 up - Center well on') 	% Print state of port 2
	currWell = 2
	trigger(2) 							% Reward if first poke
	
	if lastWell == 1 || lastWell == 3 do 	% Did the animal previously visit left/right arm?
		disp('Rewarding Well Center')
		rewardWell = 2
		trigger(1)
	end

end;

callback portin[2] down

		if rewardWell != 0 do 
			portout[rewardWell] = 0
		end
	
	disp('Portin2 down - Center well off'')		% Print state of port 2

	lastWell=2								% Well center is now the last well	

end;

callback portin[3] up
	disp('portin3 up')					% Print state of port to terminal
	trigger(1) 							% Run Error Check
	currWell = 3 						% Set currently active well
	trigger(2) 							% Reward if first poke
	
	if lastWell == 2 do					% Did animal last visit center arm?				
		if lastSideWell == 1	do			% Was previous side arm left?
			disp('Rewarding Well Right')
			rewardWell=3 				% Dispense reward from here
			trigger(1) 					% Trigger reward
		end
	end

end;   

callback portin[3] down

		if rewardWell != 0 do
			portout[rewardWell] = 0 	% Reset reward well- if not first trial
		end
	disp('Portin3 down - Right well off')
	lastWell=3 							% Well left, now last well
	lastSideWell = 3
end;




