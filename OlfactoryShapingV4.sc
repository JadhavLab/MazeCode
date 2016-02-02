%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%
%%  Name:         OlfactoryShaping
%%
%%  Purpose:      Runs a task where the animal must hold its nose
%%				  in the nosepoke for an extended period of time to
%%				  receive a reward from a reward well
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


%port variables
int reward_port1 = 1
int reward_port2 = 2
int reward_identity = 0
int odor1 = 3
int odor2 = 4

%poke variables
int nose_in = 0
int nose_check_interval = 100
int nose_held_time
int nose_held_time_start

%reward variables
int reward_delivery_time = 500
int ITI = 1000
int rewards_earned = 0
int ITIOver = 1
int ITIStartTime = 0
int ITITimeDiff = 0
int currentTime

%loop variables
int not_exit_condition = 1;


function 2
	do in 500
		portout[odor1] = 0
		portout[odor2] = 0
	end
end;

function 1
	not_exit_condition=1

	while not_exit_condition == 1 do every nose_check_interval
		if nose_in == 1 do
			nose_held_time = clock() - nose_held_time_start
		end
		trigger(2)
		if (nose_held_time >=500) && (nose_in == 1) do
			disp('Nose held time is acceptable.')
			disp(nose_held_time)
			if reward_identity == odor1 do
				portout[reward_port1] = 1
			else if reward_identity == odor2 do
				portout[reward_port2] = 1
			end
			not_exit_condition = 0
			rewards_earned = rewards_earned + 1
			disp(rewards_earned)
			do in reward_delivery_time
				portout[reward_port1] = 0
				portout[reward_port2] = 0
				disp('reward off')
				
			end
		end
	end
end;


% Nose Poke Callback
callback portin[1] up
	currentTime = clock()
	ITITimeDiff = currentTime - ITIStartTime
	disp('Nose Poke!')
	if ITITimeDiff > ITI do
		nose_in = 1
		if reward_identity == odor2 do
			portout[odor1] = 1
			reward_identity = odor1
		else do
			portout[odor2] = 1
			reward_identity = odor2
		end
		nose_held_time_start = clock()
		trigger(1)
		ITIOver = 1
	else do
	disp('ITI lockout not over')
	end
end;

% Nose Out Callback	
callback portin[1]	down
	if ITIOver == 1 do
		ITIStartTime = clock()
		ITIOver = 0
	end 
	disp('Nose out!')
	disp(nose_held_time)
	nose_held_time = 0
	nose_in = 0
	not_exit_condition = 0
end;
	

