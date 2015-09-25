

int deliverPeriod = 500  % blinking delay


int lastSideWell= 0          
int lastWell=0		 	
int currWell= 0            	
int rewardWell= 0       	
int nowRewarding = 0 	 

int count= 0                	% blink count





function 1
	nowRewarding = 1 							% nowRewarding
		portout[rewardWell]=1 					% reward
		do in deliverPeriod 						% do after waiting deliverPeriod milliseconds
			portout[rewardWell]=0 				% reset reward
			nowRewarding=0 					% no longer rewarding
		end
end



function 2
	if lastWell==0 do
		rewardWell=currWell
		trigger(1)
	end
end

function 3
	if lastSideWell == 0 && (currWell==1 || currWell == 3) do
		rewardWell=currWell
		trigger(1)
	end

end


callback portin[1] up
	disp('Portin1 up - Left well on') 		% Print state of port to terminal

	% Set current well
	currWell=1							 % Left/1 well active

	% Should we reward?
	trigger(3)							% Reward if first sidewell
	
	if lastWell == 2 do					% Check if previous well = center
		if lastSideWell == 3	do			% Check if side last visited = right
			disp('Rewarding Well Left')
			rewardWell=1 				% dispense reward from here
			trigger(1)					% trigger reward
		end
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
		disp('Rewarding Well Center')
		rewardWell = 2
		trigger(1)
	end

end

callback portin[2] down
	disp('Portin2 down - Center well off'')		% Print state of port 2
	lastWell=2								% Well center is now the last wel
end

callback portin[3] up
	disp('Portin3 up - Right well on') 		% Print state of port to terminal
	
	% Set current well
	currWell=3 						% Set currently active well

	% Should we reward?
	trigger(3)							% Reward if first sidewell
	
	if lastWell == 2 do					% Did animal last visit center arm?				
		if lastSideWell == 1	do			% Was previous side arm left?
			disp('Rewarding Well Right')
			rewardWell=3 				% Dispense reward from here
			trigger(1) 					% Trigger reward
		end
	end

end  

callback portin[3] down
	disp('Portin3 down - Right well off')
	lastWell=3 							% Well left, now last well
	lastSideWell = 3
end;




