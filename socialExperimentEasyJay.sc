
int deliverPeriod1 =1000 % reward duration- adjust this based on pump

%VARIABLES

% vars for tracking behavior in maze
	 	
int rewardWell= 0       	% reward well
int nowRewarding = 0 	% variable that keeps tabs on the reward being dispensed .. when reward is being dispensed and the system is in the midst of executing a reward function, this number hops up to a 1, and then relaxes to 0 when reward is finished.

% 1 if left, 3 if right, 2 if center ... this variable trackes the previously activated well
% vars for tracking where and how much reward
int lastWell1= 4
int lastWell2= 4


int count= 0                	% blink count
int rewardNumber = 0

int rewardPump1 = 5 % for wells 1 and A
int rewardPump2 = 6 % for wells 2 and B
int rewardPump3 = 7 % for wells 3 and C



int wellStat1=0 % well 1 is 3
int wellStat2=0  % well 2 is 2
int wellStat3=0 % well 3 is 8
int wellStatA=0 % well 4 is 1
int wellStatB=0 % well 5 is 9
int wellStatC=0 % well 6 is 11





updates off 16

% Reward delivery function
function 1
	nowRewarding = 1 							% nowRewarding
	
	portout[rewardWell]=1 					% reward
	do in deliverPeriod1 						% do after waiting deliverPeriod milliseconds
		portout[rewardWell]=0 				% reset reward
		nowRewarding=0 					% no longer rewarding
	end
end;

% beam break functions

% well 1
callback portin[3] up
	disp('Poke in well1') 		% Print state of port to terminal
	wellStat1=1
	if (lastWell1 !=1 || lastWell2 !=1) do
		if (lastWell1 !=4) do
			lastWell1 = 4 % if this well is hit and it wasnt the last well, release the wells
		end
		if (wellStatA==1) 	do				% Check if previous well = center
			disp('Rewarding - position A1 ')
			rewardWell=rewardPump1 	% dispense reward from here	
			trigger(1) % trigger reward
			rewardNumber = rewardNumber + 1
			disp(rewardNumber)	
			lastWell1=1
		end	
	end

		

end


callback portin[3] down
	disp('UnPoke in well1') 	% Print state of port to terminal
	wellStat1=0
end

% well 2
callback portin[2] up
	disp('Poke in well2') 		% Print state of port to terminal
	wellStat2=1
	if (lastWell1 !=2 || lastWell2 !=2) do
		if (lastWell1 !=4) do
			lastWell1 = 4 % if this well is hit and it wasnt the last well, release the wells
		end
		if  (wellStatB==1)	do				% Check if previous well = center
			disp('Rewarding position B2 ')
			rewardWell=rewardPump2 	% dispense reward from here	
			trigger(1)	
			rewardNumber = rewardNumber + 1
			disp(rewardNumber)	
		end
	end
	lastWell1=2

end

callback portin[2] down
	disp('UnPoke in well2') 	% Print state of port to terminal
	wellStat2=0
end

% Well 3
callback portin[8] up
	disp('Poke in well3') 		% Print state of port to terminal
	wellStat3=1
	if (lastWell1 !=3 || lastWell2 !=3) do
		if (lastWell1 !=4) do
			lastWell1 = 4 % if this well is hit and it wasnt the last well, release the wells
		end
		if (wellStatC==1) 	do				% Check if previous well = center
			disp('Rewarding position C3')
			rewardWell=rewardPump3 	% dispense reward from here	
			trigger(1)					% trigger reward
			rewardNumber = rewardNumber + 1
			disp(rewardNumber)	
		end	
	end
	lastWell1=3

end

callback portin[8] down
	disp('UnPoke in well3') 	% Print state of port to terminal
	wellStat3=0
end

% well A
callback portin[1] up
	disp('Poke in wellA') 		% Print state of port to terminal
	wellStatA=1
	if (lastWell1 !=1 || lastWell2 !=1) do
		if (lastWell2 !=4) do
			lastWell2 = 4 % if this well is hit and it wasnt the last well, release the wells
		end
		if (wellStat1==1) do					% Check if previous well = center
			disp('Rewarding position A1 ')
			rewardWell=rewardPump1 	% dispense reward from here		
			trigger(1)					% trigger reward
			rewardNumber = rewardNumber + 1
			disp(rewardNumber)	
		end
	end
	lastWell2=1

end


callback portin[1] down
	disp('UnPoke in wellA') 	% Print state of port to terminal
	wellStatA=0
end

% well B
callback portin[9] up
	disp('Poke in wellB') 		% Print state of port to terminal
	wellStatB=1
	if (lastWell1 !=2 || lastWell2 !=2) do
		if (lastWell2 !=4) do
			lastWell2 = 4 % if this well is hit and it wasnt the last well, release the wells
		end
		if (wellStat2==1) 	do				% Check if previous well = center
			disp('Rewarding position B2 ')
			rewardWell=rewardPump2 	% dispense reward from here		
			trigger(1)					% trigger reward
			rewardNumber = rewardNumber + 1
			disp(rewardNumber)	
		end
	end
	lastWell2=2

end


callback portin[9] down
	disp('UnPoke in wellB') 	% Print state of port to terminal
	wellStatB=0
end

% well C
callback portin[11] up
	disp('Poke in wellC') 		% Print state of port to terminal
	wellStatC=1
	if (lastWell1 !=3 || lastWell2 !=3) do
		if (lastWell2 !=4) do
			lastWell2 = 4 % if this well is hit and it wasnt the last well, release the wells
		end
		if (wellStat3==1) 	do				% Check if previous well = center
			disp('Rewarding position C3 ')
			rewardWell=rewardPump3	% dispense reward from here		
			trigger(1)					% trigger reward
			rewardNumber = rewardNumber + 1
			disp(rewardNumber)	
		end
	end
	lastWell2=3

end



callback portin[11] down
	disp('UnPoke in wellC') 	% Print state of port to terminal
	wellStatC=0
end;