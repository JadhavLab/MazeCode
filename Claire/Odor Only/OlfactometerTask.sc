%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%
%%  Name:         OlfactometerTask
%%
%%  Purpose:      Runs an olfactometer based task, selecting one of two
%%               smells to emit, and then rewards the animal based on whether
%%               it makes a trajectory to the proper arm.
%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%%%%%%%%%%%%%%%%%%%%%%%%
%%   VARIABLE SECTION
%%%%%%%%%%%%%%%%%%%%%%%%%


% ---------------------
% Digital Input Assignments
% ----------------------
int sample_arm = 5
int left_arm = 1
int right_arm  = 2
% ---------------------
% Digital Output Assigments
% ----------------------
int left_reward =  3
int right_reward = 4


% ---------------------
% Odorant Delivery Parameters
% ----------------------
int smell_delivery_period = 2000 % milliseconds
int smell_one = 6
int smell_two = 7
% ---------------------
% Odor to Path Variables
% ----------------------
int LEFT_PATH_ODOR = 1
int RIGHT_PATH_ODOR = 2


% ---------------------
% Reward Parameters
% ---------------------
int reward_time = 500 % how long milk is delivered
int time_until_reset = 200000 %this is the inter-trial interval


% ---------------------
% Behavior Trackers
% ---------------------
int sampled_well = 1 %has a reward well been sampled or is this the start of a training session
int last_sampled_smell = 0 % time when a smell was sampled last, used for checking if ITI has passed
int current_time = 0 %time check for ITI comparison
int time_diff = 0 %time that has passed since last poke and current poke used for ITI
int last_sampled_smell = 0
int nose_hold_time = 100 % how long the animal must poke before odor is delivered
int nose_hold_start = 0 %time that animal started to poke
int clock_update = 0  %variable that updates for the do every loop to track nose held time
int time_held = 0 %variable that has actual nose held time in increments of clock_update
int exit_condition = 0 % tracks whether the animal has removed its nose from the nose poke
int poke_void_tracker = 0 		% 0 - not in an error state, 1 in an error state
int consecutive_error_tracker = 0;

% ---------------------
% Apparatus Tracker
% ----------------------
int smell_picked = 0
int smell_digital_out = 0;

%% ZERO OUT THE CLOCK! %%
clock(reset);

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%   HELPER FUNCTION SECTION
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


% ---------------------
% Name:      AdministerSmell (1)
% Purpose:   Output to whichever digital port
% 		controls our particular smell
% ---------------------
function 2
    
    if smell_picked == 1 do
	smell_digital_out = smell_one
    else if smell_picked == 2 do
	smell_digital_out = smell_two
    end

	disp(smell_digital_out)

	portout[smell_digital_out] = 1

    	do in smell_delivery_period
     		portout[smell_digital_out] = 0    
    end

end;


%%%%%%%%%%%%%%%%%%%%%%%%%
%%   CALLBACK SECTION
%%%%%%%%%%%%%%%%%%%%%%%%%

%% DETECTION OF POKE
% When a poke is detected, we will select a smell to randomly
% administer

% Sample Arm Callback
callback portin[5] up
	disp('Nose Poke!')
	exit_condition = 0
	current_time = clock()
	nose_hold_start = current_time
	while exit_condition == 0 do every 100  % this loop checks every 100ms for how long nose has been in the nose poke
		clock_update = clock()
		time_held = clock_update - nose_hold_start
		disp(time_held)
		if  time_held  > nose_hold_time do
			 if (sampled_well == 1) || (time_diff > time_until_reset)  do
				consecutive_error_tracker = 0 % reset for error tracking
        			% Pick random smell
				if smell_picked == 1 do
					smell_picked = 2
				else do
					smell_picked = 1
				end
        			%smell_picked =  2 %for specific reward 
				%random(1) + 1 %for random reward
				disp(smell_picked)

       	 		% Administer smell selected
        			trigger(2)				% Trigger Function 2: AdminsterSmell

				% Record time sampled smell
				last_sampled_smell = clock()

    			else do
        		% NOTHING, rat already got a whiff of the sample
				% depending on how hard the task is, to shape, we might add
				% optional code to this section to allow the rat to come back
				% and be re-adminstered the same smell until the rat actually
				% pokes a well. No doubt this would help shaping in early epochs
				% when animal is still learning.
    			end
	
			% animal has not yet sampled a well for this poke
			sampled_well = 0

			% we are not in an error state anytime after request for sample
			poke_void_tracker = 0
		end
end
	time_diff = current_time - last_sampled_smell
	disp('Time difference between current sampling request and previous, see next line')
	disp(time_diff)

   
end;

callback portin[5] down
exit_condition = 1
nose_hold_start = 0
clock_update = 0
end;
%% DOES ANIMAL CORRECTLY ASSOCIATE?

% LEFT ARM Callback
callback portin[1] up
	disp('Poke Left Arm - Port up 2')

	% animal has sampled a well for the current smell
	sampled_well = 1

	if ( smell_picked == LEFT_PATH_ODOR ) && (poke_void_tracker != 1) do

		% Describe what's about to happen for matlab callback functions
		disp('Port 2 Rewarding -- Correct Left Odor Path ...')
		disp(LEFT_PATH_ODOR)
		
		disp('Poke 2 rewarded - Left')
		portout[left_reward] = 1
		% Adminster reward
		do in reward_time
			disp('Port Out Left Off')
			portout[left_reward] = 0
		end

	else do
		if (consecutive_error_tracker < 1) do
			disp('Poke 2 not rewarded - Left')
		end
		consecutive_error_tracker = consecutive_error_tracker + 1
	end


	poke_void_tracker = 1

end;

callback portin[1] down
	disp('Left Port Down - Port down 2')
end;

% RIGHT ARM Callback
callback portin[2] up
	disp('Poke Right Arm - Portin 3')

	% animal has sampled a well for the current smell
	sampled_well = 1
	
	if (smell_picked == RIGHT_PATH_ODOR) && (poke_void_tracker != 1) do
		
		% Describe what's about to happen for matlab callback functions
		disp('Port 3 Rewarding -- Correct Right Odor Path ...')
		disp(RIGHT_PATH_ODOR)
		
		portout[right_reward] = 1
		disp('Poke 1 rewarded - Right')
		% Adminster reward
		do in reward_time
			disp('Port Out Right Off')
			portout[right_reward] = 0	
		end
	else do 
		if (consecutive_error_tracker < 1) do
		disp('Poke 1 not rewarded - Right')	
		end
		consecutive_error_tracker	= consecutive_error_tracker + 1
	end


	poke_void_tracker = 1	

end;

callback portin[2] down
	disp('Right Port Down - Portin 3 Down')
end;

