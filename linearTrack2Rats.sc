
int deliverPeriod1 =500 % reward duration- adjust this based on pump

%VARIABLES

% vars for tracking behavior in maze
int rewardWell12 = 1
int rewardWellAB = 1

int lastWell12= 3
int lastWellAB= 3

int count12 = 0
int countAB = 0               	% reward count


% these are the current well IR and PUMP pins:
int rewardPump1 = 5 	% well PUMP 1 port 5
int rewardPump2 = 6 	% well PUMP 2 port 6
int rewardPumpA = 7 	% well PUMP A port 7
int rewardPumpB = 12	% well PUMP B port 12

% well IR 1 is port 3
% well IR 2 is port 2
% well IR A is port 4
% well IR B is port 9

updates off 16

% Reward delivery function
function 1
	portout[rewardWell12]=1 					% reward
	do in deliverPeriod1 					% do after waiting deliverPeriod milliseconds
		portout[rewardWell12]=0 				% reset reward
	end
end;

function 2
portout[rewardWellAB]=1 					% reward
	do in deliverPeriod1 					% do after waiting deliverPeriod milliseconds
		portout[rewardWellAB]=0 				% reset reward
	end
end;
% beam break functions

% well 1 (port 3)

callback portin[3] up
	disp('Poke in well1') 		% Print state of port to terminal
	if (lastWell12 !=1) 	do	% Check if previous well = center
			disp('Rewarding - position 1 rewards:')
			rewardWell12=rewardPump1 	% dispense reward from here
			lastWell12=1		
			trigger(1) % trigger reward
			count12 = count12 + 1
			disp(count12)
	end
end;


callback portin[3] down
	disp('UnPoke in well1') 	% Print state of port to terminal

end;

% well 2 (port 2)

callback portin[2] up
	disp('Poke in well2') 		% Print state of port to terminal

	if (lastWell12 !=2)	do	
			disp('Rewarding position 2 rewards:')
			rewardWell12=rewardPump2 	% dispense reward from here
			lastWell12=2		
			trigger(1)
			count12 = count12 + 1
			disp(count12)	
	end
end;

callback portin[2] down
	disp('UnPoke in well2') 	% Print state of port to terminal

end;

% Well A (now port 4)

callback portin[4] up
	disp('Poke in wellA') 		% Print state of port to terminal

	if (lastWellAB !=1) 	do	% Check if previous well = center
			disp('Rewarding position A rewards:')
			rewardWellAB=rewardPumpA 	% dispense reward from here
			lastWellAB=1	
			trigger(2)
			countAB = countAB + 1
			disp(countAB)
	end
end;

callback portin[4] down
	disp('UnPoke in wellA') 	% Print state of port to terminal

end;

% well B (port 9)

callback portin[9] up
	disp('Poke in wellB') 		% Print state of port to terminal

	if (lastWellAB !=2) do					% Check if previous well = center
			disp('Rewarding position B rewards:')
			rewardWellAB=rewardPumpB 	% dispense reward from here
			lastWellAB=2		
			trigger(2)					% trigger reward
			countAB = countAB + 1
			disp(countAB)
	end
end;


callback portin[9] down
	disp('UnPoke in wellB') 	% Print state of port to terminal

end;

