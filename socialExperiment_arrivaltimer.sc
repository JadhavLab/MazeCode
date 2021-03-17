
int deliverPeriod1 =1000 % reward duration- adjust this based on pump
int arrivaltime = 3500
%VARIABLES

% vars for tracking behavior in maze

int lastWellABC=4		 	% 1 if left, 3 if right, 2 if center ... this variable trackes the previously activated well
int lastWell123=4
% vars for tracking where and how much reward
int rewardWell= 0       	% reward well
int nowRewarding = 0 	% variable that keeps tabs on the reward being dispensed .. when reward is being dispensed and the system is in the midst of executing a reward function, this number hops up to a 1, and then relaxes to 0 when reward is finished.

int count = 0                	% blink count


int rewardPump1 = 5
int rewardPump2 = 6
int rewardPump3 = 7

int arrival1a=0
int arrival2b=0
int arrival3c=0


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
callback portin[3] up
	disp('Poke in well1') 		% Print state of port to terminal
	if (arrival1a==1 && lastWell123 !=1) do
			disp('Rewarding - position A1 ')
			rewardWell=rewardPump1 	% dispense reward from here
			lastWell123=1		
			trigger(1)					% trigger reward
			count = count + 1
			disp(count)
	end
	if (lastWell123 !=1 && lastWellABC !=1) 	do				% if noones here, this is the first arrival start timer
		disp('timer1 started from port 1')
		lastWell123 = 1
		arrival1a=1
		arrival2b=0
		arrival3c=0
		do in arrivaltime
			arrival1a=0
			disp('timer1 ended from port 1')
		end
	end
end;


callback portin[3] down
	disp('UnPoke in well1') 	% Print state of port to terminal
	wellStat1=0
end;

callback portin[2] up
	disp('Poke in well2') 		% Print state of port to terminal
	if (arrival2b==1 && lastWell123 !=2) do
			disp('Rewarding - position B2 ')
			rewardWell=rewardPump2 	% dispense reward from here
			lastWell123=2		
			trigger(1)					% trigger reward
			count = count + 1
			disp(count)
	end
	if (lastWell123 !=2 && lastWellABC !=2) 	do				% if noones here, this is the first arrival start timer
		disp('timer2 started from port 2')
		lastWell123 = 2
		arrival1a=0
		arrival2b=1
		arrival3c=0
		do in arrivaltime
			arrival2b=0
			disp('timer2 ended from port 2')
		end
	end
end;

callback portin[2] down
	disp('UnPoke in well2') 	% Print state of port to terminal
	wellStat2=0
end;

callback portin[8] up
	disp('Poke in well3') 		% Print state of port to terminal
	if (arrival3c==1 && lastWell123 != 3) do
			disp('Rewarding - position C3 ')
			rewardWell=rewardPump3 	% dispense reward from here
			lastWell123=3		
			trigger(1)					% trigger reward
			count = count + 1
			disp(count)
	end
	if (lastWell123 !=3 && lastWellABC !=3) 	do				% if noones here, this is the first arrival start timer
		disp('timer3 started from port 3')
		lastWell123 = 3
		arrival1a=0
		arrival2b=0
		arrival3c=1
		do in arrivaltime
			disp('timer3 ended from port 3')
			arrival3c=0
		end
	end
end;

callback portin[8] down
	disp('UnPoke in well3') 	% Print state of port to terminal
	wellStat3=0
end;

callback portin[4] up
	disp('Poke in well A ') 		% Print state of port to terminal
	if (arrival1a==1 && lastWellABC !=1) do
			disp('Rewarding - position A1 ')
			rewardWell=rewardPump1 	% dispense reward from here
			lastWellABC=1		
			trigger(1)					% trigger reward
			count = count + 1
			disp(count)
	end
	if (lastWellABC !=1 && lastWell123 !=1) 	do				% if noones here, this is the first arrival start timer
		disp('timer1 started from port A')
		lastWellABC = 1
		arrival1a=1
		arrival2b=0
		arrival3c=0
		do in arrivaltime
			disp('timer1 ended from port A')
			arrival1a = 0
		end
	end
end;

callback portin[4] down
	disp('UnPoke in wellA') 	% Print state of port to terminal
	wellStatA=0
end;

callback portin[9] up
	disp('Poke in wellB') 		% Print state of port to terminal
	if (arrival2b==1 && lastWellABC !=2) do
			disp('Rewarding - position C3 ')
			rewardWell=rewardPump2 	% dispense reward from here
			lastWellABC=2		
			trigger(1)					% trigger reward
			count = count + 1
			disp(count)
	end
	if (lastWellABC !=2 && lastWell123 !=2) 	do				% if noones here, this is the first arrival start timer
		disp('timer2 started from port B')
		lastWellABC = 2
		arrival1a=0
		arrival2b=1
		arrival3c=0
		do in arrivaltime
			disp('timer2 ended from port B')
			arrival2b=0
		end
	end
end;


callback portin[9] down
	disp('UnPoke in wellB') 	% Print state of port to terminal
	wellStatB=0
end;


callback portin[11] up
	disp('Poke in wellC') 		% Print state of port to terminal
	if (arrival3c==1 && lastWellABC !=3) do
			disp('Rewarding - position C3 ')
			rewardWell=rewardPump3 	% dispense reward from here
			lastWellABC=3		
			trigger(1)					% trigger reward
			count=count+1
			disp(count)
	end
	if (lastWellABC!=3 && lastWell123 !=3) 	do				% if noones here, this is the first arrival start timer
		disp('timer3 started from port C')		
		lastWellABC = 3
		arrival1a=0
		arrival2b=0
		arrival3c=1
		do in arrivaltime
			disp('timer3 ended from port C')		
			arrival3c=0
		end
	end
end;



callback portin[11] down
	disp('UnPoke in wellC') 	% Print state of port to terminal
	wellStatC=0
end;