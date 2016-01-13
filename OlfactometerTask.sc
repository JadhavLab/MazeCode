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
int sample_arm = 1
int left_arm = 2
int right_arm  = 3
% ---------------------
% Digital Output Assigments
% ----------------------
int left_reward =  1
int right_reward = 2


% ---------------------
% Odorant Delivery Parameters
% ----------------------
int smell_delivery_period = 2000 % milliseconds
int smell_one = 3
int smell_two = 4
% ---------------------
% Odor to Path Variables
% ----------------------
int LEFT_PATH_ODOR = 1
int RIGHT_PATH_ODOR = 2


% ---------------------
% Reward Parameters
% ---------------------
int reward_time = 1000
int time_until_reset = 200000


% ---------------------
% Behavior Trackers
% ---------------------
int sampled_well = 1
int last_sampled_smell = 0
int current_time = 0
int time_diff = 0
int last_sampled_smell = 0

int poke_void_tracker = 0 		% 0 - not in an error state, 1 in an error state

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

    do in smell_delivery_period
         portout[smell_digital_out] = 1
    end
end;


%%%%%%%%%%%%%%%%%%%%%%%%%
%%   CALLBACK SECTION
%%%%%%%%%%%%%%%%%%%%%%%%%

%% DETECTION OF POKE
% When a poke is detected, we will select a smell to randomly
% administer

% Sample Arm Callback
callback portin[1] up
	disp('Nose Poke!')

	current_time = clock()
	time_diff = current_time - last_sampled_smell
	disp('Time difference between current sampling request and previous, see next line')
	disp(time_diff)

    if (sampled_well == 1) || (time_diff > time_until_reset)  do

        % Pick random smell
        smell_picked = random(1) + 1

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
end;

%% DOES ANIMAL CORRECTLY ASSOCIATE?

% LEFT ARM Callback
callback portin[2] up
	disp('Poke Left Arm')

	% animal has sampled a well for the current smell
	sampled_well = 1

	if ( smell_picked == LEFT_PATH_ODOR ) && (poke_void_tracker != 1) do
		% Describe what's about to happen for matlab callback functions
		disp('Port 2 Rewarding -- Correct Left Odor Path ...')
		disp(LEFT_PATH_ODOR)
		
		disp('Port Out Left On')
		portout[left_reward] = 1
		% Adminster reward
		do in reward_time
			disp('Port Out Left Off')
			portout[left_reward] = 0
		end
		
	end


	poke_void_tracker = 1

end;

callback portin[2] down
	disp('Left Port Down')
end;

% RIGHT ARM Callback
callback portin[3] up
	disp('Poke Right Arm')

	% animal has sampled a well for the current smell
	sampled_well = 1
	
	if (smell_picked == RIGHT_PATH_ODOR) && (poke_void_tracker != 1) do
		
		% Describe what's about to happen for matlab callback functions
		disp('Port 3 Rewarding -- Correct Right Odor Path ...')
		disp(RIGHT_PATH_ODOR)
		
		disp('Port Out Right On')
		portout[right_reward] = 1
		% Adminster reward
		do in reward_time
			disp('Port Out Right Off')
			portout[right_reward] = 0	
		end
				
	end


	poke_void_tracker = 1	

end;

callback portin[3] down
	disp('Right Port Down')
end;

