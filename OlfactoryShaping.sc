%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%
%%  Name:         OlfactoryShaping
%%
%%  Purpose:      Runs a task where the animal must hold its nose
%%	          in the nosepoke for an extended period of time to
%%		  receive a reward from a reward well
%%
%%  Developed by Wesley Alford and Ryan Young
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%




int delay_int = 500
int nose_in = 0 % binary variable for determining if the animal nose is in the nose poke
int rewarded = 0
int reward_delivery_time = 1000 % duration that milk will be delivered in ms 
int reward_port = 1
int nose_held_time
int not_trigger =1
int nose_held_time_start
int not_exit_condition = 1;

int delay_iti = 1000;

int zero = 0
int one = 1;

int update_time = 100;

function 2	
	do in delay_int
		if rewarded == 1 do
			while zero < one  do every delay_iti 
				rewarded = 0
			end
		end
	end
end;

function 1
	disp('Entering function 1')
	if not_trigger == 1 do
		not_trigger= 0
		while not_exit_condition == 1 do every update_time
			trigger(2)
			if nose_in == 1 do
				nose_held_time = clock() %updates current time 
				%calculates the duration the animal has been nose poking
				nose_held_time = nose_held_time - nose_held_time_start 
			end
			disp(nose_held_time_start)
			if (nose_held_time >=500) && (rewarded == 0) do
				rewarded = 1
				portout[reward_port] = 1
				disp('rewarded')
				do in reward_delivery_time
					portout[reward_port] = 0
				end
			end
		end
	end
end;


% Nose Poke Callback
callback portin[1] up 
	disp('Nose Poke Start!')
	nose_in = 1 
	nose_held_time_start = clock() 
	disp(nose_held_time)

	trigger(1)
end;

% Nose Out Callback	
callback portin[1] down
	disp('Nose Poke Stop!')
	nose_held_time_start = 0
	disp(nose_held_time)
end;
