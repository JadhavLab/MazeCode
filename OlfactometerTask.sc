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
int left_arm = 3
int right_arm  = 4
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
int reward_time = 200
int time_until_reset = 200000


% ---------------------
% Behavior Trackers
% ---------------------
int sampled_well = 0
int last_sampled_smell = 0
int current_time = 0
int time_sampled_well = 0
% ---------------------
% Apparatus Tracker
% ----------------------
int smell_picked = 0
int smell_digital_out = 0;


%%%%%%%%%%%%%%%%%%%%%%%%%
%%   CALLBACK SECTION
%%%%%%%%%%%%%%%%%%%%%%%%%

%% DETECTION OF POKE
% When a poke is detected, we will select a smell to randomly
% administer

% Sample Arm Callback
callback portin[1] up

	current_time = clock()

    if sampled_well || ((current_time - time_sampled_well) > time_until_reset)  do

        % Pick random smell
        smell_picked = random(1) + 1

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

end;

%% DOES ANIMAL CORRECTLY ASSOCIATE?

% LEFT ARM Callback
callback portin[2] up

	% Describe what's about to happen for matlab callback functions
	disp('Port 2 Rewarding -- Correct Left Odor Path ...')
	disp(' ... for smell')
	disp(smell_picked)
	time_sampled_well = clock()
	% Adminster reward
	do in reward_time
		portout[left_reward] = 1
	then
		portout[left_reward] = 0
	end

end;

% RIGHT ARM Callback
callback portin[3] up
	
	if smell_picked == RIGHT_ODOR_PATH
		
		% Describe what's about to happen for matlab callback functions
		disp('Port 3 Rewarding -- Correct Right Odor Path ...')
		disp(' ... for smell')
		disp(smell_picked)
		time_sampled_well = clock()
		% Adminster reward
		do in reward_time
			portout[right_reward] = 1
		then
			portout[right_reward] = 0
		end				
	end

end;

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%   HELPER FUNCTION SECTION
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


% ---------------------
% Name:      AdministerSmell (1)
% Purpose:   Output to whichever digital port
% 		controls our particular smell
% ---------------------
function 2
    
    if smell_picked == 1
	smell_digital_out = smell_one
    else if smell_picked == 2
	smell_digital_out = smell_two
    end

    do in smell_deliever_period
         portout[smell_digital_out] = 1
    end
end;

