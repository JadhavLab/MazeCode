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
int reward_port = 1

%poke variables
int nose_in = 0
int nose_check_interval = 100
int nose_held_time
int nose_held_time_start

%reward variables
int reward_delivery_time = 500
int rewards_earned = 0

%loop variables
int not_exit_condition = 1;



function 1
	not_exit_condition=1

	while not_exit_condition == 1 do every nose_check_interval
		if nose_in == 1 do
			nose_held_time = clock() - nose_held_time_start
		end
		if (nose_held_time >=250) && (nose_in == 1) do
			disp('Nose held time is acceptable.')
			disp(nose_held_time)
			portout[reward_port] = 1
			not_exit_condition = 0
			rewards_earned = rewards_earned + 1
			disp(rewards_earned)
			do in reward_delivery_time
				portout[reward_port] = 0
				disp('reward off')
			end
		end
	end
end;

% Nose Poke Callback
callback portin[1] up
	disp('Nose Poke!')
	nose_in = 1
	nose_held_time_start = clock()
	trigger(1)
end;

% Nose Out Callback	
callback portin[1]	down
	disp('Nose out!')
	disp(nose_held_time)
	nose_held_time = 0
	nose_in = 0
	not_exit_condition = 0
end;
	

