
int deliverPeriod1 =1000 % reward duration- adjust this based on pump

%VARIABLES

% vars for tracking behavior in maze
int goalwell=4
int lastWell=4		 	% 1 if left, 3 if right, 2 if center ... this variable trackes the previously activated well
% vars for tracking where and how much reward
int rewardWell= 0       	% reward well
int nowRewarding = 0 	% variable that keeps tabs on the reward being dispensed .. when reward is being dispensed and the system is in the midst of executing a reward function, this number hops up to a 1, and then relaxes to 0 when reward is finished.

int count= 0                	% blink count


int rewardPump1 = 5
int rewardPump2 = 6
int rewardPump3 = 7



int wellStat1=0 % well 1 is 3
int wellStat2=0  % well 2 is 2
int wellStat3=0 % well 3 is 8
int wellStatA=0 % well 4 is 1
int wellStatB=0 % well 5 is 9
int wellStatC=0 % well 6 is 11

int rewardNumber = 0

updates off 16

% Reward delivery function
function 1
	
rewardWell=random(2) +1
		while rewardWell==lastWell do every 10
			rewardWell=random(2) +1
		then do

			disp(rewardWell)
			disp('now rewarding')
			nowRewarding = 1 							% nowRewarding
			portout[rewardWell]=1 					% reward
			do in deliverPeriod1 						% do after waiting deliverPeriod milliseconds
				portout[rewardWell]=0 				% reset reward
				nowRewarding=0 					% no longer rewarding	
				disp('reward done')
			end
	end
end;

function 2

end;

% beam break functions
% well 1
callback portin[3] up
	disp('Poke in well1') 		% Print state of port to terminal

	wellStat1=1
	if (lastWell !=1 && wellStatA==1 && rewardWell==1) 	do				% Check if previous well = center
			disp('Rewarding - position A1 ')
			rewardWell=rewardPump1 	% dispense reward from here
			lastWell=1		
			trigger(1)					% trigger reward
			trigger(2)
			disp(rewardWell)					% trigger new reward well
			rewardNumber = rewardNumber + 1
			disp(rewardNumber)	
	end
end;


callback portin[3] down
	disp('UnPoke in well1') 	% Print state of port to terminal
	wellStat1=0
end;


% well 2
callback portin[2] up
	disp('Poke in well2') 		% Print state of port to terminal
	wellStat2=1
	if (lastWell !=2 && wellStatB==1 && rewardWell==2) 	do				% Check if previous well = center
			disp('Rewarding position B2 ')
			rewardWell=rewardPump2 	% dispense reward from here
			lastWell=2		
			trigger(1)					% trigger reward
			trigger(2)
			disp(rewardWell)
			rewardNumber = rewardNumber + 1
			disp(rewardNumber)	
	end
end;

callback portin[2] down
	disp('UnPoke in well2') 	% Print state of port to terminal
	wellStat2=0
end;

% well 3
callback portin[8] up
	disp('Poke in well3') 		% Print state of port to terminal
	wellStat3=1
	if (lastWell !=3 && wellStatC==1 && rewardWell==3) 	do				% Check if previous well = center
			disp('Rewarding position C3 ')
			rewardWell=rewardPump3 	% dispense reward from here
			lastWell=3	
			trigger(1)					% trigger reward
			trigger(2)
			disp('Next Reward Well')
			disp(rewardWell)
			rewardNumber = rewardNumber + 1
			disp(rewardNumber)	
	end
end;

callback portin[8] down
	disp('UnPoke in well3') 	% Print state of port to terminal
	wellStat3=0
end;

% well A
callback portin[4] up
	disp('Poke in wellA') 		% Print state of port to terminal
	wellStatA=1
	if (lastWell !=1 && wellStat1==1) do					% Check if previous well = center
			disp('Rewarding position A1 ')
			rewardWell=rewardPump1 	% dispense reward from here
			lastWell=1		
			trigger(1)					% trigger reward
			trigger(2)
			rewardNumber = rewardNumber + 1
			disp(rewardNumber)	
	end
end;


callback portin[4] down
	disp('UnPoke in wellA') 	% Print state of port to terminal
	wellStatA=0
end;

% well b
callback portin[9] up
	disp('Poke in wellB') 		% Print state of port to terminal
	wellStatB=1
	if (lastWell !=2 && wellStat2==1 && rewardWell==2) 	do				% Check if previous well = center
			disp('Rewarding position B2 ')
			rewardWell=rewardPump2 	% dispense reward from here
			lastWell=2		
			trigger(1)					% trigger reward
			trigger(2)
			rewardNumber = rewardNumber + 1
			disp(rewardNumber)	
	end
end;


callback portin[9] down
	disp('UnPoke in wellB') 	% Print state of port to terminal
	wellStatB=0
end;

% well C
callback portin[11] up
	disp('Poke in wellC') 		% Print state of port to terminal
	wellStatC=1
	if (lastWell !=3 && wellStat3==1 && rewardWell==3) 	do				% Check if previous well = center
			disp('Rewarding position C3 ')
			rewardWell=rewardPump3	% dispense reward from here
			lastWell=3		
			trigger(1)					% trigger reward
			trigger(2)
			rewardNumber = rewardNumber + 1
			disp(rewardNumber)	
	end
end;

callback portin[11] down
	disp('UnPoke in wellC') 	% Print state of port to terminal
	wellStatC=0
end;